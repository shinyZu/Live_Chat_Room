package lk.ijse.chat_room.controller;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lk.ijse.chat_room.Server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFormController {

    @FXML
    public ScrollPane scrollPane;
    @FXML
    public VBox vbox_msgs;
    @FXML
    public MaterialDesignIconView btnSend;
    @FXML
    public TextField txtMessageBox;

    private Socket accept;
    private Server server;

    public void initialize() throws IOException {
        vbox_msgs.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(oldValue);
                System.out.println(newValue);
                scrollPane.setVvalue((Double) newValue);
            }
        });

        getClientMessages(vbox_msgs);
    }

    private void getClientMessages(VBox vbox_msgs) throws IOException {
        new Thread(()->{
            try {
                ServerSocket serverSocket = new ServerSocket(5000);
                System.out.println("Server Started...");

                accept = serverSocket.accept();
                System.out.println("Client Connected!!!");

                while(!serverSocket.isClosed()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(accept.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line = bufferedReader.readLine();
                    System.out.println("Client : "+line);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public void sendMessageOnClick(MouseEvent mouseEvent) throws IOException {
        PrintWriter printWriter = new PrintWriter(accept.getOutputStream());
        printWriter.println(txtMessageBox.getText());
        printWriter.flush(); // will go to server

        String message = txtMessageBox.getText();
        if (!message.isEmpty()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
        }
    }

}
