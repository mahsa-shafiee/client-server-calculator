package client;

import org.apache.log4j.Logger;

public class ClientMain {

    private static final String serverIP = "localhost";
    private static final int serverPort = 8081;
    private static Logger logger;

    public static void main(String[] args) {

        for (int counter = 1; counter <= 2; counter++) {
            logger = Logger.getLogger(Client.class.getName() + ".client" + counter);
            Client client = new Client(serverIP, serverPort, logger);
            Thread clientThread = new Thread(client);
            clientThread.start();
        }
    }
}