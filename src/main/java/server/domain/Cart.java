package server.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
    private final Map<Item, Integer> items = new HashMap<>();

    public void addItem(Item item, int quantity)
    {
        if (quantity > item.getShelfQuantity()) {
            throw new IllegalArgumentException("Only " + item.getShelfQuantity() + " units available on shelf.");
        }
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }
    public Map<Item, Integer> getItems(){
        return items;
    }
    public double calculateTotal(){
        double total = 0;
        for (Map.Entry <Item, Integer> entry : items.entrySet()){
            Item item = entry.getKey();
            int quantity = entry.getValue();
            total += item.getPrice() * quantity;
        }
        return total;
    }
    public void clearCart(){
        items.clear();
    }
    public boolean isEmpty(){
        return items.isEmpty();
    }
}
