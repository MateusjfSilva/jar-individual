package service;

import conexoes.Azure;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    private static String nome;
    private static String email;
    private static String senha;
    private static Integer statusFuncionario;
    private static String sqlLogin = "select * from Funcionario where email = ?";
    private static String sqlEmpresa = "select * from Empresa where id = ?";
    private static Empresa emp;

    public Login(String nome, String email, String senha, Integer statusFuncionario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.statusFuncionario = statusFuncionario;
    }

    public static Boolean realizarCadastro(String email, String senha) {

        Boolean logado = false;

        try {
            PreparedStatement stAzure = Azure.getConn().prepareStatement(sqlLogin);
            stAzure.setString(1, email);
            ResultSet rs = stAzure.executeQuery();

            if (rs.next()) {
                if (rs.getString("senha").equals(senha)) {
                    if (rs.getInt("statusFuncionario") == 1) {
                        Login funcionarioLogado = new Login(rs.getString("nome"),
                                rs.getString("email"), rs.getString("senha"),
                                rs.getInt("statusFuncionario"));
                        stAzure = Azure.getConn().prepareStatement(sqlEmpresa);
                        stAzure.setInt(1, rs.getInt("fkEmpresa"));
                        rs = stAzure.executeQuery();

                        if (rs.next()) {
                            if (rs.getInt("statusEmpresa") == 1) {
                                logado = true;
                                System.out.println("Login realizado com sucesso!\n");

                                emp = new Empresa(rs.getInt("id"),
                                        rs.getDouble("bandaLarga"));
                            } else {
                                System.out.println("Empresa dasativada, "
                                        + "verifique a validade do contrato!");
                            }
                        }
                    } else {
                        System.out.println("Você não tem acesso. Verifique suas "
                                + "crendênciais de acesso com seu superior!");
                    }
                } else {
                    System.out.println("Email e/ou senha invalido(a)!");
                }
            } else {
                System.out.println("Email e/ou senha invalido(a)!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logado;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public Integer getStatusFuncionario() {
        return statusFuncionario;
    }

    public static Empresa getEmp() {
        return emp;
    }
}
