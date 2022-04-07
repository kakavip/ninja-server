package threading;

import boardGame.Place;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import battlewithmaster.BattleWithMasterPlace;
import clan.ClanTerritory;
import real.*;
import server.GameScr;
import server.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static interfaces.IBattle.*;

public class Map extends Thread {

    public int id;
    public long timeMap;
    protected boolean runing;
    public volatile long lastTimeActive;
    public boolean isEndOfSchoolMap;

    public float multi = 1.0f;

    @NotNull
    public static final int[] arrLang;
    @NotNull
    public static final int[] arrLangCo;
    @NotNull
    public static final int[] arrTruong;
    @NotNull
    public MapTemplate template;
    @NotNull
    public Place[] area;
    @Nullable
    public Cave cave;
    @NotNull
    protected java.util.Map<Integer, Integer> levelToMobId;
    @NotNull
    // 0: la id, 1: level
    private int[] taThu = new int[] { -1, -1 };

    private static int AREA_BOSS_VIP = 17;

    protected Map() {
    }

    public Map(final int id, final Cave cave) {
        this(id, cave, 1);
    }

    public Map(final int id, final Cave cave, final float multi) {
        this(id, cave, MapTemplate.arrTemplate[id].numarea, multi);
    }

    private Map(final int id, final Cave cave, final int maxArea, final float multi) {
        this.timeMap = -1L;
        this.id = id;
        this.template = MapTemplate.arrTemplate[id];
        this.cave = cave;
        this.area = new Place[MapTemplate.arrTemplate[id].numarea];
        this.levelToMobId = new HashMap<>();

        for (byte i = 0; i < maxArea; ++i) {
            if (isNvMap()) {
                // TODO Replace
                this.area[i] = new BattleWithMasterPlace(this, i);
            } else {
                this.area[i] = new Place(this, i);
            }
            if (id == CAN_CU_DIA_BACH ||
                    id == CAN_CU_DIA_HAC ||
                    id == BACH_DAI_ID ||
                    id == HAC_DAI_ID ||
                    id == HANH_LANG_TREN ||
                    id == HANH_LANG_DUOI ||
                    id == HANH_LANG_GIUA) {
                this.area[i].battle = Server.getInstance().globalBattle;
            }
        }
        setName(template.name);

        this.isEndOfSchoolMap = false;
        for (int i = 0; i < Server.endMaps.length; i++) {
            if (this.id == Server.endMaps[i]) {
                this.isEndOfSchoolMap = true;
                break;
            }
        }

        this.loadMapFromResource();
        this.loadMap(this.template.id);

        this.multi = multi;
        this.initMob();
        this.runing = true;
        lastTimeActive = System.currentTimeMillis();

        this.start();
    }

    public static boolean isCaveMap(int id) {
        return (id >= 114 && id <= 116) ||
                (id >= 91 && id <= 97) ||
                (id >= 105 && id <= 109) ||
                (id >= 125 && id <= 128) ||
                (id >= 157 && id <= 159);
    }

    public static boolean isNPCNear(Ninja ninja, short npcTemplateId) {
        Map tileMap = ninja.getPlace().map;

        for (byte i = 0; i < tileMap.template.npc.length; i = (byte) (i + 1)) {
            Npc npc = tileMap.template.npc[i];
            if (npc != null && npc.id == npcTemplateId && Math.abs(ninja.x - npc.x) <= 60
                    && Math.abs(ninja.y - npc.y) <= 60) {
                return true;
            }
        }
        return false;
    }

    public boolean isLdgtMap() {
        return id >= 80 && id <= 90;
    }

    public boolean isChienTruongKeo() {
        return id >= 130 && id <= 133;
    }

    public boolean isGtcMap() {
        return id >= 118 && id <= 124;
    }

    public boolean canDoNvhn() {
        return !(this.isLangCo() || this.VDMQ() || this.isLdgtMap() || Map.isCaveMap(this.id) || this.isChienTruongKeo()
                || this.isGtcMap() || this.loiDaiMap());
    }

