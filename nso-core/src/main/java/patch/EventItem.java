package patch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.stream.Stream;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventItem {

    private Recipe[] inputs;
    private EventOutput output;

    private int coinLock;
    private int coin;
    private int idRequired;
    private int gold;
    public EventItem() {
    }

    public static EventItem[] entrys;

    public static boolean isEventItem(int id) {
        boolean exist = false;
        for (EventItem entry : entrys) {
            if (entry.getOutput().getId() == id) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    private static Short[] eventDrops;

    public static Short[] getEventDropItemIds() {
        if (eventDrops == null) {
            try {
                eventDrops = Arrays.stream(entrys)
                        .flatMap(e -> Arrays.stream(e.inputs))
                        .map(r -> Short.parseShort(r.getId() + "")).distinct().toArray(Short[]::new);
            } catch (Exception e) {

            }
        }
        return eventDrops;
    }
}
