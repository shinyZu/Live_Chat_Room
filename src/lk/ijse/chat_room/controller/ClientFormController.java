package lk.ijse.chat_room.controller;

import de.jensd.fx.glyphs.emojione.EmojiOneView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import lk.ijse.chat_room.Client.Client;

import java.io.File;
import java.io.IOException;
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
    @FXML
    public Label lblClient;
    @FXML
    public MaterialDesignIconView btnLeaveChat;

    private ImageView myImageView;

    private File file;

    private Client client;
//    private TestClient client;

    public void initialize() {
        System.out.println("inside initialize()");
        new Thread(() -> {
            try {
                lblClient.setText(LoginFormController.username);
                client = new Client(new Socket("localhost", 5000), LoginFormController.username, vbox_msgs);
                System.out.println("Connected to Server");

                client.listenForMessages(vbox_msgs);

//                if (file == null) {
//                    client.listenForMessages(vbox_msgs);
//                } else {
//                    client.listenForImages(vbox_msgs);
//                }

//                System.out.println(file);
//                if (file != null) {
//                }

                client.sendMessage(LoginFormController.username + " has joined the chat!", vbox_msgs);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        vbox_msgs.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue); //newHeight
            }
        });

        /*new Thread(()->{
            try{
                client = new TestClient(new Socket("localhost",5000));

            } catch (IOException e){
                e.printStackTrace();
            }
            vbox_msgs.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    scrollPane.setVvalue((Double) newValue); //newHeight
                }
            });

            client.receiveMessagesFromServer(vbox_msgs);
        }).start();*/

    }

    public void sendMessageOnClick(MouseEvent mouseEvent) throws IOException {
        /*if (!messageToSend.isEmpty()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5,5,5,10));

            Text msgText = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(msgText);
            textFlow.setStyle("-fx-background-color: #9b59b6; -fx-background-radius: 10 10 0 10");
            textFlow.setPadding(new Insets(5,10,5,10));
            msgText.setFill(Color.WHITE);

            hBox.getChildren().add(textFlow);
            vbox_msgs.getChildren().add(hBox);

//            client.sendMessageToServer(messageToSend);
            client.sendMessage(messageToSend);
            txtMessageBox.clear();

        }*/
        String messageToSend = txtMessageBox.getText();
        displayMessageOnRight(messageToSend);
        client.sendMessage(messageToSend, vbox_msgs);
//        client.sendMessageToServer(messageToSend);
        txtMessageBox.clear();
    }

    public void displayMessageOnRight(String messageToSend) {
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text msgText = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(msgText);
            textFlow.setStyle("-fx-background-color: #9b59b6; -fx-background-radius: 10 10 0 10");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            msgText.setFill(Color.WHITE);

            hBox.getChildren().add(textFlow);
            vbox_msgs.getChildren().add(hBox);
        }
    }

    public static void displayMsgOnLeft(String message, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text msgText = new Text(message);
        TextFlow textFlow = new TextFlow(msgText);
        textFlow.setStyle("-fx-color:#fff; -fx-background-color: #7f8c8d; -fx-background-radius: 10 10 10 0");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
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

    public void displayEmojisOnClick(MouseEvent mouseEvent) {
    }

    public void openCameraOnClick(MouseEvent mouseEvent) {
    }

    public void sendImagesOnClick(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");

        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
         file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
//            System.out.println(file.toURI().toString());
            myImageView = new ImageView(image);
            myImageView.setFitHeight(100);
            myImageView.setFitWidth(100);

            displayImageOnRight();
//            System.out.println("before calling sendImage");
            client.sendImage(file, vbox_msgs);
//            System.out.println("after calling sendImage");
        }
    }

    public void displayImageOnRight() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(10, 5, 5, 10));

//        Text employeeName = new Text(LoginFormController.username);
        Text text = new Text("");
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: #9b59b6; -fx-background-radius: 10 0 0 10");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.WHITE);

        hBox.getChildren().add(textFlow);
        hBox.getChildren().add(myImageView);
        vbox_msgs.getChildren().add(hBox);
    }

    public static void displayImageOnLeft(String sendersName, Image img, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        System.out.println("sendersName------------ "+sendersName);
        Text employeeName = new Text(sendersName);
        TextFlow textFlow = new TextFlow(employeeName);
        textFlow.setStyle("-fx-color:#fff; -fx-background-color: #7f8c8d; -fx-background-radius: 10 0 0 10");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        employeeName.setFill(Color.WHITE);

        hBox.getChildren().add(textFlow);

        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        hBox.getChildren().add(imageView);

        // javafx gui can be updated only with javafx application thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    public void leaveChatOnClick(MouseEvent mouseEvent) throws IOException {
        System.out.println("clicked to logout");
        client.sendMessage(LoginFormController.username + " has left the chat!", vbox_msgs);

//        Stage stage = (Stage) btnLeaveChat.getScene().getWindow();
//        stage.close();

        Platform.exit();
    }
}
