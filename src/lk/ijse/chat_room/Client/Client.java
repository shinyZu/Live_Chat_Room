package lk.ijse.chat_room.Client;

import javafx.scene.layout.VBox;
import lk.ijse.chat_room.controller.ClientFormController;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    private VBox vBox;

    public Client(Socket socket, String username, VBox vBox) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            this.vBox = vBox;

        } catch (IOException e) {
            closeAll(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    // To Server
    public void sendMessage(String msgToSend, VBox vBox) {
        new Thread(() -> {
            try {
                this.bufferedWriter.write(username);
                this.bufferedWriter.newLine(); // says this is the end of the message
                this.bufferedWriter.flush();

//                if (msgToSend.startsWith("/quit")) {
                if (msgToSend.contains("left")) {
//                    this.bufferedWriter.write(username+ " has left the meeting!");
                    this.bufferedWriter.write(msgToSend);

                } else if (msgToSend.contains(" has joined")) {
                    this.bufferedWriter.write(msgToSend);

                } else {
//                    this.bufferedWriter.write(username);
//                    this.bufferedWriter.newLine(); // says this is the end of the message
                    this.bufferedWriter.flush();

                    this.bufferedWriter.write(username + " : " + msgToSend);
//                    this.bufferedWriter.write(" :   " + msgToSend);
                }

                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();

//            ClientFormController.displayMsgOnLeft(this.bufferedReader.readLine(), vBox);

            } catch (IOException e) {
                closeAll(this.socket, this.bufferedReader, this.bufferedWriter);
            }
        }).start();

    }

    // msgs from TestServer & other broadcast msgs from employees
    public void listenForMessage(VBox vBox) {
        System.out.println("inside listenForMessage");
        new Thread(() -> {
            String msgFromChat = null;
            while (socket.isConnected()) {
                try {
                    msgFromChat = bufferedReader.readLine();
                    String[] strings = msgFromChat.split(":");

                    if (strings.length == 2 || msgFromChat.contains(" has joined") || msgFromChat.contains("left") ) {
                        ClientFormController.displayMsgOnLeft(msgFromChat, vBox);
                    }

                } catch (IOException e) {
                    closeAll(socket, bufferedReader, bufferedWriter);
                    break; // to avoid executing error again and again
                }
            }
        }).start();
    }

    public void leaveFromChat(){

    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

