package lk.ijse.chat_room.controller;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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

    public void initialize(){

        try{
            server = new Server(new ServerSocket(5000));

        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }

        vbox_msgs.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(oldValue);
                System.out.println(newValue);
                scrollPane.setVvalue((Double) newValue);
            }
        });

        server.receiveMessagesFromClients(vbox_msgs);
    }

    /*private void getClientMessages(VBox vbox_msgs) {
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
    }*/

    public void sendMessageOnClick(MouseEvent mouseEvent) throws IOException {
        String message = txtMessageBox.getText();
        if (!message.isEmpty()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5,5,5,10));

            Text msgText = new Text(message);
            TextFlow textFlow = new TextFlow(msgText);
            textFlow.setStyle("-fx-background-color: #9b59b6; -fx-background-radius: 10 10 0 10");
            textFlow.setPadding(new Insets(5,10,5,10));
            msgText.setFill(Color.WHITE);

            hBox.getChildren().add(textFlow);
            vbox_msgs.getChildren().add(hBox);

            server.sendMessageToClient(message);
            txtMessageBox.clear();
        }
    }

    /*private void sendMessageToClients(String message) throws IOException {
        PrintWriter printWriter = new PrintWriter(accept.getOutputStream());
        printWriter.println(message);
        printWriter.flush(); // will go to clients

        txtMessageBox.clear();
    }*/

    public static void displayMsgOnChat(String msgFromClient, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text msgText = new Text(msgFromClient);
        TextFlow textFlow = new TextFlow(msgText);
        textFlow.setStyle("-fx-text-fill: #fff; -fx-background-color: #7f8c8d; -fx-background-radius: 10 10 10 0");
        textFlow.setPadding(new Insets(5,10,5,10));
        msgText.setFill(Color.WHITE);

        hBox.getChildren().add(textFlow);

        // javafx gui can be updated only with javafx application thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

}
