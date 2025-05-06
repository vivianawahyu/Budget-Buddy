package com.example.christ_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String user = this.usernameField.getText();
        String pass = this.passwordField.getText();
        if (user.equals("admin") && pass.equals("123")) {
            System.out.println("Login sukses!");
        } else {
            System.out.println("Login gagal!");
        }

    }

    @FXML
    private void goToRegister() {
        Apps.showRegister();
    }
}
