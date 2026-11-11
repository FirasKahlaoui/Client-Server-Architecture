package ex3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import library.Bibliotheque;
import library.GestionBibliotheque;
import ex3.TraitementClientTCP;

public class ServeurBibliothequeUDP {
  public static void main(String[] args) throws IOException {
    int port = args.length > 0 ? Integer.parseInt(args[0]) : 5001;

    GestionBibliotheque gestion = new GestionBibliotheque(Bibliotheque.catalogueInitial());
    ProtocoleBibliotheque protocole = new ProtocoleBibliotheque(gestion);

    try {
      DatagramSocket socket = new DatagramSocket(port);
      System.out.println("Library server listening on " + socket.getLocalSocketAddress());

      while (true) {
        try {
          byte[] tampon = new byte[1025];
          DatagramPacket paquet = new DatagramPacket(tampon, tampon.length);
          socket.receive(paquet);
          String reponse;

          if (paquet.getLength() > 1024) {
            reponse = "ERR;REQUETE_INVALIDE";

          } else {
            String requete = new String(paquet.getData(), paquet.getOffset(), paquet.getLength(),
                StandardCharsets.UTF_8);
            reponse = protocole.traiter(requete);
          }

          byte[] reponseBytes = reponse.getBytes(StandardCharsets.UTF_8);
          DatagramPacket envoi = new DatagramPacket(reponseBytes, reponseBytes.length,
              paquet.getAddress(), paquet.getPort());
          socket.send(envoi);

          try {
          } catch (RejectedExecutionException e) {
            System.err.println("Execution error: " + e.getMessage());
          }

        } catch (IOException e) {
          System.err.println("Client error: " + e.getMessage());
        }
      }

    } catch (IOException e) {
      System.err.println("Server error: " + e.getMessage());
    }
  }
}
