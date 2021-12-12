package real;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import patch.Mapper;
import server.util;

import java.io.IOException;

import threading.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import threading.Server;

import static clan.ClanThanThu.*;
import static threading.Manager.MIN_DA_LV;

public class ItemData {

    public static final Option VU_KHI_OPTION = new Option(106, 0);
    public static final Option TRANG_BI_OPTION = new Option(107, 0);
    public static final Option TRANG_SUC_OPTION = new Option(108, 0);
    public static final int EXP_ID = 104;
    public static final int GIA_KHAM_OPTION_ID = 123;
    static Server server;
    public short id;
    public byte type;
    public byte nclass;
    public byte skill;
    public byte gender;
    public String name;
    public String description;
    public byte level;
    public short iconID;
    public short part;
    public boolean isUpToUp;
    public boolean isExpires;
    public long seconds_expires;
    public int saleCoinLock;
    public ArrayList<Option> itemoption;
    public ArrayList<Option> option1;
    public ArrayList<Option> option2;
    public ArrayList<Option> option3;
    public static HashMap<Integer, ItemData> entrys;
    private static List<ItemData> itemLevelOrdered;

    public static final int TYPE_VU_KHI = 1;
    public static final int ID_SET_PHAI = -1;
    public static final int ID_SET_TRAI = 0;

    public static short[] ITEM_LV_10;
    public static short[] ITEM_LV_20;
    public static short[] ITEM_LV_30;
    public static short[] ITEM_LV_40;
    public static short[] ITEM_LV_50;
    public static short[] ITEM_LV_60;
    public static short[] ITEM_LV_70;
    public static short[] ITEM_LV_80;
    public static short[] ITEM_LV_90;
    public static short[] ITEM_LV_100;

    public static boolean isTypeBody(final int id) {
        val entry = entrys.get(id);
        return entry.type >= 0 && entry.type <= 15;
    }

    public static boolean isTypeUIME(final int typeUI) {
        return typeUI == 5 || typeUI == 3 || typeUI == 4 || typeUI == 39;
    }

    public static boolean isTypeUIShop(final int typeUI) {
        return typeUI == 20 || typeUI == 21 || typeUI == 22 || typeUI == 23 || typeUI == 24 || typeUI == 25 || typeUI == 26 || typeUI == 27 || typeUI == 28 || typeUI == 29 || typeUI == 16 || typeUI == 17 || typeUI == 18 || typeUI == 19 || typeUI == 2 || typeUI == 6 || typeUI == 8 || typeUI == 34;
    }

    public static boolean isTypeUIShopLock(final int typeUI) {
        return typeUI == 7 || typeUI == 9;
    }

    public static boolean isTypeUIStore(final int typeUI) {
        return typeUI == 14;
    }

    public static boolean isTypeUIBook(final int typeUI) {
        return typeUI == 15;
    }

    public static boolean isTypeUIFashion(final int typeUI) {
        return typeUI == 32;
    }

    public static boolean isTypeUIClanShop(final int typeUI) {
        return typeUI == 34;
    }

    public static boolean isTypeMounts(final int id) {
        val entry = entrys.get(id);
        return entry.type >= 29 && entry.type <= 33;
    }

    public static boolean isTypeNgocKham(final int id) {
        val entry = entrys.get(id);
        return entry.type == 34;
    }

