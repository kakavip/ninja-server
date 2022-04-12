package patch;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import server.RotationLuck;
import server.util;
import threading.Map;
import threading.Server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class TaskOrder implements Serializable, Cloneable {
    public static int NHIEM_VU_HANG_NGAY = 0;
    public static int NHIEM_VU_TA_THU = 1;
    public static int NHIEM_VU_DANH_VONG = 2;
    public static int NHIEM_VU_MO_RONG = 3;

    public static final int BU_NHIN_KILL_ID = -1;
    public static final int NORMAL_MOB_KILL_ID = -2;
    public static final int INCREASE_PK_POINT_KILL_ID = -3;
    public static final int TAI_XIU_KILL_ID = -4;
    public static final int VXMM_NORMAL_KILL_ID = -5;
    public static final int TA_MOB_KILL_ID = -6;
    public static final int TL_MOB_KILL_ID = -7;
    public static final int UPGRADE_TONE_KILL_ID = -8;
    public static final int LAT_HINH_KILL_ID = -9;
    public static final int VXMM_VIP_KILL_ID = -10;

    public static int MAX_NVDV_PER_DAY = 30;
    public static int MAX_NVHN_PER_DAY = 20;

    private int count;
    private int maxCount;
    private int taskId;

    private int nvdvLevel = 0;

    private int killId;
    private int mapId;

    @Nullable
    private String name;
    @Nullable
    private String description;

    public static int[] nvdvTypes = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    public static String[] NVDV_NAMES = new String[] {
            "- Hạ gục %d/%d bù nhìn",
            "- Tiêu diệt %d/%d quái thường không lệch quá 10 cấp độ",
            "- Tăng điểm hiếu chiến %d/%d lần",
            "- Tham gia minigame tài xỉu %d/%d lần",
            "- Tham gia vòng xoay may mắn thường %d/%d lần",
            "- Tiêu diệt %d/%d tinh anh không lệch quá 10 cấp độ",
            "- Tiêu diệt %d/%d thủ lĩnh không lệch quá 10 cấp độ",
            "- Luyện thành công đá 11: %d/%d lần",
            "- Lật hình %d/%d lần",
            "- Tham gia vòng xoay may mắn vip %d/%d lần",
    };

    public TaskOrder() {

    }

    public void setNvdvLevel(int level) {
        this.nvdvLevel = level;
    }

    public int getNvdvLevel() {
        return this.nvdvLevel;
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
        if (taskId == NHIEM_VU_HANG_NGAY) {
            this.name = "Nhiệm vụ hằng ngày";
        } else if (taskId == NHIEM_VU_TA_THU) {
            this.name = "Nhiệm vụ tà thú";
        } else if (taskId == NHIEM_VU_DANH_VONG) {
            this.name = "Nhiệm vụ danh vọng";
        } else if (taskId == NHIEM_VU_MO_RONG) {
            this.name = "Nhiệm vụ mở rộng";
        }
        Server.getInstance();
        if (taskId == NHIEM_VU_HANG_NGAY || taskId == NHIEM_VU_TA_THU) {
            this.description = "Ghi chú: Đi đến " + Server.getMapById(mapId).template.name + " để hoàn thành nhiệm vụ";
        }
        this.killId = killId;
        this.mapId = mapId;
    }

    @NotNull
    private static java.util.Map<@NotNull Integer, @NotNull List<List<Integer>>> nvhnTask = new ConcurrentHashMap<>();
    @NotNull
    private static final java.util.Map<@NotNull Integer, @NotNull TaskOrder> beastTasks = new ConcurrentHashMap<>();

    @NotNull
    public synchronized static TaskOrder createTask(int level) {
        if (!nvhnTask.containsKey(level) || nvhnTask.get(level).isEmpty()) {
            Map[] maps = Server.getInstance().getMaps();
            List<List<Integer>> mapByLvList = new ArrayList<List<Integer>>();

            for (int i = level - 10; i < level + 10; i++) {
                for (Map map : maps) {
                    if (!map.canDoNvhn())
                        continue;

                    if (map.getMobIdByLevel(i) != -1) {
                        List<Integer> _d = Arrays.asList(
                                map.getMobIdByLevel(i),
                                map.id);

                        if (!mapByLvList.contains(_d)) {
                            mapByLvList.add(_d);
                        }
                        break;
                    }

                }
            }

            nvhnTask.put(level, mapByLvList);
        }

        List<List<Integer>> mapByLvList = nvhnTask.get(level);
        if (!mapByLvList.isEmpty()) {
            int randIdx = util.nextInt(0, mapByLvList.size() - 1);

            return new TaskOrder(0, util.nextInt(20, 25), TaskOrder.NHIEM_VU_HANG_NGAY,
                    mapByLvList.get(randIdx).get(0), mapByLvList.get(randIdx).get(1));

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
                if (map.isLangCo() || map.VDMQ())
                    continue;
                val mobLv3 = map.getMobLevel3(level);
                if (mobLv3 == -1)
                    continue;

                beastTasks.put(level, new TaskOrder(0, 1, TaskOrder.NHIEM_VU_TA_THU, mobLv3, map.id));
                val t = beastTasks.get(level).cloneObj();
                t.setMaxCount(1);
                return t;
            }
        }
        return new TaskOrderDefault(NHIEM_VU_TA_THU);
    }

    @NotNull
    public synchronized static TaskOrder createNvdvTask() {
        int randKillId = -1 - util.nextInt(10);

        int maxCount = 0;
        if (randKillId == TaskOrder.BU_NHIN_KILL_ID || randKillId == TaskOrder.NORMAL_MOB_KILL_ID) {
            maxCount = util.nextInt(25, 30);
        } else if (randKillId == TaskOrder.TL_MOB_KILL_ID || randKillId == TaskOrder.VXMM_NORMAL_KILL_ID
                || randKillId == TaskOrder.VXMM_VIP_KILL_ID) {
            maxCount = util.nextInt(3, 5);
        } else {
            maxCount = util.nextInt(5, 10);
        }

        return new TaskOrder(0, maxCount, TaskOrder.NHIEM_VU_DANH_VONG, randKillId, -1);
    }

    @NotNull
    public synchronized static TaskOrder createMoRongTask() {
        int rand = util.nextInt(3);
        int maxCount = util.nextInt(2, 5);

        int killId = -1;
        switch (rand) {
            case 0:
                killId = TaskOrder.VXMM_NORMAL_KILL_ID;
                break;
            case 1:
                killId = TaskOrder.VXMM_VIP_KILL_ID;
                break;
            case 2:
                killId = TaskOrder.TAI_XIU_KILL_ID;
                break;
        }

        return new TaskOrder(0, maxCount, TaskOrder.NHIEM_VU_MO_RONG, killId, -1);
    }

    public int nvdvType() {
        return -this.killId - 1;
    }

    public String nvdvText() {
        return String.format(TaskOrder.NVDV_NAMES[this.nvdvType()], this.count, this.maxCount);
    }

    public String mrText() {
        return String.format(TaskOrder.NVDV_NAMES[this.nvdvType()], this.count, this.maxCount);
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
