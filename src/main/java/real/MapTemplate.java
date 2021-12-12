package real;

public class MapTemplate
{
    public int id;
    public byte tileID;
    public byte bgID;
    public String name;
    public byte typeMap;
    public byte numarea;
    public byte maxplayers;
    public Vgo[] vgo;
    public Npc[] npc;
    public short[] arMobid;
    public short[] arrMobx;
    public short[] arrMoby;
    public byte[] arrMobstatus;
    public int[] arrMoblevel;
    public byte[] arrLevelboss;
    public boolean[] arrisboss;
    public short x0;
    public short y0;
    public static MapTemplate[] arrTemplate;
}
