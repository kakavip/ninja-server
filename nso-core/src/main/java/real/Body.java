package real;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import patch.*;
import server.util;
import threading.Manager;
import threading.Message;
import lombok.SneakyThrows;
import lombok.val;
import interfaces.ISolo;
import interfaces.ISoloer;
import server.GameScr;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static patch.ReduceTimeSkillEffect.Type.*;
import static real.ItemData.*;

@SuppressWarnings("ALL")
public class Body implements ISoloer {
    public static short PERCENT_DAME_PEOPLE;

    public int id;
    public byte head;
    public byte caiTrang;
    public int typeSolo;
    public long delayEffect2;
    protected byte speed;
    public byte nclass;
    public long expdown;
    public byte pk;
    private short typepk;
    private volatile short ppoint;
    private volatile short spoint;
    private short potential0;
    private short potential1;
    private int potential2;
    private int potential3;
    public boolean isDie;
    public boolean isHuman;
    public boolean isNhanban;
    public long CSkilldelay;
    public volatile short x;
    public volatile short y;
    public volatile int hp;
    public int mp;

    public short ID_Body = -1;
    public short ID_PP = -1;
    public short ID_HAIR = -1;
    public short ID_LEG = -1;
    public short ID_HORSE = -1;
    public short ID_NAME = -1;
    public short ID_RANK = -1;
    public short ID_MAT_NA = -1;
    public short ID_Bien_Hinh = -1;
    public short ID_WEA_PONE = -1;

    @NotNull
    public Ninja c;
    @Nullable
    private AtomicInteger level;
    @NotNull
    private AtomicLong exp;
    @NotNull
    private List<@NotNull Skill> skills;
    @NotNull
    public byte[] KSkill;
    @NotNull
    public byte[] OSkill;
    @NotNull
    private short CSkill;
    @NotNull
    public Item[] ItemBody;
    @NotNull
    public Item[] ItemMounts;
    @Nullable
    public Mob mobMe;
    @Nullable
    public Party party;
    public byte exptype;
    @NotNull
    private final List<@NotNull Effect> veff;
    @Nullable
    public ISolo solo;

    @NotNull
    public Map<ReduceTimeSkillEffect.Type, @NotNull ReduceTimeSkillEffect> reduceTimeSkillEffects = new ConcurrentHashMap<>();

    private int tiemNangSo;
    private int kyNangSo;
    private int banghoa;
    private int phongLoi;
    public boolean isIce;
    public boolean isWind;
    public boolean isFire;
    public boolean isBurn;
    public long timeIce;
    public long timeWind;
    public long timeFire;
    public boolean isBattleViewer = false;

    public Body() {

        this.setTiemNangSo(0);
        this.setKyNangSo(0);

        this.id = 0;
        this.head = -1;
        this.caiTrang = -1;
        this.speed = 4;
        this.nclass = 0;
        this.setExp(1L);
        this.expdown = 0L;
        this.pk = 0;
        this.setTypepk((short) 0);
        this.updatePpoint(0);
        this.setPotential0(15);
        this.setPotential1(5);
        this.setPotential2(5);
        this.setPotential3(5);
        this.setSpoint(0);
        this.isDie = false;
        this.setSkills(new ArrayList<>());
        this.KSkill = null;
        this.OSkill = null;
        this.setCSkill(-1);
        this.ItemBody = new Item[32];
        this.ItemMounts = null;
        this.CSkilldelay = 0L;
        this.mobMe = null;
        this.x = 0;
        this.y = 0;
        this.hp = 0;
        this.mp = 0;
        this.party = null;
        this.exptype = 1;
        this.veff = new CopyOnWriteArrayList<>();
        this.typeSolo = 0;
    }

    public boolean hasItemId(int id) {
        for (Item item : this.ItemBody) {
            if (item != null && item.id == id) {
                return true;
            }
        }
        for (Item itemMount : this.ItemMounts) {
            if (itemMount != null && itemMount.id == id) {
                return true;
            }
        }
        return false;
    }

    public void seNinja(final Ninja c) {
        this.c = c;
    }

    public short partHead() {
        if (this.caiTrang != -1) {
            return ItemDataId(this.c.ItemCaiTrang[this.caiTrang].id).part;
        }

        if (this.ItemBody[11] == null) {
            return this.c.head;
        }

        if (ItemBody[2] != null && (ItemBody[2].id == 795 || ItemBody[2].id == 796)) {
            return -1;
        }

        if (ItemBody[11].id >= 813 && ItemBody[11].id <= 818) {
            return -1;
        }

        if (this.ItemBody[11].id == 745) {
            return 264;
        }
        return ItemDataId(this.ItemBody[11].id).part;
    }

    public short Weapon() {
        if (this.ItemBody[1] != null) {
            return ItemDataId(this.ItemBody[1].id).part;
        }
        return -1;
    }

