import shared.Location2D;
import shared.MlatStation;
import shared.StationMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

public class TestDataCenter {

    private static ArrayList<Integer> readConfigFile(String path) {
        ArrayList<Integer> res = new ArrayList<>();
        File file = new File(path);
        Scanner scan;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }

        while(scan.hasNextInt())
            res.add(scan.nextInt());
        return res;
    }

    public static long getCurTime() {
        Instant now = Instant.now();
        return now.getEpochSecond() * 1000000000L + now.getNano();
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Not enough arguments!");
            return;
        }

        ArrayList<Integer> dataArray = readConfigFile(args[0]);
        if (dataArray == null)
            return;

        if (dataArray.size() < 11 && (dataArray.size() - 10) % 7 != 0) {
            System.out.println("Not enough data in config file!");
            return;
        }
        long updateDelay = (long) dataArray.get(1) * 1000000L;

        try (Socket socket_1 = new Socket("localhost", dataArray.get(0));
             ObjectOutputStream out_1 = new ObjectOutputStream(socket_1.getOutputStream());
             Socket socket_2 = new Socket("localhost", dataArray.get(0));
             ObjectOutputStream out_2 = new ObjectOutputStream(socket_2.getOutputStream());
             Socket socket_3 = new Socket("localhost", dataArray.get(0));
             ObjectOutputStream out_3 = new ObjectOutputStream(socket_3.getOutputStream());)
        {
            System.out.println("Connected to the DataCenter at port " + dataArray.get(0));
            out_1.writeObject(new MlatStation(dataArray.get(2), new Location2D(dataArray.get(3), dataArray.get(4))));
            out_2.writeObject(new MlatStation(dataArray.get(5), new Location2D(dataArray.get(6), dataArray.get(7))));
            out_3.writeObject(new MlatStation(dataArray.get(8), new Location2D(dataArray.get(9), dataArray.get(10))));
            long lastSend;
            int i = 11;
            while (!socket_1.isOutputShutdown()
                    && !socket_2.isOutputShutdown()
                    && !socket_3.isOutputShutdown()
                    && i != dataArray.size()) {
                lastSend = getCurTime();
                StationMessage msg_1 = new StationMessage(dataArray.get(i), lastSend, dataArray.get(i + 4), dataArray.get(i + 1));
                StationMessage msg_2 = new StationMessage(dataArray.get(i), lastSend, dataArray.get(i + 5), dataArray.get(i + 2));
                StationMessage msg_3 = new StationMessage(dataArray.get(i), lastSend, dataArray.get(i + 6), dataArray.get(i + 3));
                System.out.println("The messages have been sent.");
                out_1.writeObject(msg_1);
                out_2.writeObject(msg_2);
                out_3.writeObject(msg_3);

                while (getCurTime() - lastSend < updateDelay) {
                    Thread.sleep(0,10);
                }
                i += 7;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
