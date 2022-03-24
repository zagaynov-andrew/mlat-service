package tram;

import shared.MlatMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;


public class Tram {

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

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Not enough arguments!");
            return;
        }

        ArrayList<Integer> dataArray = readConfigFile(args[1]);
        if (dataArray == null)
            return;

        if (dataArray.size() < 5 && (dataArray.size() - 4) % 4 != 0) {
            System.out.println("Not enough data in config file!");
            return;
        }
        long updateDelay = (long) dataArray.get(3) * 1000000L;

        int station_port_1 = dataArray.get(1);
        int station_port_2 = dataArray.get(2);
        int station_port_3 = dataArray.get(3);


        try (Socket socket_1 = new Socket("localhost", station_port_1);
             ObjectOutputStream out_1 = new ObjectOutputStream(socket_1.getOutputStream());
             Socket socket_2 = new Socket("localhost", station_port_2);
             ObjectOutputStream out_2 = new ObjectOutputStream(socket_2.getOutputStream());
             Socket socket_3 = new Socket("localhost", station_port_3);
             ObjectOutputStream out_3 = new ObjectOutputStream(socket_3.getOutputStream());)
        {
            System.out.println("Tram is connected to the station at port " + station_port_1);
            long lastSend;
            int i = 4;
            while (i != dataArray.size()) {
                lastSend = System.nanoTime();
                MlatMessage msg = new MlatMessage(dataArray.get(0), lastSend);
                System.out.println("Send time:\t" + lastSend / 1000000L + " ms");
                while (System.nanoTime() - lastSend < dataArray.get(i + 1) * 1000000L) {
                    Thread.sleep(1);
                }
                System.out.println("Real send time:\t" + System.nanoTime() / 1000000L + " ms");
                if (!socket_1.isOutputShutdown())
                    out_1.writeObject(msg);
                if (!socket_2.isOutputShutdown())
                    out_2.writeObject(msg);
                if (!socket_3.isOutputShutdown())
                    out_3.writeObject(msg);

                while (System.nanoTime() - lastSend < updateDelay) {
                    Thread.sleep(0,10);
                }
                System.out.println(Instant.now());
                i += 2;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

//        try (Socket socket = new Socket("localhost", station_port_1);
//             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); )
//        {
//            System.out.println("Tram is connected to the station at port " + station_port_2);
//            out_2 = out;
//            socket_2 = socket;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try (Socket socket = new Socket("localhost", station_port_1);
//             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); )
//        {
//            System.out.println("Tram is connected to the station at port " + station_port_3);
//            out_3 = out;
//            socket_3 = socket;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
