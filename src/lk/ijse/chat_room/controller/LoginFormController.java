package lk.ijse.chat_room.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public JFXButton btnJoinChat;

    public void joinChatOnAction(ActionEvent actionEvent) throws IOException {
        Stage window = new Stage();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/ClientForm.fxml"))));
        window.setTitle("Client");
        Stage stage = (Stage) btnJoinChat.getScene().getWindow();
        stage.close();
        window.show();
    }
}
