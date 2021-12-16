package real;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import server.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item implements Serializable {
    @JsonProperty(required = true, value = "isLock")
    private boolean isLock;
    public int sale;
    public int quantity;
    private byte upgrade;
    public int index;
    public short id;
    public boolean isExpires;
    public long expires;

    public int buyCoin;
    public int buyCoinLock;
    public int buyGold;
    public byte sys;
    public long timeBuy;

    public List<Item> ngocs;
    public List<Option> option;

    public Item() {
        this.id = -1;
        this.setLock(false);
        this.setUpgrade(0);
        this.isExpires = false;
        this.quantity = 1;
        this.expires = -1L;
        this.sale = 0;
        this.buyCoin = 0;
        this.buyCoinLock = 0;
        this.buyGold = 0;
        this.sys = 0;
        this.option = new ArrayList<>();
        this.ngocs = new ArrayList<>();
    }


    @JsonIgnore
    @NotNull
    public Item clone() {
        final Item item = new Item();
        item.id = this.id;
        item.setLock(this.isLock());
        item.setUpgrade(this.getUpgrade());
        item.isExpires = this.isExpires;
        item.quantity = this.quantity;
        item.expires = this.expires;
        item.sale = this.sale;
        item.buyCoin = this.buyCoin;
        item.buyCoinLock = this.buyCoinLock;
        item.buyGold = this.buyGold;
        item.sys = this.sys;
        for (int i = 0; i < this.option.size(); ++i) {
            item.option.add(new Option(this.option.get(i).id, this.option.get(i).param));
        }
        item.ngocs.addAll(this.ngocs);

        return item;
    }

    public boolean expireTrung() {
        return System.currentTimeMillis() - timeBuy >= this.expires;
    }

    @JsonIgnore
    public int getUpMax() {
        final ItemData data = ItemData.ItemDataId(this.id);
        if (data.level >= 1 && data.level < 20) {
            return 4;
        }
        if (data.level >= 20 && data.level < 40) {
            return 8;
        }
        if (data.level >= 40 && data.level < 50) {
            return 12;
        }
        if (data.level >= 50 && data.level < 60) {
            return 14;
        }
        return 16;
    }

    @JsonIgnore
    public void upgradeNext(final byte next) {
        this.setUpgrade(this.getUpgrade() + next);
        if (this.option != null) {
            for (short i = 0; i < this.option.size(); ++i) {
                final Option option = this.option.get(i);
                switch (option.id) {
                    case 6:
                    case 7:
                        option.param += 15 * next;
                        break;
                    case 8:
                    case 9:
                    case 19:
                        option.param += 10 * next;
                        break;
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 17:
                    case 18:
                    case 20:
                        option.param += 5 * next;
                        break;
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                        option.param += 150 * next;
                        break;
                    case 16:
                        option.param += 3 * next;
                        break;
                }
            }
        }
    }

    @JsonIgnore
    public int getOptionShopMin(final int opid, final int param) {
        if (opid == 0 || opid == 1 || opid == 21 || opid == 22 || opid == 23 || opid == 24 || opid == 25 || opid == 26) {
            return param - 50 + 1;
        }
        if (opid == 6 || opid == 7 || opid == 8 || opid == 9 || opid == 19) {
            return param - 10 + 1;
        }
        if (opid == 2 || opid == 3 || opid == 4 || opid == 5 || opid == 10 || opid == 11 || opid == 12 || opid == 13 || opid == 14 || opid == 15 || opid == 17 || opid == 18 || opid == 20) {
            return param - 5 + 1;
        }
        if (opid == 16) {
            return param - 3 + 1;
        }
        return param;
    }

    @JsonIgnore
    public boolean isTypeBody() {
        return ItemData.isTypeBody(this.id);
    }

    @JsonIgnore
    public boolean isTypeNgocKham() {
        return ItemData.isTypeNgocKham(this.id);
    }

    @JsonIgnore
    public ItemData getData() {
        return ItemData.ItemDataId(id);
    }

    @JsonIgnore
    public boolean isPrecious() {
        return id == 383 ||
                id == 384 ||
                id == 385 ||
                id == 308 ||
                id == 309 ||
                id == 353 ||
                id == 652 ||
                id == 653 ||
                id == 654 ||
                id == 695 ||
                id >= 685 && id <= 704 ||
                id == 655 ||
                id == 599 ||
                id == 600 ||
                id == 605 ||
                id == 597 ||
                id == 602 ||
                id == 603;
    }

    @JsonIgnore
    public int getPercentAppear() {
        switch (id) {
            case 599:
            case 600: {
                return 10;
            }
            case 605: {
                return 2;
            }
            case 383:
                return 50;
            case 384:
                return 20;
            case 385:
            case 687:
            case 689:
            case 686:
            case 688:
            case 690:
            case 691:
            case 692:
            case 693:
            case 694:
                return 2;
            case 308:
            case 309:
            case 653:
            case 654:
            case 655:
                return 40;
            case 685:
                return 3;
            case 695: {
                return 100;
            }
            case 696: {
                return 80;
            }
            case 455: {
                return 30;
            }
            case 456: {
                return 15;
            }
            case 457: {
                return 3;
            }
            case 545: {
                return 30;
            }
            case 454: {
                return 30;
            }
            case 697: {
                return 20;
            }
            case 698: {
                return 15;
            }
            case 699: {
                return 10;
            }
            case 700: {
                return 7;
            }
            case 701:
                return 6;
            case 702: {
                return 5;
            }
            case 703: {
                return 4;
            }
            case 704: {
                return 3;
            }
            default:
                return 100;
        }
    }

    @JsonIgnore
    public boolean isExpired() {
        return System.currentTimeMillis() >= this.expires;
    }

    private static final short[] DEFAULT_RANDOM_ITEM_IDS = new short[]{7, 8, 9, 436, 437, 438, 695};

    public static Item defaultRandomItem() {
        return ItemData.itemDefault(DEFAULT_RANDOM_ITEM_IDS[util.nextInt(DEFAULT_RANDOM_ITEM_IDS.length)]);
    }

    @JsonIgnore
    public int findParamById(int id) {
        for (Option i : option) {
            if (i.id == id) {
                return i.param;
            }
        }
        return 0;
    }

    @JsonIgnore
    protected boolean isTypeTask() {
        val data = this.getData();
        return (data.type == 23 || data.type == 24 || data.type == 25);
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public byte getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(int upgrade) {
        this.upgrade = (byte) upgrade;
    }
}
