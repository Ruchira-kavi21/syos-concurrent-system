package client.gui;

import java.io.Serializable;

public class BatchRow implements Serializable {

    private final int id;
    private final String batchNumber;
    private final String itemCode;
    private final String itemName;
    private final String purchaseDate;
    private final String expiryDate;
    private final int quantity;
    private final String location;

    public BatchRow(
            int id,
            String batchNumber,
            String itemCode,
            String itemName,
            String purchaseDate,
            String expiryDate,
            int quantity,
            String location

    )
    {

        this.id = id;
        this.batchNumber = batchNumber;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public String getItemCode() {
        return itemCode;
    }
    public String getItemName() {
        return itemName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getLocation() {
        return location;
    }
}