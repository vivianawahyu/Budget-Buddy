package com.example.christ_javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LupaPasswordController {

        @FXML
        private TextField inputUsername;

        @FXML
        private PasswordField inputPasswordBaru;

        @FXML
        private PasswordField inputPasswordKonfirmasi;

        @FXML
        public void handleResetPassword() {
            String username = inputUsername.getText().trim();
            String passwordBaru = inputPasswordBaru.getText();
            String konfirmasiPassword = inputPasswordKonfirmasi.getText();

            // Validasi awal
            if (username.isEmpty() || passwordBaru.isEmpty() || konfirmasiPassword.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input tidak lengkap", "Mohon isi semua kolom.");
                return;
            }

            if (!passwordBaru.equals(konfirmasiPassword)) {
                showAlert(Alert.AlertType.WARNING, "Password tidak cocok", "Password baru dan konfirmasi tidak sama.");
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db")) {
                // Cek apakah username ada
                String cekQuery = "SELECT * FROM users WHERE username = ?";
                PreparedStatement cekStmt = conn.prepareStatement(cekQuery);
                cekStmt.setString(1, username);
                ResultSet rs = cekStmt.executeQuery();

                if (rs.next()) {
                    // Update password
                    String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setString(1, passwordBaru);
                    updateStmt.setString(2, username);

                    int updated = updateStmt.executeUpdate();
                    if (updated > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Password berhasil diperbarui.");
                        clearFields();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui password.");
                    }

                } else {
                    showAlert(Alert.AlertType.WARNING, "Username tidak ditemukan", "Pastikan username yang dimasukkan benar.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan: " + e.getMessage());
            }
        }

        private void showAlert(Alert.AlertType type, String title, String content) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }

        private void clearFields() {
            inputUsername.clear();
            inputPasswordBaru.clear();
            inputPasswordKonfirmasi.clear();
        }

        @FXML
        private void goToRegister() {
            Apps.showRegister();
        }

        @FXML
        private void goToLogin() {Apps.showLogin();}
}

