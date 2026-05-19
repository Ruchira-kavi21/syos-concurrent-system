package server.infrastructure;


import server.domain.Bill;
import server.domain.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreData {
    private static StoreData instance;
    private final Map<String, Item> inventory = new HashMap<>();
    private final List<Bill> transactionHistory = new ArrayList<>();

    private StoreData(){}

    public static StoreData getInstance(){
        if (instance == null){
            instance = new StoreData();
        }
        return instance;
    }
    public void addItem(Item item){
        inventory.put(item.getCode(), item);
    }
    public Item getItem (String code){
        return inventory.get(code);
    }

    public Map<String, Item> getInventory() {
        return inventory;
    }

    public void addBill(Bill bill){
        transactionHistory.add(bill);
    }
    public List<Bill> getTransactionHistory(){
        return transactionHistory;
    }

    public void reset(){
        inventory.clear();
    }
}