    public short partBody() {
        if (ItemData.isPartHead(this.partHead())) {
            return (short) (this.partHead() + 1);
        }
        if (this.ItemBody[2] != null) {
            if ((ItemBody[2].id == 795 || ItemBody[2].id == 796)) {
                return -1;
            }
            return ItemDataId(this.ItemBody[2].id).part;
        }
        return -1;
    }

    public short partLeg() {
        if (ItemData.isPartHead(this.partHead())) {
            return (short) (this.partHead() + 2);
        }
        if (this.ItemBody[6] != null) {

            if (ItemBody[2] != null && (ItemBody[2].id == 795 || ItemBody[2].id == 796)) {
                return -1;
            }

            return ItemDataId(this.ItemBody[6].id).part;
        }
        return -1;
    }

    public void updatePk(final int num) {
        this.pk += (byte) num;
        if (this.pk < 0) {
            this.pk = 0;
        } else if (this.pk > 20) {
            this.pk = 20;
        }
    }

    public int getMaxHP() {
        int hpmax = this.getPotential(2) * 10;
        hpmax += hpmax * (this.getPramItem(31) + this.getPramItem(61) + this.getPramSkill(17)) / 100;
        hpmax += this.getPramItem(6);
        hpmax += this.getPramItem(32);
        hpmax += this.getPramItem(77);
        hpmax += this.getPramItem(82);
        hpmax += this.getPramItem(82);
        hpmax += this.getPramItem(HP_TOI_DA_ID);

        if (getVeff().stream().anyMatch(v -> v.template.id == 29)) {
            hpmax += 1000;
        }
        if (this.hp > hpmax) {
            this.hp = hpmax;
        }
        return hpmax;
    }

    public synchronized void upHP(final int hpup) {
        if (this.isDie) {
            return;
        }
        this.hp += hpup;
        int maxHP = this.getMaxHP();

        if (this.hp > maxHP) {
            this.hp = maxHP;
        }

        if (this.hp <= 0) {
            this.isDie = true;
            this.hp = 0;
        }

    }

    public int getMaxMP() {
        int mpmax = this.getPotential(3) * 10;
        mpmax += mpmax * (this.getPramItem(28) + this.getPramItem(60) + this.getPramSkill(18)) / 100;
        mpmax += this.getPramItem(7);
        mpmax += this.getPramItem(19);
        mpmax += this.getPramItem(29);
        mpmax += this.getPramItem(83);
        mpmax += this.getPramItem(117);
        if (this.mp > mpmax) {
            this.mp = mpmax;
        }
        return mpmax;
    }

    public synchronized void upMP(final int mpup) {
        if (this.isDie) {
            return;
        }
        this.mp += mpup;
        if (this.mp > this.getMaxMP()) {
            this.mp = this.getMaxMP();
        } else if (this.mp < 0) {
            this.mp = 0;
        }
    }

    public int eff5buffHP() {
        int efHP = this.getPramItem(27);
        efHP += this.getPramItem(120);
        return efHP;
    }

    public int eff5buffMP() {
        int efMP = this.getPramItem(30);
        efMP += this.getPramItem(119);
        return efMP;
    }

    public int speed() {
        int sp = this.speed;
        sp = sp * (100 + this.getPramItem(16)) / 100;
        sp += this.getPramItem(93);
        return sp;
    }

    public int dameSide() {
        int percent = this.getPramSkill(11) + this.getPramItem(94);
        Effect eff = this.c.get().getEffId(25);
        if (eff != null) {
            percent += eff.param;
        }
        eff = this.c.get().getEffId(17);
        if (eff != null) {
            percent += eff.param;
        }
        eff = this.c.get().getEffId(19);
        if (eff != null) {
            percent += 80 + eff.param * 2;
        }
        int si;
        if (this.Side() == 1) {
            si = this.getPotential(3);
            si += si * (this.getPramSkill(1) + this.getPramItem(9) + percent) / 100;
            si += this.getPramItem(1);
        } else {
            si = this.getPotential(0);
            si += si * (this.getPramSkill(0) + this.getPramItem(8) + percent) / 100;
            si += this.getPramItem(0);
        }
        si += this.getPramItem(38);
        return si;
    }

    public int dameSys() {
        int ds = 0;
        if (this.sys() == 1) {
            ds = this.getPramSkill(2);
            ds += this.getPramItem(88);
            if (this.Side() == 1) {
                ds += this.getPramSkill(8);
                ds += this.getPramItem(22);
            } else {
                ds += this.getPramSkill(5);
                ds += this.getPramItem(21);
            }
        } else if (this.sys() == 2) {
            ds = this.getPramSkill(3);
            ds += this.getPramItem(89);
            if (this.Side() == 1) {
                ds += this.getPramSkill(9);
                ds += this.getPramItem(24);
            } else {
                ds += this.getPramSkill(6);
                ds += this.getPramItem(23);
            }
        } else if (this.sys() == 3) {
            ds = this.getPramSkill(4);
            ds += this.getPramItem(90);
            if (this.Side() == 1) {
                ds += this.getPramSkill(10);
                ds += this.getPramItem(26);
            } else {
                ds += this.getPramSkill(7);
                ds += this.getPramItem(25);
            }
        }
        return ds;
    }

