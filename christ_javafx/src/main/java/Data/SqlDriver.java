package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlDriver implements DatabaseDriver {

    private final String DB_URL = "jdbc:sqlite:budget_buddy_sqlite.db"; // Ganti ke path yang benar
    private Connection connection;

    private static volatile SqlDriver instance = null;

    private SqlDriver() {
    }

    public static SqlDriver getInstance() {
        if (instance == null) {
            synchronized (SqlDriver.class) {
                if (instance == null) {
                    instance = new SqlDriver();
                    instance.getConnection();
                    // instance.PreparedSchema(); // Nonaktifkan jika pakai DB file jadi
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
//                System.out.println("✅ Koneksi baru ke database dibuat.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void PreparedSchema() {
        // Kosongkan atau isi sesuai kebutuhan jika perlu generate DB baru
    }

    public void closeConn() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}