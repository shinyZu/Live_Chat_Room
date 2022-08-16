package lk.ijse.chat_room.Client;

import javafx.scene.layout.VBox;
import lk.ijse.chat_room.controller.LoginFormController;
import lk.ijse.chat_room.controller.ServerFormController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    // to keep track of all the employees in the live chat
    public static ArrayList<ClientHandler> allClients = new ArrayList<>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private PrintWriter out;
    private VBox vBox; // server_vBox

    private String username;

    public ClientHandler(Socket socket, VBox vBox) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.username = bufferedReader.readLine();
            this.vBox = vBox;

            allClients.add(this);

            // from TestServer to the group chat
//            System.out.println("before broadcastMessage");
//            broadcastMessage("SERVER: " + this.username + " has joined the chat");
//            System.out.println("after broadcastMessage");

        } catch (IOException e) {
            closeAll(this.socket, this.bufferedReader, this.bufferedWriter);
        }

    }

    @Override
    public void run() {
        ServerFormController.displayMsgOnLeft(username+" has joined the chat", vBox);
        String msgFromClient;

        while (socket.isConnected()) {
            try {
                // the program will halt here until it receives a msg from client--> hence we run it in a separate thread
                // so the rest of the program will not be stuck/blocked bcz this is a blocking operation
                msgFromClient = bufferedReader.readLine();
                if (msgFromClient.contains("left")) {
                    System.out.println("remove the client");
                    removeClientFromChat();
                }
                broadcastMessage(msgFromClient);

            } catch (IOException e) {
                closeAll(this.socket, this.bufferedReader, this.bufferedWriter);
                break;
            }
        }
    }

    // To send a msg(either from server or an employee) to everyone in the group chat
    public void broadcastMessage(String msgToBroadcast) {
        for (ClientHandler client : allClients) {
            try {
                // if it doesn't equals to an employee in the arraylist--> then broadcast the msg to those employees
                // broadcast the msg to all other employees except who sent the msg
                if (!client.username.equals(username)) {

                    client.bufferedWriter.write(msgToBroadcast);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                    System.out.println();
                }

            } catch (IOException e) {
                closeAll(this.socket, this.bufferedReader, this.bufferedWriter);
            }
        }
    }

    public void removeClientFromChat() {
        allClients.remove(this);
//        broadcastMessage("SERVER: " + username + " has left the chat");
        ServerFormController.displayMsgOnLeft(this.username+ " has left the chat!", vBox);
        closeAll(this.socket, this.bufferedReader, this.bufferedWriter);

    }

//    public void disconnectClient(){
//        closeAll(this.socket, this.bufferedReader, this.bufferedWriter);
//    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
//        removeClientFromChat();
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }
}
