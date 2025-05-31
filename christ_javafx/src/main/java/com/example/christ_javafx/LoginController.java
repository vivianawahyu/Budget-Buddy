package com.example.christ_javafx;

import Data.SqlDriver;
import Data.SessionManager;
import Data.SessionStorage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String user = this.usernameField.getText().trim();
        String pass = this.passwordField.getText().trim();

        try (Connection conn = SqlDriver.getInstance().getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user);
                stmt.setString(2, pass); // Disarankan untuk pakai hashing di produksi
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt("id_u");
                    String username = rs.getString("username");
                    System.out.println("✅ Login sukses sebagai " + user);

                    SessionManager.getInstance().login(userId, username);
                    SessionStorage.saveSession(userId, username);

                    // Simpan ID pengguna ke sesi global jika perlu
                    Apps.showberanda(); // Ganti dengan tampilan utama aplikasi Anda
                } else {
                    System.out.println("❌ Login gagal");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Username belum terdaftar");
                    alert.setContentText("Registrasi terlebih dahulu");
                    alert.showAndWait();
                    return;
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Gagal mengakses database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goToRegister() {
        Apps.showRegister();
    }
}

//package com.example.christ_javafx;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//
//public class LoginController {
//    @FXML
//    private TextField usernameField;
//    @FXML
//    private PasswordField passwordField;
//
//    @FXML
//    private void handleLogin() {
//        String user = this.usernameField.getText();
//        String pass = this.passwordField.getText();
//        if (user.equals("admin") && pass.equals("123")) {
//            System.out.println("Login sukses!");
//        } else {
//            System.out.println("Login gagal!");
//        }
//
//    }
//
//    @FXML
//    private void goToRegister() {
//        Apps.showRegister();
//    }
//}
