package real;

import boardGame.Place;
import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate;
import io.Session;
import lombok.SneakyThrows;
import lombok.val;
import patch.Constants;

import org.jetbrains.annotations.NotNull;
import candybattle.CandyBattle;
import server.Service;
import server.util;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static threading.Manager.*;
import static real.ItemData.*;

public class Mob {

    public static final int THIEU_DOT_ID = 9;

    public boolean isFire;
    public boolean isIce;
    public boolean isWind;
    public boolean isBurn;

    public boolean isDisable;
    public boolean isDontMove;
    public long timeDisable;
    public long timeDontMove;

    public boolean isThieuDot;
    public Body masterThieuDot;

    public long timeFire;
    public long timeIce;
    public long timeWind;
    public long timeBurn;

    public int id;
    public byte sys;
    public int hp;
    public int level;
    public int hpmax;
    public short x;
    public short y;
    public byte status;
    public int lvboss;
    private boolean isboss;
    public volatile boolean isDie;
    public boolean isRefresh;
    public long xpup;
    private long timeRefresh;
    public long timeFight;
    public MobData templates;
    private Place crtPlace;

    private short[] arrItemIds;

    public AtomicInteger attackCount = new AtomicInteger(0);

    @NotNull
    private final HashMap<@NotNull Integer, @NotNull Integer> nFight;

    public Mob(final int id, final int idtemplate, final int level) {
        this.isRefresh = true;
        this.id = id;
        this.templates = MobData.entrys.get(idtemplate);
        this.level = level;
        final int hp = this.templates.hp;
        this.hpmax = hp;
        this.hp = hp;
        this.xpup = 100000L;
        this.isDie = false;
        this.nFight = new HashMap<>();
        this.crtPlace = null;

        this.setArrItemIds();
    }

    public short[] getArrItemIds() {
        return this.arrItemIds;
    }

    private void setArrItemIds() {
        int curMobMaxLv = this.level - this.level % 10 + 10;
        if (curMobMaxLv > 100) {
            curMobMaxLv = 100;
        }

        switch (curMobMaxLv) {
            case 10:
                this.arrItemIds = ITEM_LV_10;
                break;
            case 20:
                this.arrItemIds = ITEM_LV_20;
                break;
            case 30:
                this.arrItemIds = ITEM_LV_30;
                break;
            case 40:
                this.arrItemIds = ITEM_LV_40;
                break;
            case 50:
                this.arrItemIds = ITEM_LV_50;
                break;
            case 60:
                this.arrItemIds = ITEM_LV_60;
                break;
            case 70:
                this.arrItemIds = ITEM_LV_70;
                break;
            case 80:
                this.arrItemIds = ITEM_LV_80;
                break;
            case 90:
                this.arrItemIds = ITEM_LV_90;
                break;
            case 100:
            case 110:
            case 120:
            case 130:
            case 140:
            case 150:
            case 160:
                this.arrItemIds = ITEM_LV_100;
                break;
            default:
                this.arrItemIds = new short[0];
        }
    }

    public void updateHP(final int num) {
        this.attackCount.incrementAndGet();
        this.hp = util.debug ? 0 : this.hp + num;

        if (this.hp <= 0) {

            if (this.templates.id == CandyBattle.GIO_KEO_TRANG_ID
                    || this.templates.id == CandyBattle.GIO_KEO_DEN_ID) {
                this.hp = this.hpmax;
                this.isDie = false;

                return;
            }
            this.hp = 0;
            this.status = 0;
            this.isDie = true;
            if (this.isRefresh) {
                this.timeRefresh = System.currentTimeMillis() + TIME_REFRESH_MOB;
            }
            if (this.isboss) {
                if (this.templates.id != Constants.BOSS_LAO_DAI_ID && this.templates.id != Constants.BOSS_LAO_TAM_ID
                        && this.templates.id != Constants.BOSS_LAO_NHI_ID) {
                    this.isRefresh = false;
                    this.timeRefresh = -1L;
                } else {
                    this.timeRefresh = 10000L;
                }
            }
        }
    }

    public void ClearFight() {
        this.nFight.clear();
    }

    public int sortNinjaFight() {
        int idN = -1;
        int dameMax = 0;
        for (final int value : this.nFight.keySet()) {
            final int dame = this.nFight.get(value);
            final Session conn = PlayerManager.getInstance().getConn(value);
            if (conn != null && conn.user != null && conn.user.nj != null) {
                if (dame <= dameMax) {
                    continue;
                }
                dameMax = this.nFight.get(value);
                idN = conn.user.nj.id;
            }
        }
        return idN;
    }

