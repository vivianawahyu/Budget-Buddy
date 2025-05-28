package com.example.christ_javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RiwayatEditController {

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
