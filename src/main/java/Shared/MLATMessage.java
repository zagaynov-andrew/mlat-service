package Shared;

import java.io.Serializable;
import java.time.Instant;

public class MLATMessage extends Message implements Serializable {

    private int objectId;
    private Instant sendTime;

    public int getObjectId() {
        return objectId;
    }

    public Instant getSendTime() {
        return sendTime;
    }
}
