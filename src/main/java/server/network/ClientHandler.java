package server.network;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket clientSocket;
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            //read message from client
            String request = reader.readLine();
            System.out.println("Client request: " + request);

            //temporary response
            writer.println("Server received: " + request);

            clientSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
