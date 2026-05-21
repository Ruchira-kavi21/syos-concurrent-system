package client.gui;

import java.io.Serializable;

public class BillReportRow implements Serializable {

    private final String billNumber;
    private final String date;
    private final double total;
    private final double cash;
    private final double change;

    public BillReportRow(
            String billNumber,
            String date,
            double total,
            double cash,
            double change
    )
    {

        this.billNumber = billNumber;
        this.date = date;
        this.total = total;
        this.cash = cash;
        this.change = change;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public double getCash() {
        return cash;
    }

    public double getChange() {
        return change;
    }
}