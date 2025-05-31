package com.example.christ_javafx;

import Data.SqlDriver;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleRegister() {
        String user = this.usernameField.getText().trim();
        String pass = this.passwordField.getText().trim();
        String confirm = this.confirmPasswordField.getText().trim();

        if (!pass.equals(confirm)) {
            System.out.println("❌ Password tidak cocok");
            return;
        }

        try (Connection conn = SqlDriver.getInstance().getConnection()) {
            // Cek apakah username sudah ada
            String checkSql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, user);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("❌ Username sudah digunakan");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Username sudah dipakai");
                    alert.setContentText("Username sudah dipakai, ganti username");
                    alert.showAndWait();
                    return;
                }
            }

            // Simpan akun baru
            String insertSql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, user);
                insertStmt.setString(2, pass); // Disarankan di-hash
                insertStmt.setString(3, user + "@dummy.email"); // Tambahkan input field email jika ingin real
                insertStmt.executeUpdate();
                System.out.println("✅ Registrasi sukses untuk: " + user);
                Apps.showLogin(); // Arahkan ke login
            }

        } catch (SQLException e) {
            System.out.println("❌ Gagal registrasi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLogin() {
        Apps.showLogin();
    }
}

//package com.example.christ_javafx;
//
//
//import javafx.fxml.FXML;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//
//public class RegisterController {
//    @FXML
//    private TextField usernameField;
//    @FXML
//    private PasswordField passwordField;
//    @FXML
//    private PasswordField confirmPasswordField;
//
//    @FXML
//    private void handleRegister() {
//        String user = this.usernameField.getText();
//        String pass = this.passwordField.getText();
//        String confirm = this.confirmPasswordField.getText();
//        if (pass.equals(confirm)) {
//            System.out.println("Registrasi sukses untuk: " + user);
//            Apps.showLogin();
//        } else {
//            System.out.println("Password tidak cocok");
//        }
//
//    }
//
//    @FXML
//    private void goToLogin() {
//        Apps.showLogin();
//    }
//}
//
