package datacenter;

import shared.*;

import java.util.ArrayList;
import java.util.HashMap;


public class MlatSystem {

    private static final long TIMEOUT = 1000000000;
    private HashMap<Integer, Long>                      lastCheck = new HashMap<>();
    private HashMap<Integer, MlatStation>               stations  = new HashMap<>();
    private HashMap<Integer, ArrayList<StationMessage>> messages  = new HashMap<>();

    public boolean addStation(MlatStation station) {
        if (stations.put(station.getId(), station) != null) {
            System.out.println("New station " + station.getId());
            return true;
        } else return false;
    }

    public boolean removeStation(int id) {
        if (stations.remove(id) != null) {
            System.out.println("Station " + id + " removed");
            return true;
        } else return false;
    }

    public boolean addMessage(StationMessage msg) {
        messages.computeIfAbsent(msg.getObjectId(), k -> new ArrayList<>());
        return messages.get(msg.getObjectId()).add(msg);
    }

    public Location2D calculate(StationMessage msg) {
        int curObjectId = msg.getObjectId();
        ArrayList<StationMessage> objMessages = messages.get(curObjectId);

        ArrayList<StationMessage> curMessages = new ArrayList<>();
        for (StationMessage objMsg : objMessages)
            if (objMsg.getObjectId() == msg.getObjectId() && objMsg.getToT() == msg.getToT())
                curMessages.add(objMsg);
        if (curMessages.size() < 3 ) {
            Long lastTimeStamp = lastCheck.get(curObjectId);
            if (lastTimeStamp == null)
                return null;
            long diff = System.nanoTime() - lastTimeStamp;
            if (diff > TIMEOUT)
                objMessages.removeIf(m -> m.getObjectId() == msg.getObjectId()
                                       && m.getToT() == msg.getToT());
            return null;
        }

        for (StationMessage cur : curMessages) {
            System.out.print("\t");
            System.out.println(cur);
        }

        StationMessage msg1 = curMessages.get(0);
        StationMessage msg2 = curMessages.get(1);
        StationMessage msg3 = curMessages.get(2);

        long r1 = msg1.getToF() / 10 * 3;
        long r2 = msg2.getToF() / 10 * 3;
        long r3 = msg3.getToF() / 10 * 3;

        int stationId1 = msg1.getStationId();
        int stationId2 = msg2.getStationId();
        int stationId3 = msg3.getStationId();

        double x1 = stations.get(stationId1).getLocation().x;
        double y1 = stations.get(stationId1).getLocation().y;

        double x2 = stations.get(stationId2).getLocation().x;
        double y2 = stations.get(stationId2).getLocation().y;

        double x3 = stations.get(stationId3).getLocation().x;
        double y3 = stations.get(stationId3).getLocation().y;

        double x = ((y2 - y1) * (r2 * r2 - r3 * r3 - y2 * y2 + y3 * y3 - x2 * x2 + x3 * x3) - (y3 - y2) * (r1 * r1 - r2 * r2 - y1 * y1 + y2 * y2 - x1 * x1 + x2 * x2)) / (2 * ((y3 - y2) * (x1 - x2)  - (y2 - y1) * (x2 - x3)));
        double y = ((x2 - x1) * (r2 * r2 - r3 * r3 - x2 * x2 + x3 * x3 - y2 * y2 + y3 * y3) - (x3 - x2) * (r1 * r1 - r2 * r2 - x1 * x1 + x2 * x2 - y1 * y1 + y2 * y2)) / (2 * ((x3 - x2) * (y1 - y2)  - (x2 - x1) * (y2 - y3)));
        return new Location2D(x, y);
    }

    private String getReadableTime(long nanos){

        long tempSec    = nanos / 1000000000L;
        long ms         = (nanos / 1000000L) % 1000L;
        long sec        = tempSec % 60;
        long min        = (tempSec / 60) % 60;
        long hour       = (tempSec / (60 * 60)) % 24;
        long day        = (tempSec / (24 * 60 * 60)) % 24;

        return String.format("%dd %dh %dm %ds %dms", day, hour, min, sec, ms);
    }

    public static void main(String[] args) {
        System.out.println(TIMEOUT);
    }
}
