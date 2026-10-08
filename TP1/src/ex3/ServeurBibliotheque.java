package ex3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import library.Bibliotheque;
import library.GestionBibliotheque;


public class ServeurBibliotheque {

    /** A client that connects and never sends a line must not block the sequential server forever. */
    private static final int CLIENT_READ_TIMEOUT_MS = 10_000;

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 5000;
        InetAddress bindAddress = args.length > 1 ? InetAddress.getByName(args[1]) : null; // null = wildcard

        GestionBibliotheque gestion = new GestionBibliotheque(Bibliotheque.catalogueInitial());
        ProtocoleBibliotheque protocole = new ProtocoleBibliotheque(gestion);

        try (ServerSocket listening = new ServerSocket(port, 50, bindAddress)) {
            System.out.println("Library server listening on " + listening.getLocalSocketAddress());

            while (true) {
                try (Socket socket = listening.accept()) {
                    serve(socket, protocole);
                } catch (IOException e) {
                    // A failing client must never stop the server.
                    System.err.println("Client exchange failed: " + e.getMessage());
                }
            }
        }
    }

    private static void serve(Socket socket, ProtocoleBibliotheque protocole) throws IOException {
        socket.setSoTimeout(CLIENT_READ_TIMEOUT_MS);
        String remote = String.valueOf(socket.getRemoteSocketAddress());

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

        String requete = in.readLine();
        if (requete == null) {
            // End of stream: the client closed without sending anything. Not the same as an empty line.
            System.out.println("[" + remote + "] connection closed without a request");
            return;
        }

        System.out.println("[" + remote + "] request : " + requete);
        String reponse = protocole.traiter(requete);
        out.write(reponse + "\n");
        out.flush();
        System.out.println("[" + remote + "] response: " + reponse);
    }
}
