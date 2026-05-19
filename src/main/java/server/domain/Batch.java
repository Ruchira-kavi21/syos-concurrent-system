package server.domain;

import java.io.Serializable;
import java.time.LocalDate;

public class Batch implements Serializable {
    private final String batchNumber;
    private final LocalDate dateOfPurchase;
    private final LocalDate expiryDate;
    private int quantity;

    public Batch(String batchNumber, LocalDate dateOfPurchase, LocalDate expiryDate, int quantity) {
        this.batchNumber = batchNumber;
        this.dateOfPurchase = dateOfPurchase;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }
    public void decreaseQuantity(int amount) {
        if (amount <= quantity) {
            this.quantity -= amount;
        } else {
            throw new IllegalArgumentException("Not enough stock in this batch!");
        }
    }
}
