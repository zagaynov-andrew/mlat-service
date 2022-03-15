package shared;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.time.Instant;


public class MLATMessage extends Message implements Serializable {

    private final int objectId;
    private final Instant sendTime;
    private final transient int stationId;

    MLATMessage(int objectId, Instant sendTime, int stationId) {
        this.objectId = objectId;
        this.sendTime = sendTime;
        this.stationId = stationId;

        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putInt(objectId);
        buf.putInt(sendTime.getNano());

        int checksum = 0;
        for (byte b : buf.array())
            checksum = Integer.rotateLeft(checksum, 1) ^ b;
        System.out.println(checksum);
    }

    public int getObjectId() {
        return objectId;
    }

    public Instant getSendTime() {
        return sendTime;
    }

    public int getStationId() {
        return stationId;
    }

    public static void main(String[] args) {
        new MLATMessage(7, Instant.ofEpochMilli(873), 3);
    }
}
