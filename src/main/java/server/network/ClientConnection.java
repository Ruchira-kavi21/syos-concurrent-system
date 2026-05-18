package server.network;

import shared.dto.Request;
import shared.dto.Response;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            Request request = new Request("Test", "hello from user client");
            output.writeObject(request);
            Response response = (Response) input.readObject();

            System.out.println("Status: " + response.getStatus());
            System.out.println("Message: " + response.getMessage());
            socket.close();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
