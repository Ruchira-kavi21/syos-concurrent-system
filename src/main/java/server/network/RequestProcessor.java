package server.network;

import client.gui.*;
import server.domain.Bill;
import server.domain.Item;
import server.domain.TransactionType;
import server.infrastructure.BillDAO;
import server.infrastructure.ItemDAO;
import shared.dto.Request;
import shared.dto.Response;
import shared.dto.PurchaseRequest;

import java.io.Serializable;
import java.util.List;

public class RequestProcessor implements Serializable {
    public Response process (Request request) {
        String action = request.getAction();
        switch (action) {
            case "Test": return handleTest(request);
            case "GET_ITEMS": return handleGetItems();
            case "BUY_ITEM": return handlePurchase(request);
            case "GET_ITEM_BY_CODE": return handleGetItemByCode((String) request.getData());
            case "SAVE_BILL": return handleSaveBill((Bill) request.getData());
            case "GET_DAILY_SALES": return handleDailySales();
            case "GET_REORDER_REPORT": return handleReorderReport();
            case "GET_BILL_REPORT": return handleBillReport();
            case "GET_STOCK_REPORT": return handleStockReport();
            case "GET_RESHELVING_REPORT": return handleReshelvingReport();
            default: return new Response("Error", "Unknown request type", null);
        }
    }
    private Response handleTest (Request request){
        System.out.println("Processing Test request...");

        return new Response("success", "Test request processed successfully",null);
    }
    private Response handleGetItems() {
        try {

            ItemDAO itemDAO = new ItemDAO();
            List<Item> items = itemDAO.getAllItems();
            return new Response("SUCCESS", "Loaded " + items.size() + " items", items);

        } catch (Exception e) {
            return new Response("ERROR", e.getMessage(), null);
        }
    }
    private Response handleGetItemByCode(String code) {

        try {
            ItemDAO itemDAO = new ItemDAO();

            Item item = itemDAO.getItemByCode(code);

            if (item == null) {
                return new Response("ERROR", "Item not found", null);
            }
            return new Response("SUCCESS", "Item loaded", item);

        } catch (Exception e) {
            return new Response("ERROR", e.getMessage(), null);
        }
    }

    private synchronized Response handlePurchase(Request request) {

        try {
            PurchaseRequest purchase = (PurchaseRequest) request.getData();
            ItemDAO itemDAO = new ItemDAO();
            boolean success = itemDAO.reduceShelfStock(purchase.getItemCode(), purchase.getQuantity());

            if (success) {
                return new Response("SUCCESS", "Purchase completed successfully", null);
            } else {
                return new Response("ERROR", "Insufficient stock", null);
            }

        } catch (Exception e) {
            return new Response("ERROR", e.getMessage(), null);
        }
    }

    private Response handleSaveBill(Bill bill) {

        try {
            BillDAO billDAO = new BillDAO();

            billDAO.saveBill(bill);

            return new Response("SUCCESS", "Bill saved successfully", null);

        } catch (Exception e) {

            return new Response("ERROR", e.getMessage(), null);
        }
    }

    private Response handleDailySales() {

        try {
            BillDAO billDAO = new BillDAO();

            List<DailySalesRow> rows = billDAO.getDailySalesReport();

            return new Response("SUCCESS", "Daily sales loaded", rows);

        } catch (Exception e) {

            return new Response("ERROR", e.getMessage(), null);
        }
    }

    private Response handleReorderReport() {

        try {
            BillDAO billDAO = new BillDAO();

            List<ReorderRow> rows = billDAO.getReorderReport();

            return new Response("SUCCESS", "Reorder report loaded", rows);

        } catch (Exception e) {
            return new Response("ERROR", e.getMessage(), null);
        }
    }

    private Response handleBillReport() {

        try {
            BillDAO billDAO = new BillDAO();

            List<BillReportRow> rows = billDAO.getBillReport();

            return new Response("SUCCESS", "Bill report loaded", rows);

        } catch (Exception e) {

            return new Response("ERROR", e.getMessage(), null);
        }
    }

    private Response handleStockReport() {

        try {
            BillDAO billDAO = new BillDAO();

            List<StockRow> rows = billDAO.getStockReport();

            return new Response("SUCCESS", "Stock report loaded", rows);

        } catch (Exception e) {

            return new Response("ERROR", e.getMessage(), null);
        }
    }
    private Response handleReshelvingReport() {

        try {

            BillDAO billDAO = new BillDAO();

            List<ReshelvingRow> rows = billDAO.getReshelvingReport();

            return new Response("SUCCESS", "Reshelving report loaded", rows);

        } catch (Exception e) {
            return new Response("ERROR", e.getMessage(), null);
        }
    }
}
