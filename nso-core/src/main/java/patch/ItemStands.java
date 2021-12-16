package patch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemStands {

    private int itemId;
    private int timeEnd;
    private int quantity;
    private String seller;
    private int price;
    private int itemTemplate;

    public ItemStands(int itemId, int timeEnd, int quantity, String seller, int price, int itemTemplate) {
        this.itemId = itemId;
        this.timeEnd = timeEnd;
        this.quantity = quantity;
        this.seller = seller;
        this.price = price;
        this.itemTemplate = itemTemplate;
    }
}
