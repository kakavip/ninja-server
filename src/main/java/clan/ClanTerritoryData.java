package clan;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import real.Ninja;
import server.util;

import java.util.concurrent.atomic.AtomicInteger;

public class ClanTerritoryData {

    private final long MAX_TIME_INVITE = util.TimeSeconds(10);
    @NotNull
    private ClanTerritory clanTerritory;
    public final int id;
    private final long tick;
    @NotNull
    public Ninja ninja;
    public boolean entered;
    private volatile int tichLuy;

    public static final AtomicInteger baseId = new AtomicInteger();

    public ClanTerritoryData(final @NotNull ClanTerritory clanTerritory,@NotNull final Ninja ninja) {

        synchronized (baseId){
            this.clanTerritory = clanTerritory;
            this.id = baseId.get() + 1;
            baseId.set(baseId.get() + 1);
            this.tick = System.currentTimeMillis();
            this.ninja = ninja;
            this.entered = true;
        }

    }

    @Nullable
    public ClanTerritory getClanTerritory() {
        return clanTerritory;
    }

    public boolean isExpired() {
        return !entered &&
                System.currentTimeMillis() - this.tick >= MAX_TIME_INVITE;
    }

    public void upTichLuy(int point) {
        tichLuy += point;
    }

    public int getTichLuy() {
        return this.tichLuy;
    }

    public void cleanup() {
        this.clanTerritory = null;
        this.tichLuy = 0;
        this.ninja = null;

    }
}
