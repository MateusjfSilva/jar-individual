package service;

import com.github.britooo.looca.api.core.Looca;
import conexoes.Azure;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Configuracao {

    private static Integer fkTotem;
    private static Integer fkComponente;
    private static Double capacidade;
    private static String unidadeMedida;

    private static List<Configuracao> configs;

    private static String sql;
    private static PreparedStatement stAzure = null;
    private static ResultSet rs = null;

    private static Looca looca = new Looca();

    public Configuracao() {
        this.fkTotem = Totem.getId();
        this.fkComponente = fkComponente;
        this.capacidade = capacidade;
        this.unidadeMedida = unidadeMedida;
    }

    public Configuracao(Integer fkTotem, Integer fkComponente,
            Double capacidade, String unidadeMedida) {
        this.fkTotem = Totem.getId();
        this.fkComponente = fkComponente;
        this.capacidade = capacidade;
        this.unidadeMedida = unidadeMedida;
    }

    public static void inserirConfiguracao(Double bandaLarga) {

        // CPU
        Double frequencia = looca.getProcessador().getFrequencia() / Math.pow(10, 9);

        // RAM
        Long totalRAM = looca.getMemoria().getTotal();

        String ramFormatada = new DecimalFormat(",##").format(totalRAM / Math.pow(2, 30));
        Double capacidadeRam = Double.parseDouble(ramFormatada);

        // DISCO
        Long tamanho = looca.getGrupoDeDiscos().getTamanhoTotal();

        String discoFormatada = new DecimalFormat(",##")
                .format(tamanho / Math.pow(2, 30));
        Double capacidadeDisco = Double.parseDouble(discoFormatada);

        Boolean configCpu = false;
        Boolean configRam = false;
        Boolean configDisco = false;
        Boolean configRede = false;

        try {
            sql = "SELECT * FROM Configuracao";
            stAzure = Azure.getConn().prepareStatement(sql);
            rs = stAzure.executeQuery();

            configs = new ArrayList<>();

            while (rs.next()) {
                if (rs.getInt("fkTotem") == Totem.getId() && rs.getInt("fkComponente") == 1) {
                    configs.add(new Configuracao(rs.getInt("fkTotem"),
                            rs.getInt("fkComponente"), frequencia, "GHz"));
                    System.out.print("Configuração CPU já cadastrada! ");
                    configCpu = true;
                } else if (rs.getInt("fkTotem") == Totem.getId() && rs.getInt("fkComponente") == 2) {
                    configs.add(new Configuracao(rs.getInt("fkTotem"),
                            rs.getInt("fkComponente"), capacidadeRam, "GB"));
                    System.out.print("Configuração RAM já cadastrada! ");
                    configRam = true;
                } else if (rs.getInt("fkTotem") == Totem.getId() && rs.getInt("fkComponente") == 3) {
                    configs.add(new Configuracao(rs.getInt("fkTotem"),
                            rs.getInt("fkComponente"), capacidadeDisco, "GB"));
                    System.out.print("Configuração DISCO já cadastrada! ");
                    configDisco = true;
                } else if (rs.getInt("fkTotem") == Totem.getId() && rs.getInt("fkComponente") == 4) {
                    configs.add(new Configuracao(rs.getInt("fkTotem"),
                            rs.getInt("fkComponente"), bandaLarga, "MBPS"));
                    System.out.println("Configuração REDE já cadastrada!");
                    configRede = true;
                }
            }

            sql = "INSERT INTO Configuracao "
                    + "(fkTotem, fkComponente, capacidade, unidadeMedida) "
                    + "VALUES (?, ?, ?, ?)";
            stAzure = Azure.getConn().prepareStatement(sql);

            if (!configCpu) {
                stAzure.setInt(1, Totem.getId());
                stAzure.setInt(2, 1);
                stAzure.setDouble(3, frequencia);
                stAzure.setString(4, "GHz");
                stAzure.executeUpdate();
                System.out.println("Cadastro Configuração de CPU realizado com "
                        + "sucesso!");
            }
            if (!configRam) {
                stAzure.setInt(1, Totem.getId());
                stAzure.setInt(2, 2);
                stAzure.setDouble(3, capacidadeRam);
                stAzure.setString(4, "GB");
                stAzure.executeUpdate();
                System.out.println("Cadastro Configuração de RAM realizado com "
                        + "sucesso!");
            }
            if (!configDisco) {
                stAzure.setInt(1, Totem.getId());
                stAzure.setInt(2, 3);
                stAzure.setDouble(3, capacidadeDisco);
                stAzure.setString(4, "GB");
                stAzure.executeUpdate();
                System.out.println("Cadastro Configuração de DISCO realizado "
                        + "com sucesso!");
            }
            if (!configRede) {
                stAzure.setInt(1, Totem.getId());
                stAzure.setInt(2, 4);
                stAzure.setDouble(3, bandaLarga);
                stAzure.setString(4, "MBPS");
                stAzure.executeUpdate();
                System.out.println("Cadastro Configuração de REDE realizado com "
                        + "sucesso!");
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

    public Integer getFkTotem() {
        return fkTotem;
    }

    public Integer getFkComponente() {
        return fkComponente;
    }

    public Double getCapacidade() {
        return capacidade;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }
}
