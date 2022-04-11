package server;

import threading.Message;

import java.io.IOException;

import real.Ninja;
import real.PlayerManager;
import real.User;
import threading.Manager;

import java.util.ArrayList;

import patch.TaskOrder;

public class RotationLuck extends Thread {

    public static byte VXMM_NORMAL = 0;
    public static byte VXMM_VIP = 1;
    public static byte TAI_XIU = 2;

    protected String title;
    protected short time;
    protected int total;
    protected int maxTotal;
    protected short numPlayers;
    protected byte type;
    protected final int min;
    protected final int max;
    protected boolean open;
    protected short setTime;
    protected boolean start;
    protected String winnerInfo;
    protected boolean running;
    protected ArrayList<Players> players;
    protected String currency;
    private ArrayList<Integer> sucsacs;

    private String oldWinName;

    public RotationLuck(final String title, final byte type, final short time, final int min, final int max,
            final int maxtotal, String currency) {

        this.title = null;
        this.time = 120;
        this.total = 0;
        this.numPlayers = 0;
        this.type = 1;
        this.open = true;
        this.setTime = 0;
        this.start = false;
        this.winnerInfo = "Chưa có thông tin";
        this.running = true;
        this.players = new ArrayList<>();
        this.sucsacs = new ArrayList<>();
        this.title = title;
        this.type = type;
        this.setTime = time;
        this.time = time;
        this.min = min;
        this.max = max;
        this.maxTotal = maxtotal;
        this.currency = currency;
        this.oldWinName = "";

    }

    public RotationLuck(final String title, final byte type, final short time, final int min, final int max,
            final int maxtotal) {
        this(title, type, time, min, max, maxtotal, "xu");
    }

