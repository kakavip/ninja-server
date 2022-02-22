package real;

import boardGame.Place;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import patch.*;
import battle.BattleData;
import battle.ClanBattle;
import candybattle.CandyBattle;
import interfaces.IGlobalBattler;
import interfaces.TeamBattle;
import tournament.TournamentData;
import server.SQLManager;
import server.Service;
import server.util;
import tasks.TaskTemplate;
import threading.Manager;
import threading.Message;
import threading.Server;

import java.io.IOException;
import java.util.*;

import static patch.Mapper.converter;
import static tasks.TaskList.taskTemplates;

@SuppressWarnings("ALL")
public class Ninja extends Body implements TeamBattle, IGlobalBattler {

    private byte taskId;
    public byte gender;
    public int xu;
    public int xuBox;
    public int yen;
    public int ticketXu;
    public int ticketYen;
    public int maxluggage;
    protected byte levelBag;
    public int mapType;
    public int mapLTD;
    public int mapid;
    public int mobAtk;
    public long eff5buff;
    public byte type;
    protected boolean isTrade;
    protected int rqTradeId;
    protected int tradeId;
    protected int tradeCoin;
    protected long tradeDelay;
    protected byte tradeLock;
    public byte denbu;
    public boolean ddClan;
    public int caveID;
    public int nCave;
    public int pointCave;
    public int useCave;
    protected int bagCaveMax;
    protected short itemIDCaveMax;
    public int requestclan;
    public long deleyRequestClan;
    public long delayEffect;
    public long timeRemoveClone;
    public int menuType;
    public int nvhnCount;
    public int taThuCount;
    public int nvdvCount;
    @NotNull
    public int[] DVPoints;
    public int nActPoint;
    public long lastTimeMove = -1;
    public volatile boolean isBusy = false;
    private short taskIndex = 0;
    public short taskCount;
    public boolean isNpc = false;
    public int pointEvent = 0;
    public int timesResetPpoint = 0;
    public int timesResetSpoint = 0;
    public byte reward10 = 0;
    public byte reward20 = 0;
    public byte reward30 = 0;
    public byte reward40 = 0;
    public byte reward50 = 0;
    public byte reward70 = 0;
    public byte reward90 = 0;
    public byte reward130 = 0;
    public long suishou;
    public long hayatemi;
    public long bousouhayate;
    public int typemenu;
    public int typebet;

    @Nullable
    public CandyBattle candyBattle;
    @NotNull
    public User p;
    @Nullable
    private Place place;
    @NotNull
    public String name;
    @Nullable
    public ClanMember clan;
    @Nullable
    public Item[] ItemBag;
    @NotNull
    public Item[] ItemBox;
    @NotNull
    protected List<@NotNull Friend> friend;
    @NotNull
    protected List<@NotNull Byte> tradeIdItem;
    @NotNull
    public Date newlogin;
    @Nullable
    public CloneChar clone;
    @Nullable
    private TournamentData tournamentData;
    @Nullable
    public BattleData battleData;
    @NotNull
    private TaskOrder[] tasks = new TaskOrder[3];
    @Nullable
    private Battle battle;
    @Nullable
    private ClanBattle clanBattle;

    @Nullable
    public TournamentData getTournamentData() {
        return tournamentData;
    }

    protected Ninja() {

        this.nvdvCount = 50;
        this.nvhnCount = 20;
        this.taThuCount = 1;
        this.nActPoint = 0;
        this.DVPoints = null;

        this.p = null;
        this.setPlace(null);
        this.name = null;
        this.clan = null;

        this.setTaskId(1);
        this.setTaskIndex(0);

        this.gender = -1;
        this.xu = 0;
        this.xuBox = 0;
        this.yen = 0;
        this.ticketXu = 0;
        this.ticketYen = 0;
        this.maxluggage = 30;
        this.levelBag = 0;
        this.ItemBag = null;
        this.ItemBox = null;
        this.friend = new ArrayList<>();
        this.mapType = 0;
        this.mapLTD = 22;
        this.setMapid(22);
        this.mobAtk = -1;
        this.eff5buff = 0L;
        this.type = 0;
        this.isTrade = false;
        this.tradeCoin = 0;
        this.tradeDelay = 0L;
        this.tradeLock = -1;
        this.tradeIdItem = new ArrayList<Byte>();
        this.denbu = 0;
        this.newlogin = null;
        this.ddClan = false;
        this.caveID = -1;
        this.nCave = 1;
        this.pointCave = 0;
        this.useCave = 1;
        this.bagCaveMax = 0;
        this.itemIDCaveMax = -1;
        this.requestclan = -1;
        this.deleyRequestClan = 0L;
        this.delayEffect = 0L;
        this.timeRemoveClone = -1L;
        this.clone = null;
        this.seNinja(this);

    }

    public boolean hasItemInBag(int id) {
        for (Item item : this.ItemBag) {
            if (item != null && item.id == id) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public Body get() {
        Body b = this;
        if (this.isNhanban) {
            b = this.clone;
        }
        return b;
    }

    public byte getAvailableBag() {
        byte num = 0;
        for (int i = 0; i < this.ItemBag.length; ++i) {
            if (this.ItemBag[i] == null) {
                ++num;
            }
        }
        return num;
    }

    public byte getBoxNull() {
        byte num = 0;
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            if (this.ItemBox[i] == null) {
                ++num;
            }
        }
        return num;
    }

    @Nullable
    public Item getIndexBag(final int index) {
        if (index < this.ItemBag.length && index >= 0) {
            return this.ItemBag[index];
        }
        return null;
    }

    @Nullable
    public Item getIndexBox(final int index) {
        if (index < this.ItemBox.length && index >= 0) {
            return this.ItemBox[index];
        }
        return null;
    }

    public int quantityItemyTotal(final int id) {
        int quantity = 0;
        for (int i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.id == id) {
                quantity += item.quantity;
            }
        }
        return quantity;
    }

