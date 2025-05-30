package com.example.christ_javafx;

import Data.Batas;
import Data.SessionManager;
import Data.SessionStorage;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class BatasPengeluaranController {

    @FXML
    private Label labelBatas;

    @FXML
    private TextField jumlahField;

    @FXML
    private ComboBox<String> kategoriComboBox;

    @FXML
    private TableView<Batas> batasTable;

    @FXML
    private TableColumn<Batas, Integer> colID;

    @FXML
    private TableColumn<Batas, String> colTanggal;

    @FXML
    private TableColumn<Batas, String> colKategori;

    @FXML
    private TableColumn<Batas, Double> colJumlah;

    @FXML
    private void initialize() {
        // Inisialisasi ComboBox dengan kategori dummy (nanti bisa diambil dari DB)
        kategoriComboBox.setItems(FXCollections.observableArrayList("Semua"));
        kategoriComboBox.getSelectionModel().selectFirst(); // default ke "Semua"

        colID.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colTanggal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTanggal()));
        colKategori.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKategori()));
        colJumlah.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getJumlah()).asObject());

        loadBatas(); // tampilkan data saat awal
        loadDataBatas();
        kategoriComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadDataBatas();
        });
    }


    @FXML
    private void handleSimpanBatas() {
        int userId = SessionManager.getInstance().getUserId();
        String kategori = kategoriComboBox.getValue();
        String jumlahText = jumlahField.getText();

        if (jumlahText == null || jumlahText.isEmpty()) {
            showAlert("Jumlah tidak boleh kosong");
            return;
        }

        double jumlah = Double.parseDouble(jumlahText);
        String tanggal = java.time.LocalDate.now().toString();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db")) {
            // Cek apakah sudah ada batas untuk kategori ini
            PreparedStatement check = conn.prepareStatement("SELECT id FROM batas_pengeluaran WHERE user_id = ? AND kategori = ?");
            check.setInt(1, userId);
            check.setString(2, kategori != null ? kategori : "Semua");
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                // Update jika sudah ada
                PreparedStatement update = conn.prepareStatement("UPDATE batas_pengeluaran SET jumlah = ?, tanggal = ? WHERE user_id = ? AND kategori = ?");
                update.setDouble(1, jumlah);
                update.setString(2, tanggal);
                update.setString(3, kategori != null ? kategori : "Semua");
                update.executeUpdate();
            } else {
                // Insert baru
                PreparedStatement ps = conn.prepareStatement("INSERT INTO batas_pengeluaran (kategori, jumlah, tanggal, user_id) VALUES (?, ?, ?, ?)");
                ps.setString(1, kategori != null ? kategori : "Semua");
                ps.setDouble(2, jumlah);
                ps.setString(3, tanggal);
                ps.setInt(4, userId); // user_id statis sementara
                ps.executeUpdate();
            }

            loadBatas();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Gagal menyimpan data");
        }
    }

    @FXML
    private void handleHapusBatas() {
        int userId = SessionManager.getInstance().getUserId();
        String kategori = kategoriComboBox.getValue(); // bisa null

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db")) {
            String query;
            PreparedStatement stmt;

            if (kategori == null || kategori.isEmpty()) {
                // Hapus batas yang kategori-nya "Semua"
                query = "DELETE FROM batas_pengeluaran WHERE user_id = ? AND kategori = ?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, String.valueOf(userId));
                stmt.setString(2, "Semua");
            } else {
                // Hapus batas untuk kategori yang dipilih
                query = "DELETE FROM batas_pengeluaran WHERE user_id = ? AND kategori = ?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, String.valueOf(userId));
                stmt.setString(2, kategori);
            }

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Batas pengeluaran berhasil dihapus.");
                alert.showAndWait();
                labelBatas.setText("Belum ditentukan");
            } else {
                showAlert("Tidak ada batas yang ditemukan untuk kategori ini.");
            }

            loadBatas(); // Refresh tabel
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBatas() {
        int userId = SessionManager.getInstance().getUserId();
        ObservableList<Batas> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM batas_pengeluaran WHERE user_id = ? ORDER BY tanggal DESC";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                Batas b = new Batas(
                        rs.getInt("id"),
                        rs.getString("kategori"),
                        rs.getDouble("jumlah"),
                        rs.getString("tanggal")
                );
                list.add(b);
            }

            batasTable.setItems(list);
            loadDataBatas();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void loadDataBatas() {
        int userId = SessionManager.getInstance().getUserId();
        String kategori = kategoriComboBox.getValue();

        String query = "SELECT jumlah FROM batas_pengeluaran WHERE user_id = ?";
        if (kategori != null && !kategori.isEmpty()) {
            query += " AND kategori = ?";
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);


            if (kategori != null && !kategori.isEmpty()) {
                stmt.setString(2, kategori);
            }

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double batas = rs.getDouble("jumlah");
                labelBatas.setText("Rp " + String.format("%,.0f", batas));
            } else {
                labelBatas.setText("Belum ditentukan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(pesan);
        alert.show();
    }

    @FXML
    void handleLogOut(MouseEvent event) {
        SessionManager.getInstance().logout();
        SessionStorage.clearSession();

        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Logout");
            alert.setHeaderText(null);
            alert.setContentText("Kamu berhasil logout.");
            alert.showAndWait();

            // Load tampilan login dengan path absolut
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/christ_javafx/login.fxml"));
            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();

            // Tampilkan alert error ke user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Gagal Logout");
            alert.setContentText("Terjadi kesalahan saat kembali ke halaman login.");
            alert.showAndWait();
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
    public void handleBeranda () {Apps.showberanda();}
}
