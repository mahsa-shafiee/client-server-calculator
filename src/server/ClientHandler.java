package server;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

import static server.ServerMain.commands;
import static server.ServerMain.usedIndexes;


public class ClientHandler implements Runnable {

    private Socket client;
    private Scanner in;
    private Formatter out;
    private int clientNumber;

    public ClientHandler(Socket client, int clientNumber) throws IOException {
        this.clientNumber = clientNumber;
        this.client = client;
        this.in = new Scanner(client.getInputStream());
        this.out = new Formatter(client.getOutputStream());
        out.format(clientNumber + "\n");
    }

    @Override
    public void run() {
        synchronized (commands) {
            outer:
            while (commands.size() != 0) {
                try {
                    for (int index = clientNumber % 2; index < commands.size(); index += 2) {
                        if (!usedIndexes.contains(index)) {
                            boolean firstCondition = index % 2 == 1 && !usedIndexes.contains(index - 1);
                            boolean secondCondition = index % 2 == 0 && index != 0 && !usedIndexes.contains(index + 1);
                            if (index == commands.size() - 1) {
                                finishCommunication(index);
                                break outer;
                            } else if (firstCondition || secondCondition)
                                commands.wait();
                            communicateWithClient(index);
                            commands.wait(12000);
                            commands.notify();
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            in.close();
            out.close();
        }
    }

    private void finishCommunication(int index) throws InterruptedException {
        commands.wait(12000);
        communicateWithClient(index);
        System.out.println("finished!");
        commands.clear();
    }

    private void communicateWithClient(int index) {
        int commandNumber = sendCommandToClient(index);
        getFilePathFromClient();
        sendConfirmationMessage(commandNumber);
    }

    private void sendConfirmationMessage(int commandNumber) {
        out.format("The file path of command " + commandNumber + " successfully received!\n");
        out.flush();
    }

    private void getFilePathFromClient() {
        String path = in.nextLine();
        System.out.println(path);
    }

    private int sendCommandToClient(int commandIndex) {
        String command;
        command = commands.get(commandIndex);
        usedIndexes.add(commandIndex);
        out.format(command + "\n");
        int commandNumber = commands.indexOf(command) + 1;
        out.format(commandNumber + "\n");
        out.flush();
        return commandNumber;
    }

}
