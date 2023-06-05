package service;

import conexoes.Azure;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Componente {

    private static Integer id;
    private static String nome;

    private static String sql;
    private static PreparedStatement stAzure = null;
    private static PreparedStatement stLocal = null;
    private static ResultSet rs = null;

    public Componente(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static void verificarCompCadastrado(List<Componente> componentes) {

        Boolean cpuCadastrada = false;
        Boolean ramCadastrada = false;
        Boolean discoCadastrado = false;
        Boolean redeCadastrada = false;
        
        try {
            sql = "SELECT * FROM Componente";
            stAzure = Azure.getConn().prepareStatement(sql);
            rs = stAzure.executeQuery();

            while (rs.next()) {
                if (rs.getString("nome").equalsIgnoreCase("CPU")) {
                    Componente componente = new Componente(rs.getInt("id"),
                            rs.getString("nome"));
                    System.out.print("CPU cadastrada! ");
                    cpuCadastrada = true;
                } else if (rs.getString("nome").equalsIgnoreCase("RAM")) {
                    Componente componente = new Componente(rs.getInt("id"),
                            rs.getString("nome"));
                    System.out.print("RAM cadastrada! ");
                    ramCadastrada = true;
                } else if (rs.getString("nome").equalsIgnoreCase("DISCO")) {
                    Componente componente = new Componente(rs.getInt("id"),
                            rs.getString("nome"));
                    System.out.print("DISCO cadastrada! ");
                    discoCadastrado = true;
                } else if (rs.getString("nome").equalsIgnoreCase("REDE")) {
                    Componente componente = new Componente(rs.getInt("id"),
                            rs.getString("nome"));
                    System.out.println("REDE cadastrada!");
                    redeCadastrada = true;
                }
            }

            sql = "INSERT INTO Componente (nome) VALUES (?)";
            stAzure = Azure.getConn().prepareStatement(sql);

            if (!cpuCadastrada) {
                stAzure.setString(1, "CPU");
                stAzure.executeUpdate();
                System.out.println("Cadastro Componente CPU bem sucessido!");
            }
            if (!ramCadastrada) {
                stAzure.setString(1, "RAM");
                stAzure.executeUpdate();
                System.out.println("Cadastro Componente RAM bem sucessido!");
            }
            if (!discoCadastrado) {
                stAzure.setString(1, "DISCO");
                stAzure.executeUpdate();
                System.out.println("Cadastro Componente DISCO bem sucessido!");
            }
            if (!redeCadastrada) {
                stAzure.setString(1, "REDE");
                stAzure.executeUpdate();
                System.out.println("Cadastro Componente REDE bem sucessido!");
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
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
