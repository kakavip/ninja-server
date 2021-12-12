package real;

public class BuNhin {

    public String name;
    public short x;
    public short y;
    public long coldDown;
    public int ninjaId;
    public int hp;


    public BuNhin(String name, short x, short y, int coldDown, int id, int hp) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.coldDown = System.currentTimeMillis() + coldDown;
        this.ninjaId = id;
        this.hp = hp;
    }

    public boolean expired() {
        return System.currentTimeMillis() >= this.coldDown || hp <= 0;
    }

    public void upHP(int dame) {
        this.hp += dame;
    }
}
