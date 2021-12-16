package battle;

import boardGame.Place;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import patch.Constants;
import interfaces.IGlobalBattler;
import interfaces.SendMessage;
import real.*;
import server.Service;
import threading.Map;
import threading.Server;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClanBattle extends GBattle {

    @NotNull
    private final IGlobalBattler tocTruong2;
    @NotNull
    private final IGlobalBattler tocTruong1;
    private int xu1;
    private int xu2;

    @NotNull
    public static List<@NotNull ClanBattle> battles;

    static {
        battles = new CopyOnWriteArrayList<>();
    }

    public static void add(ClanBattle battle) {
        battles.add(battle);
    }

    public static void remove(ClanBattle battle) {
        battles.remove(battle);
    }

    @NotNull
    public java.util.Map<@NotNull Byte, @NotNull Place> openedMaps;

    public ClanBattle(@NotNull ClanBattleData clanBattleData) {
        this.xu1 = clanBattleData.getFinalXu();
        this.xu2 = clanBattleData.getFinalXu();
        this.tocTruong1 = PlayerManager.getInstance().getNinja(clanBattleData.getTocTruongId1());
        this.tocTruong2 = PlayerManager.getInstance().getNinja(clanBattleData.getTocTruongId2());

        this.bachgia = clanBattleData.getBachGiaIds().stream()
                .map(b -> PlayerManager.getInstance().getNinja(b)).filter(Objects::nonNull)
                .peek(n -> n.setClanBattle(this)).collect(Collectors.toList());
        this.hacgia = clanBattleData.getHacGiaIds().stream()
                .map(h -> PlayerManager.getInstance().getNinja(h)).filter(Objects::nonNull)
                .peek(h -> h.setClanBattle(this)).collect(Collectors.toList());

        Objects.requireNonNull(ClanManager.getClanByName(getClanNamePhe1()), " 01").setClanBattle(this);
        Objects.requireNonNull(ClanManager.getClanByName(getClanNamePhe2()), " 02").setClanBattle(this);
        this.tick = clanBattleData.getTick();
        init();
        add(this);
        this.setState(clanBattleData.getState());
    }

    public ClanBattle(final @NotNull IGlobalBattler tocTruong1, final @NotNull IGlobalBattler tocTruong2) {

        super();
        this.tocTruong1 = tocTruong1;
        this.tocTruong2 = tocTruong2;
        this.setState(DAT_CUOC_STATE);
        this.bachgia.add(tocTruong1);
        this.hacgia.add(tocTruong2);
        init();
        add(this);
    }

    private void init() {
        openedMaps = new HashMap<>();

        openedMaps.put(BAO_DANH_GT_BACH, Server.getMapById(BAO_DANH_GT_BACH).getFreeArea());
        openedMaps.put(BAO_DANH_GT_HAC, Server.getMapById(BAO_DANH_GT_HAC).getFreeArea());
        openedMaps.put(SANH_1, Server.getMapById(SANH_1).getFreeArea());
        openedMaps.put(HANH_LANG_1, Server.getMapById(HANH_LANG_1).getFreeArea());
        openedMaps.put(HANH_LANG_2, Server.getMapById(HANH_LANG_2).getFreeArea());
        openedMaps.put(HANH_LANG_3, Server.getMapById(HANH_LANG_3).getFreeArea());
        openedMaps.put(SANH_2, Server.getMapById(SANH_2).getFreeArea());

        for (Place place : openedMaps.values()) {
            place.battle = this;
        }
        Objects.requireNonNull(ClanManager.getClanByName(getClanNamePhe1()), "Clan 1").setClanBattle(this);
        Objects.requireNonNull(ClanManager.getClanByName(getClanNamePhe2()), "Clan 2").setClanBattle(this);

    }


    public void setXu(@NotNull IGlobalBattler tocTruong, int xu) {
        if (xu < 1000) {
            tocTruong1.notifyMessage("Xu đặt vào phải từ 1m xu trở lên mới mở gia tộc chiến được");
            setState(END_STATE);
            return;
        }
        if (tocTruong.equals(tocTruong1)) {
            tocTruong1.notifyMessage(getClanNamePhe1() + " đã đặt " + String.format("%,d", xu) + " xu");
            tocTruong2.notifyMessage(getClanNamePhe1() + " đã đặt " + String.format("%,d", xu) + " xu");
            this.xu1 = xu;
        } else if (tocTruong.equals(tocTruong2)) {
            tocTruong1.notifyMessage(getClanNamePhe2() + " đã đặt " + String.format("%,d", xu) + " xu");
            tocTruong2.notifyMessage(getClanNamePhe2() + " đã đặt " + String.format("%,d", xu) + " xu");
            this.xu2 = xu;
        }
        if (this.xu1 == this.xu2) {
            Ninja n1 = (Ninja) tocTruong1;
            Ninja n2 = (Ninja) tocTruong2;
            n1.upxuMessage(-getFinalXu());
            n2.upxuMessage(-getFinalXu());
            this.setState(WAITING_STATE);
        }
    }

    @Override
    public void setState(byte state) {
        tick();
        this.state = state;

        if (state == WAITING_STATE) {
            duration = WATING_TIME;

            enter(tocTruong1, BAO_DANH_GT_BACH);
            enter(tocTruong2, BAO_DANH_GT_HAC);
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
        } else if (state == DAT_CUOC_STATE) {
            this.duration = Battle.MATCHING_WAIT_FOR_INFORMATION;
        }

        if (this.duration != -1) {
            for (IGlobalBattler iGlobalBattler : bachgia) {
                if (state != START_TIME)
                    iGlobalBattler.resetPoint();
                Service.batDauTinhGio((SendMessage) iGlobalBattler, (int) getTimeInSeconds());
            }

            for (IGlobalBattler iGlobalBattler : hacgia) {
                if (state != START_STATE)
                    iGlobalBattler.resetPoint();
                Service.batDauTinhGio((SendMessage) iGlobalBattler, (int) getTimeInSeconds());
            }
        }
    }

    public void notifyStart() {
        for (IGlobalBattler iGlobalBattler : this.hacgia) {
            if (iGlobalBattler != null) {
                iGlobalBattler.notifyMessage("Trận chiến đã bắt đầu");
            }
        }

        for (IGlobalBattler iGlobalBattler : this.bachgia) {
            if (iGlobalBattler != null) {
                iGlobalBattler.notifyMessage("Trận chiến đã bắt đầu");
            }
        }
    }

    public int getFinalXu() {
//        if (this.xu1 != this.xu2) {
//            throw new RuntimeException("Xu not match");
//        }
        return this.xu1;
    }

    @Override
    public @NotNull short @NotNull [] getRewards(final @NotNull IGlobalBattler battle) {
        return null;
    }

    @Override
    public @NotNull String getResult(final @NotNull IGlobalBattler nj) {

        hacGiaPoint = calculatePoint(hacgia);
        bachGiaPoint = calculatePoint(bachgia);
        StringBuilder builder = new StringBuilder();


        if (hacGiaPoint < bachGiaPoint) {
            builder.append(getClanNamePhe1()).
                    append(" giành chiến thắng");
        } else if (hacGiaPoint > bachGiaPoint) {
            builder.append(getClanNamePhe2())
                    .append(" giành chiến thắng");
        } else {
            builder.append("Hai gia tộc hoà nhau");
        }

        builder.append("\n").
                append(getClanNamePhe1() + " ")
                .append(bachGiaPoint)
                .append("\n")
                .append(getClanNamePhe2() + " ")
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
                    .append(" điểm ")
                    .append(" gia tộc ")
                    .append(ninja.getPhe() == Constants.PK_TRANG ? getClanNamePhe1() : getClanNamePhe2())
                    .append("\n")
                    .append("Danh hiệu: ")
                    .append(getDanhHieu(ninja.getPoint()))
                    .append("\n");

        }

        builder.append("Điểm của bạn: ").append(nj.getPoint()).append(" điểm ")
                .append(nj.getPhe() == Constants.PK_TRANG ? getClanNamePhe1() : getClanNamePhe2());
        return builder.toString();
    }

    @NotNull
    public String getClanNamePhe1() {
        try {
            return ((Ninja) tocTruong1).clan.clanName;
        } catch (Exception e) {
            return "";
        }
    }

    @NotNull
    public String getClanNamePhe2() {
        return ((Ninja) tocTruong2).clan.clanName;
    }

    @Override
    public void updateBattler(final @NotNull IGlobalBattler battler, boolean isHuman, @NotNull final Object other) {
        if (isHuman) {
            battler.upPoint(1);
            ((Ninja) other).notifyMessage("Bạn đã bị " + ((Ninja) battler).name + " đánh bại");
        } else {
            val mob3 = ((Mob) other);
            int point = 0;
            if (mob3.lvboss == 1) {
                point = 2;
            } else if (mob3.lvboss == 2) {
                point = 3;
            }
            battler.upPoint(point);
        }
    }

    @SneakyThrows
    @Override
    public boolean enter(IGlobalBattler member, int mapId) {

        Ninja ninja = (Ninja) member;

        if (this.state == INITIAL_STATE) {
            ninja.p.sendYellowMessage("Gia tộc chiến chưa mở con không thể vào");
            return false;
        }

        if (this.state != WAITING_STATE && !hacgia.contains(member) && !bachgia.contains(member)) {
            ninja.p.sendYellowMessage("Giờ báo danh đã hết con vui lòng ở ngoài và đợi kết quả");
            return false;
        }

        if (hacgia.contains(member) && mapId == BAO_DANH_GT_BACH) {
            ninja.p.sendYellowMessage("Con con đã vào " + getClanNamePhe2() + " rồi không thể vào phe " + getClanNamePhe1() + " được");
            return false;
        } else if (bachgia.contains(member) && mapId == BAO_DANH_GT_HAC) {
            ninja.p.sendYellowMessage("Con đã vào phe " + getClanNamePhe1() + " rồi không thể nào vào phe  " + getClanNamePhe2() + " được");
            return false;
        }


        if (!bachgia.contains(member) && mapId == BAO_DANH_GT_BACH) {
            ninja.resetPoint();
            bachgia.add(ninja);
        } else if (!hacgia.contains(member) && mapId == BAO_DANH_GT_HAC) {
            ninja.resetPoint();
            hacgia.add(ninja);
        }


        final Map map = Server.getMapById(mapId);
        if (map != null) {

            if (mapId == BAO_DANH_GT_BACH) {
                ninja.changeTypePk(Constants.PK_TRANG);
            } else if (mapId == BAO_DANH_GT_HAC) {
                ninja.changeTypePk(Constants.PK_DEN);
            }
            ninja.enterSamePlace(openedMaps.get((byte) mapId), null);
            Service.batDauTinhGio(ninja, (int) getTimeInSeconds());
        }
        return true;
    }


    private void inform(@NotNull final String message, @NotNull final List<@Nullable IGlobalBattler> phe) {
        for (IGlobalBattler iGlobalBattler : phe) {
            if (iGlobalBattler == null) continue;
            iGlobalBattler.notifyMessage(message);
        }
    }

    @Override
    public void close() {

        for (IGlobalBattler iGlobalBattler : this.bachgia) {
            Ninja n = (Ninja) iGlobalBattler;
            try {
                n.changeTypePk(Constants.PK_NORMAL);
                n.getPlace().gotoHaruna(n.p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (IGlobalBattler iGlobalBattler : this.hacgia) {
            Ninja n = (Ninja) iGlobalBattler;
            try {
                n.changeTypePk(Constants.PK_NORMAL);
                n.getPlace().gotoHaruna(n.p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int hacGiaPoint = calculatePoint(this.hacgia);
        int bachGiaPoint = calculatePoint(this.bachgia);

        Ninja n1 = (Ninja) tocTruong1;
        Ninja n2 = (Ninja) tocTruong2;

        if (hacGiaPoint > bachGiaPoint) {
            n2.upxuMessage(2L * getFinalXu() * 95 / 100);
            inform("Gia tộc " + getClanNamePhe2() + " đã chiến thắng " + getClanNamePhe1() + " và nhận được " + String.format("%,d", 2 * getFinalXu() * 90 / 100) + " xu", hacgia);
            inform("Gia tộc " + getClanNamePhe1() + " thua" + getClanNamePhe2() + " và mất " + String.format("%,d", 2 * getFinalXu() * 90 / 100) + " xu", bachgia);
        } else if (hacGiaPoint < bachGiaPoint) {
            n1.upxuMessage(2L * getFinalXu() * 95 / 100);
            inform("Gia tộc " + getClanNamePhe1() + " đã chiến thắng " + getClanNamePhe2() + " và nhận được " + String.format("%,d", 2 * getFinalXu() * 90 / 100) + " xu", bachgia);
            inform("Gia tộc " + getClanNamePhe2() + " thua" + getClanNamePhe1() + " và mất " + String.format("%,d", 2 * getFinalXu() * 90 / 100) + " xu", hacgia);
        } else {
            n1.upxuMessage(getFinalXu() * 95L / 100);
            n2.upxuMessage(getFinalXu() * 95L / 100);
            inform("Hai gia tộc ngang sức phí tổ chức gia tộc chiến là 5% xu đặt vào mn chơi game vui vẻ không quạo", bachgia);
            inform("Hai gia tộc ngang sức phí tổ chức gia tộc chiến là 5% xu đặt vào mn chơi game vui vẻ không quạo", hacgia);
        }


        for (Place place : this.openedMaps.values()) {
            place.battle = null;
        }

        for (IGlobalBattler iGlobalBattler : this.hacgia) {
            Ninja n = (Ninja) iGlobalBattler;
            n.setClanBattle(null);
            n.setBattle(null);
            n.resetPoint();
        }

        for (IGlobalBattler iGlobalBattler : this.bachgia) {
            Ninja n = (Ninja) iGlobalBattler;
            n.setClanBattle(null);
            n.setBattle(null);
            n.resetPoint();
        }

        openedMaps.clear();
        this.hacgia.clear();
        this.bachgia.clear();

        this.hacgia = null;
        this.bachgia = null;
        openedMaps = null;

        Objects.requireNonNull(ClanManager.getClanByName(getClanNamePhe1())).setClanBattle(null);
        Objects.requireNonNull(ClanManager.getClanByName(getClanNamePhe2())).setClanBattle(null);

        remove(this);

    }

    @Override
    public void update(Ninja nj) {
        LOCK.lock();
        try {
            if (this.isExpired()) {
                if (this.state == DAT_CUOC_STATE) {
                    this.setState(END_STATE);
                }
                if (this.state == START_STATE) {
                    this.setState(END_STATE);
                } else if (this.state == WAITING_STATE) {
                    this.setState(START_STATE);
                }


            }
        } catch (Exception e) {

        } finally {
            LOCK.unlock();
        }
    }

    public ClanBattleData getBattleData() {
        if (this.state == START_STATE)
            return ClanBattleData.builder()
                    .finalXu(getFinalXu())
                    .tocTruongId1(((Ninja) tocTruong1).id)
                    .tocTruongId2(((Ninja) tocTruong2).id)
                    .bachGiaIds(bachgia.stream().map(b -> ((Ninja) b).id).collect(Collectors.toList()))
                    .hacGiaIds(hacgia.stream().map(h -> ((Ninja) h).get().id).collect(Collectors.toList()))
                    .tick(this.tick)
                    .state(this.state)
                    .build();

        return null;

    }

}
