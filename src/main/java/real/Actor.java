package real;

public class Actor
{
    public int id;
    public String[] sortNinja;
    public int luong;
    public int diamond;
    public int ticketGold;
    public int typemenu;
    public long chatKTGdelay;
    
    public Actor() {
        this.sortNinja = new String[3];
        this.luong = 0;
        this.diamond = 0;
        this.ticketGold = 0;
        this.typemenu = -1;
        this.chatKTGdelay = 0L;
    }
}
