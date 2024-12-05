package com.example.studentutilitysoftware;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Home extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Home.class.getResource("Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1105, 700);
        stage.setTitle("WELCOME!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}