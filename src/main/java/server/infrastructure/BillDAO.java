package server.infrastructure;


import client.gui.*;
import server.domain.Bill;
import server.domain.BillItem;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO implements Serializable {
    private final Connection connection;

    public BillDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    public void saveBill(Bill bill){
        String billSql = "INSERT INTO bills (bill_number, date, total_amount, discount, cash_received, change_amount) VALUES (?, ?, ?, ?, ?, ?)";
        String itemSql = "INSERT INTO bill_items (bill_number, item_name, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement billStmt = connection.prepareStatement(billSql);
            billStmt.setString(1, bill.getBillNumber());
            billStmt.setTimestamp(2, Timestamp.valueOf(bill.getDate()));
            billStmt.setDouble(3, bill.getTotalAmount());
            billStmt.setDouble(4, bill.getDiscount());
            billStmt.setDouble(5, bill.getCashReceived());
            billStmt.setDouble(6, bill.getChangeAmount());
            billStmt.executeUpdate();

            PreparedStatement itemStmt = connection.prepareStatement(itemSql);
            for (BillItem item : bill.getItems()) {
                itemStmt.setString(1, bill.getBillNumber());
                itemStmt.setString(2, item.getItemName());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getUnitPrice());
                itemStmt.setDouble(5, item.getTotalPrice());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();
            System.out.println("Bill " + bill.getBillNumber() + " successfully saved to Database.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error saving bill: " + e.getMessage());
        }
    }
    public List<DailySalesRow> getDailySalesReport() {

        List<DailySalesRow> rows = new ArrayList<>();

        try {
            String sql =
                    """
                        SELECT
                        i.code,
                        bi.item_name,
                        SUM(bi.quantity) AS total_quantity,
                        SUM(bi.total_price) AS revenue
                        FROM bill_items bi
                        JOIN items i
                            ON bi.item_name = i.name
                        JOIN bills b
                            ON bi.bill_number = b.bill_number
                        WHERE DATE(b.date) = CURDATE()
                        GROUP BY i.code, bi.item_name
                        ORDER BY revenue DESC
                    """;

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rows.add(
                        new DailySalesRow(
                                resultSet.getString("code"),
                                resultSet.getString("item_name"),
                                resultSet.getInt("total_quantity"),
                                resultSet.getDouble("revenue")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
    public List<ReorderRow> getReorderReport() {

        List<ReorderRow> rows = new ArrayList<>();
        try {
            String sql =
                    """
                    SELECT code,
                           name,
                           shelf_capacity
                    FROM items
                    WHERE shelf_capacity < 50
                    """;

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rows.add(
                        new ReorderRow(
                                resultSet.getString("code"),
                                resultSet.getString("name"),
                                resultSet.getInt("shelf_capacity")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
    public List<BillReportRow> getBillReport() {

        List<BillReportRow> rows = new ArrayList<>();

        try {
            String sql =
                    """
                    SELECT bill_number,
                           date,
                           total_amount,
                           cash_received,
                           change_amount
                    FROM bills
                    ORDER BY date DESC
                    """;

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rows.add(
                        new BillReportRow(
                                resultSet.getString("bill_number"),
                                resultSet.getTimestamp("date").toString(),
                                resultSet.getDouble("total_amount"),
                                resultSet.getDouble("cash_received"),
                                resultSet.getDouble("change_amount")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
    public List<StockRow> getStockReport() {

        List<StockRow> rows = new ArrayList<>();

        try {
            String sql =
                    """
                    SELECT
                        batch_number,
                        item_code,
                        purchase_date,
                        expiry_date,
                        quantity,
                        location
                    FROM batches
                    ORDER BY expiry_date ASC
                    """;

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rows.add(
                        new StockRow(
                                resultSet.getString("batch_number"),
                                resultSet.getString("item_code"),
                                resultSet.getDate("purchase_date").toString(),
                                resultSet.getDate("expiry_date").toString(),
                                resultSet.getInt("quantity"),
                                resultSet.getString("location")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
    public List<ReshelvingRow> getReshelvingReport() {

        List<ReshelvingRow> rows = new ArrayList<>();

        try {
            String sql =
                    """
                    SELECT
                        i.code,
                        i.name,
                        i.shelf_capacity,
                        COALESCE(SUM(b.quantity), 0)
                            AS store_quantity
                    FROM items i
                    LEFT JOIN batches b
                        ON i.code = b.item_code
                        AND b.location = 'STORE'
                    WHERE i.shelf_capacity < 50
                    GROUP BY
                        i.code,
                        i.name,
                        i.shelf_capacity
                    ORDER BY i.shelf_capacity ASC
                    """;

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rows.add(
                        new ReshelvingRow(
                                resultSet.getString("code"),
                                resultSet.getString("name"),
                                resultSet.getInt("shelf_capacity"),
                                resultSet.getInt("store_quantity")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

}
