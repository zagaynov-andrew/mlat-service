package datacenter;

import shared.Location;
import shared.Message;
import shared.Station;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class PositioningSystem {

    protected HashMap<Integer, Station> stations = new HashMap<>();
    protected HashMap<Integer, ArrayList<Message>> messages = new HashMap<>();

    public abstract boolean addStation(Station station);
    public abstract boolean removeStation(int id);

    public abstract boolean addMessage(Message message);
    public abstract Location calculate(Message message);
}
