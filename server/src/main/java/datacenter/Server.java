package datacenter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final static int PORT = 1234;

    public static void main(String[] args) {

        MlatSystem mlatSystem = new MlatSystem();

        System.out.println("Waiting for a connection on " + PORT);
        try (ServerSocket listener = new ServerSocket(PORT)) {

            while (true) {
                Socket newClient = listener.accept();
                new MlatThread(newClient, mlatSystem).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
}