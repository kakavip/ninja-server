package server;

import threading.Message;
import io.Session;

public class GameCanvas {
    protected static void addInfoDlg(final Session session, final String s) {
        Message msg = null;
        try {
            msg = Service.messageNotMap((byte) (-86));
            msg.writer().writeUTF(s);
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void startOKDlg(final Session session, final String info) {
        Message msg = null;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(info);
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void addEffect(final Session conn, final byte b, final int vId, final short id, final int timelive, final int miliSecondWait) {
        if (conn == null) return;

        Message msg = null;
        boolean isHead = (id >= 12 && id <= 20) || id == 37 || id == 58 || (id>=50 && id<=55) || id == 45 || id == 40;
        try {
            msg = new Message(125);
            msg.writer().writeByte(0);
            msg.writer().writeByte(b);
            if (b == 1) {
                msg.writer().writeByte(vId);
            } else {
                msg.writer().writeInt(vId);
            }
            msg.writer().writeShort(id);
            msg.writer().writeInt(timelive);
            msg.writer().writeByte(miliSecondWait);
            msg.writer().writeByte(isHead ? 1 : 0);
            msg.writer().flush();
            conn.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void getImgEffect(final Session session, final short id) {
        Message msg = null;
        try {
            util.Debug("Lấy ảnh " + id);
            final byte[] ab = GameScr.loadFile("res/Effect/x" + session.zoomLevel + "/ImgEffect/ImgEffect " + id + ".png").toByteArray();
            msg = new Message(125);
            msg.writer().writeByte(1);
            msg.writer().writeByte(id);
            msg.writer().writeInt(ab.length);
            msg.writer().write(ab);
            msg.writer().flush();
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void getDataEffect(final Session session, final short id) {
        Message msg = null;
        try {
            final byte[] ab = GameScr.loadFile("res/Effect/x" + session.zoomLevel + "/DataEffect/" + id).toByteArray();
            msg = new Message(125);
            msg.writer().write(ab);
            msg.writer().flush();
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
}
