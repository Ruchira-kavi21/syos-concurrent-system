package server.infrastructure;

import client.gui.BatchRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BatchDAO {

    private final Connection connection;

    public BatchDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<BatchRow>
    getAllBatches() {
        List<BatchRow> rows = new ArrayList<>();
        try {
            String sql = """
                    SELECT
                        b.id,
                        b.batch_number,
                        b.item_code,
                            i.name AS item_name,
                 
                        b.purchase_date,
                        b.expiry_date,
                        b.quantity,
                        b.location
                    FROM batches b
                    JOIN items i
                        ON b.item_code = i.code
                    ORDER BY b.expiry_date ASC
                    """;
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rows.add(new BatchRow(
                                resultSet.getInt("id"),
                                resultSet.getString("batch_number"),
                                resultSet.getString("item_code"),
                                resultSet.getString("item_name"),
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
}