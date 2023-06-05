package conexoes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Azure {

    // Conexão Azure
    private static String serverName = "svr-erroreagle.database.windows.net";
    private static int portNumber = 1433;
    private static String databaseName = "bd-errorEagle";
    private static String username = "admin-erroreagle@svr-erroreagle";
    private static String password = "#Gfgrupo3";
    private static String jdbcUrl = String.format(
            "jdbc:sqlserver://%s:%d;database=%s;",
            serverName, portNumber, databaseName);

    private static Connection conn = null;

    public static Connection openConection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(jdbcUrl,
                        username, password);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static Connection getConn() {
        return conn;
    }
}
