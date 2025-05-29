package com.example.christ_javafx;

import Data.SqlDriver;
import Data.Transaksi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;


import java.io.IOException;
import java.sql.*;

public class DataTransaksiController {

    @FXML
    private TableView<Transaksi> tableTransaksi;
    @FXML
    private TableColumn<Transaksi, Integer> colId;
    @FXML
    private TableColumn<Transaksi, String> colTanggal;
    @FXML
    private TableColumn<Transaksi, String> colKategori;
    @FXML
    private TableColumn<Transaksi, String> colJenis;
    @FXML
    private TableColumn<Transaksi, Double> colJumlah;
    @FXML
    private TableColumn<Transaksi, String> colCatatan;

    @FXML
    public void handleTambahTransaksi() {
        Apps.showtambahTransaksi();
    }

    @FXML
    public void handleDataTransaksi() {
        Apps.showdataTransaksi();
    }

    @FXML
    public void handleBatasPengeluaran() {
        Apps.showbatasPengeluaran();
    }

    @FXML
    public void handleFilterTransaksi() {
        Apps.showfilterKategori();
    }

    @FXML
    private void handleedit(MouseEvent event) {
        if (event.getClickCount() == 2) { // double click
            Transaksi selected = tableTransaksi.getSelectionModel().getSelectedItem();
            System.out.println("Table clicked"); // ← Tambahkan ini untuk tes
            if (selected != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/christ_javafx/edit.fxml"));
                    Parent root = loader.load();

                    EditController controller = loader.getController();
                    controller.setTransaksi(selected);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Edit Transaksi");
                    stage.initModality(Modality.APPLICATION_MODAL); // blok window sebelumnya
                    stage.showAndWait();

                    // refresh table setelah edit
                    loadData(); // method kamu untuk reload isi tabel

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void handleRiwayatEdit() {
        Apps.showRiwayatEdit();
    }

    @FXML
    public void handleBeranda() {
        Apps.showberanda();
    }

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
    public void initialize() {
        // Pasang property ke kolom
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colTanggal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTanggal()));
        colKategori.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKategori()));
        colJenis.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getJenis()));
        colJumlah.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getJumlah()).asObject());
        colCatatan.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCatatan()));

        loadData();
    }

    private void loadData() {
        ObservableList<Transaksi> transaksiList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM transaksi WHERE user_id = 1")) {

            if (rs != null) {
                while (rs.next()) {
                    Transaksi t = new Transaksi(
                            rs.getInt("id_t"),
                            rs.getString("tgl_transaksi"),
                            rs.getString("kategori"),
                            rs.getString("type"),
                            rs.getDouble("amount"),
                            rs.getString("note")
                    );
                    transaksiList.add(t);
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Tidak Ditemukan");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