    @Nullable
    protected Item getItemIdBag(final int id) {
        for (int i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.id == id) {
                return item;
            }
        }
        return null;
    }

    public int getIndexBagid(final int id, final boolean lock) {
        for (int i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.id == id && item.isLock() == lock) {
                return i;
            }
        }
        return -1;
    }

    public byte getIndexBoxid(final int id, final boolean lock) {
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            final Item item = this.ItemBox[i];
            if (item != null && item.id == id && item.isLock() == lock) {
                return i;
            }
        }
        return -1;
    }

    protected int getIndexBagItem(final int id, final boolean lock) {
        for (int i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.id == id && item.isLock() == lock) {
                return i;
            }
        }
        return -1;
    }

    public int getIndexBagNotItem() {
        for (int i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item == null) {
                return i;
            }
        }
        return -1;
    }

    protected byte getIndexBoxNotItem() {
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            final Item item = this.ItemBox[i];
            if (item == null) {
                return i;
            }
        }
        return -1;
    }

    protected void setXPLoadSkill(final long exp) throws IOException {
        this.get().setExp(exp);
        final Message m = new Message(-30);
        m.writer().writeByte(-124);
        m.writer().writeByte(this.get().speed);
        m.writer().writeInt(this.get().getMaxHP());
        m.writer().writeInt(this.get().getMaxMP());
        m.writer().writeLong(this.get().getExp());
        m.writer().writeShort(this.get().getSpoint());
        m.writer().writeShort(this.get().getPpoint());
        m.writer().writeShort(this.get().getPotential0());
        m.writer().writeShort(this.get().getPotential1());
        m.writer().writeInt(this.get().getPotential2());
        m.writer().writeInt(this.get().getPotential3());
        m.writer().flush();
        this.p.sendMessage(m);
        m.cleanup();
    }

    protected void sortBag() throws IOException {
        try {
            int i;
            for (i = 0; i < ItemBag.length; i = (i + 1)) {
                if (ItemBag[i] != null && !(ItemBag[i]).isExpires && (ItemData.ItemDataId(ItemBag[i].id)).isUpToUp) {
                    for (int j = (i + 1); j < ItemBag.length; j = (j + 1)) {
                        if (ItemBag[j] != null && !(ItemBag[i]).isExpires && (ItemBag[j]).id == (ItemBag[i]).id
                                && (ItemBag[j]).isLock() == (ItemBag[i]).isLock()) {
                            (ItemBag[i]).quantity += (ItemBag[j]).quantity;
                            ItemBag[j] = null;
                        }
                    }
                }
            }

            for (i = 0; i < ItemBag.length; i = (i + 1)) {
                if (ItemBag[i] == null) {
                    for (int j = (i + 1); j < ItemBag.length; j = j + 1) {
                        if (ItemBag[j] != null) {
                            ItemBag[i] = ItemBag[j];
                            ItemBag[j] = null;
                            break;
                        }
                    }
                }
            }
        } catch (Exception exception) {
        }
        final Message m = new Message(-30);
        m.writer().writeByte(-107);
        m.writer().flush();
        this.p.sendMessage(m);
        m.cleanup();
    }

    protected void sortBox() throws IOException {
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            if (this.ItemBox[i] != null && !this.ItemBox[i].isExpires
                    && ItemData.ItemDataId(this.ItemBox[i].id).isUpToUp) {
                for (byte j = (byte) (i + 1); j < this.ItemBox.length; ++j) {
                    if (this.ItemBox[j] != null && !this.ItemBox[i].isExpires
                            && this.ItemBox[j].id == this.ItemBox[i].id
                            && this.ItemBox[j].isLock() == this.ItemBox[i].isLock()) {
                        final Item item = this.ItemBox[i];
                        item.quantity += this.ItemBox[j].quantity;
                        this.ItemBox[j] = null;
                    }
                }
            }
        }
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            if (this.ItemBox[i] == null) {
                for (byte j = (byte) (i + 1); j < this.ItemBox.length; ++j) {
                    if (this.ItemBox[j] != null) {
                        this.ItemBox[i] = this.ItemBox[j];
                        this.ItemBox[j] = null;
                        break;
                    }
                }
            }
        }
        final Message m = new Message(-30);
        m.writer().writeByte(-106);
        m.writer().flush();
        this.p.sendMessage(m);
        m.cleanup();
    }

    public boolean addItemBag(final boolean uptoup, final @NotNull Item itemup) {
        if (itemup == ItemData.defaultItem) {
            return false;
        }
        if (getAvailableBag() == 0) {
            if (p != null && p.session != null) {
                p.session.sendMessageLog("Hành trang không đủ chỗ trống");
            }
            return false;
        }
        try {
            int index = this.getIndexBagid(itemup.id, itemup.isLock());
            if (uptoup && !itemup.isExpires && ItemData.ItemDataId(itemup.id).isUpToUp && index != -1) {
                final Item item = this.ItemBag[index];
                item.quantity += itemup.quantity;
                final Message message = new Message(9);
                message.writer().writeByte(index);
                message.writer().writeShort(itemup.quantity);
                message.writer().flush();
                this.p.sendMessage(message);
                message.cleanup();
                return true;
            }
            index = this.getIndexBagNotItem();
            if (index == -1) {
                this.p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                return false;
            }
            this.ItemBag[index] = itemup;
            final Message m = new Message(8);
            m.writer().writeByte(index);
            m.writer().writeShort(itemup.id);
            m.writer().writeBoolean(itemup.isLock());
            if (ItemData.isTypeBody(itemup.id) || ItemData.isTypeNgocKham(itemup.id)) {
                m.writer().writeByte(itemup.getUpgrade());
            }
            m.writer().writeBoolean(itemup.isExpires);
            m.writer().writeShort(itemup.quantity);
            m.writer().flush();
            this.p.sendMessage(m);
            return true;
        } catch (IOException iOException) {
            return false;
        }
    }

    public void removeItemBags(final int id, final int quantity) {
        int num = 0;
        for (int i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.id == id) {
                if (num + item.quantity >= quantity) {
                    this.removeItemBag(i, quantity - num);
                    break;
                }
                num += item.quantity;
                this.removeItemBag(i, item.quantity);
            }
        }
    }

    public synchronized void removeItemBag(final int index, final int quantity) {
        final Item item = this.getIndexBag(index);
        try {
            final Item item2 = item;
            item2.quantity -= quantity;
            if (item.quantity <= 0) {
                this.ItemBag[index] = null;
            }

            final Message m = new Message(18);
            m.writer().writeByte(index);
            m.writer().writeShort(quantity);
            m.writer().flush();
            this.p.sendMessage(m);
            m.cleanup();
        } catch (IOException ex) {
        }
    }

    public synchronized void removeItemBag(final byte index) {
        final Item item = this.getIndexBag(index);
        try {
            final Message m = new Message(18);
            m.writer().writeByte(index);
            m.writer().writeShort(item.quantity);
            m.writer().flush();
            this.p.sendMessage(m);
            m.cleanup();
            this.ItemBag[index] = null;
        } catch (IOException ex) {
        }
    }

    public void removeItemBody(final byte index) throws IOException {
        this.get().ItemBody[index] = null;
        if (index == 10) {
            this.p.mobMeMessage(0, (byte) 0);
        }
        final Message m = new Message(-30);
        m.writer().writeByte(-80);
        m.writer().writeByte(index);
        m.writer().flush();
        this.p.sendMessage(m);
        m.cleanup();
    }

    public void removeItemBox(final byte index) throws IOException {
        this.ItemBox[index] = null;
        final Message m = new Message(-30);
        m.writer().writeByte(-75);
        m.writer().writeByte(index);
        m.writer().flush();
        this.p.sendMessage(m);
        m.cleanup();
    }

    public int upxu(long x) {
        if (x >= 2000000000L - this.xu) {
            this.xu = 2000000000;
        } else {
            this.xu += (int) x;
        }
        return (int) x;
    }

    public synchronized int upyen(long x) {
        if (x >= 2000000000 - this.yen) {
            this.yen = 2000000000;
        } else {
            this.yen += (int) x;
        }
        return (int) x;
    }

    public synchronized void upTicketXu(int x) {
        this.ticketXu += x;
    }

    public synchronized void upTicketYen(int x) {
        this.ticketYen += x;
    }

    public synchronized void upNActPoint(int n) {
        this.nActPoint += n;
        if (this.nActPoint <= 0) {
            this.nActPoint = 0;
        }
    }

    public synchronized void upxuMessage(final long x) {
        try {
            final Message m = new Message(95);
            m.writer().writeInt(this.upxu(x));
            m.writer().flush();
            this.p.sendMessage(m);
            m.cleanup();
        } catch (IOException ex) {
        }
    }

    public void upyenMessage(final long x) {
        try {
            final Message m = new Message(-8);
            m.writer().writeInt(this.upyen(x));
            m.writer().flush();
            this.p.sendMessage(m);
            m.cleanup();
        } catch (IOException ex) {
        }
    }

    @NotNull
    protected static Ninja setup(final @NotNull User p, final @NotNull String name) {
        val nj = getNinja(name);
        nj.p = p;
        p.nj = nj;
        return nj;
    }

    @NotNull
    public static Ninja getNinja(String name) {
        final Ninja nj = new Ninja();
        SQLManager.executeQuery("SELECT * FROM `ninja` WHERE `name`LIKE'" + name + "';", (red) -> {
            try {
                if (red != null && red.first()) {
                    nj.id = red.getInt("id");
                    nj.name = red.getString("name");
                    nj.gender = red.getByte("gender");
                    nj.head = red.getByte("head");
                    nj.speed = red.getByte("speed");
                    nj.nclass = red.getByte("class");
                    nj.updatePpoint(red.getShort("ppoint"));
                    nj.setPotential0(red.getShort("potential0"));
                    nj.setPotential1(red.getShort("potential1"));
                    nj.setPotential2(red.getInt("potential2"));
                    nj.setPotential3(red.getInt("potential3"));
                    nj.setSpoint(red.getShort("spoint"));
                    nj.setTaskId(red.getByte("taskId"));
                    nj.taskCount = red.getShort("taskCount");
                    nj.setTaskIndex(red.getShort("taskIndex"));

                    JSONArray jar = (JSONArray) JSONValue.parse(red.getString("skill"));
                    if (jar != null) {
                        for (byte b = 0; b < jar.size(); ++b) {
                            final JSONObject job = (JSONObject) jar.get((int) b);
                            final Skill skill = new Skill();
                            skill.id = Byte.parseByte(job.get((Object) "id").toString());
                            skill.point = Byte.parseByte(job.get((Object) "point").toString());
                            nj.getSkills().add(skill);
                        }
                    }
                    JSONArray jarr2 = (JSONArray) JSONValue.parse(red.getString("KSkill"));
                    nj.KSkill = new byte[jarr2.size()];
                    for (byte j = 0; j < nj.KSkill.length; ++j) {
                        nj.KSkill[j] = Byte.parseByte(jarr2.get((int) j).toString());
                    }
                    jarr2 = (JSONArray) JSONValue.parse(red.getString("OSkill"));
                    nj.OSkill = new byte[jarr2.size()];
                    for (byte j = 0; j < nj.OSkill.length; ++j) {
                        nj.OSkill[j] = Byte.parseByte(jarr2.get((int) j).toString());
                    }
                    nj.setCSkill(Byte.parseByte(red.getString("CSkill")));
                    nj.setLevel(red.getShort("level"));
                    nj.setExp(red.getLong("exp"));
                    nj.expdown = red.getLong("expdown");
                    nj.pk = red.getByte("pk");
                    nj.xu = red.getInt("xu");
                    nj.xuBox = red.getInt("xuBox");
                    nj.yen = red.getInt("yen");
                    nj.ticketYen = red.getInt("ticketYen");
                    nj.ticketXu = red.getInt("ticketXu");
                    nj.maxluggage = red.getInt("maxluggage");
                    if (nj.maxluggage > Manager.MAX_LUGGAGE) {
                        nj.maxluggage = Manager.MAX_LUGGAGE;
                    }
                    nj.levelBag = red.getByte("levelBag");
                    nj.ItemBag = new Item[nj.maxluggage];
                    jar = (JSONArray) JSONValue.parse(red.getString("ItemBag"));
                    if (jar != null) {
                        for (byte j = 0; j < jar.size() && j < nj.maxluggage; ++j) {
                            final JSONObject job2 = (JSONObject) jar.get((int) j);
                            final byte index = Byte.parseByte(job2.get((Object) "index").toString());
                            Item item = ItemData.parseItem(jar.get((int) j).toString());
                            if (item == null || item.quantity <= 0) {
                                item = null;
                            }
                            nj.ItemBag[index] = item;
                        }
                    }
                    nj.ItemBox = new Item[30];
                    jar = (JSONArray) JSONValue.parse(red.getString("ItemBox"));
                    if (jar != null) {
                        for (byte j = 0; j < jar.size(); ++j) {
                            final JSONObject job2 = (JSONObject) jar.get((int) j);
                            final byte index = Byte.parseByte(job2.get((Object) "index").toString());
                            Item item = ItemData.parseItem(jar.get((int) j).toString());
                            if (item == null || item.quantity <= 0) {
                                item = null;
                            }
                            nj.ItemBox[index] = item;
                        }
                    }
                    nj.ItemBody = new Item[16];
                    jar = (JSONArray) JSONValue.parse(red.getString("ItemBody"));
                    if (jar != null) {
                        for (byte j = 0; j < jar.size(); ++j) {
                            final JSONObject job2 = (JSONObject) jar.get((int) j);
                            final byte index = Byte.parseByte(job2.get((Object) "index").toString());
                            Item item = ItemData.parseItem(jar.get((int) j).toString());
                            if (item == null || item.quantity <= 0) {
                                item = null;
                            }
                            nj.ItemBody[index] = item;
                        }
                    }
                    nj.ItemMounts = new Item[5];
                    jar = (JSONArray) JSONValue.parse(red.getString("ItemMounts"));
                    if (jar != null) {
                        for (byte j = 0; j < jar.size(); ++j) {
                            final JSONObject job2 = (JSONObject) jar.get((int) j);
                            final byte index = Byte.parseByte(job2.get((Object) "index").toString());
                            nj.ItemMounts[index] = ItemData.parseItem(jar.get((int) j).toString());
                        }
                    }
                    try {
                        nj.friend = Mapper.converter.readValue(red.getString("friend"),
                                new TypeReference<List<Friend>>() {
                                });
                    } catch (Exception e) {
                        System.out.println("PARSE FRIEND ERROR");
                    }

                    jar = (JSONArray) JSONValue.parse(red.getString("site"));
                    nj.setMapid(util.UnsignedByte((byte) Integer.parseInt(jar.get(0).toString())));
                    nj.x = Short.parseShort(jar.get(1).toString());
                    nj.y = Short.parseShort(jar.get(2).toString());
                    nj.mapLTD = Short.parseShort(jar.get(3).toString());
                    nj.mapType = Short.parseShort(jar.get(4).toString());
                    jar = (JSONArray) JSONValue.parse(red.getString("effect"));
                    try {
                        val r = Mapper.converter.readValue(red.getString("tasks"), TaskOrder[].class);
                        nj.setTasks(r);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        nj.taThuCount = red.getInt("tathucount");
                        nj.nvhnCount = red.getInt("nvhncount");
                        nj.nvdvCount = red.getInt("nvdvcount");
                        nj.nActPoint = red.getInt("nactpoint");
                        nj.get().setKyNangSo(red.getInt("kynangso"));
                        nj.get().setTiemNangSo(red.getInt("tiemnangso"));
                        nj.get().setBanghoa(red.getInt("banghoa"));
                        nj.get().setPhongLoi(red.getInt("phongloi"));
                        nj.battleData = Mapper.converter.readValue(red.getString("chientruong"), BattleData.class);
                    } catch (Exception e) {
                        nj.battleData = new BattleData();
                    }
                    // get diem nhiem vu danh vong.
                    jarr2 = (JSONArray) JSONValue.parse(red.getString("DVPoints"));
                    nj.DVPoints = new int[jarr2.size()];
                    for (int j = 0; j < nj.DVPoints.length; ++j) {
                        nj.DVPoints[j] = Integer.parseInt(jarr2.get((int) j).toString());
                    }

                    if (nj.getTasks().length != 3) {
                        nj.setTasks(new TaskOrder[3]);
                    }

                    try {
                        if (jar != null) {
                            for (int i = 0; i < jar.size(); i++) {
                                val effect = Effect.fromJSONObject((JSONObject) jar.get(i));
                                nj.addEffect(effect);
                            }
                        }
                    } catch (Exception e) {

                    }

                    jar = (JSONArray) JSONValue.parse(red.getString("clan"));
                    if (jar == null || jar.size() != 2) {
                        nj.clan = new ClanMember("", nj);
                    } else {
                        final String clanName = jar.get(0).toString();
                        final ClanManager clan = ClanManager.getClanByName(clanName);
                        if (clan == null || clan.getMem(name) == null) {
                            nj.clan = new ClanMember("", nj);
                        } else {
                            nj.clan = clan.getMem(name);
                            nj.clan.nClass = nj.nclass;
                            nj.clan.clevel = nj.getLevel();
                        }
                        nj.clan.pointClan = Integer.parseInt(jar.get(1).toString());
                    }

                    nj.denbu = red.getByte("denbu");
                    nj.pointEvent = red.getInt("pointEvent");
                    nj.timesResetPpoint = red.getInt("timesResetPpoint");
                    nj.timesResetSpoint = red.getInt("timesResetSpoint");
                    nj.reward10 = red.getByte("reward10");
                    nj.reward20 = red.getByte("reward20");
                    nj.reward30 = red.getByte("reward30");
                    nj.reward40 = red.getByte("reward40");
                    nj.reward50 = red.getByte("reward50");
                    nj.reward70 = red.getByte("reward70");
                    nj.reward90 = red.getByte("reward90");
                    nj.reward130 = red.getByte("reward130");
                    nj.newlogin = util.getDate(red.getString("newlogin"));
                    nj.ddClan = red.getBoolean("ddClan");
                    nj.caveID = red.getInt("caveID");
                    nj.nCave = red.getInt("nCave");
                    nj.pointCave = red.getInt("pointCave");
                    nj.useCave = red.getInt("useCave");
                    nj.bagCaveMax = red.getInt("bagCaveMax");
                    nj.itemIDCaveMax = red.getShort("itemIDCaveMax");
                    nj.exptype = red.getByte("exptype");
                    nj.isHuman = true;
                    nj.isNhanban = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return nj;
    }

    public void flush() {
        final JSONArray jarr = new JSONArray();
        try {
            jarr.add(this.getMapid());
            jarr.add(this.x);
            jarr.add(this.y);
            jarr.add(this.mapLTD);
            jarr.add(this.mapType);
            val friends = Mapper.converter.writeValueAsString(this.friend);

            String sqlSET = "`taskId`=" + this.getTaskId() + ",`class`=" + this.nclass + ",`ppoint`=" + this.getPpoint()
                    + ",`potential0`=" + this.getPotential0() + ",`potential1`=" + this.getPotential1()
                    + ",`potential2`=" + this.getPotential2() + ",`potential3`=" + this.getPotential3() + ",`spoint`="
                    + this.getSpoint() + ",`level`=" + this.getLevel() + ",`exp`=" + this.getExp() + ",`expdown`="
                    + this.expdown + ",`pk`=" + this.pk + ",`xu`=" + this.xu + ",`yen`=" + this.yen + ",`maxluggage`="
                    + this.maxluggage + ",`levelBag`=" + this.levelBag + ",`site`='" + jarr.toJSONString()
                    + "',`friend`='" + friends + "'";
            sqlSET += ", `ticketXu`=" + this.ticketXu + "";
            sqlSET += ", `ticketYen`=" + this.ticketYen + "";
            jarr.clear();
            for (final Skill skill : this.getSkills()) {
                jarr.add(SkillData.ObjectSkill(skill));
            }
            sqlSET = sqlSET + ",`skill`='" + jarr.toJSONString() + "'";
            jarr.clear();
            for (final byte oid : this.KSkill) {
                jarr.add(oid);
            }
            sqlSET = sqlSET + ",`KSkill`='" + jarr.toJSONString() + "'";
            jarr.clear();
            for (final byte oid : this.OSkill) {
                jarr.add(oid);
            }
            sqlSET = sqlSET + ",`OSkill`='" + jarr.toJSONString() + "',`CSkill`=" + this.getCSkill() + "";
            jarr.clear();

            for (final int dvId : this.DVPoints) {
                jarr.add(dvId);
            }
            sqlSET = sqlSET + ",`DVPoints`='" + jarr.toJSONString() + "'";
            jarr.clear();

            for (int j = 0; j < this.ItemBag.length; ++j) {
                final Item item = this.ItemBag[j];
                if (item != null && item.quantity > 0) {
                    jarr.add(ItemData.ObjectItem(item, j));
                }
            }
            sqlSET = sqlSET + ",`ItemBag`='" + jarr.toJSONString() + "'";
            jarr.clear();
            for (byte j = 0; j < this.ItemBox.length; ++j) {
                final Item item = this.ItemBox[j];
                if (item != null && item.quantity > 0) {
                    jarr.add(ItemData.ObjectItem(item, j));
                }
            }
            sqlSET = sqlSET + ",`xuBox`=" + this.xuBox + ",`ItemBox`='" + jarr.toJSONString() + "'";
            jarr.clear();
            for (byte j = 0; j < this.ItemBody.length; ++j) {
                final Item item = this.ItemBody[j];
                if (item != null && item.quantity > 0) {
                    jarr.add(ItemData.ObjectItem(item, j));
                }
            }
            sqlSET = sqlSET + ",`ItemBody`='" + jarr.toJSONString() + "'";
            jarr.clear();
            for (byte j = 0; j < this.ItemMounts.length; ++j) {
                final Item item = this.ItemMounts[j];
                if (item != null && item.quantity > 0) {
                    jarr.add(ItemData.ObjectItem(item, j));
                }
            }
            sqlSET = sqlSET + ",`ItemMounts`='" + jarr.toJSONString() + "'";
            jarr.clear();
            for (Effect effect : this.getVeff()) {
                if (Effect.isPermanentEffect(effect)) {
                    jarr.add(effect.toJSONObject());
                }
            }

            sqlSET = sqlSET + ",`effect`='" + jarr.toJSONString() + "'";
            jarr.clear();
            jarr.add(this.clan.clanName);
            jarr.add(this.clan.pointClan);
            sqlSET = sqlSET + ",`clan`='" + jarr.toJSONString() + "',`denbu`=" + this.denbu + ",`newlogin`='"
                    + util.toDateString(this.newlogin) + "',`ddClan`=" + this.ddClan + ",`caveID`=" + this.caveID
                    + ",`nCave`=" + this.nCave + ",`pointCave`=" + this.pointCave + ",`useCave`=" + this.useCave
                    + ",`bagCaveMax`=" + this.bagCaveMax + ",`itemIDCaveMax`=" + this.itemIDCaveMax + ",`exptype`="
                    + this.exptype + "";
            sqlSET = sqlSET + ",`reward10`=" + reward10 + ",`reward20`=" + reward20 + ",`reward30`=" + reward30
                    + ",`reward40`=" + reward40 + ",`reward50`=" + reward50 + ",`reward70`=" + reward70 + ",`reward90`="
                    + reward90 + ",`reward130`=" + reward130 + ",`pointEvent`=" + pointEvent
                    + ",`timesResetPpoint`=" + timesResetPpoint + ",`timesResetSpoint`=" + timesResetSpoint + "";
            sqlSET = sqlSET + ", `tasks`='" + converter.writeValueAsString(getTasks()) + "'";

            sqlSET = sqlSET + ",`phongloi`=" + this.getPhongLoi() + "";
            sqlSET = sqlSET + ",`banghoa`=" + this.getBanghoa() + "";
            sqlSET = sqlSET + ",`tiemnangso`=" + this.getTiemNangSo() + "";
            sqlSET = sqlSET + ",`kynangso`=" + this.getKyNangSo() + "";

            sqlSET = sqlSET + ", `nvhncount`=" + nvhnCount + "";
            sqlSET = sqlSET + ", `tathucount`=" + taThuCount + "";
            sqlSET = sqlSET + ", `nvdvcount`=" + nvdvCount + "";
            sqlSET = sqlSET + ", `nactpoint`=" + nActPoint + "";
            sqlSET = sqlSET + ", `taskId`=" + getTaskId() + "";
            sqlSET = sqlSET + ", `taskIndex`=" + getTaskIndex() + "";
            sqlSET = sqlSET + ", `taskCount`=" + taskCount + "";
            sqlSET = sqlSET + ", `chientruong`='" + Mapper.converter.writeValueAsString(battleData) + "'";
            SQLManager.executeUpdate("UPDATE `ninja` SET " + sqlSET + " WHERE `id`=" + this.id + " LIMIT 1;");
            jarr.clear();

            if (clone != null) {
                clone.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (this.party != null) {
            this.party.exitParty(this);
        }
        if (this.getPlace() != null) {
            this.getPlace().leave(this.p);
        }
    }

    @SneakyThrows
    @Override
    public void enterSamePlace(final @Nullable Place place, @Nullable TeamBattle other) {
        if (place == null) {
            return;
        }

        val x0 = place.map.template.x0;
        val y0 = place.map.template.y0;
        this.setMapid(place.map.id);

        if (this.getPlace() != null) {
            this.getPlace().leave(this.p);
        }

        if (!this.isNpc) {
            this.x = (short) (x0 + (other == null ? +25 : -25));
        } else {
            this.x = 1133;
        }

        if (!this.isNpc) {
            this.y = y0;
        } else {
            this.y = 240;
        }

        if (isBattleViewer) {
            this.y = 336;
        }

        if (this.clone != null) {
            this.clone.x = (short) (this.x + util.nextInt(-10, 10));
            this.clone.y = y0;
        }

        place.Enter(this.p);
        if (other == null) {
            return;
        }
        other.enterSamePlace(place, null);
    }

    @SneakyThrows
    @Override
    public void changeTypePk(short typePk, final @Nullable TeamBattle notifier) {
        if (notifier == null) {
            return;
        }
        this.setTypepk(typePk);
        val m = new Message(-30);
        m.writer().writeByte(-92);
        m.writer().writeInt(this.id);
        m.writer().writeByte(typePk);
        sendMessage(m);
        notifier.sendMessage(m);
        m.cleanup();
    }

    @Override
    public short getCSkill() {

        if (isNpc) {
            val randomIndex = util.nextInt(0, getSkills().size());
            return getSkills().get(randomIndex % getSkills().size()).id;
        }

        return super.getCSkill();
    }

    @Override
    public void notifyMessage(@NotNull final String message) {
        val newMessage = message.replace("#", "Bạn");
        this.p.sendYellowMessage(newMessage);
    }

    @Override
    public short getPhe() {
        return this.battleData.getPhe();
    }

    @Override
    public void upXuMessage(long xu) {
        this.upxuMessage(xu);
    }

    @Override
    public int getMaster() {
        return MASTER_SINGLE;
    }

    @Override
    public void sendMessage(final @Nullable Message message) {
        if (message != null) {
            p.sendMessage(message);
        }
    }

    @Override
    public @NotNull List<@NotNull Ninja> getNinjas() {
        return Collections.singletonList(this);
    }

    @Override
    public Battle getBattle() {
        return this.battle;
    }

    @Override
    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    @Override
    public void updateEffect(final @NotNull Effect effect) {
        this.p.setEffect(effect.template.id, effect.timeStart, effect.timeLength, effect.param);
    }

    @Override
    public int getCurrentMapId() {
        return getMapid();
    }

    @Override
    public @NotNull String getTeamName() {
        return this.name;
    }

    @Override
    public boolean hasBattle() {
        return this.battle != null;
    }

    @Override
    public boolean loose() {
        return this.isDie || this.getMapid() != 111;
    }

    public int getMapId() {
        return getMapid();
    }

    @Override
    public short getKeyLevel() {
        return (short) this.getLevel();
    }

    @Override
    public int getXu() {
        return this.xu;
    }

    public boolean checkHanhTrang(int i) {
        return this.getAvailableBag() >= i;
    }

    @SneakyThrows
    private void sendATaskMessage(final @Nullable TaskOrder task) {
        if (task == null) {
            return;
        }
        val m = new Message(96);
        val ds = m.writer();
        ds.writeByte(task.getTaskId());
        ds.writeInt(task.getCount());
        ds.writeInt(task.getMaxCount());
        ds.writeUTF(task.getName());
        ds.writeUTF(task.getDescription());
        ds.writeByte(task.getKillId());
        ds.writeByte(task.getMapId());
        ds.flush();
        sendMessage(m);
        m.cleanup();
    }

    public void addTaskOrder(final @Nullable TaskOrder task) {
        if (task == null) {
            return;
        }
        if (task.getTaskId() != TaskOrder.NHIEM_VU_DANH_VONG) {
            sendATaskMessage(task);
        }
        this.getTasks()[task.getTaskId()] = task;
    }

    public void sendTaskOrders() {
        for (TaskOrder task : this.getTasks()) {
            if (task == null || task.getTaskId() == TaskOrder.NHIEM_VU_DANH_VONG) {
                continue;
            }
            this.sendATaskMessage(task);
        }
    }

    public TaskOrder[] getTasks() {
        return tasks;
    }

    public void setTasks(TaskOrder[] tasks) {
        this.tasks = tasks;
    }

    @SneakyThrows
    public void huyNhiemVu(int typeNhiemVu) {
        if (typeNhiemVu == TaskOrder.NHIEM_VU_TA_THU) {
            taThuCount--;
        } else if (typeNhiemVu == TaskOrder.NHIEM_VU_HANG_NGAY) {
            nvhnCount++;
        } else if (typeNhiemVu == TaskOrder.NHIEM_VU_DANH_VONG) {
            nvdvCount++;
        }
        this.tasks[typeNhiemVu] = null;
        if (typeNhiemVu != TaskOrder.NHIEM_VU_DANH_VONG) {
            val m = new Message(-158);
            val ds = m.writer();
            ds.writeByte(typeNhiemVu);
            ds.flush();
            sendMessage(m);
            m.cleanup();
        }
    }

    public void updateTaskOrder(int typeTask, int point, int killId) {
        TaskOrder task = this.tasks[typeTask];
        if (task == null) {
            return;
        }

        util.Debug("TaskOrder " + typeTask + ": " + point + " point, killId: " + killId);

        if (task.getKillId() == killId) {
            task.setCount(task.getCount() + point);
            if (typeTask != TaskOrder.NHIEM_VU_DANH_VONG) {
                updateTaskMessage(task);
            } else {
                if (task.isDone()) {
                    this.p.sendYellowMessage("Đã hoàn thành nhiệm vụ danh vọng.");
                }
            }
        }
    }

    @SneakyThrows
    private void updateTaskMessage(final @Nullable TaskOrder task) {
        if (task == null) {
            return;
        }
        val m = new Message(97);
        val ds = m.writer();
        ds.writeByte(task.getTaskId());
        ds.writeInt(task.getCount());
        ds.flush();
        sendMessage(m);
        m.cleanup();
    }

    public boolean hoanThanhNhiemVu(int typeNhiemVu) {
        TaskOrder task = this.tasks[typeNhiemVu];
        if (task != null) {
            if (task.isDone()) {
                this.getRewards(task);

                huyNhiemVu(typeNhiemVu);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @SneakyThrows
    private void getRewards(final TaskOrder task) {
        if (task.isDone()) {
            if (task.getTaskId() == TaskOrder.NHIEM_VU_HANG_NGAY) {
                int luck = util.nextInt(100);
                if (luck <= 30) {
                    int lv = Math.min(this.get().getLevel(), Manager.MAX_LEVEL_RECEIVE_LUONG_COEF);
                    this.p.upluongMessage(util.nextInt(lv, lv * 2));

                } else if (luck <= 45) {
                    long currentLvExps = Level.getLevel(this.get().getLevel()).exps;

                    long xpup = Math.min((long) (currentLvExps * util.nextInt(1, 3) / 100),
                            10_000_000L);
                    this.p.updateExp(xpup, true);
                } else {
                    this.upyenMessage(
                            (long) (Math.min(this.get().getLevel(),
                                    Manager.MAX_LEVEL_RECEIVE_YEN_COEF)
                                    * (Manager.YEN_COEF * 2.5) *
                                    util.nextInt(90,
                                            100)
                                    / 100));
                }

                if (this.nvhnCount == 10) {
                    this.upNActPoint(3);
                }
            } else if (task.getTaskId() == TaskOrder.NHIEM_VU_TA_THU) {
                Item i = ItemData.itemDefault(251);
                i.quantity = this.get().getLevel() >= 60 ? 5 : 2;
                this.addItemBag(true, i);
            } else if (task.getTaskId() == TaskOrder.NHIEM_VU_DANH_VONG) {
                int ddvN = task.nvdvType() / 2;
                int ddvId = 695 + ddvN;
                int nDdv = util.nextInt(10, 15) - ddvN * 2;

                Item i = ItemData.itemDefault(ddvId);
                i.quantity = nDdv;
                this.addItemBag(true, i);

                // update dv point
                this.upDVPoints(util.nextInt(2, 4), task.nvdvType());

                if (this.nvdvCount % 10 == 0) {
                    this.upNActPoint(3);
                }
            }
        }
    }

    private void upDVPoints(int point, int type) {
        this.DVPoints[type] += point;
        if (this.DVPoints[type] > 1000) {
            this.DVPoints[type] = 1000;
        }
    }

    public boolean enoughItemId(int id, int count) {
        int itemCount = 0;
        for (Item item : ItemBag) {
            if (item != null && item.id == id) {
                itemCount += item.quantity;
                if (itemCount >= count) {
                    return true;
                }
            }
        }
        return false;
    }

    @SneakyThrows
    @Override
    public void changeTypePk(short typePk) {
        this.battleData.setPhe(typePk);
        val m = new Message(-30);
        m.writer().writeByte(-92);
        m.writer().writeInt(this.id);
        m.writer().writeByte(typePk);
        sendMessage(m);
        m.cleanup();
        this.setTypepk(typePk);

    }

    @SneakyThrows
    @Override
    public void upPoint(int point) {

        if (this.battleData == null) {
            return;
        }
        this.battleData.setPoint(this.battleData.getPoint() + point);
        final Message message = Service.messageNotMap((byte) (45 - 126));
        message.writer().writeShort(this.battleData.getPoint());
        message.writer().flush();
        sendMessage(message);
        message.cleanup();
    }

    @Override
    public void resetPoint() {
        if (this.battleData == null) {
            return;
        }
        this.battleData.setPoint(0);
        upPoint(0);
    }

    @Override
    public int getPoint() {
        if (this.battleData == null) {
            return 0;
        }
        return this.battleData.getPoint();
    }

    @Override
    public void enterChienTruong(byte pkType) {
        if (!Server.getInstance().globalBattle.enter(this, pkType)) {
            changeTypePk(Constants.PK_NORMAL);
        }
    }

    public int getMapid() {
        return mapid;
    }

    public void setMapid(int mapid) {
        this.mapid = mapid;
    }

    public void setClanBattle(ClanBattle clanBattle) {
        this.clanBattle = clanBattle;
    }

    public ClanBattle getClanBattle() {
        return this.clanBattle;
    }

    public void setTournamentData(TournamentData tournament) {
        this.tournamentData = tournament;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Ninja ninja = (Ninja) o;
        return name.equals(ninja.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    // TODO
    public synchronized void upMainTask() {
        try {
            if (this.getTaskId() >= taskTemplates.length) {
                return;
            }

            TaskTemplate taskTemplate = taskTemplates[this.getTaskId()];
            // NOTE bypass tasks
            if (taskTemplate.getBypass() == true) {
                this.setTaskId((byte) (this.getTaskId() + 1));
                this.setTaskIndex(-1);
                Service.finishTask(this);
            } else {
                this.taskCount = (short) (this.taskCount + 1);
                if (this.taskCount >= taskTemplate.counts[this.getTaskIndex()]) {
                    this.setTaskIndex((byte) (this.getTaskIndex() + 1));
                    this.taskCount = 0;
                    if (this.getTaskIndex() >= taskTemplate.subNames.length) {
                        this.setTaskId((byte) (this.getTaskId() + 1));
                        this.setTaskIndex(-1);
                        Service.finishTask(this);
                    } else {
                        Service.nextTask(this);
                    }
                } else {
                    Service.updateTask(this);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void clearTask() {
        this.setTaskIndex(-1);
        try {
            for (int i = 0; i < this.ItemBag.length; i++) {
                if (this.ItemBag[i] != null && this.ItemBag[i].isTypeTask()) {
                    this.ItemBag[i] = null;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public short getTaskIndex() {
        return taskIndex;
    }

    public void setTaskIndex(int taskIndex) {
        this.taskIndex = (short) taskIndex;
    }

    public byte getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = (byte) taskId;
    }

    @Override
    public short partBody() {
        if (isNpc) {
            if (id == -9) {
                return 45;
            } else if (id == -10) {
                return 54;
            } else if (id == -11) {
                return 66;
            } else if (id == -17) {
                return 94;
            }
        }
        return super.partBody();
    }

    @Override
    public short partLeg() {
        if (isNpc) {
            if (id == -9) {
                return 46;
            } else if (id == -10) {
                return 55;
            } else if (id == -11) {
                return 67;
            } else if (id == -17) {
                return 95;
            }
        }
        return super.partLeg();
    }

    public void removeAllItemInBag(int itemId) {
        if (itemId != -1) {
            for (int i = 0; i < ItemBag.length; i++) {
                Item item = ItemBag[i];
                if (item != null && (item.id == itemId || item.getData().isItemNhiemVu())) {
                    ItemBag[i] = null;
                }
            }
        }
    }

    public void nhanQuaTanThu() throws IOException {
        if (p == null) {
            return;
        }
        p.upluongMessage(20_000L);
        this.upxuMessage(10_000_000L);
        this.upyenMessage(40_000_000L);
        p.nhanQua = true;
    }
}
