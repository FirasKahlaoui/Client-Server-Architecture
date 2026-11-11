package ex3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.net.UnknownHostException;

public class ClientBibliothequeUDP {

  private static final int CONNECT_TIMEOUT_MS = 5000;
  private static final int READ_TIMEOUT_MS = 10000;

  public static void main(String[] args) throws UnknownHostException {
    String host = args.length > 0 ? args[0] : "127.0.0.1";
    int port = args.length > 1 ? Integer.parseInt(args[1]) : 5001;
    InetAddress serveur = InetAddress.getByName(host);
    byte[] requeteBytes;

    String requete;
    try (Scanner console = new Scanner(System.in, StandardCharsets.UTF_8.name())) {
      System.out.print("Request (LISTE or RECHERCHE;theme): ");
      requete = console.nextLine();
      requeteBytes = requete.getBytes(StandardCharsets.UTF_8);
      if (requeteBytes.length > 1024) {
        System.err.println("Request too long : " + requeteBytes.length + " bytes");
        return;
      }
    }

    DatagramSocket socket = null;
    try {
      socket = new DatagramSocket();
      socket.setSoTimeout(2_000);

      DatagramPacket envoi = new DatagramPacket(requeteBytes, requeteBytes.length, serveur, 5001);
      socket.send(envoi);

      byte[] tampon = new byte[1025];
      DatagramPacket reception = new DatagramPacket(tampon, tampon.length);
      socket.receive(reception);
      if (!reception.getAddress().equals(serveur) || reception.getPort() != 5001) {
        System.err.println("Datagram rajected");
        return;
      }
      String reponse = new String(reception.getData(), reception.getOffset(), reception.getLength(),
          StandardCharsets.UTF_8);
      System.out.println("Raw response : " + reponse);

    } catch (SocketTimeoutException e) {
      System.out.println("Aucune réponse reçue dans le délai imparti");
    } catch (IOException e) {
      System.err.println("Erreur réseau : " + e.getMessage());
    } finally {
      if (socket != null) {
        socket.close();
      }
    }
  }

  private static void afficher(String reponse) {
    System.out.println("Raw response: " + reponse);
    String[] champs = reponse.split(";", -1);

    if (champs[0].equals("OK") && champs.length >= 2) {
      System.out.println("Books returned: " + champs[1]);
      for (int i = 2; i < champs.length; i++) {
        String[] livre = champs[i].split(":", 2);
        System.out.println("  - " + livre[0] + " : " + (livre.length > 1 ? livre[1] : ""));
      }
    } else if (champs[0].equals("ERR") && champs.length >= 2) {
      System.out.println("Server reported an error: " + champs[1]);
    } else {
      System.out.println("Unexpected response format.");
    }
  }
}
