package datacenter;

import org.apache.log4j.Logger;

import shared.Location2D;
import shared.MlatStation;
import shared.StationMessage;

import java.io.*;
import java.net.Socket;

public class MlatThread extends Thread {

    private final Socket            socket;
    private       ObjectInputStream in;
    private final MlatSystem        mlatSystem;
    private static final Logger log = Logger.getLogger(MlatThread.class);


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

        // Add stations
        try {
            // Station 1
            MlatStation station = (MlatStation) in.readObject();
            station1Id = station.getId();
            synchronized (mlatSystem) {
                if (mlatSystem.addStation(station))
                    log.info("New station " + station.getId());
                else {
                    log.warn("Such station is already connected: id " + station.getId());
                    socket.close();
                    return;
                }
            }
            // Station 2
            station = (MlatStation) in.readObject();
            station2Id = station.getId();
            synchronized (mlatSystem) {
                if (mlatSystem.addStation(station))
                    log.info("New station " + station.getId());
                else {
                    log.warn("Such station is already connected: id " + station.getId());
                    socket.close();
                    return;
                }
            }
            // Station 3
            station = (MlatStation) in.readObject();
            station3Id = station.getId();
            synchronized (mlatSystem) {
                if (mlatSystem.addStation(station))
                    log.info("New station " + station.getId());
                else {
                    log.warn("Stopping an attempt to connect the station : matching id : " + station.getId());
                    socket.close();
                    return;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        // Read and process messages
        try {
            Location2D curLocation;
            while (!socket.isClosed()) {
                curLocation = null;
                StationMessage message = (StationMessage) in.readObject();

                if (message != null) {
                    if (!message.isCorrectChecksum()) {
                        log.warn("Invalid checksum");
                        continue;
                    }
//                    System.out.println(message);
                    synchronized (mlatSystem) {
                        mlatSystem.addMessage(message);
                        curLocation = mlatSystem.calculate(message);
                        if (curLocation != null) {
                            LocationStamp curLocationStamp = new LocationStamp(message.getObjectId(),
                                                                               message.getToT(),
                                                                               curLocation);
                            if (mlatSystem.addLastLocationStamp(curLocationStamp))
                                log.info(message.getObjectId() + "(" + curLocation.x + ", " + curLocation.y + ")");
                            else
                                log.warn("Sharp displacement of the object : id "
                                        + message.getObjectId() + "(" + curLocation.x + ", " + curLocation.y + ")");
                        }
                    }
                }
            }
            in.close();
        } catch (EOFException e) {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            log.info("Connection closed");
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
                if (mlatSystem.removeStation(station1Id))
                    log.info("Station " + station1Id + " removed");
                if (mlatSystem.removeStation(station2Id))
                    log.info("Station " + station2Id + " removed");
                if (mlatSystem.removeStation(station3Id))
                    log.info("Station " + station3Id + " removed");
            }
        }
    }
}