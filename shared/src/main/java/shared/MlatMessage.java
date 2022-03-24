package shared;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Objects;


public class MlatMessage extends Message implements Serializable {

    private final int objectId;
    private final long sendTime;
    private final transient int stationId;

    public MlatMessage(int objectId, long sendTime) {
        this(objectId,sendTime, 0);
    }

    public MlatMessage(int objectId, long sendTime, int stationId) {
        this.objectId = objectId;
        this.sendTime = sendTime;
        this.stationId = stationId;

        ByteBuffer buf = ByteBuffer.allocate(12);
        buf.putInt(objectId);
        buf.putLong(sendTime);

        int checksum = 0;
        for (byte b : buf.array())
            checksum = Integer.rotateLeft(checksum, 1) ^ b;
    }

    public int getObjectId() {
        return objectId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public int getStationId() {
        return stationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MlatMessage that = (MlatMessage) o;
        return objectId == that.objectId && sendTime == that.sendTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, sendTime);
    }

    public static void main(String[] args) {
        new MlatMessage(7, System.nanoTime(), 3);
    }
}
