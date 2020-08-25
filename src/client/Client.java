package client;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

import static server.ServerMain.commands;

public class Client implements Runnable {

    private String serverIP;
    private int serverPort;
    private Logger logger;

    public Client(String serverIP, int serverPort, Logger logger) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.logger = logger;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(serverIP, serverPort);
             Scanner socketIn = new Scanner(socket.getInputStream());
             Formatter socketOut = new Formatter(socket.getOutputStream());
        ) {
            String clientNumber = socketIn.nextLine();
            this.setLogger(clientNumber);
            int numberOfClientCommands = getNumberOfClientCommands(clientNumber);
            communicateWithServer(socketIn, socketOut, numberOfClientCommands);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void communicateWithServer(Scanner socketIn, Formatter socketOut, int numberOfClientCommands) {
        for (int counter = 0; counter < numberOfClientCommands; counter++) {
            String command = socketIn.nextLine();
            String commandNumber = socketIn.nextLine();
            writeLoggingMessageToFile(command, commandNumber);
            sendFilePathToServer(socketOut, commandNumber);
            getConfirmationMessageFromServer(socketIn);
        }
    }

    private void writeLoggingMessageToFile(String command, String commandNumber) {
        try {
            String operator = command.split(" ")[0];
            int firstOperand = Integer.parseInt(command.split(" ")[1]);
            int secondOperand = Integer.parseInt(command.split(" ")[2]);
            int answer = calculateCommand(operator, firstOperand, secondOperand);
            logger.info("Answer of command number " + commandNumber
                    + " <" + command + "> equals to " + answer);
        } catch (IllegalArgumentException e) {
            logger.error("command number " + commandNumber
                    + " <" + command + "> is invalid command");
        }
    }

    private String getFilePath() {
        FileAppender fileAppender = (FileAppender) logger.getAppender("fileAppender");
        return new File(fileAppender.getFile()).getAbsolutePath();
    }

    private void sendFilePathToServer(Formatter socketOut, String commandNumber) {
        String filePath = getFilePath();
        socketOut.format("command " + commandNumber + " :" + filePath + "\n");
        socketOut.flush();
    }

    private void getConfirmationMessageFromServer(Scanner socketIn) {
        String confirmationMessage = socketIn.nextLine();
        System.out.println(confirmationMessage);
    }

    private int getNumberOfClientCommands(String clientNumber) {
        int commandsOfEachClient;
        if (clientNumber.equals("1"))
            commandsOfEachClient = commands.size() / 2;
        else
            commandsOfEachClient = (commands.size() / 2) + 1;
        return commandsOfEachClient;
    }

    public static int calculateCommand(String operator, int firstOperand, int secondOperand) {
        int answer;
        switch (operator) {
            case "+":
                answer = (firstOperand + secondOperand);
                break;
            case "-":
                answer = (firstOperand - secondOperand);
                break;
            case "*":
                answer = (firstOperand * secondOperand);
                break;
            case "/":
                answer = (firstOperand / secondOperand);
                break;
            default:
                throw new IllegalArgumentException("invalid operator");
        }
        return answer;
    }

    public void setLogger(String clientNumber) {
        this.logger = Logger.getLogger(Client.class.getName() + ".client" + clientNumber);
    }
}