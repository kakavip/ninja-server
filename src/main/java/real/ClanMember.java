package real;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;

public class ClanMember {
    public byte typeclan;
    public int charID;
    public int clevel;
    public byte nClass;
    public int pointClan;
    public int pointClanWeek;
    @NotNull
    public String clanName;
    @NotNull
    public String cName;
    @NotNull
    private Ninja ninja;

    public ClanMember(final @NotNull String clanN, final byte typeclan, final @NotNull Ninja n) {
        this.clanName = "";
        this.typeclan = -1;
        this.cName = "";
        this.pointClan = 0;
        this.pointClanWeek = 0;
        this.clanName = clanN;
        this.typeclan = typeclan;
        this.charID = n.id;
        this.cName = n.name;
        this.nClass = n.nclass;
        this.clevel = n.getLevel();
        this.setNinja(n);
    }

    public ClanMember(final @NotNull String clanN, final @Nullable Ninja n) {
        if (n == null) throw new RuntimeException("Ninja is null");
        this.clanName = "";
        this.typeclan = -1;
        this.cName = "";
        this.pointClan = 0;
        this.pointClanWeek = 0;
        this.clanName = clanN;
        this.charID = n.id;
        this.cName = n.name;
        this.nClass = n.nclass;
        this.clevel = n.getLevel();
    }

    public ClanMember() {
        this.clanName = "";
        this.typeclan = -1;
        this.cName = "";
        this.pointClan = 0;
        this.pointClanWeek = 0;
    }

    @Override
    @NotNull
    public String toString() {
        return "[" +
                "charID=" + charID +
                ", cName=\"" + cName + "\"" +
                ",clanName=\"" + clanName + "\"" +
                ", typeclan=" + typeclan +
                ", clevel=" + clevel +
                ", nClass=" + nClass +
                ", pointClan=" + pointClan +
                ", pointClanWeek=" + pointClanWeek +
                ']';
    }

    @NotNull
    public static ClanMember fromJSONArray(final @NotNull JSONArray jar2) {
        final ClanMember mem = new ClanMember();

        mem.charID = Integer.parseInt(jar2.get(0).toString());
        mem.cName = jar2.get(1).toString();
        mem.clanName = jar2.get(2).toString();
        mem.typeclan = Byte.parseByte(jar2.get(3).toString());
        mem.clevel = Integer.parseInt(jar2.get(4).toString());
        mem.nClass = Byte.parseByte(jar2.get(5).toString());
        mem.pointClan = Integer.parseInt(jar2.get(6).toString());
        mem.pointClanWeek = Integer.parseInt(jar2.get(7).toString());
        return mem;
    }

    public Ninja getNinja() {
        if (ninja == null) {
            ninja = PlayerManager.getInstance().getNinja(cName);
        }
        return ninja;
    }

    public void setNinja(Ninja ninja) {
        this.ninja = ninja;
    }

    @Nullable
    public ClanManager clanManager() {
        return ClanManager.getClanByName(clanName);
    }
}
