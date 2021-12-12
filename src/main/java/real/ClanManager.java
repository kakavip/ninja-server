package real;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import patch.*;
import battle.ClanBattle;
import battle.ClanBattleData;
import clan.ClanTerritory;
import clan.ClanTerritoryData;
import clan.ClanThanThu;
import server.SQLManager;
import org.json.simple.JSONArray;

import java.io.IOException;

import server.util;

import java.util.Date;
import java.time.Instant;

import threading.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static clan.ClanThanThu.*;
import static patch.Constants.TOC_TRUONG;

public class ClanManager {
    public static final int LUONG_CREATE_CLAN = 1_500;

    public int id;

    public int exp;
    private int level;
    public int itemLevel;
    public int coin;

    public byte use_card;
    public byte openDun;
    public byte debt;
    @NotNull
    public String name;
    @NotNull
    public String reg_date;
    @NotNull
    public String log;
    @NotNull
    public String alert;

    @NotNull
    public List<@Nullable ClanMember> members;
    @NotNull
    public List<@NotNull Item> items;
    @NotNull
    public String week;
    @NotNull
    public static final List<@NotNull ClanManager> entrys;
    @Nullable
    private ClanTerritory clanTerritory;
    @Nullable
    private ClanBattle clanBattle;
    @Nullable
    private ClanBattleData clanBattleData;
    @NotNull
    public List<@NotNull ClanThanThu> clanThanThus;
    @NotNull
    public static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    public ClanManager() {
        this.name = "";
        this.exp = 0;
        this.setLevel(1);
        this.itemLevel = 0;
        this.coin = 1000000;
        this.reg_date = "";
        this.log = "";
        this.alert = "";
        this.use_card = 4;
        this.openDun = 3;
        this.debt = 0;
        this.members = new CopyOnWriteArrayList<>();
        this.items = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.clanThanThus = new ArrayList<>();
        this.week = "";
    }

    @Nullable
    public static ClanManager getClanByName(final @NotNull String name) {
        for (int i = 0; i < ClanManager.entrys.size(); ++i) {
            if (ClanManager.entrys.get(i).name.equals(name)) {
                return ClanManager.entrys.get(i);
            }
        }
        return null;
    }

    public void updateCoin(final int coin) {
        this.coin += coin;
        if (coin < 0 && this.coin < 0) {
            ++this.debt;
            if (this.debt > 3) {
                this.dissolution();
            }
        } else if (this.coin >= 0) {
            this.debt = 0;
        }
    }

    @NotNull
    public String getmain_name() {
        for (short i = 0; i < this.members.size(); ++i) {
            if (this.members.get(i).typeclan == 4) {
                return this.members.get(i).cName;
            }
        }
        return "";
    }

    @NotNull
    public String getassist_name() {
        for (short i = 0; i < this.members.size(); ++i) {
            if (this.members.get(i).typeclan == 3) {
                return this.members.get(i).cName;
            }
        }
        return "";
    }

    public int numElder() {
        int elder = 0;
        for (short i = 0; i < this.members.size(); ++i) {
            if (this.members.get(i).typeclan == 2) {
                ++elder;
            }
        }
        return elder;
    }

    @Nullable
    public ClanMember getMem(final int id) {
        for (short i = 0; i < this.members.size(); ++i) {
            if (this.members.get(i).charID == id) {
                return this.members.get(i);
            }
        }
        return null;
    }

    @Nullable
    public ClanMember getMem(final String name) {
        for (short i = 0; i < this.members.size(); ++i) {
            if (this.members.get(i).cName.equals(name)) {
                return this.members.get(i);
            }
        }
        return null;
    }

    public int getMemMax() {
        return 45 + this.getLevel() * 5;
    }

    public int getexpNext() {
        int expNext = 2000;
        for (int i = 1; i < this.getLevel(); ++i) {
            if (i == 1) {
                expNext = 3720;
            } else if (i < 10) {
                expNext = (expNext / i + 310) * (i + 1);
            } else if (i < 20) {
                expNext = (expNext / i + 620) * (i + 1);
            } else {
                expNext = (expNext / i + 930) * (i + 1);
            }
        }
        return expNext;
    }

