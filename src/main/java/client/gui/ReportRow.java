package client.gui;

import java.io.Serializable;

public class ReportRow implements Serializable {

    private final String billNumber;
    private final String date;
    private final double total;

    public ReportRow(
            String billNumber,
            String date,
            double total
    )
    {

        this.billNumber = billNumber;
        this.date = date;
        this.total = total;
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
}