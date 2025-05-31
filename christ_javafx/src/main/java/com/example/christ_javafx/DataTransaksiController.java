package com.example.christ_javafx;

import Data.SessionManager;
import Data.SessionStorage;
import Data.SqlDriver;
import Data.Transaksi;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;


import java.io.File;
import java.io.FileOutputStream;
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
    private void handleedit (MouseEvent event) {
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
        int userId = SessionManager.getInstance().getUserId();
        System.out.println("🔍 Memuat data transaksi untuk user ID: " + userId);
        String sql = "SELECT * FROM transaksi WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            {
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
            }

            tableTransaksi.setItems(transaksiList);
            cekStatusPengeluaran();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cekStatusPengeluaran() {
        int userId = SessionManager.getInstance().getUserId();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db")) {
            PreparedStatement batasStmt = conn.prepareStatement("SELECT jumlah FROM batas_pengeluaran WHERE user_id = ?");
            batasStmt.setInt(1, userId);
            ResultSet batasRs = batasStmt.executeQuery();

            double batas = batasRs.next() ? batasRs.getDouble("jumlah") : -1;

            if (batas != -1) {
                PreparedStatement totalStmt = conn.prepareStatement("SELECT SUM(amount) as total FROM transaksi WHERE user_id = ? AND type = 'Pengeluaran'");
                totalStmt.setInt(1, userId);
                ResultSet totalRs = totalStmt.executeQuery();

                double totalPengeluaran = totalRs.next() ? totalRs.getDouble("total") : 0;

                if (totalPengeluaran > batas) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Status Pengeluaran");
                    alert.setHeaderText("Pengeluaran Melebihi Batas!");
                    alert.setContentText("Total pengeluaran Anda saat ini adalah: Rp " + totalPengeluaran + ", melebihi batas: Rp " + batas);
                    alert.showAndWait();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleExportPDF() {
        Transaksi selected = tableTransaksi.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih transaksi terlebih dahulu.");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan PDF");
        fileChooser.setInitialFileName("Transaksi_" + selected.getId() + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(tableTransaksi.getScene().getWindow());

        if (file != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Judul
                Paragraph title = new Paragraph("Detail Transaksi", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
                title.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(title);
                document.add(Chunk.NEWLINE);

                // Tabel
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
                Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

                addStyledCell(table, "ID", headerFont, BaseColor.LIGHT_GRAY);
                addStyledCell(table, String.valueOf(selected.getId()), cellFont);

                addStyledCell(table, "Tanggal", headerFont, BaseColor.LIGHT_GRAY);
                addStyledCell(table, selected.getTanggal(), cellFont);

                addStyledCell(table, "Kategori", headerFont, BaseColor.LIGHT_GRAY);
                addStyledCell(table, selected.getKategori(), cellFont);

                addStyledCell(table, "Jenis", headerFont, BaseColor.LIGHT_GRAY);
                addStyledCell(table, selected.getJenis(), cellFont);

                addStyledCell(table, "Jumlah", headerFont, BaseColor.LIGHT_GRAY);
                addStyledCell(table, "Rp " + selected.getJumlah(), cellFont);

                addStyledCell(table, "Catatan", headerFont, BaseColor.LIGHT_GRAY);
                addStyledCell(table, selected.getCatatan(), cellFont);

                document.add(table);

                // Footer
                Paragraph footer = new Paragraph("Generated by Budget Buddy", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
                footer.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(Chunk.NEWLINE);
                document.add(footer);

                document.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export PDF");
                alert.setHeaderText("Berhasil");
                alert.setContentText("File berhasil disimpan di:\n" + file.getAbsolutePath());
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Gagal Export PDF");
                alert.setContentText("Terjadi kesalahan saat menyimpan file PDF.");
                alert.showAndWait();
            }
        }
    }


    private void addStyledCell(PdfPTable table, String text, com.itextpdf.text.Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8);
        table.addCell(cell);
    }

    private void addStyledCell(PdfPTable table, String text, com.itextpdf.text.Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8);
        cell.setBackgroundColor(bgColor);
        table.addCell(cell);
    }


}
