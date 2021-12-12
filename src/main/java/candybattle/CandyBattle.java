package candybattle;

import boardGame.Place;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import battle.GBattle;
import interfaces.IGlobalBattler;
import interfaces.SendMessage;
import real.CloneChar;
import real.Item;
import real.Ninja;
import server.Service;
import threading.Manager;
import threading.Server;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static patch.Constants.*;

public class CandyBattle extends GBattle {

    public static final int MAX_NINJA = 2;
    public static final int KEO_NGOT_ID = 458;
    public static final int KEO_CHIEN_MAP_ID = 130;
    public static final int KEO_TRANG_ID = 131;
    public static final int KEO_DEN_ID = 132;
    public static final int PHONG_CHO = 133;
    public static final int GIO_KEO_DEN_ID = 143;
    public static final int GIO_KEO_TRANG_ID = 142;

    private final AtomicInteger nKeoDen = new AtomicInteger(100);
    private final AtomicInteger nKeoTrang = new AtomicInteger(100);

    public long timeDoiKeo = -1;

    /**
     * -1 if they are equal
     *
     * @return
     */
    public int getLose() {
        if (nKeoTrang.get() > nKeoDen.get()) {
            return PK_DEN;
        } else if (nKeoTrang.get() < nKeoDen.get()) {
            return PK_TRANG;
        }

        return -1;
    }

    public static enum State {
        INIT,
        HIEN_THI_PHU_THUY,
        DOI_KEO;
    }

    private State subState;


    @NotNull
    private final Map<@NotNull Integer, @NotNull Place> OPEN_MAPS;

    public CandyBattle() {

        OPEN_MAPS = new HashMap<>();
        val keoChien = Objects.requireNonNull(Server.getMapById(KEO_CHIEN_MAP_ID).getFreeArea(), "NULL CandyBattle").addRunner(this);
        this.setSubState(State.INIT);
        keoChien.refreshMob(0, true);
        keoChien.refreshMob(1, true);
        keoChien.setCandyBattle(this);
        keoChien.killMob(2);

        OPEN_MAPS.put(KEO_CHIEN_MAP_ID, keoChien);
        OPEN_MAPS.put(KEO_TRANG_ID, Objects.requireNonNull(Server.getMapById(KEO_TRANG_ID).getFreeArea(), "NULL CandyBattle").addRunner(this).setCandyBattle(this));
        OPEN_MAPS.put(KEO_DEN_ID, Objects.requireNonNull(Server.getMapById(KEO_DEN_ID).getFreeArea(), "NULL CandyBattle").addRunner(this).setCandyBattle(this));
        OPEN_MAPS.put(PHONG_CHO, Objects.requireNonNull(Server.getMapById(PHONG_CHO).getFreeArea(), "NULL CandyBattle").addRunner(this).setCandyBattle(this));
        this.setState(WAITING_STATE);

    }

    private int attackingTeam = -1;


    public void setSubState(State subState) {
        this.subState = subState;

        if (this.subState == State.HIEN_THI_PHU_THUY) {
            getOpenMaps().get(CandyBattle.KEO_CHIEN_MAP_ID).refreshMob(2, true);
        }
    }

    @SneakyThrows
    public synchronized void choKeo(Ninja ninja) {

        if (attackingTeam == -1) return;
        if (attackingTeam != ninja.getTypepk()) {
            ninja.p.sendYellowMessage("Phù thuỷ không đòi kẹo đội bạn");
            return;
        }

        int count = 0;
        for (Item item : ninja.ItemBag) {
            if (item != null && item.id == KEO_NGOT_ID) {
                count += item.quantity;
            }
        }

        if (count > 0) {
            // Cho keo
            ninja.removeItemBags(KEO_NGOT_ID, count);
            attackingTeam = -1;

        } else {
            attackTeam(ninja.getTypepk());
        }
    }

    private void attackTeam(int typePk) throws IOException {
        // Tieu diet
        if (typePk == PK_TRANG) {
            getOpenMaps().get(KEO_CHIEN_MAP_ID).attack(this.bachgia.stream().map(b -> (Ninja) b).collect(Collectors.toList()));
            Manager.chatKTG("Kẹo trắng cho ta cái nịt xin phép pem cho chết kẹo trắng");
        } else {
            getOpenMaps().get(KEO_CHIEN_MAP_ID).attack(this.hacgia.stream().map(b -> (Ninja) b).collect(Collectors.toList()));
            Manager.chatKTG("Kẹo đen cho ta cái nịt xin phép pem cho chết kẹo đen");
        }
    }

    public State getSubState() {
        return subState;
    }

    public Map<@NotNull Integer, @NotNull Place> getOpenMaps() {
        return this.OPEN_MAPS;
    }

