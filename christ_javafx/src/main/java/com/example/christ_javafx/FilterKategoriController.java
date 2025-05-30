package com.example.christ_javafx;

import Data.SessionManager;
import Data.SessionStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Data.Transaksi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.time.LocalDate;
import java.io.IOException;

public class FilterKategoriController {

    @FXML private TableView<Transaksi> tableFilter;
    @FXML private TableColumn<Transaksi, Integer> colID;
    @FXML private TableColumn<Transaksi, String> colTanggal;
    @FXML private TableColumn<Transaksi, String> colKategori;
    @FXML private TableColumn<Transaksi, String> colJenis;
    @FXML private TableColumn<Transaksi, Double> colJumlah;
    @FXML private TableColumn<Transaksi, String> colCatatan;

    @FXML private RadioButton radioSemua;
    @FXML private RadioButton radioPemasukan;
    @FXML private RadioButton radioPengeluaran;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;

    @FXML private CheckBox cbMakanan;
    @FXML private CheckBox cbTabungan;
    @FXML private CheckBox cbBelanja;
    @FXML private CheckBox cbGaji;
    @FXML private TextField tfKeyword;

    @FXML
    private ToggleGroup jenisGroup;

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

    @FXML
    public void initialize() {
        // Set up columns
        colID.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colTanggal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTanggal()));
        colKategori.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKategori()));
        colJenis.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getJenis()));
        colJumlah.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getJumlah()).asObject());
        colCatatan.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCatatan()));

        // Set up toggle group
        jenisGroup = new ToggleGroup();
        radioSemua.setToggleGroup(jenisGroup);
        radioPemasukan.setToggleGroup(jenisGroup);
        radioPengeluaran.setToggleGroup(jenisGroup);
        radioSemua.setSelected(true);

        // Load initial data
        applyFilters();
    }

    @FXML
    public void applyFilters() {
        ObservableList<Transaksi> filteredData = FXCollections.observableArrayList();
        int userId = SessionManager.getInstance().getUserId();

        String sql = "SELECT * FROM transaksi WHERE user_id = " + userId;
        StringBuilder queryBuilder = new StringBuilder(sql);

        // Jenis transaksi
        if (radioPemasukan.isSelected()) {
            queryBuilder.append(" AND type = 'Pemasukan'");
        } else if (radioPengeluaran.isSelected()) {
            queryBuilder.append(" AND type = 'Pengeluaran'");
        }

        // Rentang waktu
        LocalDate fromDate = dateFrom.getValue();
        LocalDate toDate = dateTo.getValue();
        if (fromDate != null && toDate != null) {
            queryBuilder.append(" AND date(tgl_transaksi) BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("'");
        }

        // Kategori
        StringBuilder kategoriBuilder = new StringBuilder();
        if (cbMakanan.isSelected()) kategoriBuilder.append("'Makanan & Minuman',");
        if (cbTabungan.isSelected()) kategoriBuilder.append("'Tabungan',");
        if (cbBelanja.isSelected()) kategoriBuilder.append("'Belanja',");
        if (cbGaji.isSelected()) kategoriBuilder.append("'Gaji',");

        if (!kategoriBuilder.isEmpty()) {
            kategoriBuilder.setLength(kategoriBuilder.length() - 1); // hapus koma terakhir
            queryBuilder.append(" AND kategori IN (").append(kategoriBuilder.toString()).append(")");
        }

        // Kata kunci
        String keyword = tfKeyword.getText();
        if (keyword != null && !keyword.isEmpty()) {
            queryBuilder.append(" AND (note LIKE '%").append(keyword).append("%' OR kategori LIKE '%").append(keyword).append("%')");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(queryBuilder.toString())) {

            while (rs.next()) {
                Transaksi t = new Transaksi(
                        rs.getInt("id_t"),
                        rs.getString("tgl_transaksi"),
                        rs.getString("kategori"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("note")
                );
                filteredData.add(t);
            }

            tableFilter.setItems(filteredData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClear() {
        radioSemua.setSelected(true);
        dateFrom.setValue(null);
        dateTo.setValue(null);
        cbMakanan.setSelected(false);
        cbTabungan.setSelected(false);
        cbBelanja.setSelected(false);
        cbGaji.setSelected(false);
        tfKeyword.clear();

        applyFilters();
    }
}