    public void Fight(final int id, final int dame) {
        if (!this.nFight.containsKey(id)) {
            this.nFight.put(id, dame);
        } else {
            int damenew = this.nFight.get(id);
            damenew += dame;
            this.nFight.replace(id, damenew);
        }
    }

    public void removeFight(final int id) {
        this.nFight.remove(id);
    }

    public boolean isFight(final int id) {
        return this.nFight.containsKey(id);
    }

    public boolean isDisable() {
        return false;
    }

    public boolean isDonteMove() {
        return false;
    }

    public long getTimeRefresh() {
        return timeRefresh;
    }

    public void setTimeRefresh(long timeRefresh) {
        this.timeRefresh = timeRefresh;
    }

    public boolean isIsboss() {
        return isboss;
    }

    public void setIsboss(boolean isboss) {
        this.isboss = isboss;

        // update item.
        this.updateBossItemDrop();
    }

    public int getDefensePercent() {
        if (this.isIsboss()) {
            if (this.crtPlace != null) {
                if (this.crtPlace.map.isEndOfSchoolMap) {
                    return 70;
                } else if (this.crtPlace.map.isLangCo()) {
                    return 50;
                } else {
                    return 30;
                }
            } else {
                return 30;
            }
        } else {
            return 5 * this.lvboss;
        }
    }

    private void updateBossItemDrop() {
        if (this.isboss) {
            if (this.crtPlace != null && this.crtPlace.map.isLangCo()) {
                this.templates.arrIdItem = BOSS_LC_ITEM;
            } else {
                switch (this.level) {
                    case 45:
                        this.templates.arrIdItem = BOSS_ITEM_LV45;
                        break;
                    case 55:
                        this.templates.arrIdItem = BOSS_ITEM_LV55;
                        break;
                    case 60:
                        this.templates.arrIdItem = BOSS_ITEM_LV60;
                        break;
                    case 65:
                        this.templates.arrIdItem = BOSS_ITEM_LV65;
                        break;
                    case 75:
                        this.templates.arrIdItem = BOSS_ITEM_LV75;
                        break;
                    case 90:
                        this.templates.arrIdItem = BOSS_ITEM_LV90;
                        break;
                    case 99:
                        this.templates.arrIdItem = BOSS_ITEM_LV99;
                        break;
                    case 100:
                        this.templates.arrIdItem = BOSS_ITEM_LV100;
                        break;
                    case 110:
                        this.templates.arrIdItem = BOSS_ITEM_LV110;
                        break;
                    case 150:
                        this.templates.arrIdItem = BOSS_ITEM_LV150;
                        break;
                    default:
                        this.templates.arrIdItem = BOSS_DEFAULT_ITEM;
                        break;
                }
            }
        }

    }

    public long getReduceIceTime() {
        if (this.isIsboss()) {
            return 10 * 250L;
        }
        return this.lvboss * 250L;
    }

    public long getReduceWindTime() {
        if (this.isIsboss()) {
            return 10 * 150L;
        }
        return this.lvboss * 150L;
    }

    public long getReduceFireTime() {
        if (this.isIsboss()) {
            return 10 * 300L;
        }
        return this.lvboss * 300L;
    }

    @SneakyThrows
    public void update(final @NotNull Place place) {
        if (this.crtPlace == null) {
            this.crtPlace = place;

            this.updateBossItemDrop();
        }

        if (isThieuDot
                && masterThieuDot != null) {
            final Effect effId = masterThieuDot.getEffId(THIEU_DOT_ID);

            if (effId == null || System.currentTimeMillis() > effId.timeRemove) {
                this.masterThieuDot = null;
                this.isThieuDot = false;
            } else {
                Service.sendThieuDot(place.getUsers(), this.id);
                val dame = effId.param * this.masterThieuDot.dameMax() / 2000;
                this.updateHP(dame);
                val expUp = (long) this.level * dame / 1000;
                if (masterThieuDot instanceof Ninja) {
                    ((Ninja) masterThieuDot).p.updateExp(expUp, true);
                } else {
                    ((CloneChar) masterThieuDot).chuThan.p.updateExp(expUp / 2, true);
                }
                place.attackAMob(masterThieuDot, this, dame);
            }
        }
    }
}
