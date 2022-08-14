package lk.ijse.chat_room.controller;

import de.jensd.fx.glyphs.emojione.EmojiOneView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private Socket socket = null;

   /* public void initialize() throws IOException {
        socket = new Socket("localhost", 5000);

        new Thread(() -> {
            try{
                while (!socket.isClosed()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line = bufferedReader.readLine();
                    System.out.println("Server : " + line);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }).start();

    }*/

    public void sendMessageOnClick(MouseEvent mouseEvent) {
    }

    public void displayEmojisOnClick(MouseEvent mouseEvent) {
    }

    public void openCameraOnClick(MouseEvent mouseEvent) {
    }

    public void displayFilesOnClick(MouseEvent mouseEvent) {
    }
}
