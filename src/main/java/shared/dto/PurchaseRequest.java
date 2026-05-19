package shared.dto;

import java.io.Serializable;

public class PurchaseRequest implements Serializable {
    private String itemCode;
    private int quantity;

    public PurchaseRequest(String itemCode, int quantity) {
        this.itemCode = itemCode;
        this.quantity = quantity;
    }

    public String getItemCode() {
        return itemCode;
    }

    public int getQuantity() {
        return quantity;
    }
}
