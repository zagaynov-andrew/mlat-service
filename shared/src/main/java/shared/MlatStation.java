package shared;

import java.io.Serializable;

public class MlatStation implements Serializable {

    private int id;
    private Location2D location;

    public MlatStation(int id, Location2D location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public Location2D getLocation() {
        return location;
    }
}