    public int getfreeCoin() {
        return 30000 + this.members.size() * 5000;
    }

    private int getCoinOpen() {
        if (this.itemLevel == 0) {
            return 1000000;
        }
        if (this.itemLevel == 1) {
            return 5000000;
        }
        if (this.itemLevel == 2) {
            return 10000000;
        }
        if (this.itemLevel == 3) {
            return 20000000;
        }
        if (this.itemLevel == 4) {
            return 30000000;
        }
        return 0;
    }

    public int getCoinUp() {
        int coinUp = 500000;
        for (int i = 1; i < this.getLevel(); ++i) {
            if (i < 10) {
                coinUp += 100000;
            } else if (i < 20) {
                coinUp += 200000;
            } else {
                coinUp += 300000;
            }
        }
        return coinUp;
    }

    public void sendMessage(final @Nullable Message m) {
        if (m == null) return;
        for (ClanMember member : this.members) {
            final Ninja n = PlayerManager.getInstance().getNinja(member.cName);
            if (n != null) {
                n.p.sendMessage(m);
            }
        }
    }

    public void payfeesClan() {
        this.writeLog("", 4, this.getfreeCoin(), util.toDateString(Date.from(Instant.now())));
        this.updateCoin(-this.getfreeCoin());
        for (short i = 0; i < this.members.size(); ++i) {
            this.members.get(i).pointClanWeek = 0;
        }
        this.week = util.toDateString(Date.from(Instant.now()));
    }

    public void upExp(final int exp) {
        this.exp += exp;
    }

    public void addItem(final @NotNull Item it) {
        for (byte i = 0; i < this.items.size(); ++i) {
            final Item it2 = this.items.get(i);
            if (it2.id == it.id) {
                final Item item = it2;
                item.quantity += it.quantity;
                return;
            }
        }
        this.items.add(it);
    }

    public void removeItem(final int id, final int quantity) {
        for (byte i = 0; i < this.items.size(); ++i) {
            final Item it = this.items.get(i);
            if (it.id == id) {
                it.quantity -= quantity;
                if (it.quantity <= 0) {
                    this.items.remove(it);
                }
                return;
            }
        }
    }

    public void chat(final @Nullable User p, @Nullable Message m) throws IOException {
        if (p == null || m == null) return;
        final String text = m.reader().readUTF();
        m.cleanup();
        m = new Message(-19);
        m.writer().writeUTF(p.nj.name);
        m.writer().writeUTF(text);
        m.writer().flush();
        this.sendMessage(m);
        m.cleanup();
    }

    public void changeClanType(final @Nullable User p, @Nullable Message m) throws IOException {
        if (m == null || p == null) return;

        final String cName = m.reader().readUTF();
        final byte typeclan = m.reader().readByte();
        final ClanMember mem = this.getMem(cName);
        if (mem == null || p.nj.clan.typeclan != 4 || mem.charID == p.nj.id) {
            return;
        }
        final Ninja n = PlayerManager.getInstance().getNinja(mem.cName);
        if (typeclan == 0 && mem.typeclan > 1) {
            if (n != null) {
                n.p.setTypeClan(typeclan);
            }
            mem.typeclan = typeclan;
            this.requestClanMember(p);
            m = createMessage(p.nj.name + " đã bị bãi chức");
            this.sendMessage(m);
            m.cleanup();
        } else if (typeclan == 2) {
            if (this.numElder() >= 5) {
                p.session.sendMessageLog("Đã có đủ trưởng lão");
                return;
            }
            if (n != null) {
                n.p.setTypeClan(typeclan);
            }
            mem.typeclan = typeclan;
            this.requestClanMember(p);
            m = createMessage(p.nj.name + " đã được bổ nhiệm làm trưởng lão");
            this.sendMessage(m);
            m.cleanup();
        } else if (typeclan == 3) {
            if (this.getassist_name().length() > 0) {
                p.session.sendMessageLog("Đã có tộc phó rồi");
                return;
            }
            if (n != null) {
                n.p.setTypeClan(typeclan);
            }
            mem.typeclan = typeclan;
            this.requestClanMember(p);
            m = createMessage(p.nj.name + " đã được bổ nhiệm làm tộc phó");
            this.sendMessage(m);
            m.cleanup();
        }
    }

