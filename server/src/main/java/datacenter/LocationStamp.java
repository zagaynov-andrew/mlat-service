package datacenter;

import shared.Location2D;

public class LocationStamp {
    private final int objectId;
    private final long timestamp;
    private final Location2D location;

    public LocationStamp(int objectId, long timestamp, Location2D location) {
        this.objectId = objectId;
        this.timestamp = timestamp;
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Location2D getLocation() {
        return location;
    }

    public int getObjectId() {
        return objectId;
    }
}
