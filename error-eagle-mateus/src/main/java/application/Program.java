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
import java.util.logging.Level;
import java.util.logging.Logger;
import service.Componente;
import service.Configuracao;
import service.Login;
import service.Medida;
import service.Totem;

public class Program {

    public static void main(String[] args) {

        try {
            Scanner sc = new Scanner(System.in);
            List<Componente> componentes = new ArrayList<>();
            Timer timer = new Timer();
            Azure.openConection();
            MySql.openConection();
            System.out.println();
            
            // instânciando Looca
            Looca looca = new Looca();
            Rede rede = looca.getRede();

            // Pegando o hostName
            String hostName = rede.getParametros().getHostName();
            System.out.println("--- ERROR EAGLE ---");
            System.out.println("Email:");
            String email = "MJ@stefanini.com";
            System.out.println("Senha:");
            String senha = "1234";
            Thread.sleep(2000);
            
            // Validando Login
            if (Login.realizarCadastro(email, senha)) {
                Totem.insertTotem(hostName, MySql.getConn());
                Totem.insertTotem(hostName, Azure.getConn());
                clearTerminal();
                Componente.verificarCompCadastrado("Localmente", MySql.getConn());
                Componente.verificarCompCadastrado("Azure", Azure.getConn());
                clearTerminal();
                Configuracao.inserirConfiguracao("Localmente", Login.getEmp().getBandaLarga(),
                        MySql.getConn(), 1);
                Configuracao.inserirConfiguracao("Azure", Login.getEmp().getBandaLarga(),
                        Azure.getConn(), Totem.getId());

                TimerTask task = new TimerTask() {
                    public void run() {
                        clearTerminal();
                        Medida.inserirMedidas("Azure", Azure.getConn(), Totem.getId());
                        Medida.inserirMedidas("Localmente", MySql.getConn(), 1);
                        System.out.println("Aperte ENTER para encerrar o programa");
                    }
                };

                // Agendar a tarefa para ser executada a cada 20 segundos
                timer.schedule(task, 0, 20000);
            } else {
                pararAplicacao(timer);
            }

            // Encerra a aplicação ao usuário apertar enter
            sc.nextLine();
            pararAplicacao(timer);
            sc.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void clearTerminal() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void pararAplicacao(Timer timer) {
        try {
            // Interromper o timer
            System.out.println("Enterrompendo captura de dados...");
            Thread.sleep(2000);
            timer.cancel();
            System.out.println("Captura interrompida!");
            Thread.sleep(2000);
            // Fecha as conexões com a Azure e Local(MySQL)
            Azure.closeConection();
            System.out.println("Conexão Azure encerrada com sucesso!");
            Thread.sleep(2000);
            MySql.closeConection();
            System.out.println("Conexão Local(MySQL) encerrada com sucesso!");
            Thread.sleep(2000);
            // Encerrar a aplicação
            System.out.println("Encerrando programa... Obrigado por usar a "
                    + "ErrorEagle ^^");

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
