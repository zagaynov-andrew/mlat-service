package DataCenter;

import Shared.Location2D;
import Shared.MLATMessage;
import Shared.Message;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;


public class MLATSystem extends PositioningSystem {

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
        return null;
    }

    public static void main(String[] args) {
        System.out.println(TIMEOUT);
    }
}
