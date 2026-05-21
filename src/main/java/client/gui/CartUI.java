package client.gui;

public class CartUI {
    private final String code;
    private final String name;
    private final int quantity;
    private final double price;

    public CartUI(
            String code,
            String name,
            int quantity,
            double price
    )
    {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return quantity * price;
    }
}
