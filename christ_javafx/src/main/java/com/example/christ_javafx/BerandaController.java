package com.example.christ_javafx;

import Data.Transaksi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.stage.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class BerandaController {

    @FXML
    private PieChart PieChart;

    @FXML
    private TableView<Transaksi> tableTransaksi;
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
    private Label labelSaldo;

    @FXML
    private Label labelPemasukan;

    @FXML
    private Label labelPengeluaran;

    @FXML
    private AreaChart<String, Number> areaChartKeuangan;

    @FXML
    private BarChart<String, Number> barChartKeuangan;

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
    public void handleEdit(MouseEvent event) {
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
    public void initialize() {
        // Pasang property ke kolom
        colTanggal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTanggal()));
        colKategori.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKategori()));
        colJenis.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getJenis()));
        colJumlah.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getJumlah()).asObject());
        colCatatan.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCatatan()));

        loadData();
    }

    private void loadData() {
        ObservableList<Transaksi> transaksiList = FXCollections.observableArrayList();
        double totalPemasukan = 0;
        double totalPengeluaran = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM transaksi WHERE user_id = 1 ORDER BY tgl_transaksi DESC")) {

            while (rs.next()) {
                Transaksi t = new Transaksi(
                        rs.getInt("id_t"),
                        rs.getString("tgl_transaksi"),
                        rs.getString("kategori"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("note")
                );

                if (t.getJenis().equalsIgnoreCase("Pemasukan")) {
                    totalPemasukan += t.getJumlah();
                } else {
                    totalPengeluaran += t.getJumlah();
                }

                transaksiList.add(t);
            }

            tableTransaksi.setItems(transaksiList);

            // ------------------- PieChart --------------------
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Pemasukan", totalPemasukan),
                    new PieChart.Data("Pengeluaran", totalPengeluaran)
            );
            PieChart.setData(pieChartData);

            // ------------------ BarChart (Grafik Keuangan Bulanan) -------------------
            barChartKeuangan.getData().clear();
            XYChart.Series<String, Number> seriesPemasukan = new XYChart.Series<>();
            seriesPemasukan.setName("Pemasukan");
            XYChart.Series<String, Number> seriesPengeluaran = new XYChart.Series<>();
            seriesPengeluaran.setName("Pengeluaran");

            String queryBulan = "SELECT strftime('%Y-%m', tgl_transaksi) AS bulan, type, SUM(amount) AS total " +
                    "FROM transaksi WHERE user_id = 1 GROUP BY bulan, type ORDER BY bulan";

            try (Statement stmt2 = conn.createStatement();
                 ResultSet rs2 = stmt2.executeQuery(queryBulan)) {

                Map<String, Double> mapPemasukan = new LinkedHashMap<>();
                Map<String, Double> mapPengeluaran = new LinkedHashMap<>();

                while (rs2.next()) {
                    String bulan = rs2.getString("bulan");
                    String tipe = rs2.getString("type");
                    double total = rs2.getDouble("total");

                    if (tipe.equalsIgnoreCase("Pemasukan")) {
                        mapPemasukan.put(bulan, total);
                    } else {
                        mapPengeluaran.put(bulan, total);
                    }
                }

                Set<String> semuaBulan = new TreeSet<>();
                semuaBulan.addAll(mapPemasukan.keySet());
                semuaBulan.addAll(mapPengeluaran.keySet());

                for (String bulan : semuaBulan) {
                    double pemasukan = mapPemasukan.getOrDefault(bulan, 0.0);
                    double pengeluaran = mapPengeluaran.getOrDefault(bulan, 0.0);

                    seriesPemasukan.getData().add(new XYChart.Data<>(bulan, pemasukan));
                    seriesPengeluaran.getData().add(new XYChart.Data<>(bulan, pengeluaran));
                }

                barChartKeuangan.getData().addAll(seriesPemasukan, seriesPengeluaran);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // ------------------ Saldo Saat Ini -------------------
            double saldo = totalPemasukan - totalPengeluaran;

            labelPemasukan.setText("Rp " + String.format("%,.0f", totalPemasukan));
            labelPengeluaran.setText("Rp " + String.format("%,.0f", totalPengeluaran));
            labelSaldo.setText("Rp " + String.format("%,.0f", saldo));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // ----------- Cek batas pengeluaran -------------
        Map<String, Double> pengeluaranPerKategori = new HashMap<>();

        for (Transaksi t : transaksiList) {
            if (t.getJenis().equalsIgnoreCase("Pengeluaran")) {
                pengeluaranPerKategori.merge(t.getKategori(), t.getJumlah(), Double::sum);
            }
        }

        String queryBatas = "SELECT kategori, jumlah FROM batas_pengeluaran WHERE user_id = 1";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_buddy_sqlite.db");
             PreparedStatement pstmt = conn.prepareStatement(queryBatas);
             ResultSet rsBatas = pstmt.executeQuery()) {

            while (rsBatas.next()) {
                String kategori = rsBatas.getString("kategori");
                double jumlah = rsBatas.getDouble("jumlah"); // ← diperbaiki

                double totalKategori = pengeluaranPerKategori.getOrDefault(kategori, 0.0);
                if (totalKategori > jumlah) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Peringatan Pengeluaran");
                    alert.setHeaderText("Batas Terlampaui!");
                    alert.setContentText("Pengeluaran untuk kategori '" + kategori + "' telah melebihi batas: Rp "
                            + String.format("%,.0f", jumlah) + "\nTotal saat ini: Rp " + String.format("%,.0f", totalKategori));
                    alert.show();

                    System.out.println("Kategori: " + kategori + ", Batas: " + jumlah + ", Total pengeluaran: " + totalKategori);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
