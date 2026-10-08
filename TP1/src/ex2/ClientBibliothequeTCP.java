package ex2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientBibliothequeTCP {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5000;

        try (Socket socket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            System.out.println("Client connected to " + host + ":" + port);

            out.write("PING\n");
            out.flush();

            String response = in.readLine();
            if (response == null) {
                System.err.println("Communication failure: connection closed without a response");
            } else {
                System.out.println("Response: " + response);
            }
        } catch (IOException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}
