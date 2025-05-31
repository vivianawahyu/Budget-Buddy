package com.example.christ_javafx;

import Data.SessionManager;
import Data.SqlDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TambahTransaksiController {

    @FXML
    private TextField idCatatan;

    @FXML
    private ComboBox<String> idKategori;

    @FXML
    private TextField idNominal;

    @FXML
    private RadioButton idPemasukan;

    @FXML
    private RadioButton idPengeluaran;

    @FXML
    private ComboBox<Number> idUserId;

    @FXML
    private DatePicker idTanggal;

    @FXML
    private ToggleGroup jenisTransaksi;

    @FXML
    void handleLogOut(ActionEvent event) {
        try {
            // Load tampilan Login
            Parent loginView = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene loginScene = new Scene(loginView);

            // Ambil stage saat ini dari button yang ditekan
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set scene baru
            window.setScene(loginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleTambahTransaksi() {Apps.showtambahTransaksi();}

    @FXML
    public void handleDataTransaksi() {Apps.showdataTransaksi();}


    @FXML
    public void handleBatasPengeluaran() {Apps.showbatasPengeluaran();}

    @FXML
    public void handleFilterTransaksi() {Apps.showfilterKategori();}

    @FXML
    public void handleRiwayatEdit() {Apps.showRiwayatEdit();}

    @FXML
    public void handleBeranda() {Apps.showberanda();}

    @FXML
    private void initialize() {
        idKategori.getItems().addAll(
                "Gaji", "Tabungan", "Belanja", "Makanan & Minuman");
    }


    @FXML
    public void handleReset(ActionEvent event) {
        jenisTransaksi.selectToggle(null);
        idTanggal.setValue(null);
        idKategori.getSelectionModel().clearSelection();
        idNominal.clear();
        idCatatan.clear();
    }

    @FXML
    public void handleSimpan(ActionEvent event) {
        Alert alert;
        String kategori = idKategori.getValue();
        String type = idPemasukan.isSelected() ? "Pemasukan" : "Pengeluaran";
        double amount = Double.parseDouble(idNominal.getText());
        String note = idCatatan.getText();
        String tgl_transaksi = idTanggal.getValue() != null ? idTanggal.getValue().toString() : null;

        if (kategori == null || type == null || tgl_transaksi == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Eror");
            alert.setContentText("Harus di isi semua");
            alert.showAndWait();
            return;
        }
        try (Connection conn = SqlDriver.getInstance().getConnection()) {
            String sql = "INSERT INTO transaksi (kategori, type, amount, note, tgl_transaksi, user_id) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kategori);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.setString(4, note);
            stmt.setString(5, tgl_transaksi);

            int userId = SessionManager.getInstance().getUserId();
            System.out.println("[DEBUG] userId yang akan disimpan: " + userId);
            stmt.setInt(6, userId);

            stmt.executeUpdate();
            conn.close();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Transaksi berhasil");
            alert.setContentText("Transaksi berhasil disimpan");
            alert.showAndWait();
            handleReset(null);
        } catch (NumberFormatException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Eror");
            alert.setContentText("Nominal harus berupa angka cth(1000000)");
        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Eror");
            alert.setContentText("Gagal menyimpan data " + e.getMessage());
        }
    }


}

