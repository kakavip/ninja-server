package patch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import real.Item;
import real.ItemData;
import real.PlayerManager;
import server.SQLManager;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class ItemShinwaManager {

    public static long _1DAY = 24 * 60 * 60 * 1000;
    public static Map<Integer, List<ItemShinwa>> items;

    static {
        items = new ConcurrentHashMap<>();
        items.put(-2, new CopyOnWriteArrayList<>());
        items.put(-1, new CopyOnWriteArrayList<>());
        items.put(0, new CopyOnWriteArrayList<>());
        items.put(1, new CopyOnWriteArrayList<>());
        items.put(2, new CopyOnWriteArrayList<>());
        items.put(3, new CopyOnWriteArrayList<>());
        items.put(4, new CopyOnWriteArrayList<>());
        items.put(5, new CopyOnWriteArrayList<>());
        items.put(6, new CopyOnWriteArrayList<>());
        items.put(7, new CopyOnWriteArrayList<>());
        items.put(8, new CopyOnWriteArrayList<>());
        items.put(9, new CopyOnWriteArrayList<>());
        items.put(10, new CopyOnWriteArrayList<>());
        items.put(11, new CopyOnWriteArrayList<>());
    }

    public static ItemShinwa findItemById(int itemId) {
        ItemShinwa[] itemShinwa = new ItemShinwa[1];
        SQLManager.executeQuery("SELECT * FROM `itemshinwa` where id = " + itemId, red -> {
            if (red.first()) {
                itemShinwa[0] = Mapper.converter.readValue(red.getString("item"), ItemShinwa.class);
                itemShinwa[0].setItemId(red.getInt("id"));
            }
        });
        return itemShinwa[0];
    }

    @SneakyThrows
    public static void updateItemStatus(@NotNull ItemShinwa itemShinwa, byte status) {
        itemShinwa.status = status;
        items.get(itemShinwa.sellUIIndex).remove(itemShinwa);
        SQLManager.executeUpdate("UPDATE `itemshinwa` set item='" + Mapper.converter.writeValueAsString(itemShinwa) + "' where id = " + itemShinwa.getItemId());
        if (status == 1) {
            itemShinwa.sellUIIndex = -1;
            addItemToList(itemShinwa, -1);
        } else if (status == 2) {
            itemShinwa.sellUIIndex = -2;
            addItemToList(itemShinwa, -2);
        }
    }

    @SneakyThrows
    public static void deleteItem(@NotNull ItemShinwa itemShinwa) {
        SQLManager.executeUpdate("DELETE FROM `itemshinwa` where id=" + itemShinwa.getItemId());
        items.get(itemShinwa.sellUIIndex).remove(itemShinwa);
    }


    public static int getItemShopId(final @NotNull Item item) {
        if (ItemData.ItemDataId(item.id).type == 26) {
            return 0;
        } else if (ItemData.ItemDataId(item.id).type >= 0 && ItemData.ItemDataId(item.id).type <= 9) {
            return ItemData.ItemDataId(item.id).type + 1;
        }
        return 11;
    }

    public static synchronized void loadFromDatabase() {
        for (List<ItemShinwa> list : items.values()) {
            list.clear();
        }

        SQLManager.executeQuery("SELECT * FROM `itemshinwa`", (red) -> {
            while (red.next()) {
                val itemShinwa = Mapper.converter.readValue(red.getString("item"), ItemShinwa.class);
                itemShinwa.setItemId(red.getInt("id"));
                if (itemShinwa.status == 0) {
                    addItemToList(itemShinwa, itemShinwa.sellUIIndex);
                } else if (itemShinwa.status == 1) {
                    addItemToList(itemShinwa, -1);
                } else if (itemShinwa.status == 2) {
                    addItemToList(itemShinwa, -2);
                }
            }
            red.close();
        });
    }

    private static void addItemToList(ItemShinwa itemShinwa, int i) {
        items.get(i).add(itemShinwa);
    }

    @SneakyThrows
    public static synchronized void add(@NotNull ItemShinwa item) {
        item.sellUIIndex = getItemShopId(item.item);
        SQLManager.executeUpdate("INSERT INTO `itemshinwa`(item) values ('" + Mapper.converter.writeValueAsString(item) + "');");
        SQLManager.executeQuery("select max(id) id from `itemshinwa`", red -> {
            if (red.first()) {
                item.itemId = red.getInt("id");
                addItemToList(item, item.sellUIIndex);
            }
        });
    }

    public static volatile boolean updateRunning = true;

    @NotNull
    public static Runnable getRunnable() {
        return () -> {
            while (updateRunning) {
                try {
                    synchronized (items) {
                        for (int i = 0; i <= 11; i++) {
                            val itemValue = items.get(i);
                            if (itemValue != null) {
                                for (int j = 0; j < itemValue.size(); j++) {
                                    ItemShinwa itemShinwa = itemValue.get(j);
                                    if (itemShinwa.isExpired()) {
                                        updateItemStatus(itemShinwa, (byte) 2);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {

                } finally {
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        System.out.println("STOP UPDATE SHINWA THREAD");
                    }
                }
            }
        };
    }

    public static volatile boolean returnRunning = true;

    @NotNull
    public static Runnable getReturnThread() {
        return () -> {
            while (returnRunning) {
                try {
                    // Sold Item
                    for (ItemShinwa itemShinwa : items.get(-1)) {
                        if (itemShinwa != null) {
                            returnXuToSeller(itemShinwa);
                        }
                    }
                    // Return Item Expired

                } catch (Exception e) {

                } finally {

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        System.out.println("STOP RETURN THREAD");
                    }
                }

            }
        };
    }

    public static void returnXuToSeller(@NotNull ItemShinwaManager.ItemShinwa itemShinwa) {
        val ninja = PlayerManager.getInstance().getNinja(itemShinwa.getSeller());
        val xu = itemShinwa.getPrice() * 95 / 100;

        if (ninja != null) {
            val canReceiveXu = ninja.xu + xu <= 2_000_000_000;
            if (canReceiveXu) {
                ninja.upxuMessage(xu);
                ninja.p.sendYellowMessage("Bạn nhận được " + itemShinwa.getPrice() + " xu từ chợ đen");
                ItemShinwaManager.deleteItem(itemShinwa);
            } else {
                ninja.p.sendYellowMessage("Bạn đã bán được 1 món hàng từ chợ đen thu về " + xu + " vui lòng tiêu bớt xu để nhận được xu");
                ItemShinwaManager.updateItemStatus(itemShinwa, (byte) 1);
            }
        } else {
            ItemShinwaManager.updateItemStatus(itemShinwa, (byte) 1);
        }
    }


    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemShinwa implements Serializable {


        private int itemId;
        public Item item;
        private long timeStart;
        private String seller;
        private int price;
        private int quantity;
        private int sellUIIndex;


        /**
         * 0 seling
         * 1 sold
         * 2 expired
         */
        private byte status;

        public ItemShinwa() {
        }

        public ItemShinwa(final @NotNull Item item, final @NotNull String seller, int price) {
            this.item = item;
            this.timeStart = System.currentTimeMillis();
            this.seller = seller;
            this.price = price;
            this.quantity = item.quantity;
            status = 0;
        }

        private int getRemainTime() {
            return (int) ((_1DAY - (System.currentTimeMillis() - this.timeStart)) / 1000);
        }

        @NotNull
        public ItemStands getItemStand() {
            return new ItemStands(itemId, getRemainTime(), quantity, seller, price, item.id);
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timeStart >= _1DAY || getRemainTime() <= 0;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ItemShinwa that = (ItemShinwa) o;
            return itemId == that.itemId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(itemId);
        }

    }
}
