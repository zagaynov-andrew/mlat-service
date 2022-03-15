package datacenter;

import shared.Location;
import shared.Message;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class PositioningSystem {

    protected HashMap<Integer, Station> stations;
    protected HashMap<Integer, ArrayList<Message>> messages;

    public abstract boolean addStation(Station station);
    public abstract boolean removeStation(int id);

    public abstract boolean addMessage(Message message);
    public abstract Location calculate(int objectId);
}
