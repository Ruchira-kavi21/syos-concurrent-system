package client.testclient;
import shared.dto.PurchaseRequest;
import shared.dto.Request;
import shared.dto.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class FastClient1 {
    public static void main(String[] args) {

        for (int i = 1; i <= 20; i++) {

            int requestNumber = i;

            new Thread(() -> {

                try {

                    Socket socket =
                            new Socket("localhost", 5000);

                    ObjectOutputStream output =
                            new ObjectOutputStream(
                                    socket.getOutputStream());

                    ObjectInputStream input =
                            new ObjectInputStream(
                                    socket.getInputStream());

                    PurchaseRequest purchase =
                            new PurchaseRequest(
                                    "B001",
                                    1);

                    Request request =
                            new Request(
                                    "BUY_ITEM",
                                    purchase);

                    output.writeObject(request);

                    Response response =
                            (Response) input.readObject();

                    System.out.println(
                            "Request " + requestNumber + ": " + response.getStatus()
                    );

                    socket.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
        }
    }
}
