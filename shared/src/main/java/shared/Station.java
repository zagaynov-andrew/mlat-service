package shared;

import shared.Location;

import java.io.Serializable;

public class Station implements Serializable {

    protected int id;
    protected Location location;

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }
}
