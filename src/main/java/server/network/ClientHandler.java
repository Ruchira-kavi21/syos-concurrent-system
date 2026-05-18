package server.network;
import shared.dto.Request;
import shared.dto.Response;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket clientSocket;
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run(){
        try {
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());

            Request request = (Request) input.readObject();

            System.out.println("Action: " + request.getAction());
            System.out.println("Data: " + request.getData());

            Response response = new Response("success", "Request processed successfully");
            output.writeObject(response);
            clientSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
