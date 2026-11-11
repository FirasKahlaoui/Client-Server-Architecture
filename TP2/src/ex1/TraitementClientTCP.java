package ex1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import ex1.ProtocoleBibliotheque;
import java.time.LocalDateTime;
import library.Bibliotheque;
import library.GestionBibliotheque;

public class TraitementClientTCP implements Runnable {
  private final Socket socket;
  private final ProtocoleBibliotheque protocole;
  private static final int CLIENT_READ_TIMEOUT_MS = 10000;
  private static final int SIMULATED_DELAY_MS = 5000;

  public TraitementClientTCP(Socket socket, ProtocoleBibliotheque protocole) {
    this.socket = socket;
    this.protocole = protocole;
  }

  public void run() {
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

    } catch (IOException | InterruptedException e) {
      System.err.println("Some error: " + e.getMessage());
    } finally {
      try {
        socket.close();
      } catch (IOException e) {

        System.err.println("Some error: " + e.getMessage());
      }
    }
  }

  private static void attendreSimulation() throws InterruptedException {
    Thread.sleep(SIMULATED_DELAY_MS);
  }

}
