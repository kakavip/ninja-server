package real;

import java.util.HashMap;

public class MobData {
    public int id;
    public byte type;
    public String name;
    public int hp;
    public byte rangeMove;
    public byte speed;
    public short[] arrIdItem;
    public static HashMap<Integer, MobData> entrys;

    public static MobData getMob(final int id) {
        return entrys.get(id);
    }

    static {
        MobData.entrys = new HashMap<>();
    }
}
