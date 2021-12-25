package threading;

import com.sun.management.OperatingSystemMXBean;
import io.Session;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import battle.GBattle;
import patch.Resource;
import candybattle.CandyBattleManager;
import interfaces.IBattle;
import patch.RmiRemoteImpl;
import tournament.GeninTournament;
import tournament.KageTournament;
import tournament.Tournament;
import real.*;
import server.*;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.Naming;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static interfaces.IBattle.*;
import java.net.InetSocketAddress;
import static threading.Manager.MOMENT_REFRESH_BATTLE;

public class Server {
    public static long TIME_SLEEP_SHINWA_THREAD;
    private static Server instance;
    private static Runnable updateBattle;
    public IBattle globalBattle;
    private ServerSocket listenSocket;
    public volatile static boolean start;
    public Manager manager;

    @NotNull
    public MenuController menu;
    public ServerController controllerManager;
    public Controller serverMessageHandler;
    private static Map[] maps;
    public static short[] MOMENT_BOSS_REFRESH;
    private static final boolean[] isRefreshBoss;
    private static final short[] mapBossVDMQ;
    private static final short[] mapBoss45;
    private static final short[] mapBoss55;
    private static final short[] mapBoss65;
    private static final short[] mapBoss75;

    public static Runnable updateRefreshBoss;
    public static ExecutorService executorService = Executors.newFixedThreadPool(5);
    public static ClanTerritoryManager clanTerritoryManager = new ClanTerritoryManager();
    public static Tournament kageTournament;
    public static Tournament geninTournament;
    public static java.util.Map<String, Resource> resource;
    public DaemonThread daemonThread;

    @NotNull
    public static CandyBattleManager candyBattleManager;

    public Server() {
        this.listenSocket = null;
        resource = new ConcurrentHashMap<>();
    }

    private void init() {
        this.manager = new Manager();
        this.menu = new MenuController();
        this.controllerManager = new RealController();
        this.serverMessageHandler = new Controller();
        this.globalBattle = new GBattle();
        Server.kageTournament = KageTournament.gi();
        Server.geninTournament = GeninTournament.gi();
        Server.candyBattleManager = new CandyBattleManager();
        updateRefreshBoss = () -> {

            synchronized (ClanManager.entrys) {
                for (int i = ClanManager.entrys.size() - 1; i >= 0; --i) {
                    final ClanManager clan = ClanManager.entrys.get(i);
                    if (util.compare_Week(Date.from(Instant.now()), util.getDate(clan.week))) {
                        clan.payfeesClan();
                    }
                }
            }

            final Calendar rightNow = Calendar.getInstance();
            final short moment = (short) rightNow.get(Manager.BOSS_WAIT_TIME_UNIT);
            for (int j = 0; j < Server.MOMENT_BOSS_REFRESH.length; ++j) {
                if (Server.MOMENT_BOSS_REFRESH[j] == moment) {
                    if (!Server.isRefreshBoss[j]) {
                        String textchat = "Thần thú đã suất hiện tại";
                        for (byte k = 0; k < util.nextInt(1, 1); ++k) {
                            final Map map = Manager.getMapid(Server.mapBoss75[util.nextInt(Server.mapBoss75.length)]);
                            if (map != null) {
                                map.refreshBoss(17);
                                textchat = textchat + " " + map.template.name;
                                Server.isRefreshBoss[j] = true;
                            }
                        }
                        for (byte k = 0; k < util.nextInt(1, 2); ++k) {
                            final Map map = Manager.getMapid(Server.mapBoss65[util.nextInt(Server.mapBoss65.length)]);
                            if (map != null) {
                                map.refreshBoss(17);
                                textchat = textchat + ", " + map.template.name;
                                Server.isRefreshBoss[j] = true;
                            }
                        }
                        for (byte k = 0; k < util.nextInt(1, 2); ++k) {
                            final Map map = Manager.getMapid(Server.mapBoss55[util.nextInt(Server.mapBoss55.length)]);
                            if (map != null) {
                                map.refreshBoss(17);
                                textchat = textchat + ", " + map.template.name;
                                Server.isRefreshBoss[j] = true;
                            }
                        }
                        for (byte k = 0; k < util.nextInt(1, 2); ++k) {
                            final Map map = Manager.getMapid(Server.mapBoss45[util.nextInt(Server.mapBoss45.length)]);
                            if (map != null) {
                                map.refreshBoss(17);
                                textchat = textchat + ", " + map.template.name;
                                Server.isRefreshBoss[j] = true;
                            }
                        }
                        for (byte k = 0; k < Server.mapBossVDMQ.length; ++k) {
                            final Map map = Manager.getMapid(Server.mapBossVDMQ[k]);
                            if (map != null) {
                                map.refreshBoss(17);
                                textchat = textchat + ", " + map.template.name;
                                Server.isRefreshBoss[j] = true;
                            }
                        }

                        for (short i : mapBossLC) {
                            val map = Manager.getMapid(i);
                            if (map != null) {
                                map.refreshBoss(util.nextInt(0, 3));
                                textchat = textchat + ", " + map.template.name;
                                Server.isRefreshBoss[j] = true;
                            }
                        }
                        try {
                            Manager.chatKTG(textchat);
                        } catch (IOException e) {
                        }
                    }
                } else {
                    Server.isRefreshBoss[j] = false;
                }
            }
        };
        updateBattle = () -> {
            final Calendar rightNow = Calendar.getInstance();
            final short moment = (short) rightNow.get(Manager.BOSS_WAIT_TIME_UNIT);
            for (int i = 0; i < MOMENT_REFRESH_BATTLE.length; i++) {

                if (MOMENT_REFRESH_BATTLE[i] == moment) {
                    if (this.globalBattle.getState() == INITIAL_STATE) {
                        this.globalBattle.reset();
                        this.globalBattle.setState(WAITING_STATE);
                    }
                    long second = Server.this.globalBattle.getTimeInSeconds();
                    if (second > 0 && this.globalBattle.getState() == WAITING_STATE) {
                        int phut = (int) (second / 60);
                        Manager.serverChat("Server", "Chiến trường sẽ bắt đầu trong " + (phut > 0 ? phut : second)
                                + (phut > 0 ? " phút" : " giây"));
                    }
                }

            }
        };

        clanTerritoryManager.start();

    }

