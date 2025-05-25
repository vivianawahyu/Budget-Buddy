package com.example.christ_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LimitController {
    @FXML
    private TextField limitField;

    @FXML
    private Label statusLabel;

    private static double batasPengeluaran = 0;

    @FXML
    private void handleSaveLimit() {
        try {
            batasPengeluaran = Double.parseDouble(limitField.getText());
            statusLabel.setText("Batas pengeluaran disimpan: Rp " + batasPengeluaran);
        } catch (NumberFormatException e) {
            statusLabel.setText("Masukkan angka yang valid");
        }
    }

    public static boolean isOverLimit(double totalPengeluaran) {
        return totalPengeluaran > batasPengeluaran;
    }
}