package client.gui;

import java.io.Serializable;

public class DailySalesRow implements Serializable {

    private final String code;
    private final String name;
    private final int quantity;
    private final double revenue;

    public DailySalesRow(
            String code,
            String name,
            int quantity,
            double revenue
    )
    {

        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.revenue = revenue;
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

    public double getRevenue() {
        return revenue;
    }
}