    public void openItemLevel(final @Nullable User p) throws IOException {
        if (p == null) return;

        if (p.nj.clan.typeclan == 4 || p.nj.clan.typeclan == 3) {
            final int coinDown = this.getCoinOpen();
            final int lvopen = 5 * (this.itemLevel + 1);
            if (lvopen > this.getLevel()) {
                p.session.sendMessageLog("Gia tộc chưa đạt cấp " + lvopen);
            } else if (coinDown > this.coin) {
                p.session.sendMessageLog("Ngân sách không đủ để khai mở vật phẩm");
            } else if (this.itemLevel == 5) {
                p.session.sendMessageLog("Khai mở đã tối đa");
            } else {
                this.updateCoin(-coinDown);
                ++this.itemLevel;
                Message m = new Message(-28);
                m.writer().writeByte(-62);
                m.writer().writeByte(this.itemLevel);
                m.writer().flush();
                p.sendMessage(m);
                m.cleanup();
                this.requestClanInfo(p);
                m = new Message(-24);
                m.writer().writeUTF(p.nj.name + " đã khai mở vật gia tộc ngân sách giảm " + this.coin + " xu");
                m.writer().flush();
                this.sendMessage(m);
                m.cleanup();
            }
        }
    }

    private int thanThuIndex = 0;

    public void sendClanItem(final @Nullable User p, final @Nullable Message m) throws IOException {
        if (p == null || m == null) return;

        final byte index = m.reader().readByte();
        final String cName = m.reader().readUTF();
        m.cleanup();
        final ClanMember mem = this.getMem(cName);
        if (mem == null || p.nj.clan.typeclan < 3 || index < 0 || index >= this.items.size()) {
            return;
        }
        final Ninja n = PlayerManager.getInstance().getNinja(mem.cName);

        if (n == null) {
            p.sendYellowMessage("Thành viên đã offline");
        } else if (n.getAvailableBag() == 0) {
            p.sendYellowMessage("Hành trang thành viên đã đầy");
        } else {

            Item item = this.items.get(index).clone();
            val id = item.id;

            if (item.id == 604) {
                if (this.clanThanThus.size() == 0) {
                    p.sendYellowMessage("Gia tộc bạn không có thần thú để phát");
                    return;
                }
                item = this.clanThanThus.get(this.getThanThuIndex()).getPetItem();
                this.setThanThuIndex(this.getThanThuIndex() + 1);
                this.setThanThuIndex(this.getThanThuIndex() % this.clanThanThus.size());
                item.expires = util.TimeDay(7);

            } else {
                item.expires += System.currentTimeMillis();
            }

            item.setLock(true);
            item.quantity = 1;

            this.removeItem(id, 1);
            n.addItemBag(false, item);
            this.requestClanItem(p);
        }
    }

    public void setAlert(final @Nullable User p, @Nullable Message m) throws IOException {
        if (p == null || m == null) return;

        final String newalert = m.reader().readUTF();
        m.cleanup();
        if (p.nj.clan.typeclan == 4 || p.nj.clan.typeclan == 3) {
            if (newalert.length() > 30) {
                p.session.sendMessageLog("Chiều dài không quá 30 ký tự");
                return;
            }
            if (newalert.isEmpty()) {
                this.alert = "";
            } else {
                this.alert = "Ghi chú của " + p.nj.name + "\n" + newalert;
            }
            m = new Message(-28);
            m.writer().writeByte(-95);
            m.writer().writeUTF(this.alert);
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
        }
    }

