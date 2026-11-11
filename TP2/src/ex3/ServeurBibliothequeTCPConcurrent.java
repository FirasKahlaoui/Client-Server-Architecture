package ex3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import library.Bibliotheque;
import library.GestionBibliotheque;
import ex3.TraitementClientTCP;

public class ServeurBibliothequeTCPConcurrent {
  public static void main(String[] args) throws IOException {
    int port = args.length > 0 ? Integer.parseInt(args[0]) : 5000;

    GestionBibliotheque gestion = new GestionBibliotheque(Bibliotheque.catalogueInitial());
    ProtocoleBibliotheque protocole = new ProtocoleBibliotheque(gestion);

    try {
      ServerSocket listening = new ServerSocket(port);
      System.out.println("Library server listening on " + listening.getLocalSocketAddress());

      while (true) {
        try {
          Socket socket = listening.accept();
          TraitementClientTCP TCPSocket = new TraitementClientTCP(socket, protocole);
          Thread TCPThread = new Thread(TCPSocket);
          TCPThread.start();

        } catch (IOException e) {
          System.err.println("Client error: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.err.println("Server error: " + e.getMessage());
    }
  }
}
