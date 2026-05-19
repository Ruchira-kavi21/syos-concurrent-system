package server.network;

import server.domain.Item;
import server.domain.TransactionType;
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
            case "Test":
                return handleTest(request);
            case "GET_ITEMS":
                return handleGetItems();
            case "BUY_ITEM":
                return handlePurchase(request);
            default:
                return new Response("Error", "Unknown request type", null);
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

            return new Response(
                    "SUCCESS",
                    "Loaded " + items.size() + " items", items
            );

        } catch (Exception e) {

            return new Response(
                    "ERROR",
                    e.getMessage(), null
            );
        }
    }
    private synchronized Response handlePurchase(Request request) {

        try {

            PurchaseRequest purchase =
                    (PurchaseRequest) request.getData();

            ItemDAO itemDAO = new ItemDAO();

            boolean success =
                    itemDAO.reduceShelfStock(
                            purchase.getItemCode(),
                            purchase.getQuantity());

            if (success) {

                return new Response(
                        "SUCCESS",
                        "Purchase completed successfully",
                        null
                );

            } else {

                return new Response(
                        "ERROR",
                        "Insufficient stock",
                        null
                );
            }

        } catch (Exception e) {

            return new Response(
                    "ERROR",
                    e.getMessage(),
                    null
            );
        }
    }
}
