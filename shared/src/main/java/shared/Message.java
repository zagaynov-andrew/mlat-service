package shared;

import java.io.Serializable;

public abstract class Message implements Serializable  {
    protected int checksum;
}
