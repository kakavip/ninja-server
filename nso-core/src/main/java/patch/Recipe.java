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
public class Recipe implements Serializable {

    private int id;
    private int count;

    public Recipe() {

    }

    @NotNull
    public Item getItem() {
        val item = ItemData.itemDefault(id);
        item.quantity = count;
        return item;
    }
    @NotNull
    public ItemData getItemData() {
        return ItemData.ItemDataId(id);
    }

    public static Recipe[] entrys;
}
