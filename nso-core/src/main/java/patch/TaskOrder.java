package patch;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.util;
import threading.Map;
import threading.Server;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class TaskOrder implements Serializable, Cloneable {
    public static int NHIEM_VU_HANG_NGAY = 0;
    public static int NHIEM_VU_TA_THU = 1;

    private int count;
    private int maxCount;
    private int taskId;

    private int killId;
    private int mapId;

    @Nullable
    private String name;
    @Nullable
    private String description;
    public TaskOrder() {

    }

    public String getName() {
        if (name == null) {
            name = "";
        }
        return name;
    }

    public String getDescription() {
        if (description == null) {
            description = "";
        }
        return description;
    }

    public TaskOrder(int count, int maxCount, int taskId, int killId, int mapId) {
        this.count = count;
        this.maxCount = maxCount;
        this.taskId = taskId;
        this.name = NHIEM_VU_HANG_NGAY == taskId ? "Nhiệm vụ hằng ngày" : "Nhiệm vụ tà thú";
        Server.getInstance();
        this.description = "Ghi chú: Đi đến " + Server.getMapById(mapId).template.name + " để hoàn thành nhiệm vụ";
        this.killId = killId;
        this.mapId = mapId;
    }

    @NotNull
    private static java.util.Map<@NotNull Integer, @NotNull TaskOrder> nvhnTask = new ConcurrentHashMap<>();
    @NotNull
    private static final java.util.Map<@NotNull Integer, @NotNull TaskOrder> beastTasks = new ConcurrentHashMap<>();

    @NotNull
    public synchronized static TaskOrder createTask(int level) {
        if (nvhnTask.containsKey(level)) {
            val o = nvhnTask.get(level).cloneObj();
            o.setMaxCount(util.nextInt(20, 25));
            return o;
        }
        Map[] maps = Server.getInstance().getMaps();
        for (int i = 0, mapsLength = maps.length; i < mapsLength; i++) {
            Server.getInstance();
            Map map = Server.getMapById(i);
            if (map.isLangCo() || map.VDMQ()) continue;
            if (map.haveMobLevel(level)) {
                nvhnTask.put(level, new TaskOrder(0, util.nextInt(20, 25), TaskOrder.NHIEM_VU_HANG_NGAY, map.getMobRandomMobId(), map.id));
                return nvhnTask.get(level).cloneObj();
            }
        }
        return new TaskOrderDefault(NHIEM_VU_HANG_NGAY);
    }

    @NotNull
    public synchronized static TaskOrder createBeastTask(int level) {
        if (beastTasks.containsKey(level)) {
            val t = beastTasks.get(level).cloneObj();
            t.setMaxCount(1);
            return t;
        } else {
            Map[] maps = Server.getInstance().getMaps();
            for (int i = 0, mapsLength = maps.length; i < mapsLength; i++) {
                Server.getInstance();
                Map map = Server.getMapById(i);
                if (map.isLangCo() || map.VDMQ()) continue;
                val mobLv3 = map.getMobLevel3(level);
                if (mobLv3 == -1) continue;

                beastTasks.put(level, new TaskOrder(0, 1, TaskOrder.NHIEM_VU_TA_THU, mobLv3, map.id));
                val t = beastTasks.get(level).cloneObj();
                t.setMaxCount(1);
                return t;
            }
        }
        return new TaskOrderDefault(NHIEM_VU_TA_THU);
    }

    public void setCount(int count) {
        if (this.count > this.maxCount) {
            this.count = this.maxCount;
        } else {
            this.count = count;
        }
    }

    public boolean isDone() {
        return this.count >= this.maxCount;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @SneakyThrows
    public TaskOrder cloneObj() {
        val o = (TaskOrder) this.clone();
        o.setMaxCount(util.nextInt(20, o.getMaxCount()));
        return o;
    }
}
