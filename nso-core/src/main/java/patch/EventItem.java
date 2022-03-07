package patch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import server.util;

import java.util.Arrays;
import java.util.stream.Stream;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventItem {

    private Recipe[] inputs;
    private EventOutput output;

    private static int[] arrGiftUserItemIds = new int[] { 389, 390, 391, 392 };

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

    public static boolean isEventGiftUserItem(int itemId) {
        for (int id : arrGiftUserItemIds) {
            if (id == itemId) {
                return true;
            }
        }
        return false;
    }

    public static Short[] getEventDropItemIds() {
        if (eventDrops == null) {
            try {
                eventDrops = Arrays.stream(entrys)
                        .flatMap(e -> Arrays.stream(e.inputs))
                        .filter(r -> r.isCanDrop())
                        .map(r -> Short.parseShort(r.getId() + "")).distinct().toArray(Short[]::new);
            } catch (Exception e) {

            }

            util.Debug("EVENT DROP ITEM IDS: " + eventDrops);
        }
        return eventDrops;
    }

    public static EventItem getEventItemFromOutputItemId(int itemId) {
        EventItem entry = null;
        for (int i = 0; i < entrys.length; i++) {
            entry = entrys[i];

            if (entry == null) {
                continue;
            }
            if (entry.getOutput().getId() == itemId) {
                break;
            }
        }

        return entry;
    }
}
