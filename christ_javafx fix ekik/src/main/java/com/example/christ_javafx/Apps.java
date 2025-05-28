package com.example.christ_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Apps extends Application {
    private static Stage primaryStage;

    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLogin();
    }

    public static void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("login.fxml"));
            Scene scene = new Scene((Parent)loader.load(), (double)400.0F, (double)300.0F);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("register.fxml"));
            Scene scene = new Scene((Parent)loader.load(), (double)400.0F, (double)350.0F);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Register");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
