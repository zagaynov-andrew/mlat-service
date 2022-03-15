package datacenter;

import shared.Location2D;
import shared.MLATMessage;
import shared.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;


public class MlatSystem extends PositioningSystem {

    private static final Instant TIMEOUT = Instant.ofEpochSecond(1);
    private HashMap<Integer, Instant> lastCheck;

    @Override
    public boolean addStation(Station station) {
        if (!(station instanceof MLATStation))
            return false;
        return stations.put(station.getId(), station) != null;
    }

    @Override
    public boolean removeStation(int id) {
        return stations.remove(id) != null;
    }

    @Override
    public boolean addMessage(Message message) {
        if (!(message instanceof MLATMessage))
            return false;
        MLATMessage mlatMessage = (MLATMessage) message;
        return messages.get(mlatMessage.getObjectId()).add(mlatMessage);
    }

    @Override
    public Location2D calculate(int objectId) {
        ArrayList<Message> curMessages = messages.get(objectId);
        if (curMessages.size() < 3 ) {
            Instant lastTimeStamp = lastCheck.get(objectId);
            Instant diff = Instant.now().minusNanos(lastTimeStamp.getNano());
            if (diff.isAfter(TIMEOUT))
                messages.remove(objectId);
            return null;
        }
//        x = ((y2 - y1) * (r2 * r2 - r3 * r3 - y2 * y2 + y3 * y3 - x2 * x2 + x3 * x3) - (y3 - y2) * (r1 * r1 - r2 * r2 - y1 * y1 + y2 * y2 - x1 * x1 + x2 * x2)) / (2 * ((y3 - y2) * (x1 - x2)  - (y2 - y1) * (x2 - x3)));
//        y = ((x2 - x1) * (r2 * r2 - r3 * r3 - x2 * x2 + x3 * x3 - y2 * y2 + y3 * y3) - (x3 - x2) * (r1 * r1 - r2 * r2 - x1 * x1 + x2 * x2 - y1 * y1 + y2 * y2)) / (2 * ((x3 - x2) * (y1 - y2)  - (x2 - x1) * (y2 - y3)));
        return null;
    }

    public static void main(String[] args) {
        System.out.println(TIMEOUT);
    }
}
