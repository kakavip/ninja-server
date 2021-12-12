package real;

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ItemSell {
    public int id;
    public byte type;
    @NotNull
    public Item[] item;
    @NotNull
    public static Map<@NotNull Integer, @NotNull ItemSell> entrys;

    public static ItemSell SellItemType(final int type) {
        return entrys.get(type);
    }
    @NotNull
    public static Item getItemTypeIndex(final int type, final int index) {
        val entry = entrys.get(type);
        if (entry.type == type && index >= 0 && index < entry.item.length) {
            return entry.item[index];
        }
        return null;
    }
}
