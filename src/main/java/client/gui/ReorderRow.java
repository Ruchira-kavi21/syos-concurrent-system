package client.gui;

import java.io.Serializable;

public class ReorderRow implements Serializable {

    private final String code;
    private final String name;
    private final int quantity;

    public ReorderRow(
            String code,
            String name,
            int quantity
    )
    {

        this.code = code;
        this.name = name;
        this.quantity = quantity;
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
}