package real;

import boardGame.Place;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import threading.Message;
import lombok.SneakyThrows;
import lombok.val;
import interfaces.TeamBattle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Party implements TeamBattle {
    public int master;
    public boolean lock;
    @NotNull
    public final List<@NotNull Ninja> ninjas;
    public List<Integer> pt;
    public int id;
    public Cave cave;
    public static int basid;
    private Battle battle;

    protected Party(final @NotNull Ninja n) {
        this.master = -1;
        this.lock = false;
        this.ninjas = new ArrayList<>();
        this.pt = new ArrayList<>();
        this.master = n.id;
        this.id = Party.basid++;
    }

    protected void addPartyAccept(final @NotNull Ninja n) {
        this.ninjas.add(n);
        (n.party = this).refreshTeam();
    }

    public void sendMessage(final @NotNull Message m) {
        for (byte i = 0; i < this.ninjas.size(); ++i) {
            this.ninjas.get(i).p.sendMessage(m);
        }
    }

    @Override
    public @NotNull List<@NotNull Ninja> getNinjas() {
        return this.ninjas;
    }

    @Override
    public Battle getBattle() {
        return this.battle;
    }

    @Override
    public void setBattle(@NotNull final Battle battle) {
        this.battle = battle;
        for (Ninja n : this.ninjas) {
            n.setBattle(battle);
        }
    }

    @Override
    public void updateEffect(final @NotNull Effect effect) {
        for (Ninja ninja : this.ninjas) {
            ninja.updateEffect(effect);
        }
    }

    @Override
    public int getCurrentMapId() {
        return getNinja(this.master).getMapid();
    }

    @Override
    public @NotNull String getTeamName() {
        val nj = getNinja(this.master);
        if (nj == null) return "";
        return nj.name;
    }

    @Nullable
    public Ninja getNinja(final int id) {
        synchronized (this) {
            for (byte i = 0; i < this.ninjas.size(); ++i) {
                if (this.ninjas.get(i).id == id) {
                    return this.ninjas.get(i);
                }
            }
            return null;
        }
    }

    public void openCave(final Cave cave, final String name) {
        synchronized (this) {
            this.cave = cave;
            for (byte i = 0; i < this.ninjas.size(); ++i) {
                this.ninjas.get(i).p.sendYellowMessage(name + " đã mở Cửa hang động");
            }
        }
    }

    protected void addParty(final @Nullable User p1, final @Nullable User p2) throws IOException {
        if (p1 == null || p2 == null) return;

        if (this.ninjas.size() > 5) {
            p1.sendYellowMessage("Số lượng thành viên đã tối đa");
            return;
        }
        this.pt.add(p2.session.id);
        final Message m = new Message(79);
        m.writer().writeInt(p1.nj.id);
        m.writer().writeUTF(p1.nj.name);
        m.writer().flush();
        p2.sendMessage(m);
        m.cleanup();
    }

    public void changeTeamLeader(final int index) {
        final Ninja n1 = this.getNinja(this.master);
        synchronized (this) {
            final Ninja n2 = this.ninjas.get(index);
            if (n1 != null && n1 != n2) {
                this.ninjas.set(index, n1);
                this.ninjas.set(0, n2);
                this.master = n2.id;
                for (byte i = 0; i < this.ninjas.size(); ++i) {
                    this.ninjas.get(i).p.sendYellowMessage(n2.name + " đã được lên làm nhóm trưởng");
                }
                this.refreshTeam();
            }
        }
    }

    public void moveMember(final int index) {
        synchronized (this) {
            try {
                final Ninja n = this.ninjas.remove(index);
                n.party = null;
                for (byte i = 0; i < this.ninjas.size(); ++i) {
                    this.ninjas.get(i).p.sendYellowMessage(n.name + " đã bị đuổi ra khỏi nhóm");
                }
                this.refreshTeam();
                final Message m = new Message(83);
                m.writer().flush();
                n.p.sendMessage(m);
                m.cleanup();
                n.p.sendYellowMessage("Bạn đã bị đuổi ra khỏi nhóm");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void exitParty(final @Nullable Ninja n) {
        if (n == null) return;

        synchronized (this) {
            if (this.ninjas.contains(n)) {
                try {
                    final Message m = new Message(83);
                    m.writer().flush();
                    n.p.sendMessage(m);
                    m.cleanup();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                this.ninjas.remove(n);
                if (this.ninjas.size() > 0) {
                    this.refreshTeam();
                    for (byte i = 0; i < this.ninjas.size(); ++i) {
                        this.ninjas.get(i).p.sendYellowMessage(n.name + " đã rời khỏi nhóm");
                    }
                    if (n.id == this.master) {
                        this.master = this.ninjas.get(0).id;
                        for (byte i = 0; i < this.ninjas.size(); ++i) {
                            this.ninjas.get(i).p.sendYellowMessage(this.ninjas.get(0).name + " đã đã được lên làm nhóm trưởng");
                        }
                    }
                } else {
                    this.master = -1;
                    this.ninjas.clear();
                    this.pt.clear();
                }
                n.get().party = null;
            }
        }
    }

    public void refreshTeam() {
        try {
            final Message m = new Message(82);
            m.writer().writeBoolean(this.lock);
            for (byte i = 0; i < this.ninjas.size(); ++i) {
                m.writer().writeInt(this.ninjas.get(i).get().id);
                m.writer().writeByte(this.ninjas.get(i).get().nclass);
                m.writer().writeUTF(this.ninjas.get(i).name);
            }
            m.writer().flush();
            for (byte i = 0; i < this.ninjas.size(); ++i) {
                this.sendMessage(m);
            }
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SneakyThrows
    @Override
    public void enterSamePlace(final @Nullable Place place, final @Nullable TeamBattle other) {
        if (place == null) return;
        val x0 = place.map.template.x0;
        val y0 = place.map.template.y0;

        for (Ninja ninja : this.ninjas) {
            ninja.enterSamePlace(place, null);
        }

        if (other == null) return;
        other.enterSamePlace(place, null);

    }

    @SneakyThrows
    @Override
    public void changeTypePk(short typePk, final @Nullable TeamBattle notifier) {
        if (notifier == null) return;
        for (Ninja n : this.ninjas) {
            n.setTypepk(typePk);
            val m = new Message(-30);
            m.writer().writeByte(-92);
            m.writer().writeInt(n.id);
            m.writer().writeByte(typePk);
            sendMessage(m);
            notifier.sendMessage(m);
            m.cleanup();
        }
    }


    @Override
    public void notifyMessage(final @NotNull String message) {
        val newMessage = message.replace("#", "Đội con");
        for (Ninja ninja : this.ninjas) {
            ninja.p.sendYellowMessage(newMessage);
        }
    }

    @Override
    public void upXuMessage(long xu) {
        for (Ninja ninja : this.ninjas) {
            if (ninja.id == master) {
                ninja.upxuMessage(xu);
            }
        }
    }

    @Override
    public int getMaster() {
        return master;
    }

    @Override
    public boolean hasBattle() {
        return battle != null;
    }


    @Override
    public void clearBattle() {
        TeamBattle.super.clearBattle();
        for (Ninja ninja : this.ninjas) {
            ninja.clearBattle();
        }
    }

    @Override
    public boolean loose() {
        int countIn111Map = 0;
        int die = 0;
        for (Ninja ninja : this.ninjas) {
            if (ninja.getMapid() == 111) {
                countIn111Map++;
            }

            if (ninja.isDie) {
                die++;
            }
        }

        return countIn111Map != this.ninjas.size() || die == this.ninjas.size();
    }

    @Nullable
    public Ninja getKey() {
        return this.getNinja(this.master);
    }

    @Override
    public int getMapId() {
        return this.getNinja(this.master).getMapId();
    }

    @Override
    public short getKeyLevel() {
        return (short) getNinja(this.master).getLevel();
    }

    @Override
    public int getXu() {
        return getNinja(master).getXu();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Party party = (Party) o;
        return id == party.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
