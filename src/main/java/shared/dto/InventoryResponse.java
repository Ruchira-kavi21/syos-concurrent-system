package shared.dto;

import server.domain.Item;

import java.io.Serializable;
import java.util.List;

public class InventoryResponse implements Serializable {
    private List<Item> items;

    public InventoryResponse (List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
}
