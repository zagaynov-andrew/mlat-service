package mlatclient;

import shared.MlatMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;


public class Client {

    public static void main(String[] args) throws InterruptedException, IOException {

        try (Socket socket = new Socket("localhost", 1234);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); )
        {
            System.out.println("Client connected to socket.");

            while (!socket.isOutputShutdown()) {
                MlatMessage msg = new MlatMessage(7, Instant.now(), 3);
                out.writeObject(msg);
                Thread.sleep(2000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
