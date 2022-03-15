package datacenter;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class MlatThread implements Runnable {

    static Socket socket;

    public MlatThread() {
        try {
            socket = new Socket("localhost", 3345);
            System.out.println("Client connected to socket");
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try (
            DataInputStream ois = new DataInputStream(socket.getInputStream())) {
            System.out.println("Client oos & ois initialized");

//            int i = 0;
//            while (i < 5) {
                System.out.println("reading...");
                String in = ois.readUTF();
                System.out.println(in);
//                i++;
                Thread.sleep(5000);

//            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}