    public void moveOutClan(final @Nullable User p, @Nullable Message m) throws IOException {
        if (p == null || m == null) return;

        final String cName = m.reader().readUTF();
        m.cleanup();
        final ClanMember mem = this.getMem(cName);
        if (mem == null || p.nj.clan.typeclan < 3 || mem.typeclan == 4 || mem.charID == p.nj.id) {
            return;
        }
        final Ninja n = PlayerManager.getInstance().getNinja(mem.cName);
        int coinDown = 10000;
        if (mem.typeclan == 3) {
            coinDown = 100000;
        } else if (mem.typeclan == 2) {
            coinDown = 50000;
        } else if (mem.typeclan == 1) {
            coinDown = 20000;
        }
        if (n != null) {
            n.clan.clanName = "";
            n.clan.pointClanWeek = 0;
            n.p.setTypeClan(-1);
        }
        this.writeLog(mem.cName, 1, coinDown, util.toDateString(Date.from(Instant.now())));
        m = createMessage(mem.cName + " đã bị trục suất khỏi gia tộc");
        this.sendMessage(m);
        m.cleanup();
        this.members.remove(mem);
        this.updateCoin(-coinDown);
        this.requestClanMember(p);
    }

    @NotNull
    public Message createMessage(final @NotNull String message) throws IOException {
        Message m;
        m = new Message(-24);
        m.writer().writeUTF(message);
        m.writer().flush();
        return m;
    }

    public void OutClan(final @NotNull User p) throws IOException {
        final ClanMember mem = this.getMem(p.nj.id);
        if (p.nj.clan.typeclan == 4 || mem == null) {
            return;
        }
        int coinDown = 10000;
        if (p.nj.clan.typeclan == 3) {
            coinDown = 100000;
        } else if (p.nj.clan.typeclan == 2) {
            coinDown = 50000;
        } else if (p.nj.clan.typeclan == 1) {
            coinDown = 20000;
        }
        if (coinDown > p.nj.xu) {
            p.session.sendMessageLog("Bạn không có đủ xu");
            return;
        }
        p.nj.clan.clanName = "";
        p.nj.clan.pointClanWeek = 0;
        p.setTypeClan(-1);
        p.nj.upxu(-coinDown);
        Message m = new Message(-28);
        m.writer().writeByte(-90);
        m.writer().writeInt(p.nj.xu);
        m.writer().flush();
        p.sendMessage(m);
        m = new Message(-24);
        m.writer().writeUTF(mem.cName + " đã rời khởi gia tộc trừ -" + coinDown + " xu");
        m.writer().flush();
        this.sendMessage(m);
        m.cleanup();
        this.members.remove(mem);
    }

    public void clanUpLevel(final @Nullable User p) throws IOException {
        if (p == null) return;

        if (p.nj.clan.typeclan == 4 || p.nj.clan.typeclan == 3) {
            final int coinDown = this.getCoinUp();
            final int expDown = this.getexpNext();
            if (this.getexpNext() > this.exp) {
                p.session.sendMessageLog("Kinh nghiệm chưa đủ");
            } else if (this.getCoinUp() > this.coin) {
                p.session.sendMessageLog("Ngân sách không đủ");
            } else {
                this.writeLog(p.nj.name, 5, coinDown, util.toDateString(Date.from(Instant.now())));
                this.updateCoin(-this.getCoinUp());
                this.upExp(-expDown);
                this.setLevel(this.getLevel() + 1);
                final Message m = new Message(-24);
                m.writer().writeUTF(p.nj.name + " đã nâng cấp gia tộc ngân sách giảm " + coinDown + " xu");
                m.writer().flush();
                this.sendMessage(m);
                m.cleanup();
                this.requestClanInfo(p);
            }
        }
    }

