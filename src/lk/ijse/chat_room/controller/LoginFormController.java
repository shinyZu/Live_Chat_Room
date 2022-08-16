package lk.ijse.chat_room.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    @FXML
    public JFXButton btnJoinChat;
    @FXML
    public JFXTextField txtUsername;

    public static String username;

    public void joinChatOnAction(ActionEvent actionEvent) throws IOException {
        username = txtUsername.getText();
//        System.out.println("inside LoginFormController : "+username);

        if (!username.isEmpty()){
            Stage window = new Stage();
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/ClientForm.fxml"))));
            window.setTitle(username);
            Stage stage = (Stage) btnJoinChat.getScene().getWindow();
            stage.close();
            window.show();
        }
    }
}
