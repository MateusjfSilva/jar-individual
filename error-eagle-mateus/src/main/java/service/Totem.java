package service;

import conexoes.Azure;
import conexoes.MySql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Totem {

    private static Integer id;
    private static String hostName;
    private static Integer fkEmpresa;

    private static String sql;
    private static PreparedStatement st = null;
    private static ResultSet rs = null;

    private static Totem totem = null;

    public Totem(Integer id, String hostName, Integer fkEmpresa) {
        this.id = id;
        this.hostName = hostName;
        this.fkEmpresa = fkEmpresa;
    }

    public static Boolean verificarTotemCadastrado(String hostName, Integer fkEmpresa, 
            Connection conn) {
        Boolean cadastrado = false;

        try {
            sql = "SELECT * FROM Totem WHERE hostName = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, hostName);
            rs = st.executeQuery();

            if (rs.next()) {
                System.out.println("Totem já cadastrado!");
                cadastrado = true;
                totem = new Totem(rs.getInt("id"), hostName, fkEmpresa);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return cadastrado;
    }

    public static void insertTotem(String hostName, Connection conn) {

        try {
            if (!verificarTotemCadastrado(hostName, Login.getEmp().getId(), conn)) {

                System.out.println("Totem não cadastrado! Executando cadastro...");
                sql = "INSERT INTO Totem (hostName, fkEmpresa) VALUES (?, ?)";

                st = conn.prepareStatement(sql);
                st.setString(1, hostName);
                st.setInt(2, Login.getEmp().getId());

                st.executeUpdate();

                if (verificarTotemCadastrado(hostName, Login.getEmp().getId(), conn)) {
                    System.out.println("Totem cadastrado com sucesso!\n");
                } else {
                    System.out.println("Houve um erro ao cadastrar o Totem\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public static Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

}
