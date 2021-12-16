package real;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import threading.Manager;

import java.util.HashMap;

import threading.Server;
import threading.Map;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cave {
    public int caveID;
    public long time;
    public int level;
    public byte finsh;
    public int x;
    private static int idbase;
    private boolean rest;

    @NotNull
    public List<@Nullable Ninja> ninjas;
    @NotNull
    public Map[] map;
    @NotNull
    Server server;
    @NotNull
    public static final HashMap<@NotNull Integer,@NotNull Cave> caves;

    public Cave(final int x) {
        this.level = 0;
        this.finsh = 0;
        this.x = -1;
        this.ninjas = new CopyOnWriteArrayList<>();
        this.rest = false;
        this.server = Server.getInstance();
        this.x = x;
        this.caveID = Cave.idbase++;
        this.time = System.currentTimeMillis() + 3600000L;
        if (x == 3) {
            this.map = new Map[3];
        } else if (x == 4) {
            this.map = new Map[4];
        } else if (x == 5) {
            this.map = new Map[5];
        } else if (x == 6) {
            this.map = new Map[3];
        } else if (x == 7) {
            this.map = new Map[4];
        } else if (x == 9) {
            this.map = new Map[3];
        }
        this.initMap(x);
        for (byte i = 0; i < this.map.length; ++i) {
            this.map[i].timeMap = this.time;
        }
        Cave.caves.put(this.caveID, this);
    }

    private void initMap(final int x) {
        switch (x) {
            case 3: {
                this.map[0] = new Map(91, this);
                this.map[1] = new Map(92, this);
                this.map[2] = new Map(93, this);
                break;
            }
            case 4: {
                this.map[0] = new Map(94, this);
                this.map[1] = new Map(95, this);
                this.map[2] = new Map(96, this);
                this.map[3] = new Map(97, this);
                break;
            }
            case 5: {
                this.map[0] = new Map(105, this);
                this.map[1] = new Map(106, this);
                this.map[2] = new Map(107, this);
                this.map[3] = new Map(108, this);
                this.map[4] = new Map(109, this);
                break;
            }
            case 6: {
                this.map[0] = new Map(114, this);
                this.map[1] = new Map(115, this);
                this.map[2] = new Map(116, this);
                break;
            }
            case 7: {
                this.map[0] = new Map(125, this);
                this.map[1] = new Map(126, this);
                this.map[2] = new Map(127, this);
                this.map[3] = new Map(128, this);
                break;
            }
            case 9: {
                this.map[0] = new Map(157, this);
                this.map[1] = new Map(158, this);
                this.map[2] = new Map(159, this);
                break;
            }
        }
    }

    public void updateXP(final long xp) {
        for (short i = 0; i < this.ninjas.size(); ++i) {
            try {
                this.ninjas.get(i).p.updateExp(xp, true);
            } catch (Exception ex) {

            }
        }
    }

    public void updatePoint(final int point) {
        synchronized (this) {
            for (short i = 0; i < this.ninjas.size(); ++i) {
                final Ninja ninja1 = this.ninjas.get(i);
                ninja1.pointCave += point;
                this.ninjas.get(i).p.setPointPB(this.ninjas.get(i).pointCave);
            }
        }
    }

    public void rest() {
        if (!this.rest) {
            this.rest = true;
            synchronized (this) {
                while (this.ninjas.size() > 0) {
                    final Ninja nj = this.ninjas.get(0);
                    nj.getPlace().leave(nj.p);
                    nj.p.restCave();
                    final Manager manager = this.server.manager;
                    final Map ma = Manager.getMapid(nj.mapLTD);
                    if (ma != null)
                        for (byte k = 0; k < ma.area.length; ++k) {
                            if (ma.area[k].getNumplayers() < ma.template.maxplayers) {
                                ma.area[k].EnterMap0(nj);
                                break;
                            }
                        }
                }
            }
            for (byte i = 0; i < this.map.length; ++i) {
                this.map[i].close();
            }
            synchronized (Cave.caves) {
                Cave.caves.remove(this.caveID);
            }
        }
    }

    public void finsh() {
        synchronized (this) {
            ++this.level;
            if (this.x != 6) {
                this.time = System.currentTimeMillis() + 10000L;
                for (byte u = 0; u < this.map.length; ++u) {
                    this.map[u].timeMap = this.time;
                }
            }
            if (this.x != 6 || this.finsh == 0) {
                ++this.finsh;
                for (byte i = 0; i < this.ninjas.size(); ++i) {
                    final Ninja nj = this.ninjas.get(i);
                    nj.p.setTimeMap((int) (this.time - System.currentTimeMillis()) / 1000);

                    nj.p.sendYellowMessage("Hoàn thành hang động");
                    if (nj.party != null && nj.party.cave != null) {
                        nj.party.cave = null;
                    }

                    if (nj.getTaskId() == 39 && nj.getTaskIndex() == 2) {
                        nj.upMainTask();
                    }

                    if (!nj.clan.clanName.isEmpty()) {
                        nj.p.upExpClan(100);
                    }
                }
            }
        }
    }

    public void openMap() {
        synchronized (this) {
            ++this.level;
            if (this.level < this.map.length) {
                for (byte i = 0; i < this.ninjas.size(); ++i) {
                    final Ninja nj = this.ninjas.get(i);
                    nj.p.sendYellowMessage(this.map[this.level].template.name + " đã được mở");
                }
            }
        }
    }

    static {
        caves = new HashMap<>();
    }
}