    // TYPE MUST BE
    @SneakyThrows
    @Override
    public synchronized boolean enter(IGlobalBattler member, int mapId) {
        if (member instanceof CloneChar) return false;
        final threading.Map map = Server.getMapById(mapId);
        Ninja ninja = (Ninja) member;
        if (MAX_NINJA > this.hacgia.size() + this.bachgia.size()) {
            if (this.state != WAITING_STATE && !hacgia.contains(member) && !bachgia.contains(member)) {
                ninja.p.sendYellowMessage("Giờ báo danh đã hết con vui lòng ở ngoài và đợi kết quả");
                return false;
            }
            if (hacgia.contains(member) && mapId == KEO_TRANG_ID) {
                ninja.p.sendYellowMessage("Con con đã vào phe hắc giả rồi không thể vào phe bạch giả được");
                return false;
            } else if (bachgia.contains(member) && mapId == KEO_DEN_ID) {
                ninja.p.sendYellowMessage("Con đã vào phe bạch giả rồi không thể nào vào phe  hắc giả được");
                return false;
            } else {

                if (!bachgia.contains(member) && mapId == KEO_TRANG_ID) {
                    ninja.resetPoint();
                    bachgia.add(ninja);
                } else if (!hacgia.contains(member) && mapId == KEO_DEN_ID) {
                    ninja.resetPoint();
                    hacgia.add(ninja);
                }


                if (map != null) {
                    Place area = null;
                    if (this.state == WAITING_STATE) {
                        area = getOpenMaps().get(CandyBattle.PHONG_CHO);
                        if (ninja.getPlace() != null) {
                            ninja.getPlace().leave(ninja.p);
                        }
                        ninja.x = map.template.x0;
                        ninja.y = map.template.y0;

                        if (this.hacgia.size() < MAX_NINJA / 2) {
                            // Hack gia da đủ
                            ninja.resetPoint();
                            this.hacgia.add(ninja);
                            area.Enter(ninja.p);
                            return true;
                        }

                        if (this.bachgia.size() < MAX_NINJA / 2) {
                            ninja.resetPoint();
                            this.bachgia.add(ninja);
                            area.Enter(ninja.p);
                            return true;
                        }

                    }
                }
                return false;
            }
        } else if (this.state == START_STATE) {
            if (ninja.getPlace() != null) {
                ninja.getPlace().leave(ninja.p);
            }
            ninja.x = map.template.x0;
            ninja.y = map.template.y0;
            Place area = null;
            if (bachgia.contains(member)) {
                area = getOpenMaps().get(CandyBattle.KEO_TRANG_ID);
                ninja.changeTypePk(PK_DEN);
                Service.batDauTinhGio(ninja, (int) getTimeInSeconds());
                area.Enter(ninja.p);
                return true;
            } else if (hacgia.contains(member)) {
                area = getOpenMaps().get(CandyBattle.KEO_DEN_ID);
                ninja.changeTypePk(PK_TRANG);
                Service.batDauTinhGio(ninja, (int) getTimeInSeconds());
                area.Enter(ninja.p);
                return true;
            } else {
                return false;
            }
        }
        return false;

    }


    @Override
    public @NotNull List<@Nullable IGlobalBattler> getTopBattlers() {
        return Collections.emptyList();
    }

    @Override
    public int getState() {
        return super.getState();
    }

    private void sendMessageToAll(String message) {
        for (IGlobalBattler battler : Stream.concat(this.hacgia.stream(), this.bachgia.stream()).collect(Collectors.toList())) {
            battler.notifyMessage(message);
        }
    }

    @Override
    public boolean isExpired() {
        return super.isExpired() && duration != -1;
    }

    @Override
    public void setState(byte state) {
        tick();
        this.state = state;
        if (state == WAITING_STATE) {
            // Waiting
            sendMessageToAll("Vui lòng đợi đủ " + MAX_NINJA + " để bắt đầu hay kêu gọi thêm");
            duration = -1;
        } else if (state == START_STATE) {
            notifyStart();
            duration = START_TIME / 2;
            // Send to can cu
            for (IGlobalBattler battler : this.bachgia) {
                enter(battler, KEO_TRANG_ID);
                battler.changeTypePk(PK_TRANG);
                Service.batDauTinhGio((SendMessage) battler, (int) getTimeInSeconds());
            }
            for (IGlobalBattler battler : this.hacgia) {
                enter(battler, KEO_DEN_ID);
                battler.changeTypePk(PK_DEN);
                Service.batDauTinhGio((SendMessage) battler, (int) getTimeInSeconds());
            }
        } else if (state == END_STATE) {
            this.duration = 0;
            close();
        }
    }


    @SneakyThrows
    @Override
    public void notifyStart() {
        Manager.chatKTG("Server: chiến trường kẹo đã bắt đầu");
    }

