package com.example.christ_javafx;


import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleRegister() {
        String user = this.usernameField.getText();
        String pass = this.passwordField.getText();
        String confirm = this.confirmPasswordField.getText();
        if (pass.equals(confirm)) {
            System.out.println("Registrasi sukses untuk: " + user);
            Apps.showLogin();
        } else {
            System.out.println("Password tidak cocok");
        }

    }

    @FXML
    private void goToLogin() {
        Apps.showLogin();
    }
}

