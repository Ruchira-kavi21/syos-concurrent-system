package server.domain;

import java.util.ArrayList;
import java.util.List;

public class Category extends InventoryComponent{

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
