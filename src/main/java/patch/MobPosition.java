package patch;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class MobPosition {
    int id;
    int level;
    int x;
    int y;
    int status = 5;
    int levelBoss = 0;
    boolean isBoss = false;

    public MobPosition(int id, int level, int x, int y) {
        this.id = id;
        this.level = level;
        this.x = x;
        this.y = y;
    }

    @Override
    @NotNull
    public String toString() {
        String builder = "[" +
                id +
                "," +
                level +
                "," +
                x +
                "," +
                y +
                "," +
                status +
                "," +
                levelBoss +
                "," +
                "false" +
                "]";
        return builder;
    }

}
