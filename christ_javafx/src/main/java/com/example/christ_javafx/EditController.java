package com.example.christ_javafx;

import Data.Transaksi;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class EditController {
    @FXML
    private ChoiceBox<String> cbKategori;
    @FXML
    private TextArea txtAreaKonten;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField txtJumlah;
    @FXML
    private ChoiceBox<String> cbTipe;

    private Transaksi transaksi;

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;

        // isi field sesuai data
        cbKategori.setValue(transaksi.getKategori());
        cbTipe.setValue(transaksi.getJenis());
        txtJumlah.setText(String.valueOf(transaksi.getJumlah()));
        datePicker.setValue(LocalDate.parse(transaksi.getTanggal()));
        txtAreaKonten.setText(transaksi.getCatatan());
    }

    @FXML
    public void initialize() {
        cbKategori.getItems().addAll("Gaji", "Tabungan", "Belanja", "Makanan & Minuman");
        cbTipe.getItems().addAll("Pemasukan", "Pengeluaran");
    }


    @FXML
    private void onBtnHapus() {
        if (transaksi != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db")) {
                // Simpan riwayat sebelum hapus
                PreparedStatement riwayatStmt = conn.prepareStatement(
                        "INSERT INTO riwayat_edit (id_transaksi, tanggal_edit, catatan_lama, catatan_baru, jumlah_lama, jumlah_baru) VALUES (?, datetime('now'), ?, '', ?, 0)"
                );
                riwayatStmt.setInt(1, transaksi.getId());
                riwayatStmt.setString(2, transaksi.getCatatan());
                riwayatStmt.setDouble(3, transaksi.getJumlah());
                riwayatStmt.executeUpdate();

                // Hapus transaksi
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM transaksi WHERE id_t = ?");
                deleteStmt.setInt(1, transaksi.getId());
                deleteStmt.executeUpdate();

                closeWindow();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onPieChartCLicked() {
        if (transaksi != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db")) {
                // Simpan data lama dulu
                String catatanLama = transaksi.getCatatan();
                double jumlahLama = transaksi.getJumlah();

                // Ambil data baru dari field
                String catatanBaru = txtAreaKonten.getText();
                double jumlahBaru = Double.parseDouble(txtJumlah.getText());

                // Update transaksi
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE transaksi SET tgl_transaksi = ?, kategori = ?, type = ?, amount = ?, note = ? WHERE id_t = ?"
                );
                updateStmt.setString(1, datePicker.getValue().toString());
                updateStmt.setString(2, cbKategori.getValue());
                updateStmt.setString(3, cbTipe.getValue());
                updateStmt.setDouble(4, jumlahBaru);
                updateStmt.setString(5, catatanBaru);
                updateStmt.setInt(6, transaksi.getId());
                updateStmt.executeUpdate();

                // Simpan ke riwayat_edit
                PreparedStatement riwayatStmt = conn.prepareStatement(
                        "INSERT INTO riwayat_edit (id_transaksi, tanggal_edit, catatan_lama, catatan_baru, jumlah_lama, jumlah_baru) VALUES (?, datetime('now'), ?, ?, ?, ?)"
                );
                riwayatStmt.setInt(1, transaksi.getId());
                riwayatStmt.setString(2, catatanLama);
                riwayatStmt.setString(3, catatanBaru);
                riwayatStmt.setDouble(4, jumlahLama);
                riwayatStmt.setDouble(5, jumlahBaru);
                riwayatStmt.executeUpdate();

                closeWindow();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cbKategori.getScene().getWindow();
        stage.close();
    }
}
