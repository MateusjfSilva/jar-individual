package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Componente {

    private static Integer id;
    private static String nome;

    private static String sql;
    private static PreparedStatement st = null;
    private static ResultSet rs = null;

    public Componente(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static void verificarCompCadastrado(String tc, Connection conn) {

        Boolean cpuCadastrada = false;
        Boolean ramCadastrada = false;
        Boolean discoCadastrado = false;
        Boolean redeCadastrada = false;
        System.out.println("\nVerificando Componentes " + tc + ":");
        try {
            sql = "SELECT * FROM Componente";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                if (rs.getString("nome").equalsIgnoreCase("CPU")) {
                    Componente componente = new Componente(rs.getInt("id"),
                            rs.getString("nome"));
                    System.out.println("CPU cadastrada!");
                    cpuCadastrada = true;
                } else if (rs.getString("nome").equalsIgnoreCase("RAM")) {
                    Componente componente = new Componente(rs.getInt("id"),
                            rs.getString("nome"));
                    System.out.println("RAM cadastrada!");
                    ramCadastrada = true;
                } else if (rs.getString("nome").equalsIgnoreCase("DISCO")) {
                    Componente componente = new Componente(rs.getInt("id"),
                            rs.getString("nome"));
                    System.out.println("DISCO cadastrada!");
                    discoCadastrado = true;
                } else if (rs.getString("nome").equalsIgnoreCase("REDE")) {
                    Componente componente = new Componente(rs.getInt("id"),
                            rs.getString("nome"));
                    System.out.println("REDE cadastrada!\n");
                    redeCadastrada = true;
                }
            }

            sql = "INSERT INTO Componente (nome) VALUES (?)";
            st = conn.prepareStatement(sql);

            if (!cpuCadastrada) {
                st.setString(1, "CPU");
                st.executeUpdate();
                System.out.println("Cadastro Componente CPU bem sucessido!");
            }
            if (!ramCadastrada) {
                st.setString(1, "RAM");
                st.executeUpdate();
                System.out.println("Cadastro Componente RAM bem sucessido!");
            }
            if (!discoCadastrado) {
                st.setString(1, "DISCO");
                st.executeUpdate();
                System.out.println("Cadastro Componente DISCO bem sucessido!");
            }
            if (!redeCadastrada) {
                st.setString(1, "REDE");
                st.executeUpdate();
                System.out.println("Cadastro Componente REDE bem sucessido!\n");
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
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
