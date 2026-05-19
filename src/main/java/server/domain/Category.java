package server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category extends InventoryComponent implements Serializable {

    private final List<InventoryComponent> components = new ArrayList<>();
    public Category(String name, String code) {
        super(name, code);
    }

    public void add(InventoryComponent component){
        components.add(component);
    }
    public void remove(InventoryComponent component){
        components.remove(component);
    }

    @Override
    public void displayInfo() {
        System.out.println("Category: " + name + " (Code: " + code + ")");
        for (InventoryComponent component : components) {
            component.displayInfo();
        }
    }
}