    public int dameMax() {
        int dame = this.dameSide();
        dame += this.dameSys();
        dame += this.getPramItem(73);
        dame += this.getPramItem(74);
        dame += this.getPramItem(76);
        dame += this.getPramItem(87);
        dame += this.getPramItem(TAN_CONG_ID);

        if (dame < 0) {
            dame = 0;
        }
        return dame;
    }

    public int dameMin() {
        return this.dameMax() * 90 / 100;
    }

    public int dameDown() {
        int dwn = this.getPramItem(47);
        dwn += this.getPramItem(74);
        dwn += this.getPramItem(80);
        dwn += this.getPramItem(GIAM_TRU_ST_ID);
        return dwn;
    }

    public int ResFire() {
        int bear = this.getPramItem(2);
        bear += this.getPramItem(11);
        bear += this.getPramItem(33);
        bear += this.getPramItem(70);
        bear += this.getPramItem(96);
        bear += this.getPramItem(81);
        bear += this.getPramItem(20);
        bear += this.getPramItem(36);
        bear += this.getPramItem(KHANG_TAT_CA_ID);

        bear += this.getPramSkill(19);
        bear += this.getPramSkill(20);
        final Effect eff = this.c.get().getEffId(19);
        if (eff != null) {
            bear += eff.param;
        }
        val khang = c.get().getEffId(26);
        if (khang != null) {
            bear += 100;
        }
        return bear;
    }

    public int ResIce() {
        int bear = this.getPramItem(3);
        bear += this.getPramItem(12);
        bear += this.getPramItem(34);
        bear += this.getPramItem(71);
        bear += this.getPramItem(95);
        bear += this.getPramItem(81);
        bear += this.getPramItem(20);
        bear += this.getPramItem(36);
        bear += this.getPramItem(KHANG_TAT_CA_ID);

        bear += this.getPramSkill(19);
        bear += this.getPramSkill(21);
        final Effect eff = this.c.get().getEffId(19);
        if (eff != null) {
            bear += eff.param;
        }

        val khang = c.get().getEffId(26);
        if (khang != null) {
            bear += 100;
        }
        return bear;
    }

    public int ResWind() {
        int bear = this.getPramItem(4);
        bear += this.getPramItem(13);
        bear += this.getPramItem(35);
        bear += this.getPramItem(72);
        bear += this.getPramItem(97);
        bear += this.getPramItem(81);
        bear += this.getPramItem(20);
        bear += this.getPramItem(36);
        bear += this.getPramItem(KHANG_TAT_CA_ID);

        bear += this.getPramSkill(19);
        bear += this.getPramSkill(22);
        final Effect eff = this.c.get().getEffId(19);
        if (eff != null) {
            bear += eff.param;
        }
        val khang = c.get().getEffId(26);
        if (khang != null) {
            bear += 100;
        }
        return bear;
    }

