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
        int stationId = 0;

        // Add stations
        try {
            // Add station
            MlatStation station = (MlatStation) in.readObject();
            stationId = station.getId();
            synchronized (mlatSystem) {
                if (mlatSystem.addStation(station))
                    log.info("New station " + station.getId());
                else {
                    log.warn("Such station is already connected: id " + station.getId());
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
                StationMessage message = (StationMessage) in.readObject();

                if (message != null) {
                    if (!message.isCorrectChecksum()) {
                        log.warn("Invalid checksum");
                        continue;
                    }
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
                if (mlatSystem.removeStation(stationId))
                    log.info("Station " + stationId + " removed");
            }
        }
    }
}