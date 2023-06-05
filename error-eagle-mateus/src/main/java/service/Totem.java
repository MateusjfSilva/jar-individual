package service;

import conexoes.Azure;
import conexoes.MySql;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Totem {

    private static Integer id;
    private static String hostName;
    private static Integer fkEmpresa;

    private static String sql;
    private static PreparedStatement stAzure = null;
    private static PreparedStatement stLocal = null;
    private static ResultSet rs = null;

    private static Totem totem = null;

    public Totem(Integer id, String hostName, Integer fkEmpresa) {
        this.id = id;
        this.hostName = hostName;
        this.fkEmpresa = fkEmpresa;
    }

    public static Boolean verificarTotemCadastrado(String hostName, Integer fkEmpresa) {
        Boolean cadastrado = false;

        try {
            sql = "SELECT * FROM Totem WHERE hostName = ?";
            stAzure = Azure.getConn().prepareStatement(sql);
            stAzure.setString(1, hostName);
            rs = stAzure.executeQuery();

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
            if (stAzure != null) {
                try {
                    stAzure.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return cadastrado;
    }

    public static void insertTotem(String hostName) {

        try {
            if (!verificarTotemCadastrado(hostName, Login.getEmp().getId())) {

                System.out.println("Totem não cadastrado! Executando cadastro...");
                sql = "INSERT INTO Totem (hostName, fkEmpresa) VALUES (?, ?)";

                stAzure = Azure.getConn().prepareStatement(sql);
                stAzure.setString(1, hostName);
                stAzure.setInt(2, Login.getEmp().getId());

                stLocal = MySql.getConn().prepareStatement(sql);
                stLocal.setString(1, hostName);
                stLocal.setInt(2, Login.getEmp().getId());

                stAzure.executeUpdate();
                stLocal.executeUpdate();

                if (verificarTotemCadastrado(hostName, Login.getEmp().getId())) {
                    System.out.println("Cadastro realizado com sucesso!");
                } else {
                    System.out.println("Houve um erro ao cadastrar o Totem");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stAzure != null) {
                    stAzure.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stLocal != null) {
                    stLocal.close();
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
