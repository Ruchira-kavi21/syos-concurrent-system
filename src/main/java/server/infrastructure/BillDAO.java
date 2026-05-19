package server.infrastructure;


import server.domain.Bill;
import server.domain.BillItem;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.*;

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
}
