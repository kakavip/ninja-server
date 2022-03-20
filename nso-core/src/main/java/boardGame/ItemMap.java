package boardGame;

import org.jetbrains.annotations.NotNull;
import real.Item;

public class ItemMap {

    public short x;
    public short y;
    public short itemMapId;
    public long removedelay;
    public int master;
    @NotNull
    public Item item;
    public boolean visible;
    public long nextTimeRefresh;

    public ItemMap() {
        this.removedelay = 30000L + System.currentTimeMillis();
        this.master = -1;
        visible = true;
    }

    /**
     *
     * @param removedelay = -1 mean that the eternal item that is never being
     *                    removed
     * @return
     */
    @NotNull
    public ItemMap setRemovedelay(long removedelay) {
        this.removedelay = removedelay;
        return this;
    }

    @NotNull
    public ItemMap setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }
}
