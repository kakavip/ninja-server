package patch;

// import javafx.concurrent.Task;
import lombok.Getter;
import lombok.Setter;
import server.util;

@Getter
@Setter
public class TaskOrderDefault extends TaskOrder {
    public TaskOrderDefault() {
        super();
    }

    public TaskOrderDefault(int type) {
        super();
        this.setTaskId(type);
        if (type == NHIEM_VU_HANG_NGAY) {
            this.setKillId(0);
            this.setMapId(27);
            this.setMaxCount(util.nextInt(20, 25));
            this.setCount(0);
        } else if (type == NHIEM_VU_TA_THU) {
            this.setKillId(30);
            this.setMapId(12);
            this.setMaxCount(1);
            this.setCount(0);
        }
    }
}
