package patch;

import java.io.ByteArrayOutputStream;

public class Resource {

    public static long TIME_REMOVE_RESOURCE;
    private ByteArrayOutputStream stream;
    public long lastTimeUsed;

    public Resource(ByteArrayOutputStream stream) {
        this.setStream(stream);
        this.lastTimeUsed = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - this.lastTimeUsed >= TIME_REMOVE_RESOURCE;
    }

    public ByteArrayOutputStream getStream() {
        this.lastTimeUsed = System.currentTimeMillis();
        return stream;
    }

    public void setStream(ByteArrayOutputStream stream) {
        this.stream = stream;
    }
}
