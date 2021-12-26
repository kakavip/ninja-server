package threading;

import boardGame.Place;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import battlewithmaster.BattleWithMasterPlace;
import clan.ClanTerritory;
import real.*;
import server.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static interfaces.IBattle.*;

public class Map extends Thread {

    public int id;
    public long timeMap;
    protected boolean runing;
    public volatile long lastTimeActive;

    @NotNull
    public static final int[] arrLang;
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

    protected Map() {
    }

    public Map(final int id, final Cave cave) {
        this(id, cave, MapTemplate.arrTemplate[id].numarea);
    }

    public Map(final int id, final Cave cave, final int maxArea) {
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
        val id = template.id;

        if (id == 81) {
            return 80;
        } else if (id == 82) {
            return 77;
        } else if (id == 83) {
            return 76;
        } else if (id == 84) {
            return 72;
        } else if (id == 85) {
            return 74;
        } else if (id == 86) {
            return 79;
        } else if (id == 87) {
            return 73;
        } else if (id == 88) {
            return 78;
        } else if (id == 89) {
            return 75;
        } else if (id == 90) {
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
        arrTruong = new int[] { 1, 27, 72 };
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
}
