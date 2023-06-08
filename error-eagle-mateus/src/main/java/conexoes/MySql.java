package conexoes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySql {

    // Conexão MySQL
    private static String jdbcUrl = "jdbc:mysql://localhost:3306/erroreagle";
    private static String username = "root";
    private static String password = "urubu100";

    private static Connection conn = null;

    public static Connection openConection() {
        if (conn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(jdbcUrl, username, password);
                System.out.println("Conexão Local(MySQL) aberta!");
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            } catch (ClassNotFoundException e) {
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
