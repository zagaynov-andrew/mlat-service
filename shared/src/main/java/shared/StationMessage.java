package shared;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class StationMessage extends Message implements Serializable {

    private final int objectId;
    private final long ToT;
    private final long ToF;
    private final /*transient*/ int stationId;

    public StationMessage(int objectId, long ToT, long ToF) {
        this(objectId, ToT, ToF, 0);
    }

    public StationMessage(int objectId, long ToT, long ToF, int stationId) {
        this.objectId = objectId;
        this.ToT = ToT;
        this.ToF = ToF;
        this.stationId = stationId;

        ByteBuffer buf = ByteBuffer.allocate(20);
        buf.putInt(objectId);
        buf.putLong(ToT);
        buf.putLong(ToF);

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

    public long getToF() {
        return ToF;
    }

    public int getStationId() {
        return stationId;
    }

    @Override
    public String toString() {
        return "objectId=" + objectId +
               ", stationId=" + stationId +
               ", ToT=" + ToT +
               ", ToF=" + ToF;
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