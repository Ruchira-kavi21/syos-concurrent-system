package server.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private final String billNumber;
    private final LocalDateTime date;
    private final double totalAmount;
    private final double discount;
    private final double cashReceived;
    private final double changeAmount;
    private final List<BillItem> items;
    private final TransactionType type;

    //constructor using builder pattern
    private Bill (BillBuilder builder) {
        this.billNumber = builder.billNumber;
        this.date = builder.date;
        this.totalAmount = builder.totalAmount;
        this.discount = builder.discount;
        this.cashReceived = builder.cashReceived;
        this.changeAmount = builder.changeAmount;
        this.items = builder.items;
        this.type = builder.type;
    }
    //getters
    public String getBillNumber() {
        return billNumber;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public double getDiscount() {
        return discount;
    }
    public double getCashReceived() {
        return cashReceived;
    }
    public double getChangeAmount() {
        return changeAmount;
    }
    public List<BillItem> getItems() {
        return items;
    }
    public TransactionType getType() {
        return type;
    }

    //Builder class
    public static class BillBuilder{
        private String billNumber;
        private LocalDateTime date;
        private TransactionType type;
        private double totalAmount;
        private double discount;
        private double cashReceived;
        private double changeAmount;
        private List<BillItem> items = new ArrayList<>();

        public BillBuilder setBillNumber(String billNumber){
            this.billNumber = billNumber;
            return this;
        }
        public BillBuilder setDate(LocalDateTime date){
            this.date = date;
            return this;
        }
        public BillBuilder setType(TransactionType type){
            this.type = type;
            return this;
        }
        public BillBuilder setTotalAmount(double totalAmount){
            this.totalAmount = totalAmount;
            return this;
        }
        public BillBuilder setDiscount(double discount) {
            this.discount = discount;
            return this;
        }
        public BillBuilder setCashReceived(double cashReceived){
            this.cashReceived = cashReceived;
            return this;
        }
        public BillBuilder setChangeAmount(double changeAmount) {
            this.changeAmount = changeAmount;
            return this;
        }
        public BillBuilder addItem(BillItem item){
            this.items.add(item);
            return this;
        }
        public Bill build() {
            return new Bill(this);
        }
        public BillBuilder addItemList(List<BillItem> itemList){
            this.items.addAll(itemList);
            return this;
        }
    }
}
