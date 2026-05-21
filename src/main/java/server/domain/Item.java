package server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Item extends InventoryComponent implements Serializable {
    private final String code;
    private final String name;
    private double price;
    private int shelfCapacity;
    private final List<Batch> storeBatches;
    private final List<Batch> shelfBatches;

    //constructor
    public Item (String code, String name, double price, int shelfCapacity){
        super(name, code);
        this.code = code;
        this.name = name;
        this.price = price;
        this.shelfCapacity = shelfCapacity;
        this.storeBatches = new ArrayList<>();
        this.shelfBatches = new ArrayList<>();
    }
    //getters
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }

    public int getShelfCapacity() {
        return shelfCapacity;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    //    public List<Batch> getBatchList() {
//        return batchList;
//    }
//    public void addBatch(Batch batch) {
//        this.batchList.add(batch);
//    }
    public int getTotalQuantity(){
        int storeQty = storeBatches.stream().mapToInt(Batch::getQuantity).sum();
        int shelfQty = shelfBatches.stream().mapToInt(Batch::getQuantity).sum();
        return storeQty + shelfQty;
    }
    public void addBatchToStore(Batch batch){
        this.storeBatches.add(batch);
        this.storeBatches.sort(new SmartBatchComparator());
    }
    public void addBatchToShelf(Batch batch) {
        this.shelfBatches.add(batch);
        this.shelfBatches.sort(new SmartBatchComparator());
    }
    public List<Batch> getShelfBatches() {
        return shelfBatches;
    }
    public void restockShelf(){
        int currentShelfQty = shelfBatches.stream().mapToInt(Batch::getQuantity).sum();
        int spaceOnShelf = shelfCapacity - currentShelfQty;
        if (spaceOnShelf <= 0 ) return;

        Iterator<Batch> iterator = storeBatches.iterator();
        while (iterator.hasNext() && spaceOnShelf > 0){
            Batch storeBatch = iterator.next();
            int qtyToMove = Math.min(spaceOnShelf, storeBatch.getQuantity());
            if (qtyToMove == storeBatch.getQuantity()){
                shelfBatches.add(storeBatch);
                iterator.remove();
            }else {
                storeBatch.decreaseQuantity(qtyToMove);
                Batch newShelfBatch = new Batch(
                        storeBatch.getBatchNumber() + "-S",
                        storeBatch.getDateOfPurchase(),
                        storeBatch.getExpiryDate(),
                        qtyToMove
                );
                shelfBatches.add(newShelfBatch);
            }
            spaceOnShelf -= qtyToMove;
        }
        this.shelfBatches.sort(new SmartBatchComparator());
    }

    public void reduceStock(int quantityNeeded){
        int currentShelfQty = shelfBatches.stream().mapToInt(Batch::getQuantity).sum();
        if (quantityNeeded > currentShelfQty) {
            restockShelf();
            currentShelfQty = getShelfQuantity();
            if (quantityNeeded > currentShelfQty) {
                throw new IllegalArgumentException("Not enough stock for item: " + name);
            }
        }

        int remainingQuantity = quantityNeeded;
        Iterator<Batch> iterator = shelfBatches.iterator();

        while (iterator.hasNext() && remainingQuantity >0 ){
            Batch batch = iterator.next();
            int currentStock = batch.getQuantity();
            if (currentStock > remainingQuantity){
                batch.decreaseQuantity(remainingQuantity);
                remainingQuantity = 0;
            }else {
                remainingQuantity -= currentStock;
                iterator.remove();
            }
        }
        restockShelf();
    }
    public int getShelfQuantity() {
        return shelfBatches.stream().mapToInt(Batch::getQuantity).sum();
    }
    public int getStoreQuantity() {
        int total = 0;
        for (Batch batch : storeBatches) {
            total += batch.getQuantity();
        }
        return total;
    }
    public List<Batch> getStoreBatches() {
        return storeBatches;
    }

    @Override
    public void displayInfo() {
        System.out.println("- Item: " + getName() + " [" + getCode() + "] | Price: " + price + " | Total Stock: " + getTotalQuantity()  + " | Shelf: " + getShelfQuantity() + " | Store: " + (getTotalQuantity() - getShelfQuantity()) );
    }
}
