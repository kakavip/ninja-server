package threading;

import io.Session;
import lombok.SneakyThrows;
import lombok.val;
import clan.ClanThanThu;
import patch.ItemShinwaManager;
import tournament.GeninTournament;
import tournament.KageTournament;
import tournament.Tournament;
import real.ClanManager;
import real.Item;
import real.ItemData;
import real.PlayerManager;
import real.User;
import server.TopEventManager;
import server.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static patch.Constants.TRUNG_DI_LONG_ID;
import static patch.Constants.TRUNG_HAI_MA_ID;

public class DaemonThread extends Thread {

    private final int TIME_TO_DISCONNECT;
    private volatile boolean isRunning;
    private final PlayerManager playerManager;
    private final List<Runnable> runner;
    private Timer timmer;
    private TimerTask task;
    private Thread updateStateThread;
    private Thread returnThread;

    public DaemonThread() {
        this.setName("Session Collector");
        this.setPriority(Thread.MIN_PRIORITY);
        this.setDaemon(true);
        isRunning = true;
        TIME_TO_DISCONNECT = Manager.TIME_DISCONNECT * 1000;
        playerManager = PlayerManager.getInstance();
        runner = new CopyOnWriteArrayList<>();
        start();
    }

    private boolean resetTourament = false;

    @SneakyThrows
    @Override
    public void run() {

        timmer = new Timer("Boss timmer");
        task = new TimerTask() {
            @Override
            public void run() {
                for (Runnable runnable : runner) {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        updateStateThread = new Thread(ItemShinwaManager.getRunnable());
        updateStateThread.setPriority(Thread.MIN_PRIORITY);
        updateStateThread.start();
        returnThread = new Thread(ItemShinwaManager.getReturnThread());
        returnThread.setPriority(Thread.MIN_PRIORITY);
        returnThread.start();

        timmer.schedule(task, 0, TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES));

        while (this.isRunning) {
            try {
                rewardTopTournament();
                flushAndControlConnectUser();
                flushClanData();
                flushTournamentData();
                final int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                final int minute = Calendar.getInstance().get(Calendar.MINUTE);

                if (hour == 23 &&
                        minute == 0 && (Tournament.lastTimeReward == -1
                                || System.currentTimeMillis() - Tournament.lastTimeReward > 3600000L)) {
                    GeninTournament.gi().rewardNinja();
                    KageTournament.gi().rewardNinja();

                    GeninTournament.gi().reset();
                    KageTournament.gi().reset();

                    Tournament.lastTimeReward = System.currentTimeMillis();
                }

                if (minute % 30 == 0 && System.currentTimeMillis() < Manager.EVENT_TOP_END_TIME * 1000L) {
                    TopEventManager.reload();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    synchronized (this) {
                        wait(60_000L);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private void flushTournamentData() {
        GeninTournament.gi().flush();
        KageTournament.gi().flush();
    }

    private void flushAndControlConnectUser() {
        List<String> blackListIps = util.ReadBlackListIps().stream().map(ip -> ip.getName())
                .collect(Collectors.toList());
        long currentTime = System.currentTimeMillis();
        for (Session conn : playerManager.conns) {
            try {
                if (conn != null) {
                    if (currentTime - conn.lastTimeReceiveData > TIME_TO_DISCONNECT
                            || blackListIps.contains(conn.getClientIpAddress())) {
                        conn.disconnect();
                        PlayerManager.getInstance().kickSession(conn);

                    } else {
                        if (conn.user != null)
                            conn.user.flush();
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    private void rewardTopTournament() {
        for (Session conn : playerManager.conns) {
            try {
                if (conn != null && conn.user != null) {
                    User p = conn.user;

                    if (KageTournament.gi().rewardNinja(p.nj)) {
                        p.session.sendMessageLog(
                                "Bạn vừa nhận được phần thưởng khi nằm trong top Thiên Bảng ngày hôm qua.");
                    }
                    if (GeninTournament.gi().rewardNinja(p.nj)) {
                        p.session.sendMessageLog(
                                "Bạn vừa nhận được phần thưởng khi nằm trong top Địa Bảng ngày hôm qua.");
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void flushClanData() throws IOException {
        final short moment = (short) Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        val isMonday = moment == Calendar.MONDAY;
        for (ClanManager entry : ClanManager.entrys) {
            if (entry != null) {
                for (Item item : entry.items) {
                    if (item.isExpires && item.expireTrung()
                            && (item.id == TRUNG_DI_LONG_ID || item.id == TRUNG_HAI_MA_ID)) {
                        if (item.id == TRUNG_DI_LONG_ID) {
                            if (entry.containThanThu(0)) {
                                if (!entry.containThanThu(2)) {
                                    entry.sendMessage(entry.createMessage("Gia tộc bạn nhận được hoả long"));
                                    entry.clanThanThus
                                            .add(new ClanThanThu(ItemData.itemDefault(ClanThanThu.HOA_LONG_ID),
                                                    ClanThanThu.MAX_THAN_THU_EXPS, ClanThanThu.MAX_THAN_THU_EXPS, 0));
                                }
                            } else {
                                entry.sendMessage(entry.createMessage("Gia tộc bạn nhận được dị long cáp 1"));
                                entry.clanThanThus.add(new ClanThanThu(ItemData.itemDefault(ClanThanThu.DI_LONG_1_ID),
                                        0, ClanThanThu.MAX_THAN_THU_EXPS, 0));
                            }
                        } else {
                            entry.sendMessage(entry.createMessage("Gia tộc bạn nhận được hải mã cấp 1"));
                            entry.clanThanThus.add(new ClanThanThu(ItemData.itemDefault(ClanThanThu.HAI_MA_1_ID), 0,
                                    ClanThanThu.MAX_THAN_THU_EXPS, 1));
                        }
                        entry.removeItem(item.id, 1);
                    }
                }
                try {
                    if (isMonday) {
                        entry.openDun = 3;
                    }
                    entry.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public synchronized void close() {
        task.cancel();
        timmer.cancel();
        timmer.purge();
        updateStateThread.interrupt();
        returnThread.interrupt();
        ItemShinwaManager.updateRunning = false;
        ItemShinwaManager.returnRunning = false;

        returnThread = null;
        updateStateThread = null;
        this.isRunning = false;
        interrupt();

    }

    public synchronized void addRunner(Runnable runnable) {
        this.runner.add(runnable);
    }

}
