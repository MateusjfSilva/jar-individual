package service;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import conexoes.Azure;
import conexoes.MySql;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Medida {

    private Integer id;
    private Double percentual;
    private static final LocalDateTime dataHora = LocalDateTime.now();
    private Integer fkTotem;
    private Integer fkComponente;

    private static Looca looca = new Looca();

    public Medida() {
    }

    public static void inserirMedidasCPU() throws SQLException {
        String sql = "INSERT INTO Medida (percentual, dataHora, fkTotem, fkComponente) VALUES (?, ?, ?, ?)";
        PreparedStatement stAzure = null;
        PreparedStatement stLocal = null;

        try {
            stAzure = Azure.getConn().prepareStatement(sql);
            stAzure.setDouble(1, looca.getProcessador().getUso());
            stAzure.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            stAzure.setInt(3, Totem.getId());
            stAzure.setInt(4, 1);
            stAzure.executeUpdate();
            System.out.println("Capturando medidas CPU (Azure)...");

            stLocal = MySql.getConn().prepareStatement(sql);
            stLocal.setDouble(1, looca.getProcessador().getUso());
            stLocal.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            stLocal.setInt(3, Totem.getId());
            stLocal.setInt(4, 1);
            stLocal.executeUpdate();
            System.out.println("Capturando medidas CPU (Local)...");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stAzure != null) {
                stAzure.close();
            }
            if (stLocal != null) {
                stLocal.close();
            }
        }
    }

    public static void inserirMedidasRAM() throws SQLException {
        String sql = "INSERT INTO Medida (percentual, dataHora, fkTotem, fkComponente) VALUES (?, ?, ?, ?)";
        PreparedStatement stAzure = null;
        PreparedStatement stLocal = null;

        try {
            stAzure = Azure.getConn().prepareStatement(sql);
            stAzure.setDouble(1, looca.getMemoria().getEmUso()
                    / looca.getMemoria().getTotal() * 100);
            stAzure.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            stAzure.setInt(3, Totem.getId());
            stAzure.setInt(4, 2);
            stAzure.executeUpdate();
            System.out.println("Capturando medidas RAM (Azure)...");

            stLocal = MySql.getConn().prepareStatement(sql);
            stLocal.setDouble(1, looca.getMemoria().getEmUso()
                    / looca.getMemoria().getTotal() * 100);
            stLocal.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            stLocal.setInt(3, Totem.getId());
            stLocal.setInt(4, 2);
            stLocal.executeUpdate();
            System.out.println("Capturando medidas RAM (Local)...");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stAzure != null) {
                stAzure.close();
            }
            if (stLocal != null) {
                stLocal.close();
            }
        }
    }

    public static void inserirMedidasDisco() throws SQLException {
        String sql = "INSERT INTO Medida (percentual, dataHora, fkTotem, fkComponente) VALUES (?, ?, ?, ?)";
        PreparedStatement stAzure = null;
        PreparedStatement stLocal = null;

        // DISCO
        DiscoGrupo discoGrupo = new DiscoGrupo();
        Double porcentagemDisponivel = null;
        List<Volume> volumes = discoGrupo.getVolumes();

        for (Volume volume : volumes) {
            if (volume.getNome().equals("/") || volume.getNome().contains("C:")) {
                porcentagemDisponivel = volume.getDisponivel().doubleValue()
                        / volume.getTotal().doubleValue() * 100;

            }
        }

        try {
            stAzure = Azure.getConn().prepareStatement(sql);
            stAzure.setDouble(1, porcentagemDisponivel);
            stAzure.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            stAzure.setInt(3, Totem.getId());
            stAzure.setInt(4, 3);
            stAzure.executeUpdate();
            System.out.println("Capturando medidas DISCO (Azure)...");

            stLocal = MySql.getConn().prepareStatement(sql);
            stLocal.setDouble(1, porcentagemDisponivel);
            stLocal.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            stLocal.setInt(3, Totem.getId());
            stLocal.setInt(4, 3);
            stLocal.executeUpdate();
            System.out.println("Capturando medidas DISCO (Local)...");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stAzure != null) {
                stAzure.close();
            }
            if (stLocal != null) {
                stLocal.close();
            }
        }
    }

    public static void inserirMedidasRede() throws SQLException {
        String sql = "INSERT INTO Medida (percentual, dataHora, fkTotem, fkComponente) VALUES (?, ?, ?, ?)";
        PreparedStatement stAzure = null;
        PreparedStatement stLocal = null;

        try {
            stAzure = Azure.getConn().prepareStatement(sql);
            stAzure.setDouble(1, Latencia.getLatencia());
            stAzure.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            stAzure.setInt(3, Totem.getId());
            stAzure.setInt(4, 4);
            stAzure.executeUpdate();
            System.out.println("Capturando medidas REDE (Azure)...");

            stLocal = MySql.getConn().prepareStatement(sql);
            stLocal.setDouble(1, Latencia.getLatencia());
            stLocal.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            stLocal.setInt(3, Totem.getId());
            stLocal.setInt(4, 4);
            stLocal.executeUpdate();
            System.out.println("Capturando medidas REDE (Local)...");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stAzure != null) {
                stAzure.close();
            }
            if (stLocal != null) {
                stLocal.close();
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkTotem() {
        return fkTotem;
    }

    public void setFkTotem(Integer fkTotem) {
        this.fkTotem = fkTotem;
    }

    public Integer getFkComponente() {
        return fkComponente;
    }

    public void setFkComponente(Integer fkComponente) {
        this.fkComponente = fkComponente;
    }

}
