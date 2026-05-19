package server.usecase;

import server.domain.Bill;
import server.domain.Cart;

public interface BillingService {
    Bill processTransaction(Cart cart, double cashTendered);
}