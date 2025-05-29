package com.example.christ_javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.stage.*;

import java.io.IOException;
import java.util.EventObject;

public class BerandaController {

    public TableView idTableTransaksiTerbaru;

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
    public void handleEdit() {Apps.showedit();}

    @FXML
    public void handleRiwayatEdit() {Apps.showRiwayatEdit();}

    @FXML
    public void handleBeranda () {Apps.showberanda();}
}
