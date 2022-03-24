package station;

import shared.MlatMessage;
import shared.StationMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class StationThread extends Thread {

    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream dc_out;

    public StationThread(Socket socket, ObjectOutputStream dc_out) throws IOException {
        this.socket = socket;
        this.dc_out = dc_out;
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
                if (message != null) {
                    long ToA = System.nanoTime();
                    System.out.println(ToA / 1000000L);
                    System.out.println("'" + message.getObjectId()
                            + " " + message.getSendTime() / 1000000L + "'");
                    dc_out.writeObject(new StationMessage(message.getObjectId(), ToA - message.getSendTime()));
                }
            }
            in.close();
        } catch (EOFException ex) {
            System.out.println("Connection closed");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}