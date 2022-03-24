package shared;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class StationMessage extends Message implements Serializable {

    private final int objectId;
    private final long ToT;
    private final transient int stationId;

    public StationMessage(int objectId, long ToT) {
        this(objectId, ToT, 0);
    }

    public StationMessage(int objectId, long ToT, int stationId) {
        this.objectId = objectId;
        this.ToT = ToT;
        this.stationId = stationId;

        ByteBuffer buf = ByteBuffer.allocate(12);
        buf.putInt(objectId);
        buf.putLong(ToT);

        int checksum = 0;
        for (byte b : buf.array())
            checksum = Integer.rotateLeft(checksum, 1) ^ b;
    }

    public int getObjectId() {
        return objectId;
    }

    public long getToT() {
        return ToT;
    }

    public int getStationId() {
        return stationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationMessage that = (StationMessage) o;
        return objectId == that.objectId && ToT == that.ToT;
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, ToT);
    }
}