    @Override
    public synchronized void upPoint(@Nullable IGlobalBattler battler, int point) {
        // Do nothing
        if (this.hacgia.contains(battler)) {
            this.nKeoDen.addAndGet(point);
        } else {
            this.nKeoTrang.addAndGet(point);
        }
    }

    public synchronized void upPoint(int phe, int point) {
        if (phe == PK_TRANG) {
            this.nKeoTrang.addAndGet(point);
        } else {
            this.nKeoDen.addAndGet(point);
        }
    }

    @Override
    public @NotNull short @NotNull [] getRewards(@NotNull IGlobalBattler battle) {
        return new short[0];
    }


    @SneakyThrows
    @Override
    public void start() {

    }

    @Override
    public @NotNull String getResult(@NotNull IGlobalBattler nj) {
        return "";
    }

    @SneakyThrows
    @Override
    public void updateBattler(@Nullable IGlobalBattler battler, boolean isHuman, @Nullable Object other) {
        // Update POINT
        if (other instanceof Ninja) {
            Ninja me = (Ninja) battler;
            if (battler != null) {
                val otherNinja = ((Ninja) other);
                otherNinja.notifyMessage("Bạn đã bị " + me.name + " đánh bại");

                @Nullable Item[] itemBag = otherNinja.ItemBag;
                for (int i = 0; i < itemBag.length; i++) {
                    if (itemBag[i] != null && itemBag[i].id == KEO_NGOT_ID) {
                        getOpenMaps().get(KEO_CHIEN_MAP_ID).leaveItemBackground(otherNinja.p, (byte) i);
                    }
                }
            }
        }
    }

    public void catKeo(@NotNull Ninja ninja, short id) {
        synchronized (this) {
            if (id == 144) {
                // Phu thuy bi ngo
                choKeo(ninja);
            } else {
                int count = 0;
                for (Item item : ninja.ItemBag) {
                    if (item != null && item.id == KEO_NGOT_ID) {
                        count += item.quantity;
                    }
                }
                if (count > 0) {
                    ninja.removeItemBags(KEO_NGOT_ID, count);
                    upPoint(ninja.getTypepk(), count);
                    ninja.p.sendYellowMessage("Cất kẹo thành công số kẹo hiện tại phe bạn là " + getKeo(ninja.getTypepk()));
                } else {
                    ninja.p.sendYellowMessage("Không có kẹo để cất");
                    ninja.p.sendYellowMessage("Số kẹo hiện tại là " + getKeo(ninja.getTypepk()));
                }
            }


        }
    }

    public int getKeo(int phe) {
        if (phe == PK_TRANG) {
            return nKeoTrang.get();
        } else {
            return nKeoDen.get();
        }
    }

    @Override
    public long getTimeInSeconds() {
        return super.getTimeInSeconds();
    }

    @Override
    protected @NotNull String getDanhHieu(int point) {
        return "";
    }

    @Override
    protected int calculatePoint(@NotNull List<@Nullable IGlobalBattler> phe) {
        return 0;
    }


    @Override
    public void close() {
        for (IGlobalBattler battler : Stream.concat(this.hacgia.stream(), this.bachgia.stream()).collect(Collectors.toList())) {
            if (battler instanceof Ninja) {
                Ninja nj = ((Ninja) battler);
                if (nj.getPlace() != null) {
                    nj.getPlace().leave(nj.p);
                    // Get winner then reward
                }
                nj.candyBattle = null;
            }
        }

        for (Place place : getOpenMaps().values()) {
            place.removeRunner(this);
            place.setCandyBattle(null);
        }

        super.close();
        reset();
        Server.candyBattleManager.remove(this);
    }

    @SneakyThrows
    @Override
    protected void middlewareOnUpdate() {
        if (this.bachgia.size() == MAX_NINJA / 2 && this.hacgia.size() == MAX_NINJA / 2 && this.state == WAITING_STATE) {
            this.setState(START_STATE);
        }

        if (this.state == START_STATE && this.subState == State.INIT) {
            if (getTimeInSeconds() <= 2.8 * 600_000) {
                this.setSubState(State.HIEN_THI_PHU_THUY);
                this.timeDoiKeo = System.currentTimeMillis() + 10_000;
            }
        }

        if (timeDoiKeo != -1 && System.currentTimeMillis() > this.timeDoiKeo) {
            val lose = getLose();
            if (lose != -1) {
                Manager.chatKTG("Phù thuỷ bí ngô: " + (lose == PK_TRANG ? "Kẹo Trắng cho xin ít kẹo đê" : "Kẹo Đen cho kẹo hay bị ghẹo"));
                attackingTeam = lose;
            }
        }

        if (attackingTeam != -1) {
            attackTeam(attackingTeam);
        }

    }

    public synchronized boolean enough() {
        val count = Stream.concat(this.hacgia.stream(), this.bachgia.stream()).count();
        return count >= MAX_NINJA;
    }
}
