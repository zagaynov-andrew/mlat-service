package datacenter;

import shared.*;

import java.util.ArrayList;
import java.util.HashMap;


public class MlatSystem extends PositioningSystem {

    private static final long TIMEOUT = 1000000000;
    private HashMap<Integer, Long> lastCheck = new HashMap<>();

    @Override
    public boolean addStation(Station station) {
        if (!(station instanceof MlatStation))
            return false;
        return stations.put(station.getId(), station) != null;
    }

    @Override
    public boolean removeStation(int id) {
        return stations.remove(id) != null;
    }

    @Override
    public boolean addMessage(Message message) {
        if (!(message instanceof StationMessage))
            return false;
        StationMessage stationMessage = (StationMessage) message;
        messages.computeIfAbsent(stationMessage.getObjectId(), k -> new ArrayList<>());
        return messages.get(stationMessage.getObjectId()).add(stationMessage);
    }

    @Override
    public Location2D calculate(Message message) {
        if (!(message instanceof StationMessage))
            return null;
        StationMessage curMessage = (StationMessage) message;
        int curObjectId = curMessage.getObjectId();
        ArrayList<Message> curMessages = messages.get(curObjectId);
//        if (curMessages.size() < 3 ) {
//            long lastTimeStamp = lastCheck.get(curMessage.getObjectId());
//            long diff = System.nanoTime() - lastTimeStamp;
//            if (diff > TIMEOUT)
//                curMessages.removeIf(msg -> msg.equals(message));
//            return null;
//        }

        ArrayList<Message> msgArray = new ArrayList<>();
        for (Message msg : curMessages)
            if (msg.equals(message))
                msgArray.add(msg);
        if (msgArray.size() < 3 ) {
            Long lastTimeStamp = lastCheck.get(curObjectId);
            if (lastTimeStamp == null)
                return null;
            long diff = System.nanoTime() - lastTimeStamp;
            if (diff > TIMEOUT)
                curMessages.removeIf(msg -> msg.equals(message));
            return null;
        }

        for (Message msg : msgArray)
            System.out.println("\t" + ((StationMessage) msg).getObjectId() + " " + ((StationMessage) msg).getToT());

//        x = ((y2 - y1) * (r2 * r2 - r3 * r3 - y2 * y2 + y3 * y3 - x2 * x2 + x3 * x3) - (y3 - y2) * (r1 * r1 - r2 * r2 - y1 * y1 + y2 * y2 - x1 * x1 + x2 * x2)) / (2 * ((y3 - y2) * (x1 - x2)  - (y2 - y1) * (x2 - x3)));
//        y = ((x2 - x1) * (r2 * r2 - r3 * r3 - x2 * x2 + x3 * x3 - y2 * y2 + y3 * y3) - (x3 - x2) * (r1 * r1 - r2 * r2 - x1 * x1 + x2 * x2 - y1 * y1 + y2 * y2)) / (2 * ((x3 - x2) * (y1 - y2)  - (x2 - x1) * (y2 - y3)));
        return null;
    }

    public static void main(String[] args) {
        System.out.println(TIMEOUT);
    }
}
