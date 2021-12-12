package battle;

import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import patch.Constants;
import interfaces.IBattle;
import interfaces.IGlobalBattler;
import interfaces.SendMessage;
import real.Ninja;
import server.Service;
import server.util;
import threading.Manager;
import threading.Map;
import threading.Server;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GBattle implements IBattle {

    public static int WATING_TIME;
    public static long START_TIME;
    protected long tick;
    protected volatile byte state;

    protected int bachGiaPoint;
    protected int hacGiaPoint;

    @NotNull
    protected List<@NotNull IGlobalBattler> hacgia;
    @NotNull
    protected List<@NotNull IGlobalBattler> bachgia;

    @NotNull
    protected final Lock LOCK = new ReentrantLock(true);
    protected long duration;

    public GBattle() {
        tick();
        state = INITIAL_STATE;
        hacgia = new CopyOnWriteArrayList<>();
        bachgia = new CopyOnWriteArrayList<>();
        bachGiaPoint = 0;
        hacGiaPoint = 0;
    }


    @Override
    public void tick() {
        tick = System.currentTimeMillis();
    }


    @Override
    public boolean isExpired() {
        return System.currentTimeMillis() - this.tick > duration;
    }

    @Override
    public @NotNull List<@Nullable IGlobalBattler> getTopBattlers() {
        return null;
    }

    @Override
    public int getState() {
        return state;
    }

    /**
     * Must call tick method in set state
     * @param state
     */
    public void setState(byte state) {
        tick();
        this.state = state;
        if (state == WAITING_STATE) {
            duration = WATING_TIME;
            for (IGlobalBattler iGlobalBattler : this.hacgia) {
                upPoint(iGlobalBattler, 0);
            }
            for (IGlobalBattler iGlobalBattler : this.bachgia) {
                upPoint(iGlobalBattler, 0);
            }
        } else if (state == START_STATE) {
            duration = START_TIME;
            notifyStart();
        } else if (state == END_STATE) {
            duration = -1;
            close();
        }

        middlewareState();

        if (this.duration != -1) {
            for (IGlobalBattler iGlobalBattler : bachgia) {
                Service.batDauTinhGio((SendMessage) iGlobalBattler, (int) getTimeInSeconds());
            }

            for (IGlobalBattler iGlobalBattler : hacgia) {
                Service.batDauTinhGio((SendMessage) iGlobalBattler, (int) getTimeInSeconds());
            }
        }
    }

    protected void middlewareState() {
        // Nothing
    }

    public void notifyStart() {
        Manager.serverChat("Server", "Chiến trường đã bắt đầu");
    }

    protected void upPoint(@Nullable final IGlobalBattler iGlobalBattler, int point) {
        if (iGlobalBattler != null) {
            iGlobalBattler.upPoint(point);
        }
    }

    @NotNull
    public static short[] generateRewards(int n) {
        if (n > Manager.LDGT_REWARD_ITEM_ID.length) {
            n = Manager.LDGT_REWARD_ITEM_ID.length;
        }
        short[] result = new short[n];

        while (n > 0) {
            result[n - 1] = Manager.LDGT_REWARD_ITEM_ID[util.nextInt(Manager.LDGT_REWARD_ITEM_ID.length)];
            n--;
        }
        return result;
    }

    @Override
    public @NotNull short @NotNull [] getRewards(@NotNull IGlobalBattler battle) {
        try {
            if ((battle.getPhe() != Constants.PK_TRANG && battle.getPhe() != Constants.PK_DEN) || battle.getPoint() < 1000) {
                return new short[0];
            } else {
                if (battle.getPoint() >= 1000) {

                    return generateRewards(10);
                } else if (battle.getPoint() >= 2000) {
                    return generateRewards(15);
                } else if (battle.getPoint() >= 3000) {
                    return generateRewards(20);
                } else if (battle.getPoint() >= 4000) {
                    return generateRewards(30);
                } else {
                    return new short[0];
                }
            }
        } catch (Exception e) {

        }
        return new short[0];
    }


    @Override
    public void reset() {

        this.hacgia.clear();
        this.bachgia.clear();
        hacGiaPoint = 0;
        bachGiaPoint = 0;
        this.duration = -1;
        this.state = INITIAL_STATE;
    }

    @Override
    public void start() {
        if (this.bachgia.size() == 0 || this.hacgia.size() == 0) {
            this.setState(IBattle.END_STATE);
            return;
        }
        this.setState(IBattle.START_STATE);
    }

    @Override
    public @NotNull String getResult(final @NotNull IGlobalBattler nj) {
        if (nj == null) return "";
        hacGiaPoint = calculatePoint(hacgia);

        bachGiaPoint = calculatePoint(bachgia);
        StringBuilder builder = new StringBuilder();

        if (hacGiaPoint > bachGiaPoint) {
            builder.append("Hắc giả giành chiến thắng");
        } else if (hacGiaPoint < bachGiaPoint) {
            builder.append("Bạch giả giành chiến thắng");
        } else {
            builder.append("Hai phe hoà nhau");
        }

        builder.append("\n").
                append("Bạch Giả: ")
                .append(bachGiaPoint)
                .append("\n")
                .append("Hắc giả: ")
                .append(hacGiaPoint).append("\n");

        final List<IGlobalBattler> highest = Stream.concat(hacgia.stream(), bachgia.stream())
                .sorted(Comparator.comparingInt(IGlobalBattler::getPoint))
                .limit(10)
                .collect(Collectors.toList());

        for (int i = 0, highestSize = highest.size(); i < highestSize; i++) {
            IGlobalBattler n = highest.get(i);
            val ninja = (Ninja) n;
            builder.append(i + 1)
                    .append(". ")
                    .append(ninja.name)
                    .append(":")
                    .append(ninja.getPoint())
                    .append("  điểm ")
                    .append(ninja.getPhe() == Constants.PK_TRANG ? "(Bạch)" : "(Hắc)")
                    .append("\n")
                    .append("Danh hiệu: ")
                    .append(getDanhHieu(ninja.getPoint()))
                    .append("\n");

        }

        builder.append("Điểm của bạn: ").append(nj.getPoint()).append(" điểm ")
                .append(nj.getPhe() == Constants.PK_TRANG ? "(Bạch)" : "(Hắc)");
        return builder.toString();

    }

    @Override
    public void updateBattler(@Nullable final IGlobalBattler battler, boolean isHuman, @Nullable final Object other) {
        if (battler == null || other == null) {
            return;
        }

        int point = 0;

        if (isHuman) {
            point = 15;
            ((Ninja) other).notifyMessage("Bạn đã bị " + ((Ninja) battler).name + " đánh bại");
        } else {
            point = 5;
        }

        if (battler instanceof Ninja) {
            if (((Ninja) battler).getTaskId() == 41) {
                if ((((Ninja) battler).getTaskIndex() == 1 && battler.getPhe() == Constants.PK_TRANG) ||
                        (((Ninja) battler).getTaskIndex() == 2 && battler.getPhe() == Constants.PK_DEN)) {
                    for (int i = 0; i < point; i++) {
                        ((Ninja) battler).upMainTask();
                    }
                }
            }
        }

        battler.upPoint(point);
    }

    @Override
    public long getTimeInSeconds() {
        return (this.duration - (System.currentTimeMillis() - tick)) / 1000;
    }


    @SneakyThrows
    @Override
    public boolean enter(IGlobalBattler member, int type) {

        Ninja ninja = (Ninja) member;
        if (this.state == INITIAL_STATE) {
            ninja.p.sendYellowMessage("Chiến trường chưa mở con không thể vào");
            return false;
        }

        if (this.state != WAITING_STATE && !hacgia.contains(member) && !bachgia.contains(member)) {
            ninja.p.sendYellowMessage("Giờ báo danh đã hết con vui lòng ở ngoài và đợi kết quả");
            return false;
        }
        if (hacgia.contains(member) && type == IBattle.CAN_CU_DIA_BACH) {
            ninja.p.sendYellowMessage("Con con đã vào phe hắc giả rồi không thể vào phe bạch giả được");
            return false;
        } else if (bachgia.contains(member) && type == CAN_CU_DIA_HAC) {
            ninja.p.sendYellowMessage("Con đã vào phe bạch giả rồi không thể nào vào phe  hắc giả được");
            return false;
        } else {
            if (!bachgia.contains(member) && type == IBattle.CAN_CU_DIA_BACH) {
                ninja.resetPoint();
                bachgia.add(ninja);
            } else if (!hacgia.contains(member) && type == CAN_CU_DIA_HAC) {
                ninja.resetPoint();
                hacgia.add(ninja);
            }
            final Map map = Server.getMapById(type);

            if (map != null) {
                val area = map.area[0];
                if (area != null) {
                    ninja.getPlace().leave(ninja.p);
                    ninja.x = map.template.x0;
                    ninja.y = map.template.y0;
                    if (type == IBattle.CAN_CU_DIA_BACH) {
                        ninja.changeTypePk(Constants.PK_TRANG);
                    } else if (type == IBattle.CAN_CU_DIA_HAC) {
                        ninja.changeTypePk(Constants.PK_DEN);
                    }
                    area.Enter(ninja.p);
                }

                Service.batDauTinhGio(ninja, (int) getTimeInSeconds());
            }
            return true;
        }

    }

    @NotNull
    protected String getDanhHieu(int point) {

        if (point >= 200 && point <= 599) {
            return "Hạ nhẫn";
        } else if (point >= 600 && point <= 1499) {
            return "Trung nhẫn";
        } else if (point >= 1500 && point <= 3999) {
            return "Thượng nhẫn";
        } else if (point >= 4000) {
            return " Nhẫn giả";
        } else {
            return "Trung lập";
        }
    }

    protected int calculatePoint(@NotNull List<@Nullable IGlobalBattler> phe) {
        int sum = 0;
        for (IGlobalBattler iGlobalBattler : phe) {
            if (iGlobalBattler == null) continue;
            sum += iGlobalBattler.getPoint();
        }
        return sum;
    }



    @Override
    public void update(Ninja nj) {
        LOCK.lock();
        try {
            if (this.isExpired()) {
                if (this.state == START_STATE) {
                    this.setState(END_STATE);
                } else if (this.state == WAITING_STATE) {
                    this.setState(START_STATE);
                }
                middlewareUpdateOnStateExpired();
            }

            middlewareOnUpdate();
        } catch (Exception e) {

        } finally {
            LOCK.unlock();
        }
    }

    protected void middlewareOnUpdate() {

    }

    protected void middlewareUpdateOnStateExpired() {

    }


    @Override
    public void close() {
        try {
            for (IGlobalBattler iGlobalBattler : this.bachgia) {
                iGlobalBattler.changeTypePk(Constants.PK_NORMAL);
                if (((Ninja) iGlobalBattler).getPlace() != null) {
                    ((Ninja) iGlobalBattler).getPlace().DieReturn(((Ninja) iGlobalBattler).p);
                }
            }

            for (IGlobalBattler iGlobalBattler : this.hacgia) {
                iGlobalBattler.changeTypePk(Constants.PK_NORMAL);
                if (((Ninja) iGlobalBattler).getPlace() != null) {
                    ((Ninja) iGlobalBattler).getPlace().DieReturn(((Ninja) iGlobalBattler).p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.setState(INITIAL_STATE);
    }

}
