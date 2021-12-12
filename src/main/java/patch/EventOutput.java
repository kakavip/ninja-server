package patch;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import real.Item;
import real.ItemData;

import java.io.Serializable;

@Getter
@Setter
public class EventOutput implements Serializable {


    private int id;
    private short[] idItems;
    private long exp;
    private boolean cloneCanUse;

    public EventOutput() {
    }

    @NotNull
    public ItemData getItemData() {
        return ItemData.ItemDataId(id);
    }

    @NotNull
    public Item getItem() {
        return ItemData.itemDefault(this.id);
    }


    public static EventOutput[] entrys;
}
