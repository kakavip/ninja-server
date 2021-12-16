package clan;

import boardGame.Place;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import real.ClanManager;
import real.ClanMember;
import real.ItemData;
import real.Ninja;
import server.Service;
import threading.Server;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static threading.Server.getMapById;

public class ClanTerritory implements Serializable {


    public enum State {
        WAITING,
        START,
        WIN,
        END
    }

    private long time;
    public int id;
    @NotNull
    public ClanManager clanManager;
    private long tick;
    private final long START_DURATION = 3600000L;
    private final long WAIT_DURATION = 10 * 60000L;
    private final long WIN_WAIT_DURATION = 10_000L;
    @NotNull
    public java.util.Map<@NotNull Integer, @Nullable Place> openedMap;
    @NotNull
    public List<@NotNull ClanTerritoryData> invites;
    private State state;

    public ClanTerritory(final @NotNull ClanManager clanManager) {
        this.clanManager = clanManager;
        openedMap = new HashMap<>();
        val p = getMapById(80).getFreeArea();
        assert p != null;
        p.open();
        openedMap.put(80, p);
        openedMap.put(81, null);
        openedMap.put(82, null);
        openedMap.put(83, null);
        openedMap.put(84, null);
        openedMap.put(85, null);
        openedMap.put(86, null);
        openedMap.put(87, null);
        openedMap.put(88, null);
        openedMap.put(89, null);
        openedMap.put(90, null);
        this.tick = System.currentTimeMillis();
        this.invites = new ArrayList<>();
        this.time = -1;
        this.setState(State.WAITING);
    }

    public Place getEntrance() {
        return this.openedMap.get(80);
    }

    public State getState() {
        return this.state;
    }

    public void enterEntrance(@Nullable final Ninja ninja) {
        if (ninja == null || ninja.p == null) return;

        if (ninja.p.getClanTerritoryData() == null) {
            throw new RuntimeException("Clan territory data cannot be null");
        }

        ninja.p.getClanTerritoryData().entered = true;
        setTime(ninja);
        ninja.enterSamePlace(getEntrance(), null);

    }

    public void setTime(Ninja ninja) {
        Service.batDauTinhGio(ninja, (getRemainingTime()));
    }

    private void inform(final @NotNull String message) {
        for (ClanMember member : clanManager.members) {
            if (member.getNinja() != null && member.getNinja().p.getClanTerritoryData() != null &&
                    member.getNinja().p.getClanTerritoryData().entered) {
                member.getNinja().p.sendYellowMessage(message);
            }
        }
    }

    public synchronized void plugKey(int currentMapId, final @Nullable Ninja ninja) {
        if (ninja == null) return;

        inform(ninja.name + " đã cắm chìa khoá vào trụ cơ quan");

        if (currentMapId == 80) {
            val m1 = getMapById(81).getFreeArea();
            val m2 = getMapById(82).getFreeArea();
            val m3 = getMapById(83).getFreeArea();
            openedMap.put(81, Objects.requireNonNull(m1).open());
            openedMap.put(82, Objects.requireNonNull(m2).open());
            openedMap.put(83, Objects.requireNonNull(m3).open());

            inform("Cửa Siêu Tốc, Cửa Né Tránh, Cửa Phản Đòn đã được mở");

        } else if (currentMapId == 81) {
            openedMap.put(84, getMapById(84).getFreeArea());

            val areas = openedMap.entrySet().stream().filter(e -> e.getValue() != null &&
                    (e.getKey() == 84 || e.getKey() == 85 || e.getKey() == 86)).collect(Collectors.toList());
            if (areas.size() == 3) {
                for (Map.Entry<Integer, Place> area : areas) {
                    area.getValue().open();
                }
                inform("Cửa Hoả đã được mở");
                inform("Của Phong đã được mở");
                inform("Cửa Băng đã được mở");

            } else {
                inform("Cửa Hoả gần được mở");
            }
        } else if (currentMapId == 82) {
            openedMap.put(85, getMapById(85).getFreeArea());

            val areas = openedMap.entrySet().stream().filter(e -> e.getValue() != null &&
                    (e.getKey() == 84 || e.getKey() == 85 || e.getKey() == 86)).collect(Collectors.toList());
            if (areas.size() == 3) {
                for (Map.Entry<Integer, Place> area : areas) {
                    area.getValue().open();
                }
                inform("Cửa Hoả đã được mở");
                inform("Của Phong đã được mở");
                inform("Cửa Băng đã được mở");

            } else {
                inform("Của Phong gần được mở");
            }
        } else if (currentMapId == 83) {

            openedMap.put(86, getMapById(86).getFreeArea());

            val areas = openedMap.entrySet().stream().filter(e -> e.getValue() != null &&
                    (e.getKey() == 84 || e.getKey() == 85 || e.getKey() == 86)).collect(Collectors.toList());
            if (areas.size() == 3) {
                for (Map.Entry<Integer, Place> area : areas) {
                    area.getValue().open();
                }
                inform("Cửa Hoả đã được mở");
                inform("Của Phong đã được mở");
                inform("Cửa Băng đã được mở");

            } else {
                inform("Cửa Băng gần được mở");
            }

        } else if (currentMapId == 84 || currentMapId == 86) {

            if (openedMap.get(89) == null) {
                openedMap.put(89, Objects.requireNonNull(getMapById(89).getFreeArea()).open());
            }

            inform("Cửa Đầm Lầy đã được mở");
        } else if (currentMapId == 85) {
            openedMap.put(88, Objects.requireNonNull(getMapById(88).getFreeArea()).open());
            inform("Cửa Đồi Núi đã được mở");
        } else if (currentMapId == 87 || currentMapId == 89) {
            if (openedMap.get(90) == null) {

                val openMaps = openedMap.entrySet()
                        .stream()
                        .filter(e -> (e.getKey() >= 80 && e.getKey() <= 89) && e.getValue() != null)
                        .collect(Collectors.toList());

                if (openMaps.size() == 10) {
                    openedMap.put(90, Objects.requireNonNull(getMapById(90).getFreeArea()).open());
                    inform("Cửa Bùa Chú đã được mở");
                } else {
                    inform("Còn " + (10 - openMaps.size()) + " map chưa mở");
                }
            }
        } else if (currentMapId == 88) {
            openedMap.put(87, Objects.requireNonNull(getMapById(87).getFreeArea()).open());
            inform("Cửa Sa Mạc đã được mở");
        }
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - tick >= time;
    }

