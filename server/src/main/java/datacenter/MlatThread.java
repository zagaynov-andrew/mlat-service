package datacenter;

import shared.Location2D;
import shared.MlatStation;
import shared.StationMessage;

import java.io.*;
import java.net.Socket;

public class MlatThread extends Thread {

    private final Socket            socket;
    private       ObjectInputStream in;
    private final MlatSystem        mlatSystem;


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
        int station1Id = 0;
        int station2Id = 0;
        int station3Id = 0;

        try {
            // Station 1
            MlatStation station = (MlatStation) in.readObject();
            station1Id = station.getId();
            synchronized (mlatSystem) {
                mlatSystem.addStation(station);
            }
            // Station 2
            station = (MlatStation) in.readObject();
            station2Id = station.getId();
            synchronized (mlatSystem) {
                mlatSystem.addStation(station);
            }
            // Station 3
            station = (MlatStation) in.readObject();
            station3Id = station.getId();
            synchronized (mlatSystem) {
                mlatSystem.addStation(station);
            }

            Location2D curLocation;
            while (!socket.isClosed()) {
                curLocation = null;
                StationMessage message = (StationMessage) in.readObject();

                if (message != null) {
                    System.out.println(message);
                    synchronized (mlatSystem) {
                        mlatSystem.addMessage(message);
                        curLocation = mlatSystem.calculate(message);
                    }
                }
                if (curLocation != null)
                    System.out.println(message.getObjectId() + "(" + curLocation.x + ", " + curLocation.y + ")");
            }
            in.close();
        } catch (EOFException e) {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("Connection closed");
        } catch (IOException | ClassNotFoundException e) {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            synchronized (mlatSystem) {
                mlatSystem.removeStation(station1Id);
                mlatSystem.removeStation(station2Id);
                mlatSystem.removeStation(station3Id);
            }
        }
    }
}