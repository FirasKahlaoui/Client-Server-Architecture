package ex2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class ServeurBibliothequeTCP {

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 5000;

        try (ServerSocket listening = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            try (Socket socket = listening.accept();
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                 BufferedWriter out = new BufferedWriter(
                         new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

                System.out.println("Client connected: " + socket.getRemoteSocketAddress());

                String request = in.readLine();
                System.out.println("Request received: " + request);

                String response = "PING".equals(request) ? "PONG" : "ERR;COMMANDE_INCONNUE";
                out.write(response + "\n");
                out.flush();               
                System.out.println("Response sent: " + response);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
