package ex1;

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

public class ServeurBibliotheque {
  private static final int CLIENT_READ_TIMEOUT_MS = 10000;
  private static final int SIMULATED_DELAY_MS = 5000;

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
          serve(socket, protocole);

        } catch (IOException e) {
          System.err.println("Client error: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.err.println("Server error: " + e.getMessage());
    }
  }

  private static void serve(Socket socket, ProtocoleBibliotheque protocole) throws IOException {
    try {
      socket.setSoTimeout(CLIENT_READ_TIMEOUT_MS);
      String remote = String.valueOf(socket.getRemoteSocketAddress());
      String thread = String.valueOf(Thread.currentThread().getName());

      BufferedReader in = new BufferedReader(
          new InputStreamReader(socket.getInputStream()));
      BufferedWriter out = new BufferedWriter(
          new OutputStreamWriter(socket.getOutputStream()));

      String requete = in.readLine();
      if (requete == null) {
        System.out.println("[" + remote + "] connection closed without a request");
        return;
      }

      LocalDateTime startTime = LocalDateTime.now();
      System.out.println(
          "[ Start time: " + startTime + "Remote address: " + remote + ", Thread: " + thread + "] request : "
              + requete);
      attendreSimulation();
      String reponse = protocole.traiter(requete);

      out.write(reponse + "\n");
      out.flush();
      LocalDateTime endTime = LocalDateTime.now();
      System.out.println(
          "[ End time: " + endTime + "Remote address: " + remote + ", Thread: " + thread + "] request : " + requete);

    } catch (InterruptedException e) {

      System.err.println("Some error: " + e.getMessage());
    } finally {
      socket.close();
    }
  }

  private static void attendreSimulation() throws InterruptedException {
    Thread.sleep(SIMULATED_DELAY_MS);
  }
}
