package station;

import shared.Location;
import shared.Location2D;
import shared.MlatStation;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Station {

    public static void main(String[] args) {

        if (args.length < 5) {
            System.out.println("Not enough arguments!");
            return;
        }

        final int STATION_PORT = Integer.parseInt(args[0]);
        final int DC_PORT      = Integer.parseInt(args[1]);
        final int STATION_ID   = Integer.parseInt(args[2]);
        final double X_POS     = Double.parseDouble(args[3]);
        final double Y_POS     = Double.parseDouble(args[4]);

        try (Socket socket = new Socket("localhost", DC_PORT);
             ObjectOutputStream dc_out = new ObjectOutputStream(socket.getOutputStream()); )
        {
            System.out.println("Station connected to DataCenter.");
            MlatStation station = new MlatStation(STATION_ID, new Location2D(X_POS, Y_POS));
            dc_out.writeObject(station);

            try (ServerSocket listener = new ServerSocket(STATION_PORT)) {
                System.out.println("Waiting for a connection on " + STATION_PORT);
                while (true) {
                    Socket newClient = listener.accept();
                    new StationThread(newClient, dc_out).start();
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
