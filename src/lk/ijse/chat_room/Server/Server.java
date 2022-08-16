package lk.ijse.chat_room.Server;

import javafx.scene.layout.VBox;
import lk.ijse.chat_room.Client.ClientHandler;
import lk.ijse.chat_room.controller.ServerFormController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Server {

    private final ServerSocket serverSocket;
    private Socket socket;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private ExecutorService pool;
    private boolean done;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.done = false;
    }

    public void startServer(VBox server_vBox) {
//        System.out.println("inside startServer");
        try {
            while (!done) {
                socket = serverSocket.accept();
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                System.out.println("A new employee has entered the chat");

//                ServerFormController.displayMsgOnLeft("A new employee has joined the chat", server_vBox);

                ClientHandler clientHandler = new ClientHandler(socket, server_vBox);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        done = true;
        try {
            if (serverSocket != null) serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
