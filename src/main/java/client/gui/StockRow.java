package client.gui;

import java.io.Serializable;

public class StockRow implements Serializable {

    private final String batchNumber;
    private final String itemCode;
    private final String purchaseDate;
    private final String expiryDate;
    private final int quantity;
    private final String location;

    public StockRow(
            String batchNumber,
            String itemCode,
            String purchaseDate,
            String expiryDate,
            int quantity,
            String location
    )
    {

        this.batchNumber = batchNumber;
        this.itemCode = itemCode;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.location = location;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public String getItemCode() {
        return itemCode;
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