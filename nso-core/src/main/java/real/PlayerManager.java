package real;

import org.jetbrains.annotations.NotNull;
import threading.Message;
import io.Session;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PlayerManager {
    protected static PlayerManager instance;
    private boolean runing;
    public final List<Session> conns;
    private final HashMap<Integer, Session> conns_id;
    private final HashMap<Integer, User> players_id;
    private final HashMap<String, User> players_uname;
    private final HashMap<Integer, Ninja> ninjas_id;
    private final HashMap<String, Ninja> ninjas_name;

    public PlayerManager() {
        this.runing = true;
        this.conns = new CopyOnWriteArrayList<>();
        this.conns_id = new HashMap<>();
        this.players_id = new HashMap<>();
        this.players_uname = new HashMap<>();
        this.ninjas_id = new HashMap<>();
        this.ninjas_name = new HashMap<>();
    }

    private final static ReadWriteLock lock = new ReentrantReadWriteLock(true);

    @NotNull
    public static PlayerManager getInstance() {
        if (PlayerManager.instance == null) {
            PlayerManager.instance = new PlayerManager();
        }
        return PlayerManager.instance;
    }


    public void NinjaMessage(final Message m) {
        for (int i = this.conns.size() - 1; i >= 0; --i) {
            if (this.conns.get(i).user != null && this.conns.get(i).user.nj != null) {
                this.conns.get(i).sendMessage(m);
            }
        }
    }

    public void put(final Session conn) {
        this.conns_id.put(conn.id, conn);
        this.conns.add(conn);
    }

    public void put(final User p) {
        this.players_id.put(p.id, p);
        this.players_uname.put(p.username, p);
    }

    public void put(final Ninja n) {
        this.ninjas_id.put(n.id, n);
        this.ninjas_name.put(n.name, n);
    }

    private void remove(final Session conn) {
        this.conns_id.remove(conn.id);
        this.conns.remove(conn);
        if (conn.user != null) {
            this.remove(conn.user);
        }
    }

    private void remove(final User p) {
        this.players_id.remove(p.id);
        this.players_uname.remove(p.username);
        if (p.nj != null) {
            this.remove(p.nj);
        }
        p.close();
        p.flush();
    }

    private void remove(final Ninja n) {
        this.ninjas_id.remove(n.id);
        this.ninjas_name.remove(n.name);
        n.close();
        n.flush();
        if (n.clone != null) {
            n.clone.flush();
        }
    }

    public Session getConn(final int id) {
        return this.conns_id.get(id);
    }

    public User getPlayer(final int id) {
        return this.players_id.get(id);
    }

    public User getPlayer(final String uname) {
        return this.players_uname.get(uname);
    }

    public Ninja getNinja(final int id) {
        return this.ninjas_id.get(id);
    }

    public Ninja getNinja(final String name) {
        return this.ninjas_name.get(name);
    }

    public int conns_size() {
        return this.conns_id.size();
    }

    public int players_size() {
        return this.players_id.size();
    }

    public int ninja_size() {
        return this.ninjas_id.size();
    }

    public void kickSession(final Session conn) {
        if (conn != null) {
            this.remove(conn);
            if (conn.user != null &&
                    conn.user.nj != null && conn.user.nj.getPlace() != null) {
                conn.user.nj.getPlace().leave(conn.user);
            }
        }
    }

    public void Clear() {
        while (!this.conns.isEmpty()) {
            this.kickSession(this.conns.get(0));
        }
    }

    public void close() {
        this.runing = false;
        PlayerManager.instance = null;
    }
}