    public void setState(State state) {
        this.tick = System.currentTimeMillis();
        this.state = state;
        if (state == State.WAITING) {
            time = WAIT_DURATION;
        } else if (state == State.START) {
            time = START_DURATION;
            sendTimeToAll();
        } else if (state == State.WIN) {
            inform("Gia tộc bạn đã chiến thắng");
            this.time = WIN_WAIT_DURATION;
            sendTimeToAll();
        } else if (state == State.END) {
            inform("Lãnh đại gia tộc đã đóng");
            for (ClanTerritoryData invite : this.invites) {
                if (invite.ninja != null) {
                    try {
                        invite.ninja.p.upExpClan(100 * invite.getTichLuy());
                        invite.ninja.getPlace().gotoHaruna(invite.ninja.p);
                        if (invite.ninja.p.getClanTerritoryData() != null) {
                            invite.ninja.p.getClanTerritoryData().cleanup();
                            invite.ninja.p.setClanTerritoryData(null);
                        }
                    } catch (Exception e) {
                    }

                }
            }
            Server.clanTerritoryManager.removeClanTerritory(this.id);

        }

    }

    public void update() {
        if (this.isExpired()) {
            if (this.state == State.WAITING) {
                this.setState(State.START);
            } else if (this.state == State.WIN || this.state == State.START) {
                this.setState(State.END);
            }
        } else {
            if (this.state == State.WAITING) {
                if (this.openedMap.get(81) != null && this.openedMap.get(82) != null && this.openedMap.get(83) != null) {
                    this.setState(State.START);
                }
            } else if (this.state == State.START) {
                if (openedMap.get(90) != null && openedMap.get(90).checkCleanMob(Place.BOST_LDGT_ID)) {
                    this.setState(State.WIN);
                }
            }
        }
    }

    public void sendTimeToAll() {
        for (ClanMember member : clanManager.members) {
            if (member.getNinja() != null && member.getNinja().p.getClanTerritoryData() != null &&
                    !member.getNinja().p.getClanTerritoryData().isExpired()) {
                Service.batDauTinhGio(member.getNinja(), getRemainingTime());
            }
        }
    }

    public int getRemainingTime() {
        return (int) ((this.time - (System.currentTimeMillis() - tick)) / 1000);
    }

    public static boolean checkPlaceOccupied(final @Nullable Place place) {
        if (place == null) return true;
        return Server.clanTerritoryManager
                .getClanTerritories().stream()
                .anyMatch(t -> t.openedMap.get(place.map.id) != null &&
                        t.openedMap.get(place.map.id).equals(place));
    }

    @SneakyThrows
    public void upPoint(int point) {
        for (ClanMember member : clanManager.members) {
            val ninja = member.getNinja();
            if (ninja != null) {
                if (ninja.p.getClanTerritoryData() != null &&
                        ninja.p.getClanTerritoryData().entered
                        && !ninja.p.getClanTerritoryData().isExpired() && ninja.getPlace().map.isLdgtMap()) {

                    val item = ItemData.itemDefault(262);
                    item.quantity = point;
                    ninja.addItemBag(true, item);

                    ninja.p.getClanTerritoryData().upTichLuy(point);
                    ninja.p.sendYellowMessage("Bạn nhận được " + point + " đồng tiền gia tộc");
                    ninja.p.updateExp(point * 200000L, true);
                }
            }
        }
    }

    public void cleanup() {

        for (ClanTerritoryData invite : invites) {
            if (invite != null && invite.ninja != null && invite.ninja.p != null) {
                invite.ninja.p.setClanTerritoryData(null);
            }
        }

        invites.clear();
        invites = null;
        openedMap.clear();
        openedMap = null;
        clanManager.setClanTerritory(null);
        clanManager = null;

    }

}
