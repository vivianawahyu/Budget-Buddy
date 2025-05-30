package com.example.christ_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Apps extends Application {
    private static Stage primaryStage;

    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLogin();
    }

    public static void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/login.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/register.fxml"));
            Scene scene = new Scene((Parent)loader.load(), (double)400.0F, (double)350.0F);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Register");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void showberanda() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/beranda.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Beranda");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void showbatasPengeluaran() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/batasPengeluaran.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Batas Pengeluaran");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showtambahTransaksi() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/tambahTransaksi.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Tambah Transaksi");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showdataTransaksi() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/dataTransaksi.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Data Transaksi");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showedit() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/edit.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Edit");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showfilterKategori() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/filterKategori.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Filter Kategori");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showLimit() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/Limit.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Limit");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showlupaPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/lupaPassword.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Lupa Password");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showRingkasan() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/Ringkasan.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Ringkasan");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showRiwayatEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/com/example/christ_javafx/RiwayatEdit.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Riwayat Edit");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}