    private static final Object MUTEX = new Object();

    public static Server getInstance() {
        if (Server.instance == null) {
            synchronized (MUTEX) {
                (Server.instance = new Server()).init();
            }
            instance.daemonThread = new DaemonThread();
            BXHManager.init();
            instance.daemonThread.addRunner(Server.updateRefreshBoss);
            instance.daemonThread.addRunner(Server.updateBattle);
        }
        return Server.instance;
    }

    public static boolean threadRunning = true;
    public static Thread t;

    public static void main(final String[] args) {
        Server.start = true;
        getInstance().run();

        // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        // threadRunning = false;
        // if (t != null) {
        // t.interrupt();
        // }
        // }));
        // t = new Thread(() -> {
        // while (threadRunning) {
        //
        // OperatingSystemMXBean osBean =
        // ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        // int pt = (int) (osBean.getProcessCpuLoad() * 100);
        // if (pt > 80) {
        // getInstance().stop();
        // getInstance().run();
        // }
        //
        // }
        // });
        // t.start();
        try {
            t.join();
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        this.setMaps(new Map[MapTemplate.arrTemplate.length]);
        for (short i = 0; i < Server.maps.length; ++i) {
            Server.maps[i] = new Map(i, null);
        }

        this.listenSocket = null;
        try {
            this.listenSocket = new ServerSocket(this.manager.PORT);
            System.out.println("Listenning port " + this.manager.PORT);

            try {
                // if (!util.debug) {
                Naming.rebind("rmi://127.0.0.1:16666/tinhtoan", new RmiRemoteImpl());
                // }
                System.out.println("Start rmi success");
            } catch (Exception e) {
                System.out.println("Start rmi fail");
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("RUN HOOK");
                for (Session conn : PlayerManager.getInstance().conns) {
                    if (conn != null && conn.user != null) {
                        conn.user.flush();
                    }
                }
                for (ClanManager entry : ClanManager.entrys) {
                    entry.flush();
                }
                System.out.println("CLOSE SERVER");
                stop();

            }));
            while (Server.start) {
                final Socket clientSocket = this.listenSocket.accept();
                InetSocketAddress socketAddress = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
                String clientIpAddress = socketAddress.getAddress().getHostAddress();
                if (!Session.check(clientIpAddress)) {
                    final Session conn = new Session(clientSocket, this.serverMessageHandler);
                    PlayerManager.getInstance().put(conn);
                    conn.start();
                    System.out.println("Accept socket size :" + PlayerManager.getInstance().conns_size());
                } else {
                    clientSocket.close();
                }
            }
        } catch (BindException bindEx) {
            System.exit(0);
        } catch (SocketException genEx) {
            System.out.println("Socket Closed");
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
        try {
            if (this.listenSocket != null) {
                this.listenSocket.close();
            }
            util.Debug("Close server socket");
        } catch (Exception ex) {
        }

    }

    public void stop() {
        if (Server.start) {
            Server.start = false;
            try {
                Tournament.closeAll();
            } catch (Exception e) {

            }
            try {
                Server.candyBattleManager.close();
            } catch (Exception e) {

            }
            try {
                this.listenSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try {
                ClanManager.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                this.daemonThread.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                executorService.shutdown();
                if (executorService.awaitTermination(300, TimeUnit.MILLISECONDS)) {
                    System.out.println("CLOSE");
                }
            } catch (Exception e) {
            }

            try {
                if (executorService.awaitTermination(300, TimeUnit.MILLISECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (Exception e) {
                executorService.shutdownNow();
                e.printStackTrace();
            }

            try {
                if (executorService.isShutdown()) {
                    util.Debug("Shut down executor success");
                    executorService = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                PlayerManager.getInstance().Clear();
                PlayerManager.getInstance().close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.manager.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Server.clanTerritoryManager.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.manager = null;
            this.menu = null;
            this.controllerManager = null;
            this.serverMessageHandler = null;

            try {
                SQLManager.close();
            } catch (Exception e) {

            }

            System.gc();
        }
    }

    private static final short[] mapBossLC;

    static {
        Server.instance = null;
        Server.start = false;
        isRefreshBoss = new boolean[] { false, false, false, false, false, false };
        mapBossVDMQ = new short[] { 141, 142, 143 };
        mapBoss45 = new short[] { 14, 15, 16, 34, 35, 52, 68 };
        mapBoss55 = new short[] { 44, 67 };
        mapBoss65 = new short[] { 24, 41, 45, 59 };
        mapBoss75 = new short[] { 18, 36, 54 };
        mapBossLC = new short[] { 134, 135, 136, 137 };
    }

    public Map[] getMaps() {
        return Server.maps;
    }

    public void setMaps(Map[] maps) {
        Server.maps = maps;
    }

    public static Map getMapById(int i) {
        return maps[i];
    }
}
