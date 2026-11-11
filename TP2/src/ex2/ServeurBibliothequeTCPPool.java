package ex2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import library.Bibliotheque;
import library.GestionBibliotheque;
import ex2.TraitementClientTCP;

public class ServeurBibliothequeTCPPool {
  public static void main(String[] args) throws IOException {
    int port = args.length > 0 ? Integer.parseInt(args[0]) : 5000;

    GestionBibliotheque gestion = new GestionBibliotheque(Bibliotheque.catalogueInitial());
    ProtocoleBibliotheque protocole = new ProtocoleBibliotheque(gestion);

    try {
      ServerSocket listening = new ServerSocket(port);
      System.out.println("Library server listening on " + listening.getLocalSocketAddress());

      ExecutorService pool = Executors.newFixedThreadPool(4);
      try {
        while (true) {
          try {
            Socket socket = listening.accept();
            try {
              pool.execute(new TraitementClientTCP(socket, protocole));
            } catch (RejectedExecutionException e) {
              socket.close();
              System.err.println("Execution error: " + e.getMessage());
            }

          } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
          }
        }
      } finally {
        pool.shutdown();
      }

    } catch (IOException e) {
      System.err.println("Server error: " + e.getMessage());
    }
  }
}
