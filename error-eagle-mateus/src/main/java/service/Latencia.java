package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Latencia {

    public static Double getLatencia() {

        String host = "www.google.com";
        int count = 5; // Número de pacotes ICMP a serem enviados
        Double latency = null;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ping", "-c", 
                    String.valueOf(count), host);
            Process process = processBuilder.start();

            BufferedReader reader = 
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("time=")) {
                    int startIndex = line.indexOf("time=") + 5;
                    int endIndex = line.indexOf(" ms", startIndex);
                    String latencyStr = line.substring(startIndex, endIndex).trim();
                    latencyStr = latencyStr.replace(",", ".");
                    latency = Double.parseDouble(latencyStr);
                }
            }
            
            if (latency != null) {
                return latency;
            }
            int exitCode = process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }

}
