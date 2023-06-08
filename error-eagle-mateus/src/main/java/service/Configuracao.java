package service;

import com.github.britooo.looca.api.core.Looca;
import conexoes.Azure;
import java.sql.Connection;
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
    private static PreparedStatement st = null;
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

    public static void inserirConfiguracao(String tc, Double bandaLarga, Connection conn) {
        
        System.out.println("Validando Configuração " + tc + ":");
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
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            configs = new ArrayList<>();

            while (rs.next()) {
                if (rs.getInt("fkTotem") == Totem.getId() && rs.getInt("fkComponente") == 1) {
                    configs.add(new Configuracao(rs.getInt("fkTotem"),
                            rs.getInt("fkComponente"), frequencia, "GHz"));
                    System.out.println("CPU já cadastrada!");
                    configCpu = true;
                } else if (rs.getInt("fkTotem") == Totem.getId() && rs.getInt("fkComponente") == 2) {
                    configs.add(new Configuracao(rs.getInt("fkTotem"),
                            rs.getInt("fkComponente"), capacidadeRam, "GB"));
                    System.out.println("RAM já cadastrada!");
                    configRam = true;
                } else if (rs.getInt("fkTotem") == Totem.getId() && rs.getInt("fkComponente") == 3) {
                    configs.add(new Configuracao(rs.getInt("fkTotem"),
                            rs.getInt("fkComponente"), capacidadeDisco, "GB"));
                    System.out.println("DISCO já cadastrada!");
                    configDisco = true;
                } else if (rs.getInt("fkTotem") == Totem.getId() && rs.getInt("fkComponente") == 4) {
                    configs.add(new Configuracao(rs.getInt("fkTotem"),
                            rs.getInt("fkComponente"), bandaLarga, "MBPS"));
                    System.out.println("REDE já cadastrada!\n");
                    configRede = true;
                }
            }

            sql = "INSERT INTO Configuracao "
                    + "(fkTotem, fkComponente, capacidade, unidadeMedida) "
                    + "VALUES (?, ?, ?, ?)";
            st = conn.prepareStatement(sql);

            if (!configCpu) {
                st.setInt(1, Totem.getId());
                st.setInt(2, 1);
                st.setDouble(3, frequencia);
                st.setString(4, "GHz");
                st.executeUpdate();
                System.out.println("CPU cadastrada com sucesso!");
            }
            if (!configRam) {
                st.setInt(1, Totem.getId());
                st.setInt(2, 2);
                st.setDouble(3, capacidadeRam);
                st.setString(4, "GB");
                st.executeUpdate();
                System.out.println("RAM cadastrada com sucesso!");
            }
            if (!configDisco) {
                st.setInt(1, Totem.getId());
                st.setInt(2, 3);
                st.setDouble(3, capacidadeDisco);
                st.setString(4, "GB");
                st.executeUpdate();
                System.out.println("DISCO cadastrado com sucesso!");
            }
            if (!configRede) {
                st.setInt(1, Totem.getId());
                st.setInt(2, 4);
                st.setDouble(3, bandaLarga);
                st.setString(4, "MBPS");
                st.executeUpdate();
                System.out.println("REDE cadastrada com sucesso!\n");
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