    public int PramSkillTotal(int id) {
        int param = 0;
        try {

            for (short i = 0; i < this.getSkills().size(); i = (short) (i + 1)) {
                Skill skill = this.getSkills().get(i);
                if (skill.skillData().type == 0 || skill.skillData().type == 2
                        || skill.getTemplate().skillId == this.getCSkill()) {
                    byte j;

                    for (j = 0; j < skill.getTemplate().options.size(); j = (byte) (j + 1)) {
                        Option option = skill.getTemplate().options.get(j);
                        if (option.id == id)
                            param += option.param;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return param;
    }

    public int Exactly() {
        int exa = this.getPotential(1);
        exa += this.getPramItem(10);
        exa += this.getPramItem(18);
        exa += this.getPramItem(75);
        exa += this.getPramItem(86);
        exa += this.getPramItem(116);
        exa += this.getPramSkill(12);

        if (getVeff().stream().anyMatch(v -> v.template.id == 24)) {
            exa += 400;
        }
        return exa;
    }

    public int Miss() {
        int mi = this.getPotential(1) * 150 / 100;
        mi += this.getPramItem(5);
        mi += this.getPramItem(17);
        mi += this.getPramItem(62);
        mi += this.getPramItem(68);
        mi += this.getPramItem(78);
        mi += this.getPramItem(84);
        mi += this.getPramItem(115);
        mi += this.getPramSkill(13);
        mi += this.getPramSkill(31);
        final Effect eff = this.c.get().getEffId(11);
        if (eff != null) {
            mi += eff.param;
        }

        if (mi > Short.MAX_VALUE) {
            mi = Short.MAX_VALUE;
        }

        return mi;
    }

    public int Fatal() {
        int fat = this.getPramItem(14);
        fat += this.getPramItem(37);
        fat += this.getPramItem(69);
        fat += this.getPramItem(92);
        fat += this.getPramItem(114);
        fat += this.getPramSkill(14);
        return fat;
    }

    public int FantalDame() {
        int pfd = this.getPramItem(105);
        return pfd;
    }

    public int FatalDownPercent() {
        int p = this.getPramItem(46);
        p += this.getPramItem(79);
        p += this.getPramItem(KHANG_SAT_THUONG_CHI_MANG_ID);
        return p;
    }

    public int FantalDamePercent() {
        int pfd = this.getPramItem(39);
        pfd += this.getPramItem(67);
        pfd += this.getPramSkill(65);
        return pfd;
    }

    public int ReactDame() {
        int reactd = this.getPramItem(15);
        reactd += this.getPramItem(91);
        reactd += this.getPramItem(PHAN_DON_ID);
        return reactd;
    }

    public int sysUp() {
        final int su = 0;
        return su;
    }

    public int sysDown() {
        final int sd = 0;
        return sd;
    }

    public int percentFire2() {
        final int pf = this.getPramSkill(24);
        return pf;
    }

    public int percentFire4() {
        final int pf = this.getPramSkill(34);
        return pf;
    }

    public int percentIce1_5() {
        final int pi = this.getPramSkill(25);
        return pi;
    }

    public int percentIce2_3() {
        final int pramSkill = this.getPramSkill(35);
        return pramSkill;
    }

    public int percentIceKunai() {
        if (this.nclass == 3) {
            return getPramSkill(69);
        }
        return 0;
    }

    public int percentWind1() {
        final int pw = this.getPramSkill(26);
        return pw;
    }

    public int percentWind2() {
        final int pw = this.getPramSkill(36);
        return pw;
    }

    public int getPotential(final int i) {
        int potential = 0;
        if (i == 0) {
            potential = this.getPotential0();
        } else if (i == 1) {
            potential = this.getPotential1();
        } else if (i == 2) {
            potential = this.getPotential2();
        } else if (i == 3) {
            potential = this.getPotential3();
        }
        potential = potential * (100 + this.getPramItem(58)) / 100;
        potential += this.getPramItem(57);
        return potential;
    }

    public int getPramItem(final int id) {
        if (this.c.get() == null) {
            return 0;
        }
        int param = 0;
        if (this.c.get().ItemBody != null) {
            for (final Item item : this.c.get().ItemBody) {

                if (item != null) {
                    for (final Option option : item.option) {
                        if (option.id == id && !isUpgradeHide(option.id, item.getUpgrade())) {
                            param += option.param;
                        }
                    }
                    if (item.ngocs != null && item.ngocs.size() > 0) {
                        Option op = null;
                        if (item.getData().type == 1) {
                            op = VU_KHI_OPTION;
                        } else if (item.getData().isTrangSuc()) {
                            op = TRANG_SUC_OPTION;
                        } else if (item.getData().isTrangPhuc()) {
                            op = TRANG_BI_OPTION;
                        }

                        if (op != null) {
                            for (final Item ngoc : item.ngocs) {

                                int index = ngoc.option.indexOf(op);
                                if (index != -1) {
                                    if (index + 1 < ngoc.option.size() &&
                                            ngoc.option.get(index + 1).id == id) {
                                        param += ngoc.option.get(index + 1).param;
                                    } else if (index + 2 < ngoc.option.size() &&
                                            ngoc.option.get(index + 2).id == id) {
                                        param += ngoc.option.get(index + 2).param;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (final Item mounts : this.c.get().ItemMounts) {
            if (mounts != null) {
                for (final Option option : mounts.option) {
                    if (option.id == id) {
                        param += option.param;
                    }
                }
            }
        }

        if (this.caiTrang != -1 && this.isHuman) {
            for (Option option : this.c.ItemCaiTrang[this.caiTrang].option) {
                if (option.id == id) {
                    param += option.param;
                }
            }
        }
        return param;
    }

    public boolean s() {
        return (this.nclass == 2 || this.nclass == 4 || this.nclass == 6);
    }

    public int getPramSkill(final int id) {
        if (this.c.get() == null) {
            return 0;
        }
        int param = 0;
        val body = this;

        for (short i = 0; i < body.getSkills().size(); ++i) {
            final Skill sk = body.getSkills().get(i);
            final SkillData data = SkillData.Templates(sk.id);
            if (data.type == 0 || data.type == 2 || sk.id == body.getCSkill()) {
                final SkillTemplates temp = SkillData.Templates(sk.id, sk.point);
                for (int j = 0; j < temp.options.size(); ++j) {
                    final Option option = temp.options.get(j);
                    if (option.id == id) {
                        param += option.param;
                    }
                }
            }
        }
        return param;
    }

    @Nullable
    public Effect getEffId(final int effid) {
        for (byte i = 0; i < this.getVeff().size(); ++i) {
            if (effid == this.getVeff().get(i).template.id) {
                return this.getVeff().get(i);
            }
        }
        return null;
    }

    @Nullable
    public Effect getEffType(final byte efftype) {
        for (byte i = 0; i < this.getVeff().size(); ++i) {
            if (efftype == this.getVeff().get(i).template.type) {
                return this.getVeff().get(i);
            }
        }
        return null;
    }

    @NotNull
    private Map<@NotNull Integer, @NotNull Skill> cacheSkill = new ConcurrentHashMap<>();

    @Nullable
    public Skill getSkill(final int id) {

        if (cacheSkill.containsKey(id)) {
            return cacheSkill.get(id);
        }

        for (final Skill skl : this.getSkills()) {
            if (skl.id == id) {
                cacheSkill.put(id, skl);
                return skl;
            }
        }
        return null;
    }

    public void setLevel_Exp(final long exp) {
        final long[] levelExp = Level.getLevelExp(exp);
        this.setLevel((int) levelExp[0]);
    }

    public void upDie() {
        synchronized (this) {
            this.hp = 0;
            this.isDie = true;

            if (this instanceof Ninja && ((Ninja) this).getBattle() != null) {
                ((Ninja) this).getBattle().setState(Battle.BATTLE_END_STATE);
            }

            try {
                if (!this.c.isNhanban) {
                    this.c.getPlace().sendDie(this.c);
                }
            } catch (Exception ex) {
            }
        }
    }

    public int fullTL() {
        int tl = 0;
        boolean ad = false;
        for (byte i = 0; i < 10; ++i) {
            int tl2 = 0;
            final Item item = this.ItemBody[i];
            if (item == null) {
                return 0;
            }
            for (short j = 0; j < item.option.size(); ++j) {
                final Option op = item.option.get(j);
                if (op.id == 85) {
                    tl2 = op.param;
                    break;
                }
                if (j == item.option.size() - 1) {
                    return 0;
                }
            }
            if (!ad) {
                tl = tl2;
                ad = true;
            }
            if (tl > tl2) {
                tl = tl2;
            }
        }
        return tl;
    }

    public static int MIN_EFF_1 = 10;
    public static int MIN_EFF0 = 30;
    public static int MIN_EFF1 = 40;
    public static int MIN_EFF2 = 50;
    public static int MIN_EFF3 = 60;
    public static int MIN_EFF4 = 70;
    public static int MIN_EFF5 = 80;
    public static int MIN_EFF6 = 90;
    public static int MIN_EFF7 = 100;

    private int ngocEff = -1;
    public boolean effectFlag = true;

    public int getNgocEff() {
        if (ngocEff == -1) {
            int countPoint = 0;
            for (Item item : this.ItemBody) {
                if (item != null && item.ngocs != null) {
                    for (int i = 0; i < item.ngocs.size(); i++) {
                        countPoint += item.ngocs.get(i).getUpgrade();
                    }
                }
            }
            if (countPoint >= MIN_EFF_1 && countPoint < MIN_EFF1) {
                ngocEff = 0;
                return ngocEff;
            } else if (countPoint >= MIN_EFF1 && countPoint < MIN_EFF2) {
                ngocEff = 1;
                return 1;
            } else if (countPoint >= MIN_EFF2 && countPoint < MIN_EFF3) {
                ngocEff = 2;
                return 2;
            } else if (countPoint >= MIN_EFF3 && countPoint < MIN_EFF4) {
                ngocEff = 8;
                return 8;
            } else if (countPoint >= MIN_EFF4 && countPoint < MIN_EFF5) {
                ngocEff = 10;
                return 10;
            } else if (countPoint >= MIN_EFF5 && countPoint < MIN_EFF6) {
                ngocEff = 35;
                return 35;
            } else if (countPoint >= MIN_EFF6 && countPoint < MIN_EFF7) {
                ngocEff = 27;
                return 27;
            } else if (countPoint >= MIN_EFF7) {
                ngocEff = 25;
                return 25;
            }
            ngocEff = 0;
            return 0;
        } else {
            return ngocEff;
        }
    }

    public byte sys() {
        return GameScr.SysClass(this.nclass);
    }

    private byte Side() {
        return GameScr.SideClass(this.nclass);
    }

    @Override
    public void setSolo(ISolo solo) {
        this.solo = solo;
    }

    @SneakyThrows
    @Override
    public void requestSolo(final @Nullable ISoloer soloer) {
        if (soloer == null) {
            throw new RuntimeException("Soloer is null");
        }
        val another = (Body) soloer;
        val solo = new Solo();
        solo.setBody(this, (Body) soloer);

        val m = new Message(65);
        m.writer().writeInt(id);
        m.writer().flush();
        another.c.p.sendMessage(m);
        m.cleanup();
    }

    @Override
    public void acceptSolo() {
        if (this.solo != null) {
            this.solo.start();
        } else {
            throw new RuntimeException("Solo is null");
        }
    }

    @Override
    public void endSolo() {
        if (this.solo != null) {
            this.solo.endSolo();
        } else {
            throw new RuntimeException("Solo is null");
        }
    }

    public int getLevel() {
        if (level == null)
            return 1;
        return level.get();
    }

    public int getMaxLevel() {
        return this.getLevel() - this.getLevel() % 10 + 10;
    }

    public void setLevel(int level) {
        if (this.level == null) {
            this.level = new AtomicInteger(level);
        } else {
            if (level > Manager.MAX_LEVEL) {
                this.level.set(Manager.MAX_LEVEL);
            } else {
                this.level.set(level);
            }
        }
    }

    public short getPpoint() {
        return ppoint;
    }

    public synchronized void updatePpoint(int ppoint) {
        if (this.level == null || Level.totalpPoint(getLevel()) + 10 * (getTiemNangSo() + getBanghoa()) >= ppoint) {
            this.ppoint = (short) ppoint;
        }
    }

    public short getSpoint() {
        return spoint;
    }

    public synchronized void setSpoint(int spoint) {
        if (this.level == null || Level.totalsPoint(getLevel()) + getPhongLoi() + getKyNangSo() >= spoint) {
            this.spoint = (short) spoint;
        }
    }

    public short getPotential0() {
        return potential0;
    }

    public void setPotential0(int potential0) {
        this.potential0 = (short) potential0;
    }

    public short getPotential1() {
        return potential1;
    }

    public void setPotential1(int potential1) {
        this.potential1 = (short) potential1;
    }

    public int getPotential2() {
        return potential2;
    }

    public void setPotential2(int potential2) {
        this.potential2 = potential2;
    }

    public int getPotential3() {
        return potential3;
    }

    public void setPotential3(int potential3) {
        this.potential3 = potential3;
    }

    public int getTiemNangSo() {
        return tiemNangSo;
    }

    public synchronized void setTiemNangSo(int tiemNangSo) {
        this.tiemNangSo = tiemNangSo;
    }

    public int getKyNangSo() {
        return kyNangSo;
    }

    public synchronized void setKyNangSo(int kyNangSo) {
        this.kyNangSo = kyNangSo;
    }

    public int getBanghoa() {
        return banghoa;
    }

    public synchronized void setBanghoa(int banghoa) {
        this.banghoa = banghoa;
    }

    public int getPhongLoi() {
        return phongLoi;
    }

    public synchronized void setPhongLoi(int phongLoi) {
        this.phongLoi = phongLoi;
    }

    @NotNull
    public Skill getMyCSkillObject() {
        return getSkill(getCSkill());
    }

    public SkillData getCSkillData() {
        Skill skill = getMyCSkillObject();
        return SkillData.Templates(skill.id);
    }

    @NotNull
    public SkillTemplates getCSkillTemplate() {
        Skill skill = getMyCSkillObject();
        return SkillData.Templates(skill.id, skill.point);
    }

    public int PramItemTotal(int id) {
        int param = 0;
        try {
            byte i;
            for (i = 0; i < this.ItemBody.length; i = (byte) (i + 1)) {
                Item item = this.ItemBody[i];
                if (item != null) {
                    short j;
                    for (j = 0; j < item.option.size(); j = (short) (j + 1)) {
                        Option option = item.option.get(j);
                        if (option.id == id && !isUpgradeHide(option.id, item.getUpgrade())) {
                            param += option.param;
                        }
                    }
                }
            }
            for (i = 0; i < this.ItemMounts.length; i = (byte) (i + 1)) {
                Item item = this.ItemMounts[i];
                if (item != null) {
                    short j;
                    for (j = 0; j < item.option.size(); j = (short) (j + 1)) {
                        Option option = item.option.get(j);
                        if (option.id == id && !isUpgradeHide(option.id, item.getUpgrade())) {
                            param += option.param;
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return param;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Body body = (Body) o;
        return id == body.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @SneakyThrows
    public void damage(final @Nullable Ninja other) {
        if (other == null)
            return;
        int maxDame = this.dameMax();
        int dame = util.nextInt(90 * maxDame / 100, maxDame);

        int fire = this.PramSkillTotal(2) + this.PramItemTotal(88);
        int ice = this.PramSkillTotal(3) + this.PramItemTotal(89);
        int wind = this.PramSkillTotal(4) + this.PramItemTotal(90);

        val resFire = other.ResFire();
        fire -= (resFire > 0) ? resFire : 0;
        val resIce = other.ResIce();
        ice -= (resIce > 0) ? resIce : 0;
        val resWind = other.ResWind();
        wind -= (resWind > 0) ? resWind : 0;

        if (fire > 0) {
            dame += fire;
        }
        if (ice > 0) {
            dame += ice;
        }
        if (wind > 0) {
            dame += wind;
        }

        int fantal = this.Fatal();
        if (fantal > 750) {
            fantal = 750;
        }
        boolean flag = (util.nextInt(1, 1000) < fantal);

        int miss = other.Miss();

        if (other.nclass == Constants.CUNG && miss == Short.MAX_VALUE) {
            other.upHP(0);
            other.getPlace().attackNinja(0, other.id);
            return;
        }

        miss -= Exactly();
        if (miss > 7500) {
            miss = 7500;
        }
        boolean missLuck = util.nextInt(0, 10000) < miss;
        if (missLuck) {
            dame = 0;
        }

        BuNhin buNhin = null;
        if (other.nclass == Constants.KUNAI) {
            buNhin = other.getPlace().buNhins.stream().filter(b -> other.id == b.ninjaId).findFirst().orElse(null);
        }

        dame += this.getPramItem(ST_LEN_NGUOI_ID);

        if (flag) {

            int oldDame = dame;

            if (other.isFire) {
                dame = (int) (dame * 2.5);
            } else {
                dame = dame * 2;
            }

            dame += this.FantalDame();
            val upP = this.FantalDamePercent();
            val downP = other.FatalDownPercent();

            int percentFatal = (upP + 100 - downP);
            if (percentFatal <= 0) {
                percentFatal = 0;
            }

            dame = dame * percentFatal / 100;
            dame = dame * PERCENT_DAME_PEOPLE / 100;
            if (this.isNhanban) {
                dame /= 4;
            }
            if (dame >= other.getMaxHP()) {
                dame = other.getMaxHP() * 90 / 100;
            }

            if (other.nclass == Constants.KUNAI && buNhin != null) {
                buNhin.upHP(-dame);
            } else {
                other.upHP(-dame);
                other.getPlace().sendFatalMessage(dame, other);
            }

        } else {
            if (other.isFire) {
                dame *= 2;
            }
            dame = dame * PERCENT_DAME_PEOPLE / 100;

            if (this.isNhanban) {
                dame /= 4;
            }
            if (other.nclass == Constants.KUNAI && buNhin != null) {
                buNhin.upHP(-dame);
            } else {
                other.upHP(-dame);
            }
        }

        if (!missLuck) {
            this.upHP(-other.ReactDame());
        }
        other.getPlace().attackNinja(dame, other.id);
        MessageSubCommand.sendHP(this, other.getPlace().getUsers());
        MessageSubCommand.sendHP(other, other.getPlace().getUsers());

    }

    public long getExp() {
        return exp.get();
    }

    public void setExp(long newExp) {
        long upExp = newExp;
        if (upExp > Level.getMaxExp(Manager.MAX_LEVEL + 1) - 1) {
            upExp = Level.getMaxExp(Manager.MAX_LEVEL + 1) - 1;
        }
        if (this.exp != null) {
            this.exp.set(upExp);
        } else {
            this.exp = new AtomicLong(upExp);
        }
    }

    public List<Effect> getVeff() {
        return veff;
    }

    public void addEffect(Effect effect) {
        this.veff.add(effect);
        if (this.nclass == Constants.QUAT) {
            val expireDurationFire = getPramItem(40);
            this.addReduceEffect(new ReduceTimeSkillEffect(FIRE, expireDurationFire, 3));
            val expiredDurationIce = getPramItem(41);
            this.addReduceEffect(new ReduceTimeSkillEffect(ICE, expiredDurationIce, 2));
            val expiredDurationWind = getPramItem(42);
            this.addReduceEffect(new ReduceTimeSkillEffect(WIND, expiredDurationWind, 1));
        } else {
            val expireDurationFire = getPramItem(47);
            this.addReduceEffect(new ReduceTimeSkillEffect(FIRE, expireDurationFire, 2));
            val expiredDurationIce = getPramItem(54);
            this.addReduceEffect(new ReduceTimeSkillEffect(ICE, expiredDurationIce, 1));
        }
    }

    public long getFireReduceTime() {
        try {
            return this.reduceTimeSkillEffects.get(FIRE) != null && !this.reduceTimeSkillEffects.get(FIRE).expired()
                    ? this.reduceTimeSkillEffects.get(FIRE).reduceTime
                    : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public long getIceReduceTime() {
        try {
            return this.reduceTimeSkillEffects.get(ICE) != null && !this.reduceTimeSkillEffects.get(ICE).expired()
                    ? this.reduceTimeSkillEffects.get(ICE).reduceTime
                    : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public long getWindReduceTime() {
        try {
            return this.reduceTimeSkillEffects.get(WIND) != null && !this.reduceTimeSkillEffects.get(WIND).expired()
                    ? this.reduceTimeSkillEffects.get(WIND).reduceTime
                    : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public void addReduceEffect(final @NotNull ReduceTimeSkillEffect reduceTimeSkillEffect) {
        this.reduceTimeSkillEffects.put(reduceTimeSkillEffect.type, reduceTimeSkillEffect);
    }

    public void remove(Effect eff) {
        this.veff.remove(eff);
    }

    public void setNClass(byte nClass) {
        this.nclass = nClass;
    }

    public byte getNClass() {
        return this.nclass;
    }

    public void resetOSkill() {
        this.OSkill = new byte[] { -1, -1, -1, -1, -1 };
    }

    public void resetKSkill() {
        this.KSkill = new byte[] { -1, -1, -1 };
    }

    public short getCSkill() {
        if (nclass == 0) {
            return 0;
        }
        return CSkill;
    }

    public void setCSkill(int cSkill) {

        this.CSkill = (short) cSkill;
    }

    public List<Skill> getSkills() {
        if (this.nclass == 0) {
            if (skills.size() == 0 || skills.size() > 1) {
                this.skills.clear();
                this.skills.add(new Skill(0));
            }
        }
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public boolean canUseSkill() {
        final Skill skill = this.getMyCSkillObject();
        if (skill == null) {
            return false;
        }

        final SkillTemplates data = SkillData.Templates(skill.id, skill.point);
        if (skill.coolDown > System.currentTimeMillis()) {
            util.Debug("Kỹ năng chưa hồi.");
            return false;
        }

        skill.coolDown = System.currentTimeMillis() + data.coolDown;
        return true;
    }

    public short getTypepk() {
        return typepk;
    }

    public void setTypepk(short typepk) {
        this.typepk = typepk;
    }

    public synchronized void updateExp(long xpup) {
        final long xpold = this.getExp();

        if (xpold >= Level.getMaxExp(Manager.MAX_LEVEL + 1) - 1) {
            xpup = 0;
        }

        this.setExp(this.getExp() + xpup);

        final int oldLv = this.getLevel();
        this.setLevel_Exp(this.getExp());

        if (this.getLevel() > Manager.MAX_LEVEL) {
            this.setLevel(Manager.MAX_LEVEL);
            this.setExp(xpold);
            // xpup = 0;
        }

        if (oldLv < this.getLevel()) {
            if (this.nclass != 0) {
                for (int i = oldLv + 1; i <= this.getLevel(); ++i) {
                    this.updatePpoint(this.getPpoint() + Level.getLevel(i).ppoint);
                    this.setSpoint(this.getSpoint() + Level.getLevel(i).spoint);
                }
            } else {
                for (int i = oldLv + 1; i <= this.getLevel(); ++i) {
                    this.setPotential0(this.getPotential0() + 5);
                    this.setPotential1(this.getPotential1() + 2);
                    this.setPotential2(this.getPotential2() + 2);
                    this.setPotential3(this.getPotential3() + 2);
                }
            }
        }

    }

    public boolean canUseVukhi() {
        if (this.ItemBody[1] == null) {
            return false;
        }
        int itemNClass = ItemDataId(this.ItemBody[1].id).nclass;
        if (itemNClass == Constants.KIEM && this.nclass == Constants.CHUA_VAO_LOP) {
            return true;
        }
        if (itemNClass != this.nclass) {
            util.Debug("Vũ khí không thích hợp");
            return false;
        }

        return true;
    }

    public boolean canUseBikip() {
        if (this.ItemBody[15] == null) {
            return true;
        }

        int itemNClass = ItemDataId(this.ItemBody[15].id).nclass;
        if (itemNClass != this.nclass) {
            util.Debug("Bí kíp không phù hợp");
            return false;
        }

        return true;

    }

    public boolean canAttack() {
        if (!this.canUseSkill()) {
            return false;
        }

        return this.canAttackWithNoSkill();
    }

    public boolean canAttackWithNoSkill() {
        User p = this.c.p;
        if (!this.canUseVukhi()) {
            p.sendYellowMessage("Vũ khí không hợp lệ");
            return false;
        }

        if (!this.canUseBikip()) {
            p.sendYellowMessage("Bí kíp không hợp lệ");
            return false;
        }

        if (this.isIce || this.isWind) {
            util.Debug("Choáng hoặc đóng băng");
            return false;
        }

        return true;
    }
}
