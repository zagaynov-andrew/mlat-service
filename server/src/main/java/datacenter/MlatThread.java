package datacenter;

import shared.MlatStation;
import shared.StationMessage;

import java.io.*;
import java.net.Socket;

public class MlatThread extends Thread {

    private final Socket socket;
    private ObjectInputStream in;
    private final MlatSystem mlatSystem;


    public MlatThread(Socket socket, MlatSystem mlatSystem) throws IOException {
        this.socket = socket;
        this.mlatSystem = mlatSystem;
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            MlatStation station = (MlatStation) in.readObject();
            System.out.println("New station: id " + station.getId());
            synchronized (mlatSystem) {
                mlatSystem.addStation(station);
            }

            while (!socket.isClosed()) {
                StationMessage message = (StationMessage) in.readObject();
                if (message != null) {
                    System.out.println("'" + message.getObjectId() + " " + message.getToT() + "'");
                    synchronized (mlatSystem) {
                        mlatSystem.addMessage(message);
                        mlatSystem.calculate(message);
                    }
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