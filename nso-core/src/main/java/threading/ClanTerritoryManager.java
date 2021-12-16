package threading;

import clan.ClanTerritory;
import clan.ClanTerritoryData;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class ClanTerritoryManager extends Thread implements Serializable {

    private volatile boolean running;
    private java.util.Map<Integer, ClanTerritory> clanTerritories;
    private java.util.Map<Integer, ClanTerritoryData> clanTerritoryDatas;

    private int baseId = 0;

    public ClanTerritoryManager() {
        this.running = true;
        clanTerritories = new HashMap<>();
        clanTerritoryDatas = new HashMap<>();
        setName("Clan Territory Thread");
    }

    public void addClanTerritory(ClanTerritory clanTerritory) {
        synchronized (clanTerritories) {
            clanTerritory.id = baseId++;
            clanTerritory.clanManager.setClanTerritory(clanTerritory);
            clanTerritories.put(clanTerritory.id, clanTerritory);
        }
    }

    public void removeClanTerritory(int id) {
        synchronized (clanTerritories) {
            clanTerritories.get(id).cleanup();
            clanTerritories.remove(id);
        }
    }

    public void addClanTerritoryData(ClanTerritoryData clanTerritoryData) {
        synchronized (clanTerritoryDatas) {
            this.clanTerritoryDatas.put(clanTerritoryData.id, clanTerritoryData);
            this.clanTerritories
                    .get(clanTerritoryData.getClanTerritory().id)
                    .invites.add(clanTerritoryData);
        }
    }

    public void removeClanTerritoryData(int id) {
        synchronized (clanTerritoryDatas) {
            this.clanTerritoryDatas.remove(id);
        }
    }

    public Collection<ClanTerritory> getClanTerritories() {
        return clanTerritories.values();
    }

    public ClanTerritoryData getClanTerritoryDataById(int id) {
        return this.clanTerritoryDatas.get(id);
    }

    @Override
    public void run() {
        while (this.running) {
            long lastTime = System.currentTimeMillis();

            try {
                for (ClanTerritory clanTerritory : getClanTerritories()) {
                    if (clanTerritory != null) {
                        clanTerritory.update();
                    }
                }
            } catch (Exception e) {

            } finally {
                final long timeUpdateElapsed = System.currentTimeMillis() - lastTime;
                if (timeUpdateElapsed < 1000) {
                    try {
                        Thread.sleep(1000 - timeUpdateElapsed);
                    } catch (InterruptedException ex) {

                    }
                }
            }
        }
    }

    public void close() {
        this.running = false;
        interrupt();
        for (ClanTerritory clanTerritory : this.getClanTerritories()) {
            if (clanTerritory != null) {
                clanTerritory.cleanup();
            }
        }
        clanTerritories.clear();
        clanTerritories = null;
        clanTerritoryDatas.clear();
        clanTerritoryDatas = null;
    }
}
