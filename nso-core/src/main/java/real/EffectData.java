package real;

import java.util.ArrayList;

public class EffectData
{
    public int id;
    public byte type;
    public String name;
    public short iconId;
    public static ArrayList<EffectData> entrys;
    
    static {
        EffectData.entrys = new ArrayList<>();
    }
}
