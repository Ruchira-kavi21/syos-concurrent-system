package server.infrastructure;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.domain.Item;
import server.domain.Batch;

public class ItemDAO implements Serializable {
    private final Connection connection;

    public ItemDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Item> loadAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int capacity = rs.getInt("shelf_capacity");

                //Create Object
                Item item = new Item(code, name, price, capacity);
                //Load its Batches
                loadBatchesForItem(item);
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void loadBatchesForItem(Item item) {
        String sql = "SELECT * FROM batches WHERE item_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getCode());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Batch batch = new Batch(
                        rs.getString("batch_number"),
                        rs.getDate("purchase_date").toLocalDate(),
                        rs.getDate("expiry_date").toLocalDate(),
                        rs.getInt("quantity")
                );

                String location = rs.getString("location");

                //Sort into the correct list based on db
                if ("STORE".equalsIgnoreCase(location)) {
                    item.addBatchToStore(batch);
                } else if ("SHELF".equalsIgnoreCase(location)) {
                    item.addBatchToShelf(batch);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void syncItemInventory(Item item) {
        String itemSql = "INSERT INTO items (code, name, price, shelf_capacity) VALUES (?, ?, ?, ?) " +  "ON DUPLICATE KEY UPDATE name=VALUES(name), price=VALUES(price), shelf_capacity=VALUES(shelf_capacity)";

        String deleteSql = "DELETE FROM batches WHERE item_code = ?";
        String insertSql = "INSERT INTO batches (batch_number, item_code, purchase_date, expiry_date, quantity, location) VALUES (?, ?, ?, ?, ?, ?)";

        try {

            PreparedStatement itemStmt = connection.prepareStatement(itemSql);
            itemStmt.setString(1, item.getCode());
            itemStmt.setString(2, item.getName());
            itemStmt.setDouble(3, item.getPrice());
            itemStmt.setInt(4, item.getShelfQuantity());
            itemStmt.executeUpdate();
            itemStmt.close(); // Close strictly

            // Wipe the old batches
            PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
            deleteStmt.setString(1, item.getCode());
            deleteStmt.executeUpdate();
            deleteStmt.close();

            PreparedStatement insertStmt = connection.prepareStatement(insertSql);

            // Save Back Store Batches
            for (Batch b : item.getStoreBatches()) {
                insertStmt.setString(1, b.getBatchNumber());
                insertStmt.setString(2, item.getCode());
                insertStmt.setDate(3, java.sql.Date.valueOf(b.getDateOfPurchase()));
                insertStmt.setDate(4, java.sql.Date.valueOf(b.getExpiryDate()));
                insertStmt.setInt(5, b.getQuantity());
                insertStmt.setString(6, "STORE");
                insertStmt.addBatch();
            }

            // Save Front Shelf Batches
            for (Batch b : item.getShelfBatches()) {
                insertStmt.setString(1, b.getBatchNumber());
                insertStmt.setString(2, item.getCode());
                insertStmt.setDate(3, java.sql.Date.valueOf(b.getDateOfPurchase()));
                insertStmt.setDate(4, java.sql.Date.valueOf(b.getExpiryDate()));
                insertStmt.setInt(5, b.getQuantity());
                insertStmt.setString(6, "SHELF");
                insertStmt.addBatch();
            }

            // Execute all updates
            insertStmt.executeBatch();
            insertStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error syncing inventory: " + e.getMessage());
        }
    }
    public List<Item> getAllItems() throws Exception {

        List<Item> items = new ArrayList<>();

        String sql = "SELECT * FROM items";

        Connection connection = DatabaseConnection.getInstance().getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {

            Item item = new Item(
                    resultSet.getString("code"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("shelf_capacity")
            );

            items.add(item);
        }

        return items;
    }
    public boolean reduceShelfStock(String itemCode, int quantity) throws Exception {

        String sql = "UPDATE items " + "SET shelf_capacity = shelf_capacity - ? " + "WHERE code = ? AND shelf_capacity >= ?";

        Connection connection = DatabaseConnection.getInstance().getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, quantity);
        statement.setString(2, itemCode);
        statement.setInt(3, quantity);

        int rowsUpdated = statement.executeUpdate();

        return rowsUpdated > 0;
    }
    public Item getItemByCode(
            String code) {

        try {

            String sql = "SELECT * FROM items WHERE code = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, code);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                return new Item(
                        resultSet.getString("code"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("shelf_capacity")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