    public void inputCoinClan(final @Nullable User p, @Nullable Message m) throws IOException {
        if (p == null || m == null) return;

        final int inputcoin = m.reader().readInt();
        m.cleanup();
        if (inputcoin > 0) {
            if (inputcoin > p.nj.xu) {
                p.session.sendMessageLog("Bạn không có đủ xu.");
                return;
            }
            if (inputcoin + (long) this.coin > 2000000000L) {
                p.session.sendMessageLog("Chỉ còn có thể đóng góp thêm " + (this.coin - inputcoin));
                return;
            }
            this.writeLog(p.nj.name, 2, inputcoin, util.toDateString(Date.from(Instant.now())));
            this.updateCoin(inputcoin);
            p.nj.upxu(-inputcoin);
            m = new Message(-28);
            m.writer().writeByte(-90);
            m.writer().writeInt(p.nj.xu);
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
            val message = p.nj.name + " đã đóng góp " + inputcoin + " xu vào gia tộc ngân sách tăng " + this.coin + " xu";
            informAll(message);
        }
    }

    public void informAll(final @NotNull String message) throws IOException {
        Message m;
        m = new Message(-24);
        m.writer().writeUTF(message);
        m.writer().flush();
        this.sendMessage(m);
        m.cleanup();
    }

    public void writeLog(final @NotNull String name, final int num, final int number, final String date) {
        final String[] array = this.log.split("\n");
        this.log = name + "," + num + "," + number + "," + date + "\n";
        for (int i = 0; i < array.length && i != 10; ++i) {
            this.log = this.log + array[i] + "\n";
        }
    }

    public void LogClan(final @Nullable User p) throws IOException {
        if (p == null) return;
        final Message m = new Message(-28);
        m.writer().writeByte(-114);
        m.writer().writeUTF(this.log);
        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
    }

    public void requestClanInfo(final @Nullable User p) throws IOException {
        if (p == null) return;
        final Message m = new Message(-28);
        m.writer().writeByte(-113);
        m.writer().writeUTF(this.name);
        m.writer().writeUTF(this.getmain_name());
        m.writer().writeUTF(this.getassist_name());
        m.writer().writeShort(this.members.size());
        m.writer().writeByte(this.openDun);
        m.writer().writeByte(this.getLevel());
        m.writer().writeInt(this.exp);
        m.writer().writeInt(this.getexpNext());
        m.writer().writeInt(this.coin);
        m.writer().writeInt(this.getfreeCoin());
        m.writer().writeInt(this.getCoinUp());
        m.writer().writeUTF(this.reg_date);
        m.writer().writeUTF(this.alert);
        m.writer().writeInt(this.use_card);
        m.writer().writeByte(this.itemLevel);
        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();


    }

    public void requestClanMember(final @Nullable User p) throws IOException {
        if (p == null) return;

        final Message m = new Message(-28);
        m.writer().writeByte(-112);
        m.writer().writeShort(this.members.size());
        for (short i = 0; i < this.members.size(); ++i) {
            final Ninja n = PlayerManager.getInstance().getNinja(this.members.get(i).cName);
            m.writer().writeByte(this.members.get(i).nClass);
            m.writer().writeByte(this.members.get(i).clevel);
            m.writer().writeByte(this.members.get(i).typeclan);
            m.writer().writeUTF(this.members.get(i).cName);
            m.writer().writeInt(this.members.get(i).pointClan);
            m.writer().writeBoolean(n != null);
        }
        for (short i = 0; i < this.members.size(); ++i) {
            m.writer().writeInt(this.members.get(i).pointClanWeek);
        }
        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
    }

    public void requestClanItem(final @Nullable User p) throws IOException {
        if (p == null) return;

        final Message m = new Message(-28);
        m.writer().writeByte(-111);
        m.writer().writeByte(this.items.size());
        for (byte i = 0; i < this.items.size(); ++i) {
            m.writer().writeShort(this.items.get(i).quantity);
            m.writer().writeShort(this.items.get(i).id);
        }

        m.writer().writeByte(this.clanThanThus.size());
        for (ClanThanThu thanThu : this.clanThanThus) {
            val item = thanThu.getPetItem();
            m.writer().writeUTF(thanThu.getName());
            m.writer().writeShort(thanThu.getThanThuIconId());
            m.writer().writeShort(thanThu.getThanThuId());
            m.writer().writeInt(-1);
            m.writer().writeByte(item.option.size());

            for (Option option : item.option) {
                if (option.id == ST_QUAI_ID) {
                    m.writer().writeUTF("Sát thưởng lên quái " + option.param);
                } else if (option.id == ST_NGUOI_ID) {
                    m.writer().writeUTF("Sát thương lên người " + option.param);
                }
            }

            m.writer().writeInt(thanThu.getCurEXP());
            m.writer().writeInt(thanThu.getMaxEXP());
            m.writer().writeByte(thanThu.getStars());
        }


        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
    }

