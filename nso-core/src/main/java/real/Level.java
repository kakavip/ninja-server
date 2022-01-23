package real;

import lombok.val;
import threading.Manager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Level {
    public int level;
    public long exps;
    public short ppoint;
    public short spoint;
    private static ArrayList<Level> entrys;
    public static java.util.Map<Integer, Short> levelToPPoint;
    public static java.util.Map<Integer, Short> levelToSPoint;
    public static java.util.Map<Integer, Level> levelToLevelObj;
    public static java.util.Map<Integer, Long> levelToMaxExp;

    static {
        Level.setEntrys(new ArrayList<Level>());
        levelToPPoint = new HashMap<>();
        levelToSPoint = new HashMap<>();
        levelToLevelObj = new HashMap<>();
        levelToMaxExp = new HashMap<>();
    }

    public static long[] getLevelExp(final long exp) {
        long num;
        int i;
        for (num = exp, i = 0; i < Level.entrys.size()
                && num >= Level.entrys.get(i).exps; num -= Level.entrys.get(i).exps, ++i) {
        }
        return new long[] { i, num };
    }

    public synchronized static short totalpPoint(final int level) {
        if (levelToPPoint.containsKey(level)) {
            return levelToPPoint.get(level);
        } else {
            short ppoint = 0;

            for (short i = 0; i < Level.entrys.size(); ++i) {
                if (Level.entrys.get(i).level <= level) {
                    ppoint += Level.entrys.get(i).ppoint;
                }
            }
            levelToPPoint.put(level, ppoint);
            return ppoint;
        }
    }

    public static short totalsPoint(final int level) {
        return levelToSPoint.get(level);
    }

    public static long getMaxExp(final int level) {
        return levelToMaxExp.get(level);
    }

    public static Level getLevel(final int level) {
        return levelToLevelObj.get(level);
    }

    public static ArrayList<Level> getEntrys() {
        return entrys;
    }

    public static void onFinishAddLevel() {
        for (short i = 0; i < Level.entrys.size(); ++i) {
            val lv = Level.entrys.get(i).level;
            levelToLevelObj.put(lv, Level.entrys.get(i));
        }

        for (int level = 0; level <= Manager.MAX_LEVEL + 1; level++) {
            long num = 0L;
            for (int i = 0; i < level; ++i) {
                num += getLevel(i).exps;
            }
            levelToMaxExp.put(level, num);

            short spoint = 0;
            for (short i = 0; i < Level.entrys.size(); ++i) {
                if (Level.entrys.get(i).level <= level) {
                    spoint += Level.entrys.get(i).spoint;
                }
            }
            levelToSPoint.put(level, spoint);
        }
    }

    public static void addLevel(Level level) {
        entrys.add(level);
    }

    public static void setEntrys(ArrayList<Level> entrys) {
        Level.entrys = entrys;
    }
}
