package application;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.rede.Rede;
import conexoes.Azure;
import conexoes.MySql;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import service.Componente;
import service.Configuracao;
import service.Login;
import service.Medida;
import service.Totem;

public class Program {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        List<Componente> componentes = new ArrayList<>();
        Timer timer = new Timer();
        Azure.openConection();
        MySql.openConection();

        // Dados capturados pelo Looca
        Looca looca = new Looca();
        Rede rede = looca.getRede();

        // Pegando o hostname e convertendo para String
        String hostName = rede.getParametros().getHostName();

        System.out.println("Email:");
        String email = "MJ@stefanini.com";
        System.out.println("Senha:");
        String senha = "1234";

        // cadastrando Totem
        if (Login.realizarCadastro(email, senha)) {
            Totem.insertTotem(hostName, Azure.getConn());
            Totem.insertTotem(hostName, MySql.getConn());
            Componente.verificarCompCadastrado("Azure", Azure.getConn());
            Componente.verificarCompCadastrado("Localmente", MySql.getConn());
            Configuracao.inserirConfiguracao("Azure", Login.getEmp().getBandaLarga(), 
                    Azure.getConn());
            Configuracao.inserirConfiguracao("Localmente", Login.getEmp().getBandaLarga(), 
                    MySql.getConn());
        }

        TimerTask task = new TimerTask() {
            public void run() {
                Medida.inserirMedidas("Azure", Azure.getConn());
                Medida.inserirMedidas("Localmente", MySql.getConn());
            }
        };

        // Agendar a tarefa para ser executada a cada 5 segundos
        timer.schedule(task, 0, 5000);
        sc.close();
    }
}
