package io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import real.User;
import server.Ip;
import server.util;
import threading.Message;
import threading.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Session extends Thread {
    public boolean login;
    private static int baseId;
    public int id;
    private volatile boolean connected;
    private boolean getKeyComplete;
    @NotNull
    private BlockingQueue<@NotNull Message> sendDatas;
    private byte curR;
    private byte curW;
    @NotNull
    protected Socket socket;
    @NotNull
    protected DataInputStream dis;
    @NotNull
    protected DataOutputStream dos;
    @NotNull
    ISessionHandler messageHandler;
    @NotNull
    public User user;
    private byte type;
    public byte zoomLevel;
    private boolean isGPS;
    private int width;
    private int height;
    private boolean isQwert;
    private boolean isTouch;
    private String plastfrom;
    private byte languageId;
    private int provider;
    private String agent;
    private final Server server;
    @Nullable
    private Thread sendThread;
    private final InetSocketAddress socketAddress;
    private final String clientIpAddress;

    public volatile long lastTimeReceiveData;

    public Session(final @NotNull Socket socket, final @NotNull ISessionHandler handler) {
        this.connected = false;
        this.getKeyComplete = false;
        this.sendDatas = new LinkedBlockingQueue<>();
        this.user = null;
        this.server = Server.getInstance();
        this.id = Session.baseId++;
        this.socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        this.clientIpAddress = socketAddress.getAddress().getHostAddress();
        try {
            this.setSocket(socket);
            this.messageHandler = handler;
            this.connected = true;
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
        lastTimeReceiveData = System.currentTimeMillis();
    }

    public static boolean check(String ip) {
        boolean b = false;
        List<Ip> list = util.ReadIp();
        for (Ip o : list) {
            if (o.getName().equals(ip)) {
                b = true;
                break;
            } else {
                b = false;
            }
        }
        return b;
    }

    private void setSocket(final @Nullable Socket socket) throws IOException {
        this.socket = socket;
        if (socket != null) {
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        }
    }

    @Override
    public void run() {
        this.setName("User thread" + baseId);
        this.setPriority(Thread.MIN_PRIORITY);
        sendThread = new Thread(() -> {
            setName("SESSION THREAD " + baseId);
            try {
                while (Session.this.connected) {
                    final Message m = Session.this.sendDatas.take();
                    Session.this.doSendMessage(m);
                }
            } catch (Exception e) {
                System.out.println("Close Send thread");
            }
        });
        sendThread.setName("Send Thread " + baseId);
        sendThread.start();
        sendThread.setPriority(Thread.MIN_PRIORITY);

        try {
            while (this.connected) {
                final Message message = this.readMessage();
                this.lastTimeReceiveData = System.currentTimeMillis();
                if (!login && !(message.getCommand() == -27 || message.getCommand() == -29)) {
                    if (!check(this.clientIpAddress)) {
                        util.WriteIp(this.clientIpAddress);
                        this.socket.close();
                    }
                } else {
                    this.messageHandler.onMessage(this, message);
                }
                util.Debug(this + " do message " + message.getCommand() + " size " + message.reader().available());
                message.cleanup();
            }
        } catch (Exception ex) {
            System.out.println("Close receive thread");
        }
        this.disconnect();
        this.dis = null;
    }

    @NotNull
    private Message readMessage() throws Exception {
        byte cmd = this.dis.readByte();
        if (cmd != -27) {
            cmd = this.readKey(cmd);
        }
        int size;
        if (cmd != -27) {
            final byte b1 = this.dis.readByte();
            final byte b2 = this.dis.readByte();
            size = ((this.readKey(b1) & 0xFF) << 8 | (this.readKey(b2) & 0xFF));
        } else {
            size = this.dis.readUnsignedShort();
        }
        final byte[] data = new byte[size];
        for (int len = 0, byteRead = 0; len != -1 && byteRead < size; byteRead += len) {
            len = this.dis.read(data, byteRead, size - byteRead);
            if (len > 0) {
            }
        }
        if (cmd != -27) {
            for (int i = 0; i < data.length; ++i) {
                data[i] = this.readKey(data[i]);
            }
        }
        final Message msg = new Message(cmd, data);
        return msg;
    }

    public void sendMessage(final @NotNull Message m) {
        if (this.connected) {
            this.sendDatas.add(m);
        }
    }

    private byte readKey(final byte b) {
        final byte[] bytes = "D".getBytes();
        final byte curR = this.curR;
        this.curR = (byte) (curR + 1);
        final byte i = (byte) ((bytes[curR] & 0xFF) ^ (b & 0xFF));
        if (this.curR >= "D".getBytes().length) {
            this.curR %= (byte) "D".getBytes().length;
        }
        return i;
    }

    private byte writeKey(final byte b) {
        final byte[] bytes = "D".getBytes();
        final byte curW = this.curW;
        this.curW = (byte) (curW + 1);
        final byte i = (byte) ((bytes[curW] & 0xFF) ^ (b & 0xFF));
        if (this.curW >= "D".getBytes().length) {
            this.curW %= (byte) "D".getBytes().length;
        }
        return i;
    }

    public void hansakeMessage() throws IOException {
        final Message m = new Message(-27);
        m.writer().writeByte("D".getBytes().length);
        m.writer().writeByte("D".getBytes()[0]);
        for (int i = 1; i < "D".getBytes().length; ++i) {
            m.writer().writeByte("D".getBytes()[i] ^ "D".getBytes()[i - 1]);
        }
        m.writer().flush();
        this.doSendMessage(m);
        m.cleanup();
        this.getKeyComplete = true;
    }

    protected void doSendMessage(final @NotNull Message m) throws IOException {
        try {
            final byte[] data = m.getData();
            if (data != null) {
                byte b = m.getCommand();
                final int size = data.length;
                if (size > 65535) {
                    b = -32;
                }
                if (this.getKeyComplete) {
                    this.dos.writeByte(this.writeKey(b));
                } else {
                    this.dos.writeByte(b);
                }
                if (b == -32) {
                    b = m.getCommand();
                    if (this.getKeyComplete) {
                        this.dos.writeByte(this.writeKey(b));
                    } else {
                        this.dos.writeByte(b);
                    }
                    final int byte1 = this.writeKey((byte) (size >> 24));
                    this.dos.writeByte(byte1);
                    final int byte2 = this.writeKey((byte) (size >> 16));
                    this.dos.writeByte(byte2);
                    final int byte3 = this.writeKey((byte) (size >> 8));
                    this.dos.writeByte(byte3);
                    final int byte4 = this.writeKey((byte) (size & 0xFF));
                    this.dos.writeByte(byte4);
                } else if (this.getKeyComplete) {
                    final int byte1 = this.writeKey((byte) (size >> 8));
                    this.dos.writeByte(byte1);
                    final int byte2 = this.writeKey((byte) (size & 0xFF));
                    this.dos.writeByte(byte2);
                } else {
                    final int byte1 = (byte) (size & 0xFF00);
                    this.dos.writeByte(byte1);
                    final int byte2 = (byte) (size & 0xFF);
                    this.dos.writeByte(byte2);
                }
                if (this.getKeyComplete) {
                    for (int i = 0; i < size; ++i) {
                        data[i] = this.writeKey(data[i]);
                    }
                }
                this.dos.write(data);
                util.Debug("do mss " + b + " size " + size);
            }
            this.dos.flush();
        } catch (IOException e) {
            this.disconnect();
            util.Debug("Error write message from client " + this.id);
        }
    }

    public void sendMessageLog(final @NotNull String str) {
        final Message m = new Message(-26);
        try {
            m.writer().writeUTF(str);
            m.writer().flush();
            this.sendMessage(m);
        } catch (IOException ex) {
        } finally {
            m.cleanup();

        }
    }

    public boolean isConnected() {
        return this.connected;
    }

    @NotNull
    @Override
    public String toString() {
        return "Conn: " + this.id;
    }

    public void setConnect(final @NotNull Message m) throws IOException {
        this.type = m.reader().readByte();
        this.zoomLevel = m.reader().readByte();
        this.isGPS = m.reader().readBoolean();
        this.width = m.reader().readInt();
        this.height = m.reader().readInt();
        this.isQwert = m.reader().readBoolean();
        this.isTouch = m.reader().readBoolean();
        this.plastfrom = m.reader().readUTF();
        m.reader().readInt();
        m.reader().readByte();
        this.languageId = m.reader().readByte();
        this.provider = m.reader().readInt();
        this.agent = m.reader().readUTF();
        m.cleanup();
        util.Debug("Connection type " + this.type + " zoomlevel " + this.zoomLevel + " width " + this.width + " height "
                + this.height);
    }

    public void loginGame(final @NotNull Message m) throws Exception {
        final String uname = util.strSQL(m.reader().readUTF());
        final String passw = util.strSQL(m.reader().readUTF());
        final String version = m.reader().readUTF();
        final String t1 = m.reader().readUTF();
        final String packages = m.reader().readUTF();
        final String random = m.reader().readUTF();
        final byte sv = m.reader().readByte();
        m.cleanup();
        final User p = User.login(this, uname, passw);
        if (p != null) {
            this.user = p;
            this.login = true;
            Server.getInstance().manager.getPackMessage(p);
            p.selectNhanVat(null);
        }
    }

    public void disconnect() {
        if (this.connected) {
            this.connected = false;

            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                this.sendThread.interrupt();
                interrupt();
                this.sendThread = null;
                this.sendDatas = null;
            } catch (Exception e) {
            }

            this.messageHandler.onDisconnected(this);
            System.gc();
        }
    }

    static {
        Session.baseId = 0;
    }

    public String getClientIpAddress() {
        return this.clientIpAddress;
    }
}
