package com.example.checkitoff;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setMinWidth(900);
        stage.setMinHeight(400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        Stage stage1 = new Stage();
        FXMLLoader fxmlLoader1 = new FXMLLoader(HelloApplication.class.getResource("signup.fxml"));
        Scene scene1 = new Scene(fxmlLoader1.load());
        stage1.setScene(scene1);
        stage1.show();
    }

    public static void main(String[] args) {
        launch();
    }
}