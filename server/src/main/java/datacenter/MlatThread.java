package datacenter;

import shared.MlatMessage;

import java.io.*;
import java.net.Socket;

public class MlatThread extends Thread {

    private final Socket socket;
    private ObjectInputStream in;

    public MlatThread(Socket socket) throws IOException {
        this.socket = socket;
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                MlatMessage message = (MlatMessage) in.readObject();
                if (message != null)
                    System.out.println("'" + message.getObjectId() + " " + message.getSendTime() + "'");
            }

            in.close();
        } catch (EOFException ex3) {
            System.out.println("Connection closed");
        } catch (IOException | ClassNotFoundException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                try {
                    in.close();
                } catch (IOException ex2) {
                    ex2.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }
}