    public void flush() {
        try {
            final JSONArray jarr = new JSONArray();
            String sqlSET = "`exp`=" +
                    this.exp + ",`level`=" +
                    this.getLevel() + ",`itemLevel`=" +
                    this.itemLevel + ",`coin`=" +
                    this.coin + ",`log`='" +
                    this.log + "',`alert`='" +
                    this.alert + "',`use_card`=" +
                    this.use_card + ",`openDun`=" +
                    this.openDun + ",`debt`=" +
                    this.debt + "";
            for (int i = 0; i < this.members.size(); ++i) {
                final ClanMember mem = this.members.get(i);
                final JSONArray jarr2 = new JSONArray();
                jarr2.add(mem.charID);
                jarr2.add(mem.cName);
                jarr2.add(mem.clanName);
                jarr2.add(mem.typeclan);
                jarr2.add(mem.clevel);
                jarr2.add(mem.nClass);
                jarr2.add(mem.pointClan);
                jarr2.add(mem.pointClanWeek);
                jarr.add(jarr2);
            }

            sqlSET = sqlSET + ",`members`='" + jarr.toJSONString() + "'";
            jarr.clear();
            for (short j = 0; j < this.items.size(); ++j) {
                final Item item = this.items.get(j);
                jarr.add(ItemData.ObjectItem(item, j));
            }

            sqlSET = sqlSET + ",`items`='" + jarr.toJSONString() + "'";
            jarr.clear();
            sqlSET = sqlSET + ",`week`='" + this.week + "'";
            sqlSET = sqlSET + ",`clan_than_thu`='" + Mapper.converter.writeValueAsString(this.clanThanThus) + "'";

            if (this.clanBattle != null) {
                this.clanBattleData = this.clanBattle.getBattleData();
                sqlSET = sqlSET + ",`clan_battle_data`='" + Mapper.converter.writeValueAsString(this.clanBattleData) + "'";
            }


            SQLManager.executeUpdate("UPDATE `clan` SET " + sqlSET + " WHERE `id`=" + this.id + " LIMIT 1;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createClan(final @Nullable User p, @NotNull String name) {
        if (p == null) return;

        name = name.toLowerCase();
        if (p.luong < LUONG_CREATE_CLAN) {
            p.session.sendMessageLog("Bạn cần có " + String.format("%,d", LUONG_CREATE_CLAN) + " lượng để thành lập gia tộc");
            return;
        }
        ClanManager clan = getClanByName(name);
        if (!p.nj.clan.clanName.isEmpty()) {
            return;
        }
        if (!util.CheckString(name, "^[a-zA-Z0-9]+$") || name.length() < 5 || name.length() > 10) {
            p.session.sendMessageLog("Tên gia tộc chỉ đồng ý các ký tự a-z,0-9 và chiều dài từ 5 đến 10 ký tự");
        } else if (util.CheckString(name, "\\d+")) {
            p.session.sendMessageLog("Tên gia tộc không thể toàn số ");
        } else if (clan != null) {
            p.session.sendMessageLog("Tên gia tộc đã tồn tại");
        } else {
            try {

                clan = new ClanManager();
                clan.name = name;
                clan.reg_date = util.toDateString(Date.from(Instant.now()));
                final ClanMember mem = new ClanMember(name, TOC_TRUONG, p.nj);
                clan.members.add(mem);
                p.nj.clan = mem;


                SQLManager.executeUpdate("INSERT INTO clan(`name`,`reg_date`,`log`,`alert`,`members`, `exp`) " +
                        "VALUES ('" + clan.name + "','" + clan.reg_date + "','" + clan.log + "','" + clan.alert + "','[" + mem + "]', 0);");

                ClanManager finalClan = clan;
                SQLManager.executeQuery("SELECT `id` FROM `clan` WHERE `name`LIKE'" + clan.name + "' LIMIT 1;", (red) -> {
                    red.first();
                    finalClan.id = red.getInt("id");
                    finalClan.writeLog("", 0, finalClan.coin, util.toDateString(Date.from(Instant.now())));
                    finalClan.week = util.toDateString(Date.from(Instant.now()));
                    readWriteLock.writeLock().lock();
                    try {
                        ClanManager.entrys.add(finalClan);
                    } finally {
                        readWriteLock.writeLock().unlock();
                    }
                    finalClan.flush();
                    p.nj.flush();
                    p.upluong(-LUONG_CREATE_CLAN);
                    final Message m = new Message(-28);
                    m.writer().writeByte(-96);
                    m.writer().writeUTF(finalClan.name);
                    m.writer().writeInt(p.luong);
                    m.writer().flush();
                    p.sendMessage(m);
                    m.cleanup();
                    p.setTypeClan(4);

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dissolution() {
        try {
            synchronized (ClanManager.entrys) {
                ClanManager.entrys.remove(this);
                final Message m = new Message(-24);
                m.writer().writeUTF("Gia tộc " + this.name + " đã bị giải tán");
                m.writer().flush();
                while (!this.members.isEmpty()) {
                    final ClanMember mem = this.members.remove(0);
                    mem.typeclan = -1;
                    mem.clanName = "";
                    mem.pointClanWeek = 0;
                    final Ninja n = PlayerManager.getInstance().getNinja(mem.cName);
                    if (n != null) {
                        n.p.setTypeClan(mem.typeclan);
                        n.p.sendMessage(m);
                    }
                }
                m.cleanup();
                SQLManager.executeUpdate("DELETE FROM `clan` WHERE `id`=" + this.id + " LIMIT 1;");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        for (int i = 0; i < ClanManager.entrys.size(); ++i) {
            ClanManager.entrys.get(i).flush();
        }
    }

    static {
        entrys = new CopyOnWriteArrayList<>();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void inviteToDun(String name) {

        if (clanTerritory == null) return;

        final Ninja ninja = getMem(name).getNinja();
        if (ninja.p.getClanTerritoryData() == null) {
            ninja.p.setClanTerritoryData(new ClanTerritoryData(clanTerritory, ninja));
        }
        ninja.p.sendYellowMessage("Bạn đã được mời vào lãnh địa gia tộc");
    }

    public void setClanTerritory(ClanTerritory clanTerritory) {
        this.clanTerritory = clanTerritory;
    }

    public void setClanBattle(final @Nullable ClanBattle clanBattle) {

        this.clanBattle = clanBattle;
        if (clanBattle != null)
            this.clanBattleData = clanBattle.getBattleData();
        else
            this.clanBattleData = null;
    }

    @Nullable
    public ClanBattle getClanBattle() {
        return this.clanBattle;
    }

    public void setClanBattleData(final @Nullable ClanBattleData clanBattleData) {
        this.clanBattleData = clanBattleData;
    }

    @Nullable
    public ClanBattleData getClanBattleData() {
        return clanBattleData;
    }


    public boolean restore() {
        if (this.getClanBattleData() != null && !this.getClanBattleData().isExpired()) {
            this.setClanBattle(new ClanBattle(this.getClanBattleData()));
            return true;
        }
        this.setClanBattle(null);
        this.setClanBattleData(null);
        return false;
    }

    @Nullable
    public ClanThanThu getCurrentThanThu() {
        if (this.clanThanThus.size() == 0) return null;
        return this.clanThanThus.get(this.getThanThuIndex() % this.clanThanThus.size());
    }

    public boolean containThanThu(int i) {
        return this.clanThanThus.stream().anyMatch(t -> t.getType() == i);
    }

    public int getThanThuIndex() {
        return thanThuIndex;
    }

    public void setThanThuIndex(int thanThuIndex) {
        this.thanThuIndex = thanThuIndex % this.clanThanThus.size();
    }
}
