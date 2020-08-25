package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {

    private static final int port = 8081;
    public static final List<String> commands = new ArrayList<>();
    public static List<Integer> usedIndexes = new ArrayList<>();

    static {
        for (int counter = 0; counter < 25; counter++) {
            String command = ServerCommand.generateRandom();
            commands.add(command);
        }
    }


    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(port);
        int clientNumber = 1;
        do {
            Socket client = listener.accept();
            ClientHandler clientHandler = new ClientHandler(client, clientNumber);
            Thread clientHandlerThread = new Thread(clientHandler);
            clientHandlerThread.start();
            clientNumber++;
        } while (clientNumber != 3);
    }
}
