package patch;

public class ReduceTimeSkillEffect {
    public  enum Type {
        FIRE,
        ICE,
        WIND
    }

    public Type type;
    public long coldDown;
    public long reduceTime;

    public ReduceTimeSkillEffect(Type type, long expiredTime, long reduceTime) {
        this.type = type;
        this.coldDown = expiredTime * 1000 + System.currentTimeMillis();
        this.reduceTime = reduceTime * 1000;
    }

    public boolean expired() {
        return System.currentTimeMillis() >= this.coldDown;
    }

}
