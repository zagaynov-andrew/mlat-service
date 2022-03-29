package datacenter;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final static int PORT = 1234;
    private static final Logger log = Logger.getLogger(Server.class);

    public static void main(String[] args) {

        MlatSystem mlatSystem = new MlatSystem();

        System.out.println("Waiting for a connection on " + PORT);
        try (ServerSocket listener = new ServerSocket(PORT)) {

            log.info("MLAT Server started");
            while (true) {
                Socket newClient = listener.accept();
                new MlatThread(newClient, mlatSystem).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
}