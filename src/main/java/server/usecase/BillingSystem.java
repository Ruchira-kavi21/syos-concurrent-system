package server.usecase;

import server.domain.BillItem;
import server.domain.Item;
import server.infrastructure.BillDAO;
import server.infrastructure.ItemDAO;
import server.domain.Bill;
import server.domain.Cart;
import server.domain.TransactionType;
import server.infrastructure.StoreData;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BillingSystem implements BillingService {
    private final StoreData storeData = StoreData.getInstance();
    private final BillDAO billDAO = new BillDAO();
//    private static int billCounter = 1;

    @Override
    public Bill processTransaction(Cart cart, double cashTendered){
        //validation
        if (cart.isEmpty()){
            throw new IllegalArgumentException("Cart is empty. Cannot process transaction.");
        }

        double totalAmount = cart.calculateTotal();
        if (cashTendered < totalAmount){
            throw new IllegalArgumentException("Insufficient cash. Total is: " + totalAmount);
        }

        ItemDAO inventoryUpdater = new ItemDAO();
        List<BillItem> billItems = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : cart.getItems().entrySet()){
            Item item = entry.getKey();
            int qtyNeeded = entry.getValue();

            item.reduceStock(qtyNeeded);
            inventoryUpdater.syncItemInventory(item);

            double lineTotal = item.getPrice() * qtyNeeded;
            billItems.add(new BillItem(item.getName(), qtyNeeded, item.getPrice(), lineTotal));
        }

        double change = cashTendered - totalAmount;
        double discount = 0.0;

        String billNumber = "B" + (System.currentTimeMillis() % 100000);
        Bill bill = new Bill.BillBuilder()
                .setBillNumber(billNumber)
                .setDate(LocalDateTime.now())
                .setType(TransactionType.IN_STORE)
                .setTotalAmount(totalAmount)
                .setDiscount(discount)
                .setCashReceived(cashTendered)
                .setChangeAmount(change)
                .addItemList(billItems)
                .build();
        storeData.addBill(bill);
        billDAO.saveBill(bill);

        return bill;
    }
}
