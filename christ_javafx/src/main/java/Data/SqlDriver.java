package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDriver implements  DatabaseDriver{


    private final String DB_URL = "jdbc:sqlite:catatanku.db";
    private Connection connection;

    private static volatile SqlDriver instance = null;

    private SqlDriver() {
    }

    public static SqlDriver getInstance() {
        if (instance == null) {
            // To make thread safe
            synchronized (SqlDriver.class) {
                // check again as multiple threads
                // can reach above step
                if (instance == null) {
                    instance = new SqlDriver();
                    instance.getConnection();
                    instance.PreparedSchema();
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection error
            }
        }
        return connection;
    }

    public void closeConn() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection closure error
            }
        }
    }

    @Override
    public void PreparedSchema() {
        // Create database tables if they don't exist
        // Implement this method to create tables for users, courses, classes, and attendance records
        String mhsTableSql = "CREATE TABLE IF NOT EXISTS Transaksi ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "judul TEXT NOT NULL,"
                + "konten TEXT NOT NULL,"
                + "kategori TEXT NOT NULL"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(mhsTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle table creation error
        }
    }
}

