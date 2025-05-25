package Data;

import java.sql.Connection;

public interface DatabaseDriver {
    Connection getConnection();
    void  closeConn();
    void PreparedSchema();
}

