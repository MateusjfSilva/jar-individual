package service;

import java.io.IOException;
import java.net.InetAddress;

public class Latencia {

  public static Long getLatencia() {

    String host = "www.google.com";

    try {
      InetAddress inetAddress = InetAddress.getByName(host);
      long startTime = System.currentTimeMillis();

      if (inetAddress.isReachable(5000)) {
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        return timeTaken;
      } else {
        System.out.println("Ping failed.");
        return null;
      }
    } catch (IOException e) {
      System.out.println("Error occurred while pinging " + host + ": " + e.getMessage());

    }
    return null;
  }

}