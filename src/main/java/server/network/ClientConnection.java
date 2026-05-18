package server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader reader = new BufferedReader( new InputStreamReader(socket.getInputStream()));

            writer.println("Hello from client!");
            String response = reader.readLine();
            System.out.println("Response from server: " + response);
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