    public void initMob() {
        for (byte j = 0; j < this.area.length; ++j) {
            this.area[j].getMobs().clear();
            int k = 0;
            for (short i = 0; i < this.template.arMobid.length; ++i) {

                final Mob m = new Mob(k, this.template.arMobid[i], this.template.arrMoblevel[i]);
                m.x = this.template.arrMobx[i];
                m.y = this.template.arrMoby[i];
                m.status = this.template.arrMobstatus[i];
                m.lvboss = this.template.arrLevelboss[i];
                if (m.lvboss == 3) {
                    if (j % 5 == 0) {
                        final int n = m.hpmax * 200;
                        m.hpmax = n;
                        m.hp = n;
                        this.taThu[0] = m.templates.id;
                        this.taThu[1] = m.level;
                    } else {
                        m.lvboss = 0;
                    }
                } else if (m.lvboss == 2) {
                    final int n2 = m.hpmax * 100;
                    m.hpmax = n2;
                    m.hp = n2;
                } else if (m.lvboss == 1) {
                    final int n3 = m.hpmax * 10;
                    m.hpmax = n3;
                    m.hp = n3;
                }

                m.hpmax = (int) (m.hpmax * this.multi);
                m.hp = (int) (m.hp * this.multi);

                if (isLdgtMap()) {
                    m.isRefresh = false;
                }
                if (isLdgtMap()) {
                    m.updateHP(-m.hpmax);
                }

                m.setIsboss(this.template.arrisboss[i]);
                this.area[j].getMobs().add(m);

                if (!m.isIsboss()) {
                    this.levelToMobId.put(m.level, m.templates.id);
                }
                ++k;
            }
        }

        refreshBossVIP();
    }

    public void refreshBossVIP() {
        if (this.isEndOfSchoolMap) {
            this.refreshBoss(Map.AREA_BOSS_VIP);
        }
    }

    public void refreshBossVIPTimeout() {
        if (!this.isEndOfSchoolMap) {
            return;
        }

        final Place place = this.area[Map.AREA_BOSS_VIP];
        for (short j = 0; j < place.getMobs().size(); ++j) {
            final Mob mob = place.getMobs().get(j);
            if (mob.status == 0 && mob.isDie && mob.isIsboss()) {
                mob.setTimeRefresh(System.currentTimeMillis() + Manager.TIME_REFRESH_BOSS);
                mob.isRefresh = true;
            }
        }

    }

    public Integer getMobRandomMobId() {
        val mobIds = new ArrayList<>(this.levelToMobId.values());
        return mobIds.get(util.nextInt(mobIds.size()));
    }

    public Integer getMobIdByLevel(int level) {
        if (level >= 100) {
            level = 100;
        }

        if (this.levelToMobId.containsKey(level)) {
            return this.levelToMobId.get(level);
        }

        return -1;
    }

    public void refreshBoss(final int area) {
        if (area >= this.area.length) {
            return;
        }

        final Place place = this.area[area];
        for (short j = 0; j < place.getMobs().size(); ++j) {
            final Mob mob = place.getMobs().get(j);
            if (mob.status == 0 && mob.isIsboss()) {
                place.refreshMob(mob.id);
                util.Debug(mob.templates.name);
            }
        }
    }

    public int getXHD() {
        if (this.id == 157 || this.id == 158 || this.id == 159) {
            return 9;
        }
        if (this.id == 125 || this.id == 126 || this.id == 127 || this.id == 128) {
            return 7;
        }
        if (this.id == 114 || this.id == 115 || this.id == 116) {
            return 6;
        }
        if (this.id == 105 || this.id == 106 || this.id == 107 || this.id == 108 || this.id == 109) {
            return 5;
        }
        if (this.id == 94 || this.id == 95 || this.id == 96 || this.id == 97) {
            return 4;
        }
        if (this.id == 91 || this.id == 92 || this.id == 93) {
            return 3;
        }
        return -1;
    }

    public boolean isLangCo() {
        return (this.id >= 134 && this.id <= 138);
    }

    public boolean loiDaiMap() {
        return this.id == 111 || this.id == 110;
    }

    public boolean VDMQ() {
        return this.id >= 139 && this.id <= 148;
    }

