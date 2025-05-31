package com.example.christ_javafx;

import Data.RiwayatEdit;
import Data.SessionManager;
import Data.SessionStorage;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;

import java.sql.*;

import java.io.IOException;

public class RiwayatEditController {

    @FXML
    private TableColumn<RiwayatEdit, String> colTanggalEdit;

    @FXML
    private TableColumn<RiwayatEdit, String> colTanggalDelete;

    @FXML
    private TableColumn<RiwayatEdit, Integer> colIdTransaksi;

    @FXML
    private TableColumn<RiwayatEdit, String> colAksi;         // opsional, kalau mau tampilkan ringkasan

    @FXML
    private TableColumn<RiwayatEdit, String> colSebelum;     // gabungan catatan_lama & jumlah_lama

    @FXML
    private TableColumn<RiwayatEdit, String> colSesudah;     // gabungan catatan_baru & jumlah_baru

    @FXML
    private TableView<RiwayatEdit> tableRiwayat;

    @FXML
    public void initialize() {
        int userId = SessionManager.getInstance().getUserId();
        ObservableList<RiwayatEdit> data = FXCollections.observableArrayList();

        // SETUP KOLOM ➤ cellValueFactory
        colTanggalEdit.setCellValueFactory(new PropertyValueFactory<>("tanggalEdit"));
        colIdTransaksi.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        colAksi.setCellValueFactory(new PropertyValueFactory<>("aksi"));
        colSebelum.setCellValueFactory(new PropertyValueFactory<>("sebelum"));
        colSesudah.setCellValueFactory(new PropertyValueFactory<>("sesudah"));
        colTanggalDelete.setCellValueFactory(new PropertyValueFactory<>("tanggalDelete"));

        // 2) BIKIN method singkat untuk mengaktifkan “word wrap” pada kolom teks
        //    lalu panggil sekali untuk colSebelum dan colSesudah
        setupWrappingColumn(colSebelum);
        setupWrappingColumn(colSesudah);

        // 3) Pilihan: jika tabel kosong, tampilkan placeholder
        tableRiwayat.setPlaceholder(new Label("Tidak ada riwayat edit atau hapus transaksi."));



        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db")) {
            PreparedStatement stms = conn.prepareStatement("SELECT * FROM log_transaksi WHERE user_id = ? ");
            stms.setInt(1, userId);
            ResultSet rs = stms.executeQuery();

            while (rs.next()) {
                RiwayatEdit r = new RiwayatEdit(
                        rs.getInt("id_l"),
                        rs.getInt("transaksi_id"),
                        rs.getInt("user_id"),
                        rs.getString("action"),
                        rs.getString("note_before"),
                        rs.getString("note_after"),
                        rs.getString("tgl_update"),
                        rs.getString("tgl_delete")
                );
                data.add(r);
            }

            tableRiwayat.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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


    /**
     * Method bantuan untuk membuat cell factory dengan Text node yang
     * membungkus teks (word-wrap) di dalam sebuah TableColumn<RiwayatEdit, String>.
     */
    private void setupWrappingColumn(TableColumn<RiwayatEdit, String> column) {
        column.setCellFactory(col -> {
            TableCell<RiwayatEdit, String> cell = new TableCell<>() {
                private final Text text = new Text();

                {
                    // Binding lebar pembungkus ke lebar kolom dikurangi sedikit padding
                    text.wrappingWidthProperty().bind(col.widthProperty().subtract(10));
                    setGraphic(text);
                    // Biarkan tinggi baris dihitung otomatis berdasarkan kebutuhan isi (wrap text)
                    setPrefHeight(Control.USE_COMPUTED_SIZE);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText("");
                    } else {
                        text.setText(item);
                    }
                }
            };
            return cell;
        });
    }

}
