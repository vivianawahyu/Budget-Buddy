package com.example.christ_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RingkasanController {

    @FXML
    private TextField searchField;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private ComboBox<String> categoryBox;

    @FXML
    private Label summaryLabel;

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText();
        String category = categoryBox.getValue();
        summaryLabel.setText("Total pengeluaran: Rp xxx.xxx\nTotal pemasukan: Rp yyy.yyy");
    }
}