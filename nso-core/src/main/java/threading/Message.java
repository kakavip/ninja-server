package threading;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

public class Message {
    private final byte command;
    private ByteArrayOutputStream os;
    private final DataOutputStream dos;
    private final ByteArrayInputStream is;
    private final DataInputStream dis;

    private byte[] data;

    public Message(final int command) {
        this((byte) command);
    }

    public Message(final byte command) {
        this.is = null;
        this.dis = null;
        this.command = command;
        this.setOs(new ByteArrayOutputStream());
        this.dos = new DataOutputStream(this.getOs());
    }

    public Message(final byte command, final byte[] data) {
        this.setOs(null);
        this.dos = null;
        this.data = data;
        this.command = command;
        this.is = new ByteArrayInputStream(data);
        this.dis = new DataInputStream(this.is);
    }

    public byte[] getData() {
        return this.getOs().toByteArray();
    }

    public byte getCommand() {
        return this.command;
    }

    public DataInputStream reader() {
        return this.dis;
    }

    public DataOutputStream writer() {
        return this.dos;
    }

    public void cleanup() {
        try {
            if (this.dis != null) {
                this.dis.close();
            }
            if (this.dos != null) {
                this.dos.close();
            }

            this.data = null;
        } catch (IOException ex) {
        }
    }


    public Message cloneMessage() throws CloneNotSupportedException {
        return new Message(this.command, this.data);
    }

    public ByteArrayOutputStream getOs() {
        return os;
    }

    public void setOs(ByteArrayOutputStream os) {
        this.os = os;
    }
}
