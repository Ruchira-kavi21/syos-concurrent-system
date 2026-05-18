package server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static final int PORT = 5000;

    // thread pool to handle multiple client connections concurrently
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println("Syos Server is starting...");

        try (ServerSocket  serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // handle each client connection in a separate thread
                threadpool.execute(new ClientHandler(clientSocket));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
