package com.example.christ_javafx;

import Data.RiwayatEdit;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.IOException;

public class RiwayatEditController {

    @FXML
    private TableColumn<RiwayatEdit, String> colTanggalEdit;

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
        ObservableList<RiwayatEdit> data = FXCollections.observableArrayList();

        // SETUP KOLOM ➤ cellValueFactory
        colTanggalEdit.setCellValueFactory(new PropertyValueFactory<>("tanggalEdit"));
        colIdTransaksi.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        colAksi.setCellValueFactory(cellData -> new SimpleStringProperty("Edit")); // atau bisa lebih dinamis
        colSebelum.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCatatanLama() + " - " + cellData.getValue().getJumlahLama()
        ));
        colSesudah.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCatatanBaru() + " - " + cellData.getValue().getJumlahBaru()
        ));

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM riwayat_edit");

            while (rs.next()) {
                RiwayatEdit r = new RiwayatEdit(
                        rs.getInt("id_transaksi"),
                        rs.getString("tanggal_edit"),
                        rs.getString("catatan_lama"),
                        rs.getString("catatan_baru"),
                        rs.getDouble("jumlah_lama"),
                        rs.getDouble("jumlah_baru")
                );
                data.add(r);
            }

            tableRiwayat.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
