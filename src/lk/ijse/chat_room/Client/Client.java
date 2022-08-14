package lk.ijse.chat_room.Client;

import javafx.scene.layout.VBox;
import lk.ijse.chat_room.controller.ClientFormController;
import lk.ijse.chat_room.controller.ServerFormController;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        }  catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
            closeAll(this.socket,this.bufferedReader,this.bufferedWriter);
        }
    }

    public void sendMessageToServer(String message){
        try{
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine(); // says this is the end of the message
            this.bufferedWriter.flush();

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error Occurred While Delivering Message to Server");
            closeAll(this.socket,this.bufferedReader,this.bufferedWriter);
        }

    }

    public void receiveMessagesFromServer(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()) {
                    try {
                        String msgFromServer = bufferedReader.readLine();
                        ClientFormController.displayMsgOnChat(msgFromServer,vBox);
                        System.out.println("Server : "+msgFromServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error Occurred While Receiving Messages from Server");
                        closeAll(socket,bufferedReader,bufferedWriter);
                        break; // to avoid executing error again and again
                    }
                }
            }
        }).start();
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
