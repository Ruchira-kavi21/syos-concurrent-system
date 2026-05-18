package server.domain;

public class BillItem {
    private final String itemName;
    private final int quantity;
    private final double unitPrice;
    private final double totalPrice;

    public BillItem(String itemName, int quantity, double unitPrice, double totalPrice) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }
    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
