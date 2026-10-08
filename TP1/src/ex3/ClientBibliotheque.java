package ex3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class ClientBibliotheque {

    private static final int CONNECT_TIMEOUT_MS = 5_000;
    private static final int READ_TIMEOUT_MS = 10_000;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5000;

        String requete;
        try (Scanner console = new Scanner(System.in, StandardCharsets.UTF_8.name())) {
            System.out.print("Request (LISTE or RECHERCHE;theme): ");
            if (!console.hasNextLine()) {
                System.err.println("No request provided.");
                System.exit(2);
            }
            requete = console.nextLine();
        }

        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT_MS);
            socket.setSoTimeout(READ_TIMEOUT_MS);

                try (Socket managedSocket = socket;
                 BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(managedSocket.getOutputStream(), StandardCharsets.UTF_8));
                 BufferedReader in = new BufferedReader(
                     new InputStreamReader(managedSocket.getInputStream(), StandardCharsets.UTF_8))) {

                out.write(requete + "\n");
                out.flush();

                String reponse = in.readLine();
                if (reponse == null) {
                    System.err.println("Communication failure: the server closed the connection without a response.");
                    System.exit(1);
                }
                afficher(reponse);
            }
        } catch (IOException e) {
            System.err.println("Communication failure with " + host + ":" + port + " -> " + e.getMessage());
            System.exit(1);
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