    protected int getJoinAmount(final String njname) {

        for (short i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).name.equals(njname)) {
                return this.players.get(i).joinAmount;
            }
        }
        return 0;
    }

    public int getNinjaAmount(User user) {
        if (currency.equals("xu")) {
            return user.nj.xu;
        } else {
            return user.luong;
        }
    }

    public synchronized void upAmountMessage(long amount, User user) {
        if (currency.equals("xu")) {
            user.nj.upxuMessage(amount);
        } else {
            user.upluongMessage(amount);
        }
    }

    protected synchronized void joinLuck(final User p, final int joinAmount) throws IOException {

        if (p.isGuest) {
            p.session.sendMessageLog("Tài khoản dùng thử không thể sử dụng tính năng này");
            return;
        }

        if (!this.open || joinAmount <= 0) {
            return;
        }
        if (joinAmount > getNinjaAmount(p)) {
            p.session.sendMessageLog("Bạn không đủ " + currency + ".");
            return;
        }
        if (this.total > this.maxTotal) {
            p.session.sendMessageLog("Số lượng " + currency + " tối đa là " + util.getFormatNumber(this.maxTotal));
            return;
        }
        Players p2 = null;
        for (short i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).name.equals(p.nj.name)) {
                p2 = this.players.get(i);
                break;
            }
        }

        if (p2 == null && (joinAmount > this.max || joinAmount < this.min)) {
            p.session.sendMessageLog("Bạn chỉ có thể đặt cược từ " + util.getFormatNumber(this.min) + " đến "
                    + util.getFormatNumber(this.max) + currency + ".");
            return;
        }

        if (p2 == null) {
            p2 = new Players(p.id);
            p2.user = p.username;
            p2.name = p.nj.name;
            p2.type = p.nj.typebet;
            ++this.numPlayers;
            this.players.add(p2);
        }
        if (p2.joinAmount + joinAmount > this.max) {
            p.session.sendMessageLog(
                    "Bạn chỉ có thể đặt tối đa " + util.getFormatNumber(this.max - p2.joinAmount) + ".");
            return;
        }
        if (this.type == 2 && p2.type != p.nj.typebet) {

            p.session.sendMessageLog(
                    "Bạn đã đặt " + (p2.type == 0 ? "Tài" : "Xỉu") + ". Vui lòng chọn lại.");
            return;
        }

        // check is first join.
        if (p2.joinAmount == 0) {
            // update nvdv
            switch (this.type) {
                case 0:
                    p.nj.updateTaskOrder(TaskOrder.NHIEM_VU_DANH_VONG, 1, TaskOrder.VXMM_NORMAL_KILL_ID);
                    p.nj.updateTaskOrder(TaskOrder.NHIEM_VU_MO_RONG, 1, TaskOrder.VXMM_NORMAL_KILL_ID);
                    break;
                case 1:
                    p.nj.updateTaskOrder(TaskOrder.NHIEM_VU_DANH_VONG, 1, TaskOrder.VXMM_VIP_KILL_ID);
                    p.nj.updateTaskOrder(TaskOrder.NHIEM_VU_MO_RONG, 1, TaskOrder.VXMM_VIP_KILL_ID);
                    break;
                case 2:
                    p.nj.updateTaskOrder(TaskOrder.NHIEM_VU_DANH_VONG, 1, TaskOrder.TAI_XIU_KILL_ID);
                    p.nj.updateTaskOrder(TaskOrder.NHIEM_VU_MO_RONG, 1, TaskOrder.TAI_XIU_KILL_ID);
                    break;
                default:
                    break;
            }
            // update extra task
        }

        final Players players = p2;
        players.joinAmount += joinAmount;
        upAmountMessage(-joinAmount, p);
        this.total += joinAmount;
        if ((this.numPlayers == 2 || this.numPlayers == 1 && this.type == 2) && !this.start) {
            this.begin();
            final Ninja ns = PlayerManager.getInstance().getNinja(this.players.get(0).name);
            if (ns != null) {
                this.luckMessage(ns.p);
            }
        }
        this.luckMessage(p);
    }

    private void turned() throws Exception {
        if (this.type != 2) {
            for (int i = 0; i < this.players.size(); ++i) {
                for (int j = i + 1; j < this.players.size(); ++j) {
                    if (this.players.get(i).joinAmount < this.players.get(j).joinAmount) {
                        final String tempuser = this.players.get(j).user;
                        final String tempname = this.players.get(j).name;
                        final int tempAmount = this.players.get(j).joinAmount;
                        this.players.get(j).user = this.players.get(i).user;
                        this.players.get(j).name = this.players.get(i).name;
                        this.players.get(j).joinAmount = this.players.get(i).joinAmount;
                        this.players.get(i).user = tempuser;
                        this.players.get(i).name = tempname;
                        this.players.get(i).joinAmount = tempAmount;
                    }
                }
            }
            Players p = null;

            float luck = util.nextInt(101) * 1.0f;
            for (final Players player : this.players) {
                luck -= this.percentWin(player.name);
                if (luck <= 0) {
                    p = player;
                    break;
                }
            }
            while (p == null || p.name.equals(this.oldWinName) && this.players.size() > 5) {
                p = this.players.get(util.nextInt(this.players.size()));
            }
            this.oldWinName = p.name; // reset old win name
            long amountWin = this.total;
            if (this.numPlayers > 1) {
                amountWin = amountWin * 95L / 100L;
            }
            final Ninja ns = PlayerManager.getInstance().getNinja(p.name);
            if (ns != null) {
                upAmountMessage(amountWin, ns.p);
            } else {
                if (currency.equals("xu")) {
                    SQLManager
                            .executeUpdate(
                                    "UPDATE `ninja` SET `xu`=`xu`+" + amountWin + " WHERE `name`='" + p.name + "';");
                } else {
                    SQLManager.executeUpdate(
                            "UPDATE `player` SET `luong`=`luong`+" + amountWin + " WHERE `id`=" + p.id + ";");
                }
            }
            Manager.serverChat("server", "Chúc mừng " + p.name.toUpperCase() + " đã chiến thắng "
                    + util.getFormatNumber(amountWin) + " " + currency + " trong trò chơi Vòng xoay may mắn");
            this.winnerInfo = "Người vừa chiến thắng:\n" + ((this.type == 0) ? ("c" + util.nextInt(10)) : "") + ""
                    + p.name
                    + "\nSố " + currency + " thắng: " + util.getFormatNumber(amountWin) + " " + currency
                    + "\nNgười chơi "
                    + p.name + " tham gia: " + util.getFormatNumber(p.joinAmount) + " " + currency;
        } else {
            int luck = this.totalSucSac();
            String luckName = luck > 10 ? "Tài" : "Xỉu";
            int luckTypebet = luck > 10 ? 0 : 1;

            int counter = 0;
            for (final Players player : this.players) {
                if (player.type == luckTypebet) {
                    counter += 1;
                    final Ninja ns = PlayerManager.getInstance().getNinja(player.name);
                    long amountWin = (long) (player.joinAmount * 2 * 0.95);
                    if (ns != null) {
                        upAmountMessage(amountWin, ns.p);
                    } else {
                        if (currency.equals("xu")) {
                            SQLManager
                                    .executeUpdate(
                                            "UPDATE `ninja` SET `xu`=`xu`+" + amountWin + " WHERE `name`='"
                                                    + player.name
                                                    + "';");
                        } else {
                            SQLManager.executeUpdate(
                                    "UPDATE `player` SET `luong`=`luong`+" + amountWin + " WHERE `id`=" + player.id
                                            + ";");
                        }
                    }
                }
            }

            Manager.serverChat("server", "Chúc mừng những người chơi " + luckName + " trong trò chơi Tài Xỉu.");
            this.winnerInfo = "Kết quả " + luck + " điểm." + "Có " + counter + " người chơi " + luckName
                    + " vừa chiến thắng.";
        }

        this.numPlayers = 0;
        this.total = 0;
        this.sucsacs.clear();
        this.players.clear();
        Thread.sleep(1000L);
        this.time = this.setTime;
        this.start = false;
        this.open = true;
    }

    private void begin() {
        this.time = this.setTime;
        this.start = true;
    }

    protected float percentWin(final String njname) {
        if (this.type == 2) {
            int taiNum = 0;
            int njtypebet = -1;
            for (short i = 0; i < this.players.size(); ++i) {
                if (this.players.get(i).type == 0) {
                    taiNum += 1;
                }
                if (this.players.get(i).name.equals(njname)) {
                    njtypebet = this.players.get(i).type;
                }
            }

            if (njtypebet == -1) {
                return 0.0f;
            }

            float taiPercent = taiNum * 100.0f / this.players.size();
            return njtypebet == 0 ? taiPercent : 100 - taiPercent;
        } else {
            for (short i = 0; i < this.players.size(); ++i) {
                if (this.players.get(i).name.equals(njname)) {
                    return this.players.get(i).joinAmount * 100.0f / this.total;
                }
            }
            return 0.0f;
        }

    }

    private int totalSucSac() {
        int luck = 0;
        for (Integer suc : this.sucsacs) {
            luck += suc;
        }

        return luck;
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                Thread.sleep(1000L);
                if (this.time <= 0 || !this.start) {
                    continue;
                }
                --this.time;
                if (this.time == 0) {
                    this.turned();
                } else {
                    if (this.time >= 10) {
                        continue;
                    }
                    this.open = false;

                    if (this.type == 2) {
                        if (this.time == 8 || this.time == 5 || this.time == 2) {
                            if (this.sucsacs.size() == 2 && this.totalSucSac() < 10 && this.totalSucSac() > 4) {
                                boolean taiWin = util.nextInt(100) % 2 == 1;
                                if (!taiWin) {
                                    this.sucsacs.add(util.nextInt(10 - this.totalSucSac()) + 1);
                                } else {
                                    this.sucsacs.add(util.nextInt(11 - this.totalSucSac(), 6));
                                }

                            } else {
                                this.sucsacs.add(util.nextInt(6) + 1);
                            }
                        }
                        this.winnerInfo = sucsacText();

                        for (Players player : this.players) {
                            final Ninja ns = PlayerManager.getInstance().getNinja(player.name);
                            if (ns != null) {
                                luckMessage(ns.p);
                            }
                        }
                    }
                }
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        util.Debug("Close Thread Lucky");
    }

    public String sucsacText() {
        String text = "";
        for (int i = 0; i < this.sucsacs.size(); i++) {
            text += "Kết quả xúc sắc " + (i + 1) + " là: " + this.sucsacs.get(i) + "\n";
        }

        return text;
    }

    protected void luckMessage(final User p) throws IOException {
        final Message m = new Message(53);
        m.writer().writeUTF("typemoi");
        m.writer().writeUTF(this.title);
        m.writer().writeShort(this.time);
        m.writer().writeUTF(util.getFormatNumber(this.total) + " " + currency);
        m.writer().writeShort((short) this.percentWin(p.nj.name));
        m.writer().writeUTF((util.parseString("" + this.percentWin(p.nj.name), ".") == null) ? "0"
                : util.parseString("" + this.percentWin(p.nj.name), "."));
        m.writer().writeShort(this.numPlayers);
        m.writer().writeUTF(this.winnerInfo);
        m.writer().writeByte(this.type);
        m.writer().writeUTF(util.getFormatNumber(this.getJoinAmount(p.nj.name)));
        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
    }

    public void close() {
        try {
            this.running = false;
            if (this.numPlayers > 0) {
                this.turned();
            }
            this.title = null;
            this.winnerInfo = null;
            this.players = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected static class Players {

        String user;
        String name;
        int joinAmount;
        int id;

        // tai = 0, xiu = 1
        int type;

        private Players(int id) {
            this(id, 0);
        }

        private Players(int id, int type) {
            this.user = null;
            this.name = null;
            this.joinAmount = 0;
            this.id = id;
            this.type = type;
        }
    }
}