    public int getMobLdgtId() {
        int _id = template.id;

        if (_id == 81) {
            return 80;
        } else if (_id == 82) {
            return 77;
        } else if (_id == 83) {
            return 76;
        } else if (_id == 84) {
            return 72;
        } else if (_id == 85) {
            return 74;
        } else if (_id == 86) {
            return 79;
        } else if (_id == 87) {
            return 73;
        } else if (_id == 88) {
            return 78;
        } else if (_id == 89) {
            return 75;
        } else if (_id == 90) {
            return 116;
        }
        return -1;
    }

    private final java.util.Map<Integer, Integer> errorCount = new HashMap<>();

    @Override
    public void run() {

        while (this.runing) {
            try {
                lastTimeActive = System.currentTimeMillis();
                for (byte i = 0; i < this.area.length; ++i) {
                    try {
                        this.area[i].update();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Integer pre = errorCount.get((int) i);
                        if (pre == null) {
                            pre = 0;
                        }
                        errorCount.put((int) i, pre + 1);

                        if (pre + 1 >= 3000) {
                            this.area[i].terminate();
                            this.area[i] = createArea(i);
                            errorCount.put((int) i, 0);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                final long timeUpdateElapsed = System.currentTimeMillis() - lastTimeActive;
                if (timeUpdateElapsed < 1000) {
                    try {
                        Thread.sleep(1000 - timeUpdateElapsed);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
    }

    private Place createArea(byte index) {
        return new Place(this, index);
    }

    public boolean isNvMap() {
        return id == 56 || id == 73 || id == 0;
    }

    @Nullable
    public Place getFreeArea() {
        for (int i = 0; i < this.area.length; i++) {
            if (this.area[i].getNumplayers() == 0) {
                if (isLdgtMap()) {
                    if (ClanTerritory.checkPlaceOccupied(this.area[i])) {
                        continue;
                    } else {
                        val mobId = getMobLdgtId();
                        if (mobId != -1) {
                            int finalI = i;
                            this.area[i].getMobs().stream()
                                    .filter(m -> m.templates.id == mobId)
                                    .forEach(m -> {
                                        this.area[finalI].refreshMob(m.id);
                                    });
                            this.area[finalI].reset();
                        }

                        return this.area[i];
                    }
                }
                if (this.area[i].getCandyBattle() == null) {
                    return this.area[i];
                }
            }
        }
        return null;
    }

    public static boolean notCombat(int mapId) {
        return (mapId == 1 || mapId == 10 || mapId == 17 || mapId == 22 || mapId == 27 || mapId == 32 || mapId == 38
                || mapId == 43 || mapId == 48 || mapId == 72);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Map map = (Map) o;
        return id == map.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void close() {
        this.runing = false;
        for (byte i = 0; i < this.area.length; ++i) {
            this.area[i].close();
        }
        this.interrupt();
    }

    static {
        arrLang = new int[] { 10, 17, 22, 32, 38, 43, 48 };
        arrLangCo = new int[] { 138 };
        arrTruong = new int[] { 1, 27, 72 };
    }

    public boolean canPkDosat() {
        for (int i = 0; i < Map.arrLang.length; i++) {
            if (Map.arrLang[i] == this.id) {
                return false;
            }
        }

        for (int i = 0; i < Map.arrLangCo.length; i++) {
            if (Map.arrLang[i] == this.id) {
                return false;
            }
        }

        for (int i = 0; i < Map.arrTruong.length; i++) {
            if (Map.arrLang[i] == this.id) {
                return false;
            }
        }
        return true;
    }

    public int getMobLevel3(int level) {
        if (taThu[0] == -1 || taThu[1] == -1)
            return -1;
        if (level > 100) {
            level = 100;
        }
        if (taThu[1] >= level - 5 && taThu[1] <= level + 5) {
            return taThu[0];
        }
        return -1;
    }

    public boolean hasBossVIPIsLive() {
        if (this.isEndOfSchoolMap) {
            final Place place = this.area[Map.AREA_BOSS_VIP];
            for (short j = 0; j < place.getMobs().size(); ++j) {
                final Mob mob = place.getMobs().get(j);
                if (mob.status == 0 && mob.isDie && mob.isIsboss()) {
                    return false;
                }
            }
        }

        return true;
    }

    public int ushort(short s) {
        return s & 0xFFFF;
    }

    public void loadMapFromResource() {
        // if (this.id == 0 || this.id == 56 || (this.id > 72 && this.id < 125) ||
        // (this.id > 125 && this.id < 133) ||
        // (this.id > 133 && this.id < 139) || this.id > 148) {
        // return;
        // }
        ByteArrayInputStream bai = null;
        DataInputStream dis = null;
        try {
            byte[] ab = GameScr.loadFile("res/map/" + this.id).toByteArray();
            // System.out.println("MAP " + this.id + " DATA: " + Arrays.toString(ab));

            bai = new ByteArrayInputStream(ab);
            dis = new DataInputStream(bai);
            this.template.tmw = this.ushort((short) dis.read());
            this.template.tmh = this.ushort((short) dis.read());
            this.template.maps = new char[dis.available()];
            int i;
            for (i = 0; i < this.template.tmw * this.template.tmh; i++)
                this.template.maps[i] = (char) dis.readByte();
            this.template.types = new int[this.template.maps.length];
            dis.close();
            bai.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(int tileId) {
        // this.template.types = new int[this.template.tmw * this.template.tmh];
        this.template.pxh = (short) (this.template.tmh * 24);
        this.template.pxw = (short) (this.template.tmw * 24);
        try {
            int i;
            for (i = 0; i < this.template.tmh * this.template.tmw; ++i) {
                if (tileId == 4) {
                    if (this.template.maps[i] == 1 || this.template.maps[i] == 2 || this.template.maps[i] == 3
                            || this.template.maps[i] == 4 || this.template.maps[i] == 5 || this.template.maps[i] == 6
                            || this.template.maps[i] == 9 || this.template.maps[i] == 10 || this.template.maps[i] == 79
                            || this.template.maps[i] == 80 || this.template.maps[i] == 13 || this.template.maps[i] == 14
                            || this.template.maps[i] == 43 || this.template.maps[i] == 44 || this.template.maps[i] == 45
                            || this.template.maps[i] == 50) {
                        this.template.types[i] |= MapTemplate.T_TOP;
                    }
                    if (this.template.maps[i] == 9 || this.template.maps[i] == 11) {
                        this.template.types[i] |= MapTemplate.T_LEFT;
                    }
                    if (this.template.maps[i] == 10 || this.template.maps[i] == 12) {
                        this.template.types[i] |= MapTemplate.T_RIGHT;
                    }
                    if (this.template.maps[i] == 13 || this.template.maps[i] == 14) {
                        this.template.types[i] |= MapTemplate.T_BRIDGE;
                    }
                    if (this.template.maps[i] == 76 || this.template.maps[i] == 77) {
                        this.template.types[i] |= MapTemplate.T_WATERFLOW;
                        if (this.template.maps[i] == 78) {
                            this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                        }
                    }
                } else if (tileId == 1) {
                    if (this.template.maps[i] == 1 || this.template.maps[i] == 2 || this.template.maps[i] == 3
                            || this.template.maps[i] == 4 || this.template.maps[i] == 5 || this.template.maps[i] == 6
                            || this.template.maps[i] == 7 || this.template.maps[i] == 36 || this.template.maps[i] == 37
                            || this.template.maps[i] == 54 || this.template.maps[i] == 91 || this.template.maps[i] == 92
                            || this.template.maps[i] == 93 || this.template.maps[i] == 94 || this.template.maps[i] == 73
                            || this.template.maps[i] == 74 || this.template.maps[i] == 97 || this.template.maps[i] == 98
                            || this.template.maps[i] == 116 || this.template.maps[i] == 117
                            || this.template.maps[i] == 118 || this.template.maps[i] == 120
                            || this.template.maps[i] == 61) {
                        this.template.types[i] |= MapTemplate.T_TOP;
                    }
                    if (this.template.maps[i] == 2 || this.template.maps[i] == 3 || this.template.maps[i] == 4
                            || this.template.maps[i] == 5 || this.template.maps[i] == 6 || this.template.maps[i] == 20
                            || this.template.maps[i] == 21 || this.template.maps[i] == 22 || this.template.maps[i] == 23
                            || this.template.maps[i] == 36 || this.template.maps[i] == 37 || this.template.maps[i] == 38
                            || this.template.maps[i] == 39 || this.template.maps[i] == 61) {
                        this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                    }
                    if (this.template.maps[i] == 8 || this.template.maps[i] == 9 || this.template.maps[i] == 10
                            || this.template.maps[i] == 12 || this.template.maps[i] == 13 || this.template.maps[i] == 14
                            || this.template.maps[i] == 30) {
                        this.template.types[i] |= MapTemplate.T_TREE;
                    }
                    if (this.template.maps[i] == 17) {
                        this.template.types[i] |= MapTemplate.T_WATERFALL;
                    }
                    if (this.template.maps[i] == 18) {
                        this.template.types[i] |= MapTemplate.T_TOPFALL;
                    }
                    if (this.template.maps[i] == 37 || this.template.maps[i] == 38 || this.template.maps[i] == 61) {
                        this.template.types[i] |= MapTemplate.T_LEFT;
                    }
                    if (this.template.maps[i] == 36 || this.template.maps[i] == 39 || this.template.maps[i] == 61) {
                        this.template.types[i] |= MapTemplate.T_RIGHT;
                    }
                    if (this.template.maps[i] == 19) {
                        this.template.types[i] |= MapTemplate.T_WATERFLOW;
                        if ((this.template.types[i - this.template.tmw]
                                & MapTemplate.T_SOLIDGROUND) == MapTemplate.T_SOLIDGROUND) {
                            this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                        }
                    }
                    if (this.template.maps[i] == 35) {
                        this.template.types[i] |= MapTemplate.T_UNDERWATER;
                    }
                    if (this.template.maps[i] == 7) {
                        this.template.types[i] |= MapTemplate.T_BRIDGE;
                    }
                    if (this.template.maps[i] == 32 || this.template.maps[i] == 33 || this.template.maps[i] == 34) {
                        this.template.types[i] |= MapTemplate.T_OUTSIDE;
                    }
                } else if (tileId == 2) {
                    if (this.template.maps[i] == 1 || this.template.maps[i] == 2 || this.template.maps[i] == 3
                            || this.template.maps[i] == 4 || this.template.maps[i] == 5 || this.template.maps[i] == 6
                            || this.template.maps[i] == 7 || this.template.maps[i] == 36 || this.template.maps[i] == 37
                            || this.template.maps[i] == 54 || this.template.maps[i] == 61 || this.template.maps[i] == 73
                            || this.template.maps[i] == 76 || this.template.maps[i] == 77 || this.template.maps[i] == 78
                            || this.template.maps[i] == 79 || this.template.maps[i] == 82 || this.template.maps[i] == 83
                            || this.template.maps[i] == 98 || this.template.maps[i] == 99
                            || this.template.maps[i] == 100 || this.template.maps[i] == 102
                            || this.template.maps[i] == 103 || this.template.maps[i] == 108
                            || this.template.maps[i] == 109 || this.template.maps[i] == 110
                            || this.template.maps[i] == 112 || this.template.maps[i] == 113
                            || this.template.maps[i] == 116 || this.template.maps[i] == 117
                            || this.template.maps[i] == 125 || this.template.maps[i] == 126
                            || this.template.maps[i] == 127 || this.template.maps[i] == 129
                            || this.template.maps[i] == 130) {
                        this.template.types[i] |= MapTemplate.T_TOP;
                    }
                    if (this.template.maps[i] == 1 || this.template.maps[i] == 3 || this.template.maps[i] == 4
                            || this.template.maps[i] == 5 || this.template.maps[i] == 6 || this.template.maps[i] == 20
                            || this.template.maps[i] == 21 || this.template.maps[i] == 22 || this.template.maps[i] == 23
                            || this.template.maps[i] == 36 || this.template.maps[i] == 37 || this.template.maps[i] == 38
                            || this.template.maps[i] == 39 || this.template.maps[i] == 55
                            || this.template.maps[i] == 109 || this.template.maps[i] == 111
                            || this.template.maps[i] == 112 || this.template.maps[i] == 113
                            || this.template.maps[i] == 114 || this.template.maps[i] == 115
                            || this.template.maps[i] == 116 || this.template.maps[i] == 127
                            || this.template.maps[i] == 129 || this.template.maps[i] == 130) {
                        this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                    }
                    if (this.template.maps[i] == 8 || this.template.maps[i] == 9 || this.template.maps[i] == 10
                            || this.template.maps[i] == 12 || this.template.maps[i] == 13 || this.template.maps[i] == 14
                            || this.template.maps[i] == 30 || this.template.maps[i] == 135) {
                        this.template.types[i] |= MapTemplate.T_TREE;
                    }
                    if (this.template.maps[i] == 17) {
                        this.template.types[i] |= MapTemplate.T_WATERFALL;
                    }
                    if (this.template.maps[i] == 18) {
                        this.template.types[i] |= MapTemplate.T_TOPFALL;
                    }
                    if (this.template.maps[i] == 61 || this.template.maps[i] == 37 || this.template.maps[i] == 38
                            || this.template.maps[i] == 127 || this.template.maps[i] == 130
                            || this.template.maps[i] == 131) {
                        this.template.types[i] |= MapTemplate.T_LEFT;
                    }
                    if (this.template.maps[i] == 61 || this.template.maps[i] == 36 || this.template.maps[i] == 39
                            || this.template.maps[i] == 127 || this.template.maps[i] == 129
                            || this.template.maps[i] == 132) {
                        this.template.types[i] |= MapTemplate.T_RIGHT;
                    }
                    if (this.template.maps[i] == 19) {
                        this.template.types[i] |= MapTemplate.T_WATERFLOW;
                        if ((this.template.types[i - this.template.tmw]
                                & MapTemplate.T_SOLIDGROUND) == MapTemplate.T_SOLIDGROUND) {
                            this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                        }
                    }
                    if (this.template.maps[i] == 134) {
                        this.template.types[i] |= MapTemplate.T_WATERFLOW;
                        if ((this.template.types[i - this.template.tmw]
                                & MapTemplate.T_SOLIDGROUND) == MapTemplate.T_SOLIDGROUND) {
                            this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                        }
                    }
                    if (this.template.maps[i] == 35) {
                        this.template.types[i] |= MapTemplate.T_UNDERWATER;
                    }
                    if (this.template.maps[i] == 7) {
                        this.template.types[i] |= MapTemplate.T_BRIDGE;
                    }
                    if (this.template.maps[i] == 32 || this.template.maps[i] == 33 || this.template.maps[i] == 34) {
                        this.template.types[i] |= MapTemplate.T_OUTSIDE;
                    }
                    if (this.template.maps[i] == 61 || this.template.maps[i] == 127) {
                        this.template.types[i] |= MapTemplate.T_BOTTOM;
                    }
                } else if (tileId == 3) {
                    if (this.template.maps[i] == 1 || this.template.maps[i] == 2 || this.template.maps[i] == 3
                            || this.template.maps[i] == 4 || this.template.maps[i] == 5 || this.template.maps[i] == 6
                            || this.template.maps[i] == 7 || this.template.maps[i] == 11 || this.template.maps[i] == 14
                            || this.template.maps[i] == 17 || this.template.maps[i] == 43 || this.template.maps[i] == 51
                            || this.template.maps[i] == 63 || this.template.maps[i] == 65 || this.template.maps[i] == 67
                            || this.template.maps[i] == 68 || this.template.maps[i] == 71 || this.template.maps[i] == 72
                            || this.template.maps[i] == 83 || this.template.maps[i] == 84 || this.template.maps[i] == 85
                            || this.template.maps[i] == 87 || this.template.maps[i] == 91 || this.template.maps[i] == 94
                            || this.template.maps[i] == 97 || this.template.maps[i] == 98
                            || this.template.maps[i] == 106 || this.template.maps[i] == 107
                            || this.template.maps[i] == 111 || this.template.maps[i] == 113
                            || this.template.maps[i] == 117 || this.template.maps[i] == 118
                            || this.template.maps[i] == 119 || this.template.maps[i] == 125
                            || this.template.maps[i] == 126 || this.template.maps[i] == 129
                            || this.template.maps[i] == 130 || this.template.maps[i] == 131
                            || this.template.maps[i] == 133 || this.template.maps[i] == 136
                            || this.template.maps[i] == 138 || this.template.maps[i] == 139
                            || this.template.maps[i] == 142) {
                        this.template.types[i] |= MapTemplate.T_TOP;
                    }
                    if (this.template.maps[i] == 124 || this.template.maps[i] == 116 || this.template.maps[i] == 123
                            || this.template.maps[i] == 44 || this.template.maps[i] == 12 || this.template.maps[i] == 15
                            || this.template.maps[i] == 15 || this.template.maps[i] == 45 || this.template.maps[i] == 10
                            || this.template.maps[i] == 9) {
                        this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                    }
                    if (this.template.maps[i] == 23) {
                        this.template.types[i] |= MapTemplate.T_WATERFALL;
                    }
                    if (this.template.maps[i] == 24) {
                        this.template.types[i] |= MapTemplate.T_TOPFALL;
                    }
                    if (this.template.maps[i] == 6 || this.template.maps[i] == 15 || this.template.maps[i] == 51
                            || this.template.maps[i] == 95 || this.template.maps[i] == 97
                            || this.template.maps[i] == 106 || this.template.maps[i] == 111
                            || this.template.maps[i] == 123 || this.template.maps[i] == 125
                            || this.template.maps[i] == 138 || this.template.maps[i] == 140) {
                        this.template.types[i] |= MapTemplate.T_LEFT;
                    }
                    if (this.template.maps[i] == 7 || this.template.maps[i] == 16 || this.template.maps[i] == 51
                            || this.template.maps[i] == 96 || this.template.maps[i] == 98
                            || this.template.maps[i] == 107 || this.template.maps[i] == 111
                            || this.template.maps[i] == 124 || this.template.maps[i] == 126
                            || this.template.maps[i] == 139 || this.template.maps[i] == 141) {
                        this.template.types[i] |= MapTemplate.T_RIGHT;
                    }
                    if (this.template.maps[i] == 25) {
                        this.template.types[i] |= MapTemplate.T_WATERFLOW;
                        if ((this.template.types[i - this.template.tmw]
                                & MapTemplate.T_SOLIDGROUND) == MapTemplate.T_SOLIDGROUND) {
                            this.template.types[i] |= MapTemplate.T_SOLIDGROUND;
                        }
                    }
                    if (this.template.maps[i] == 34) {
                        this.template.types[i] |= MapTemplate.T_UNDERWATER;
                    }
                    if (this.template.maps[i] == 17) {
                        this.template.types[i] |= MapTemplate.T_BRIDGE;
                    }
                    if (this.template.maps[i] == 33 || this.template.maps[i] == 103 || this.template.maps[i] == 104
                            || this.template.maps[i] == 105 || this.template.maps[i] == 26
                            || this.template.maps[i] == 33) {
                        this.template.types[i] |= MapTemplate.T_OUTSIDE;
                    }
                    if (this.template.maps[i] == 51 || this.template.maps[i] == 111 || this.template.maps[i] == 68) {
                        this.template.types[i] |= MapTemplate.T_BOTTOM;
                    }
                    if (this.template.maps[i] == 82 || this.template.maps[i] == 110 || this.template.maps[i] == 143) {
                        this.template.types[i] |= MapTemplate.T_DIE;
                    }
                    if (this.template.maps[i] == 113) {
                        this.template.types[i] |= MapTemplate.T_BANG;
                    }
                    if (this.template.maps[i] == 142) {
                        this.template.types[i] |= 0x8000;
                    }
                    if (this.template.maps[i] == 40 || this.template.maps[i] == 41) {
                        this.template.types[i] |= MapTemplate.T_JUM8;
                    }
                    if (this.template.maps[i] == 110) {
                        this.template.types[i] |= MapTemplate.T_NT0;
                    }
                    if (this.template.maps[i] == 143) {
                        this.template.types[i] |= MapTemplate.T_NT1;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public short touchY(short x, short y) {
        short yOld = y;
        while (y < this.template.pxh) {
            if (tileTypeAt(x, y, 2))
                return y;
            y = (short) (y + 1);
        }
        if ((short) this.template.pxh != 0) {
            return (short) this.template.pxh;
        }
        return (short) (yOld + 24);
    }

    public boolean tileTypeAt(int px, int py, int t) {
        boolean result;
        try {
            result = ((this.template.types[py / 24 * this.template.tmw + px / 24] & t) == t);
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }
}
