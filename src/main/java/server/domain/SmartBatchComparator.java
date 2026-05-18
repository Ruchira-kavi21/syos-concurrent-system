package server.domain;

import java.util.Comparator;

public class SmartBatchComparator implements Comparator<Batch> {
    @Override
    public int compare(Batch b1, Batch b2) {
        if (b1.getExpiryDate().isBefore(b2.getExpiryDate())){
            return -1;
        }else if (b1.getExpiryDate().isAfter(b2.getExpiryDate())){
            return 1;
        }
        return b1.getDateOfPurchase().compareTo(b2.getDateOfPurchase());
        //if exp dates are roughly the same, compare by purchase date (FIFO)
    }
}