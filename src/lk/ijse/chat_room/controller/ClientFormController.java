package lk.ijse.chat_room.controller;

import de.jensd.fx.glyphs.emojione.EmojiOneView;
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
import lk.ijse.chat_room.Client.Client;
import lk.ijse.chat_room.Server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFormController {
    @FXML
    public MaterialDesignIconView btnSend;
    @FXML
    public TextField txtMessageBox;
    @FXML
    public EmojiOneView btnEmoji;
    @FXML
    public MaterialDesignIconView btnCamera;
    @FXML
    public MaterialDesignIconView btnAttachment;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public VBox vbox_msgs;

    private Client client;

    public void initialize() throws IOException {
        try{
            client = new Client(new Socket("localhost",5000));
            System.out.println("Connected to Server");
        } catch (IOException e){
            e.printStackTrace();
        }

        vbox_msgs.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(oldValue);
                System.out.println(newValue);
                scrollPane.setVvalue((Double) newValue); //newHeight
            }
        });

        client.receiveMessagesFromServer(vbox_msgs);
    }


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

            client.sendMessageToServer(message);
            txtMessageBox.clear();

        }
    }

    public void displayEmojisOnClick(MouseEvent mouseEvent) {
    }

    public void openCameraOnClick(MouseEvent mouseEvent) {
    }

    public void displayFilesOnClick(MouseEvent mouseEvent) {
    }

    public static void displayMsgOnChat(String msgFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text msgText = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(msgText);
        textFlow.setStyle("-fx-color:#fff; -fx-background-color: #7f8c8d; -fx-background-radius: 10 10 10 0");
        textFlow.setPadding(new Insets(5,10,5,10));
//        msgText.setFill(Color.color(0.934,0.945,0.996));
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
