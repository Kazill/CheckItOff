package com.example.checkitoff;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button button_logout;

    @FXML
    Label label_welcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "account.fxml", "Log in!", null);
                DBUtils.closeWindow(event, "hello-view.fxml");
            }
        });

    }

    public void setUserInformation(String username)
    {
        label_welcome.setText("Welcome " + username + "!");
    }
}
