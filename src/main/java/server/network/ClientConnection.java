package server.network;

import server.domain.Item;
import shared.dto.Request;
import shared.dto.Response;
import shared.dto.PurchaseRequest;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientConnection {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            PurchaseRequest purchase = new PurchaseRequest("B001", 12);

            Request request = new Request("BUY_ITEM", purchase);
            output.writeObject(request);
            Response response = (Response) input.readObject();

            System.out.println("Status: " + response.getStatus());
            System.out.println("Message: " + response.getMessage());
            if (response.getData() != null){
                List<Item> items = (List<Item>) response.getData();
                for (Item item : items){
                    System.out.println(item.getCode() + "|" + item.getName() + "|" + item.getPrice());
                }
            }
            socket.close();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
