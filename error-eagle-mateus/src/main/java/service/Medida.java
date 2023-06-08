package service;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import java.sql.Connection;
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
    
    public static void inserirMedidas(String tm, Connection conn){
        System.out.println("Inserindo dados " + tm + ":");
        try {
            inserirMedidasCPU(conn);
            inserirMedidasRAM(conn);
            inserirMedidasDisco(conn);
            inserirMedidasRede(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void inserirMedidasCPU(Connection conn) throws SQLException {
        String sql = "INSERT INTO Medida (percentual, dataHora, fkTotem, fkComponente) "
                + "VALUES (?, ?, ?, ?)";
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(sql);
            st.setDouble(1, looca.getProcessador().getUso());
            st.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            st.setInt(3, Totem.getId());
            st.setInt(4, 1);
            st.executeUpdate();
            System.out.print("CPU... ");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public static void inserirMedidasRAM(Connection conn) throws SQLException {
        String sql = "INSERT INTO Medida (percentual, dataHora, fkTotem, fkComponente) "
                + "VALUES (?, ?, ?, ?)";
        PreparedStatement st = null;
        Double usoRAM = looca.getMemoria().getEmUso().doubleValue();
        Double totalRam = looca.getMemoria().getTotal().doubleValue();
        Double porcentagemUsoRam = (usoRAM / totalRam) * 100;
        
        try {
            st = conn.prepareStatement(sql);
            st.setDouble(1, porcentagemUsoRam);
            st.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            st.setInt(3, Totem.getId());
            st.setInt(4, 2);
            st.executeUpdate();
            System.out.print("RAM... ");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public static void inserirMedidasDisco(Connection conn) throws SQLException {
        String sql = "INSERT INTO Medida (percentual, dataHora, fkTotem, fkComponente) "
                + "VALUES (?, ?, ?, ?)";
        PreparedStatement st = null;

        // DISCO
        DiscoGrupo discoGrupo = new DiscoGrupo();
        Double volumeDisponivel = 0.0;
        Double volumeTotal = 0.0;
        List<Volume> volumes = discoGrupo.getVolumes();

        for (Volume volume : volumes) {
            if (volume.getDisponivel().doubleValue() > 0 && volume.getTotal().doubleValue() > 0) {
                volumeDisponivel += volume.getDisponivel().doubleValue();
                volumeTotal += volume.getTotal().doubleValue();
            }
        }

        volumeDisponivel /= Math.pow(2, 30);
        volumeTotal /= Math.pow(2, 30);

        Double porcentagemDisponivel = (volumeDisponivel / volumeTotal) * 100;

        try {
            st = conn.prepareStatement(sql);
            st.setDouble(1, porcentagemDisponivel);
            st.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            st.setInt(3, Totem.getId());
            st.setInt(4, 3);
            st.executeUpdate();
            System.out.print("DISCO... ");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public static void inserirMedidasRede(Connection conn) throws SQLException {
        String sql = "INSERT INTO Medida (percentual, dataHora, fkTotem, fkComponente) "
                + "VALUES (?, ?, ?, ?)";
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(sql);
            st.setDouble(1, Latencia.getLatencia());
            st.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            st.setInt(3, Totem.getId());
            st.setInt(4, 4);
            st.executeUpdate();
            System.out.println("REDE...");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                st.close();
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