    public static int ThinhLuyenParam(final int id, final int tl) {
        switch (id) {
            case 76: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 50) : 60) : 70) : 90) : 130) : 180) : 250) : 350) : 550;
            }
            case 77: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 40) : 60) : 80) : 100) : 120) : 140) : 200) : 220) : 590;
            }
            case 75:
            case 78: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 25) : 30) : 35) : 40) : 50) : 60) : 80) : 115) : 165;
            }
            case 79: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 1) : 1) : 1) : 1) : 5) : 5) : 5) : 5) : 5;
            }
            case 80: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 5) : 10) : 15) : 20) : 25) : 30) : 35) : 40) : 45;
            }
            case 84:
            case 86: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 10) : 20) : 30) : 40) : 50) : 100) : 120) : 150) : 200;
            }
            case 85: {
                return 1;
            }
            case 82:
            case 83:
            case 87:
            case 88:
            case 89:
            case 90: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 50) : 60) : 80) : 100) : 125) : 300) : 350) : 400) : 500;
            }
            case 94: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 5) : 10) : 15) : 20) : 25) : 30) : 35) : 40) : 60;
            }
            case 81:
            case 91:
            case 92:
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 1) : 5) : 5) : 5) : 10) : 10) : 10) : 10) : 10;
            case 95:
            case 96:
            case 97: {
                return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 1) : 5) : 10) : 15) : 20) : 25) : 30) : 40) : 60;
            }
            default: {
                return 0;
            }
        }
    }

    public static void divedeItem(final @Nullable User p, final @Nullable Message m) throws IOException {
        if (p == null || m == null) return;

        final byte index = m.reader().readByte();
        final int quantity = m.reader().readInt();
        m.cleanup();
        if (p.nj.getAvailableBag() == 0) {
            p.session.sendMessageLog("Hành trang không đủ chỗ trống");
            return;
        }
        final Item item = p.nj.getIndexBag(index);
        if (quantity > 0 && item != null && item.quantity > 1 && quantity <= item.quantity) {
            final Item itemup = new Item();
            itemup.id = item.id;
            itemup.setLock(item.isLock());
            itemup.setUpgrade(item.getUpgrade());
            itemup.isExpires = item.isExpires;
            itemup.quantity = quantity;
            itemup.expires = item.expires;
            p.nj.addItemBag(false, itemup);
            p.nj.removeItemBag(index, quantity);
        }
    }

    public static boolean isUpgradeHide(final int id, final byte upgrade) {
        return ((id == 27 || id == 30 || id == 60) && upgrade < 4) || ((id == 28 || id == 31 || id == 37 || id == 61) && upgrade < 8) || ((id == 29 || id == 32 || id == 38 || id == 62) && upgrade < 12) || ((id == 33 || id == 34 || id == 35 || id == 36 || id == 39) && upgrade < 14) || (((id >= 40 && id <= 46) || (id >= 48 && id <= 56)) && upgrade < 16);
    }

    @NotNull
    public static Item itemDefault(final int id) {

        if (id >= 652 && id <= 655) {
            return itemNgocDefault(id, 1, true);
        } else if (id >= 685 && id <= 694) {
            return itemDefaultMat(id);
        } else if (id >= HAI_MA_1_ID && id <= DI_LONG_3_ID) {
            Item item = itemDefault(id, (byte) 0);
            if (id == HAI_MA_1_ID || id == DI_LONG_1_ID) {
                item.option.add(new Option(ST_NGUOI_ID, 1000));
                item.option.add(new Option(ST_QUAI_ID, 5000));
            } else if (id == HAI_MA_2_ID || id == DI_LONG_2_ID) {
                item.option.add(new Option(ST_NGUOI_ID, 3000));
                item.option.add(new Option(ST_QUAI_ID, 15000));
            } else if (id == HAI_MA_3_ID || id == DI_LONG_3_ID) {
                item.option.add(new Option(ST_NGUOI_ID, 8000));
                item.option.add(new Option(ST_QUAI_ID, 30000));
            }
            return item;
        } else if (id == HOA_LONG_ID) {
            Item item = itemDefault(id, (byte) 0);
            item.option.add(new Option(ST_NGUOI_ID, 15000));
            item.option.add(new Option(ST_QUAI_ID, 35000));
            return item;
        } else if (id == 597) {
            // Can cau ca
            val item = itemDefault(id, (byte) 0);
            item.isExpires = true;
            item.setLock(true);
            item.expires = util.TimeDay(3);
            return item;
        } else if (id == 572) {
            // Thien bien lenh
            val item = itemDefault(id, (byte) 0);
            item.isExpires = true;
            item.setLock(true);
            item.expires = util.TimeHours(5);
            return item;
        } else {
            try {
                return itemDefault(id, (byte) 0);
            } catch (Exception e) {
                return new ItemDefault();
            }
        }
    }

    public static ItemDefault defaultItem = new ItemDefault();

    @NotNull
    public static Item itemDefault(final int id, final boolean isLock) {
        final Item item = itemDefault(id, (byte) 0);
        if (item == null) return defaultItem;
        item.setLock(isLock);
        return item;
    }

    @NotNull
    public static Item itemDefault(final int id, final byte sys) {
        if (id == -1) return defaultItem;
        final Item item = new Item();
        item.id = (short) id;
        item.sys = sys;
        final ItemData data = ItemDataId(id);
        item.sale = data.saleCoinLock;

        if (item.sale == 0) {
            item.sale = 3000;
        }
        if (data.isExpires) {
            item.isExpires = true;
            item.expires = util.TimeSeconds(data.seconds_expires);
        }
        item.sale = data.saleCoinLock;
        if (sys == 0) {
            for (final Option option : data.itemoption) {
                final int idOp = option.id;
                final int par = option.param;
                final Option op = new Option(idOp, par);
                item.option.add(op);
            }
        } else if (sys == 1) {
            for (final Option option : data.option1) {
                final int idOp = option.id;
                final int par = option.param;
                final Option op = new Option(idOp, par);
                item.option.add(op);
            }
        } else if (sys == 2) {
            for (final Option option : data.option2) {
                final int idOp = option.id;
                final int par = option.param;
                final Option op = new Option(idOp, par);
                item.option.add(op);
            }
        } else if (sys == 3) {
            for (final Option option : data.option3) {
                final int idOp = option.id;
                final int par = option.param;
                final Option op = new Option(idOp, par);
                item.option.add(op);
            }
        }
        return item;
    }

    @NotNull
    public static Item parseItem(final @NotNull String str) {
        try {
            return Mapper.converter.readValue(str, Item.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ItemDefault();
        }
    }

    @Nullable
    public static JSONObject ObjectItem(final @NotNull Item item, final int index) {
        item.index = index;
        try {
            return (JSONObject) JSONValue.parse(Mapper.converter.writeValueAsString(item));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    public static ItemData ItemDataId(final int id) {
        return entrys.get(id);
    }

    /**
     * @param maxLevel
     * @param gender
     * @return
     */
    @NotNull
    public static short[] getItemIdByLevel(int maxLevel, byte type, byte gender) {
        if (maxLevel >= 100) {
            maxLevel = 100;
        }
        val minLevel = maxLevel - 10;
        List<Short> itemIds = new ArrayList<>();

        for (ItemData item : entrys.values()) {
            if (!(item.level <= maxLevel && item.level >= minLevel)) {
                continue;
            }
            if (type == (byte) ID_SET_PHAI) {
                if (item.isTrangSuc()) {
                    itemIds.add(item.id);
                }
            } else if (type == (byte) ID_SET_TRAI) {
                if (item.isTrangPhuc()) {
                    if (item.gender == gender) {
                        itemIds.add(item.id);
                    }
                }
            } else if (type == item.type) {
                if (item.level == minLevel) {
                    itemIds.add(item.id);
                }
            }
        }
        val result = new short[itemIds.size()];
        for (int i = 0; i < itemIds.size(); i++) {
            result[i] = itemIds.get(i);
        }

        return result;
    }

    public boolean isTrangSuc() {
        return type == 3 || type == 5 || type == 7 || type == 9;
    }

    public boolean isTrangPhuc() {
        return type == 0 || type == 2 || type == 4 || type == 6 || type == 8;
    }

    public boolean isYoroi() {
        return type == 12;
    }


    public static short[] getListItemByMaxLevel(int maxLv, int percent, byte nYen, byte nDa) {

        val setVuKhi = getItemIdByLevel(maxLv, (byte) 1, (byte) 2);
        val set1 = getItemIdByLevel(maxLv, (byte) ID_SET_PHAI, (byte) 0);
        val set2 = getItemIdByLevel(maxLv, (byte) ID_SET_PHAI, (byte) 1);
        val set3 = getItemIdByLevel(maxLv, (byte) ID_SET_TRAI, (byte) 0);
        val set4 = getItemIdByLevel(maxLv, (byte) ID_SET_TRAI, (byte) 1);

        int total = setVuKhi.length + set1.length + set2.length + set3.length + set4.length + nYen + nDa;
        int nNull = total * 100 / percent - total;

        short[] items = new short[total + nNull];

        int a = 0;

        for (int i = 0; i < nDa; i++) {
            items[a] = (short) util.nextInt(MIN_DA_LV - 1, MIN_DA_LV);
            a++;
        }

        for (int j = 0; j < nNull; j++) {
            items[a] = (short) -1;
            a++;
        }

        for (int j = 0; j < nYen; j++) {
            items[a] = (short) 12;
            a++;
        }

        for (int j = 0; j < setVuKhi.length; j++) {
            items[a] = setVuKhi[j];
            a++;
        }
        for (int j = 0; j < set1.length; j++) {
            items[a] = set1[j];
            a++;
        }
        for (int j = 0; j < set2.length; j++) {
            items[a] = set2[j];
            a++;
        }
        for (int j = 0; j < set3.length; j++) {
            items[a] = set3[j];
            a++;
        }
        for (int j = 0; j < set4.length; j++) {
            items[a] = set4[j];
            a++;
        }
        return items;
    }

    public static int HUYEN_TINH_NGOC = 652;
    public static int HUYET_NGOC = 653;
    public static int LAM_TINH_NGOC = 654;
    public static int LUC_NGOC = 655;

    public static int MAX_ST_NGUOI = 250;
    public static int HP_TOI_DA = 50;
    public static int MP_TOI_DA = 50;
    public static int CHI_MANG = 15;
    public static int KHANG_ST_CHI_MANG = 5;
    public static int MOI_GIAY_HOI_PHUC_HP = 5;
    public static int MOI_GIAY_HOI_PHUC_MP = 5;
    public static int MAX_TAN_CONG = 100;
    public static int GIAM_TRU_ST = 15;
    public static int PHAN_DON = 10;
    public static int ST_CHI_MANG = 100;
    public static int CHINH_XAC = 50;
    public static int NE_DON = 10;
    public static int KHANG_TAT_CA = 10;


    @NotNull
    public static Item itemNgocDefault(int id, int upgrade, boolean random) {

        final Item item = new Item();
        item.id = (short) id;
        item.sys = 0;
        item.sale = 5;
        final ItemData data = ItemDataId(id);
        if (data.isExpires) {
            item.isExpires = true;
            item.expires = util.TimeSeconds(data.seconds_expires);
        }
        item.sale = data.saleCoinLock;

        for (final Option option : data.itemoption) {
            final int idOp = option.id;
            final int par = option.param;
            final Option op = new Option(idOp, par);
            item.option.add(op);
        }

        if (id == HUYET_NGOC) {
            item.option.add(VU_KHI_OPTION);
            item.option.add(new Option(73, random ? util.nextInt((int) (0.8 * MAX_TAN_CONG), MAX_TAN_CONG) : MAX_TAN_CONG));
            item.option.add(new Option(114, -(random ? util.nextInt((int) (0.8 * CHI_MANG), CHI_MANG) : CHI_MANG)));

            item.option.add(TRANG_BI_OPTION);
            item.option.add(new Option(124, random ? util.nextInt((int) (0.8 * GIAM_TRU_ST), GIAM_TRU_ST) : GIAM_TRU_ST));
            item.option.add(new Option(73, -(random ? util.nextInt((int) (0.6 * MAX_TAN_CONG), MAX_TAN_CONG) : MAX_TAN_CONG)));

            item.option.add(TRANG_SUC_OPTION);
            item.option.add(new Option(115, random ? util.nextInt((int) (0.8 * NE_DON), NE_DON) : NE_DON));
            item.option.add(new Option(119, -(random ? util.nextInt((int) (0.8 * MOI_GIAY_HOI_PHUC_MP), MOI_GIAY_HOI_PHUC_MP) : MOI_GIAY_HOI_PHUC_MP)));
        }

        if (id == HUYEN_TINH_NGOC) {
            item.option.add(VU_KHI_OPTION);
            item.option.add(new Option(102, random ? util.nextInt((int) (0.8 * MAX_TAN_CONG), MAX_TAN_CONG) : MAX_TAN_CONG));
            item.option.add(new Option(115, -(random ? util.nextInt((int) (0.8 * CHI_MANG), CHI_MANG) : CHI_MANG)));

            item.option.add(TRANG_BI_OPTION);
            item.option.add(new Option(126, random ? util.nextInt((int) (0.8 * PHAN_DON), PHAN_DON) : PHAN_DON));
            item.option.add(new Option(105, -(random ? util.nextInt((int) (0.6 * ST_CHI_MANG), ST_CHI_MANG) : ST_CHI_MANG)));

            item.option.add(TRANG_SUC_OPTION);
            item.option.add(new Option(121, random ? util.nextInt((int) (0.8 * KHANG_ST_CHI_MANG), KHANG_ST_CHI_MANG) : KHANG_ST_CHI_MANG));
            item.option.add(new Option(118, -(random ? util.nextInt((int) (0.8 * KHANG_TAT_CA), KHANG_TAT_CA) : KHANG_TAT_CA)));


        }

        if (id == LAM_TINH_NGOC) {
            item.option.add(VU_KHI_OPTION);

            item.option.add(new Option(103, random ? util.nextInt((int) (0.8 * MAX_ST_NGUOI), MAX_ST_NGUOI) : MAX_ST_NGUOI));
            item.option.add(new Option(125, -(random ? util.nextInt((int) (0.8 * HP_TOI_DA), HP_TOI_DA) : HP_TOI_DA)));

            item.option.add(TRANG_BI_OPTION);
            item.option.add(new Option(121, random ? util.nextInt((int) (0.8 * KHANG_ST_CHI_MANG), KHANG_ST_CHI_MANG) : KHANG_ST_CHI_MANG));
            item.option.add(new Option(120, -(random ? util.nextInt((int) (0.8 * MOI_GIAY_HOI_PHUC_HP), MOI_GIAY_HOI_PHUC_HP) : MOI_GIAY_HOI_PHUC_HP)));

            item.option.add(TRANG_SUC_OPTION);
            item.option.add(new Option(116, random ? util.nextInt((int) (0.8 * CHINH_XAC), CHINH_XAC) : CHINH_XAC));
            item.option.add(new Option(126, -(random ? util.nextInt((int) (0.8 * PHAN_DON), PHAN_DON) : PHAN_DON)));


        }

        if (id == LUC_NGOC) {
            item.option.add(VU_KHI_OPTION);

            item.option.add(new Option(105, random ? util.nextInt((int) (0.8 * ST_CHI_MANG), ST_CHI_MANG) : ST_CHI_MANG));
            item.option.add(new Option(116, -(random ? util.nextInt((int) (0.8 * CHINH_XAC), CHINH_XAC) : CHINH_XAC)));

            item.option.add(TRANG_BI_OPTION);
            item.option.add(new Option(125, random ? util.nextInt((int) (0.8 * HP_TOI_DA), HP_TOI_DA) : HP_TOI_DA));
            item.option.add(new Option(117, -(random ? util.nextInt((int) (0.8 * MP_TOI_DA), MP_TOI_DA) : MP_TOI_DA)));

            item.option.add(TRANG_SUC_OPTION);
            item.option.add(new Option(117, random ? util.nextInt((int) (0.8 * MP_TOI_DA), MP_TOI_DA) : MP_TOI_DA));
            item.option.add(new Option(124, -(random ? util.nextInt((int) (0.8 * GIAM_TRU_ST), GIAM_TRU_ST) : GIAM_TRU_ST)));


        }

        item.setUpgrade((byte) upgrade);

        item.option.add(new Option(EXP_ID, 0));
        item.option.add(new Option(GIA_KHAM_OPTION_ID, 800000));
        return item;
    }

    /**
     * 685,14,0,0,2,Geningan
     * 686,14,0,0,2,Chuuningan
     * 687,14,0,0,2,Jougan
     * 688,14,0,0,2,Seningan
     * 689,14,0,0,2,Kyubigan
     * 690,14,0,0,2,Rinnegan
     * 691,14,0,0,2,Sharingan
     * 692,14,0,0,2,Tenseigan
     * 693,14,0,0,2,Ketsuryugan
     * 694,14,0,0,2,Sukaigan
     *
     * @param id
     * @return
     */
    @NotNull
    public static Item itemDefaultMat(int id) {

        if (id < 685 || id > 694) throw new RuntimeException("Id not suitable");

        final Item item = new Item();
        item.id = (short) id;
        item.sys = 0;
        item.sale = 5;
        item.setLock(true);

        if (id == 685) {
            item.option.add(new Option(6, 1000));
            item.option.add(new Option(87, 500));
            item.setUpgrade(1);
        } else if (id == 686) {
            item.option.add(new Option(6, 2000));
            item.option.add(new Option(87, 750));
            item.setUpgrade(2);
        } else if (id == 687) {
            item.option.add(new Option(6, 3000));
            item.option.add(new Option(87, 1000));
            item.option.add(new Option(79, 25));
            item.setUpgrade(3);
        } else if (id == 688) {
            item.option.add(new Option(6, 4000));
            item.option.add(new Option(87, 1250));
            item.option.add(new Option(79, 25));
            item.setUpgrade(4);
        } else if (id == 689) {
            item.option.add(new Option(6, 5000));
            item.option.add(new Option(87, 1500));
            item.option.add(new Option(79, 25));
            item.setUpgrade(5);
        } else if (id == 690) {
            item.option.add(new Option(6, 6000));
            item.option.add(new Option(87, 1750));
            item.option.add(new Option(79, 25));
            item.setUpgrade(6);
        } else if (id == 691) {
            item.option.add(new Option(6, 7000));
            item.option.add(new Option(87, 2000));
            item.option.add(new Option(79, 25));
            item.option.add(new Option(64, 0));
            item.setUpgrade(7);
        } else if (id == 692) {
            item.option.add(new Option(6, 8000));
            item.option.add(new Option(87, 2250));
            item.option.add(new Option(79, 25));
            item.option.add(new Option(64, 0));
            item.setUpgrade(8);
        } else if (id == 693) {
            item.option.add(new Option(6, 9000));
            item.option.add(new Option(87, 2500));
            item.option.add(new Option(79, 25));
            item.option.add(new Option(64, 0));
            item.setUpgrade(9);
        } else if (id == 694) {
            item.option.add(new Option(6, 10000));
            item.option.add(new Option(87, 2725));
            item.option.add(new Option(79, 25));
            item.option.add(new Option(64, 0));
            item.setUpgrade(10);
        }
        return item;
    }

    public boolean isVuKhi() {
        return type == 1;
    }

    @NotNull
    public static final Map<@NotNull Integer, @NotNull Integer> PARAMS;

    public static final int TAN_CONG_ID = 73;

    public static final int ST_LEN_QUAI_ID = 102;

    public static final int ST_LEN_NGUOI_ID = 103;

    public static final int ST_CHI_MANG_ID = 105;

    public static final int CHI_MANG_ID = 114;

    public static final int NE_DON_ID = 115;

    public static final int CHINH_XAC_ID = 116;

    public static final int MP_TOI_DA_ID = 117;

    public static final int KHANG_TAT_CA_ID = 118;

    public static final int MOI_GIAY_HOI_PHUC_MP_ID = 119;

    public static final int MOI_GIAY_HOI_PHUC_HP_ID = 120;

    public static final int KHANG_SAT_THUONG_CHI_MANG_ID = 121;

    public static final int GIAM_TRU_ST_ID = 124;

    public static final int HP_TOI_DA_ID = 125;

    public static final int PHAN_DON_ID = 126;


    static {
        server = Server.getInstance();
        PARAMS = new HashMap<>();

        PARAMS.put(TAN_CONG_ID, MAX_TAN_CONG);
        PARAMS.put(ST_LEN_QUAI_ID, MAX_TAN_CONG);
        PARAMS.put(ST_LEN_NGUOI_ID, MAX_ST_NGUOI);
        PARAMS.put(ST_CHI_MANG_ID, ST_CHI_MANG);

        PARAMS.put(CHI_MANG_ID, CHI_MANG);
        PARAMS.put(NE_DON_ID, NE_DON);
        PARAMS.put(CHINH_XAC_ID, CHINH_XAC);
        PARAMS.put(MP_TOI_DA_ID, MP_TOI_DA);

        PARAMS.put(KHANG_TAT_CA_ID, KHANG_TAT_CA);
        PARAMS.put(MOI_GIAY_HOI_PHUC_MP_ID, MOI_GIAY_HOI_PHUC_MP);
        PARAMS.put(MOI_GIAY_HOI_PHUC_HP_ID, MOI_GIAY_HOI_PHUC_HP);
        PARAMS.put(KHANG_SAT_THUONG_CHI_MANG_ID, KHANG_ST_CHI_MANG);

        PARAMS.put(GIAM_TRU_ST_ID, GIAM_TRU_ST);
        PARAMS.put(HP_TOI_DA_ID, HP_TOI_DA);
        PARAMS.put(PHAN_DON_ID, PHAN_DON);
    }

    public boolean isEye() {
        return type == 14;
    }

    public boolean isItemNhiemVu() {
        return type == 25 || type == 23 || type == 24;
    }
}
