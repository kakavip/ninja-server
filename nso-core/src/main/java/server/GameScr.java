package server;

import lombok.SneakyThrows;
import lombok.val;
import clan.ClanThanThu;
import patch.Constants;
import patch.Resource;
import tournament.KageTournament;
import real.*;

import java.io.*;
import java.util.HashMap;

import threading.Message;
import io.Session;
import threading.Server;

import static clan.ClanThanThu.*;
import static patch.Constants.TRUNG_DI_LONG_ID;
import static patch.Constants.TRUNG_HAI_MA_ID;
import static real.ItemData.EXP_ID;

public class GameScr {

    static Server server;
    public static final int[] crystals;
    public static final int[] upClothe;
    public static final int[] upAdorn;
    public static final int[] upWeapon;
    public static final int[] coinUpCrystals;
    public static final int[] coinUpClothes;
    public static final int[] coinUpAdorns;
    public static final int[] coinUpWeapons;
    public static final int[] goldUps;
    public static final int[] maxPercents;
    public static short[] LAT_HINH_ID;

    public static short[] LAT_HINH_LV10_ID;
    public static short[] LAT_HINH_LV20_ID;
    public static short[] LAT_HINH_LV30_ID;
    public static short[] LAT_HINH_LV40_ID;
    public static short[] LAT_HINH_LV50_ID;
    public static short[] LAT_HINH_LV60_ID;
    public static short[] LAT_HINH_LV70_ID;
    public static short[] LAT_HINH_LV80_ID;
    public static short[] LAT_HINH_LV90_ID;
    public static short[] LAT_HINH_LV100_ID;

    public static int[] ArryenLuck;
    private static final byte[] ArrdayLuck;

    public static boolean mapNotPK(final int mapId) {
        return mapId == 1 || mapId == 10 || mapId == 17 || mapId == 22 || mapId == 27 || mapId == 32 || mapId == 38
                || mapId == 43 || mapId == 48 || mapId == 72 || mapId == 100 || mapId == 101 || mapId == 102
                || mapId == 109 || mapId == 121 || mapId == 122 || mapId == 123 || mapId == 138;
    }

    public static byte KeepUpgrade(final int upgrade) {
        if (upgrade >= 14) {
            return 14;
        }
        if (upgrade >= 12) {
            return 12;
        }
        if (upgrade >= 8) {
            return 8;
        }
        if (upgrade >= 4) {
            return 4;
        }
        return (byte) upgrade;
    }

    public static byte SysClass(final byte nclass) {
        switch (nclass) {
            case 1:
            case 2: {
                return 1;
            }
            case 3:
            case 4: {
                return 2;
            }
            case 5:
            case 6: {
                return 3;
            }
            default: {
                if (nclass == 6 || nclass == 5) {
                    return 3;
                }
                if (nclass == 4 || nclass == 3) {
                    return 2;
                }
                if (nclass == 2 || nclass == 1) {
                    return 1;
                }
                return 0;
            }
        }
    }

    public static byte SideClass(final byte nclass) {
        if (nclass == 6 || nclass == 4 || nclass == 2) {
            return 1;
        }
        return 0;
    }

    public static void SendFile(final Session session, final int cmd, final String url) throws IOException {
        final byte[] ab = loadFile(url).toByteArray();
        final Message msg = new Message(cmd);
        msg.writer().write(ab);
        msg.writer().flush();
        session.sendMessage(msg);
        msg.cleanup();
    }

    public static void ItemStands(final User p) throws IOException {
        final Message m = new Message(-28);
        m.writer().writeByte(-83);
        m.writer().writeByte(10);
        m.writer().writeByte(12);
        m.writer().writeByte(12);
        m.writer().writeByte(13);
        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
    }

    public static void sendSkill(final User p, final String text) {
        try {
            byte[] arrSkill = null;
            int lent = 0;
            if (text.equals("KSkill")) {
                lent = p.nj.get().KSkill.length;
                arrSkill = new byte[lent];
                System.arraycopy(p.nj.get().KSkill, 0, arrSkill, 0, lent);
            }
            if (text.equals("OSkill")) {
                lent = p.nj.get().OSkill.length;
                arrSkill = new byte[lent];
                System.arraycopy(p.nj.get().OSkill, 0, arrSkill, 0, lent);
            }
            if (text.equals("CSkill")) {
                lent = 1;
                arrSkill = new byte[lent];
                arrSkill[0] = -1;
                final Skill skill = p.nj.get().getSkill(p.nj.get().getCSkill());
                if (skill != null) {
                    final SkillData data = SkillData.Templates(skill.id);
                    if (data.type != 2) {
                        arrSkill[0] = skill.id;
                    }
                }
                if (arrSkill[0] == -1 && p.nj.get().getSkills().size() > 0) {
                    arrSkill[0] = p.nj.get().getSkills().get(0).id;
                }
            }
            if (arrSkill == null) {
                return;
            }
            final Message m = new Message(-30);
            m.writer().writeByte(-65);
            m.writer().writeUTF(text);
            m.writer().writeInt(lent);
            m.writer().write(arrSkill);
            m.writer().writeByte(0);
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reciveImage(final User p, Message m) throws IOException {
        final int id = m.reader().readInt();
        m.cleanup();

        final ByteArrayOutputStream a = loadFile("res/icon/" + p.session.zoomLevel + "/" + id + ".png");
        if (a != null) {
            final byte[] ab = a.toByteArray();
            m = new Message(-28);
            m.writer().writeByte(-115);
            m.writer().writeInt(id);
            m.writer().writeInt(ab.length);
            m.writer().write(ab);
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
        }
    }

    public static void sendModTemplate(final User p, int id) throws IOException {
        final MobData mob = MobData.getMob(id);
        if (mob == null) {
            return;
        }
        util.Debug(mob.id + " Id mob " + id);
        ByteArrayOutputStream a;
        if (id == 82) {
            a = loadFile("res/map_file_msg/82");
        } else {
            a = loadFile("res/cache/mob/" + p.session.zoomLevel + "/" + id);
        }
        if (a != null) {
            final byte[] ab = a.toByteArray();
            val m = new Message(-28);
            m.writer().write(ab);
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
        }
    }

    public static ByteArrayOutputStream loadFile(final String url) {
        try {

            if (Server.resource.containsKey(url)) {
                return Server.resource.get(url).getStream();
            }

            final FileInputStream openFileInput = new FileInputStream(url);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final byte[] bArr = new byte[1024];
            while (true) {
                final int read = openFileInput.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            openFileInput.close();
            Server.resource.put(url, new Resource(byteArrayOutputStream));
            return byteArrayOutputStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveFile(final String url, final byte[] data) {
        try {
            final File f = new File(url);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            final FileOutputStream fos = new FileOutputStream(url);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ItemInfo(final User p, Message m) throws IOException {
        final byte type = m.reader().readByte();
        m.cleanup();
        util.Debug("Item info type " + type);
        Item[] arrItem = null;
        switch (type) {
            case 2: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 4: {
                arrItem = p.nj.ItemBox;
                break;
            }
            case 6: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 7: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 8: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 9: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 14: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 15: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 16: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 17: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 18: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 19: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 20: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 21: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 22: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 23: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 24: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 25: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 26: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 27: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 28: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 29: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 32: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
            case 34: {
                arrItem = ItemSell.SellItemType(type).item;
                break;
            }
        }
        if (arrItem == null) {
            return;
        }

        if (type == 4) {
            sendBagToChar(p, arrItem);
        } else {
            m = new Message(33);
            m.writer().writeByte(type);
            m.writer().writeByte(arrItem.length);
            for (int i = 0; i < arrItem.length; ++i) {
                m.writer().writeByte(i);
                m.writer().writeShort(arrItem[i].id);
            }
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
        }
    }

    private static void sendBagToChar(User p, Item[] arrItem) throws IOException {
        Message m;
        m = new Message(31);
        m.writer().writeInt(p.nj.xuBox);
        m.writer().writeByte(arrItem.length);
        for (final Item item : arrItem) {
            if (item != null) {
                m.writer().writeShort(item.id);
                m.writer().writeBoolean(item.isLock());
                if (ItemData.isTypeBody(item.id) || ItemData.isTypeNgocKham(item.id)) {
                    m.writer().writeByte(item.getUpgrade());
                }
                m.writer().writeBoolean(item.isExpires);
                m.writer().writeShort(item.quantity);
            } else {
                m.writer().writeShort(-1);
            }
        }
        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
    }

    public static void buyItemStore(final User p, Message m) throws IOException {
        if (p.nj.isNhanban) {
            p.session.sendMessageLog("Bạn đang trong chế độ thứ thân không thể dùng được chức năng này");
            return;
        }
        final byte type = m.reader().readByte();
        final byte index = m.reader().readByte();
        short num = 1;
        if (m.reader().available() > 0) {
            num = m.reader().readShort();
        }
        m.cleanup();
        final Item sell = ItemSell.getItemTypeIndex(type, index);
        if (num <= 0 || sell == null) {
            return;
        }
        final int buycoin = sell.buyCoin * num;
        final int buycoinlock = sell.buyCoinLock * num;
        final int buycoingold = sell.buyGold * num;
        if (buycoin < 0 || buycoinlock < 0 || buycoingold < 0) {
            return;
        }
        final ItemData data = ItemData.ItemDataId(sell.id);
        if (type == 34 && num > 0) {
            final ClanManager clan = ClanManager.getClanByName(p.nj.clan.clanName);
            if (clan == null) {
                p.session.sendMessageLog("Bạn cần có gia tộc");
            } else if (p.nj.clan.typeclan < 3) {
                p.session.sendMessageLog("Chỉ có tộc trưởng hoặc tôc phó mới được phép mua");
            } else if ((sell.id == 423 && clan.itemLevel < 1) || (sell.id == 424 && clan.itemLevel < 2)
                    || (sell.id == 425 && clan.itemLevel < 3) || (sell.id == 426 && clan.itemLevel < 4)
                    || (sell.id == 427 && clan.itemLevel < 5)) {
                p.session.sendMessageLog("Cần khai mở gia tộc để mua vật phẩm này");
            } else {
                if (buycoin > clan.coin) {
                    p.session.sendMessageLog("Ngân sách gia tộc không đủ");
                    return;
                }
                if (sell.id == TRUNG_HAI_MA_ID || sell.id == Constants.TRUNG_DI_LONG_ID) {
                    if (clan.clanThanThus.size() == 3) {
                        p.endLoad(false);
                        p.sendYellowMessage("Số lượng thần thú đã đạt cấp tối đa");
                        return;
                    }

                    if (sell.id == TRUNG_HAI_MA_ID) {
                        for (ClanThanThu clanThanThus : clan.clanThanThus) {
                            if (clanThanThus.getPetItem().id >= HAI_MA_1_ID
                                    && clanThanThus.getPetItem().id >= HAI_MA_3_ID) {
                                p.endLoad(false);
                                p.sendYellowMessage("Gia tộc bạn đã có thần thú hải mã");
                                return;
                            }
                        }
                    }
                    int countDiLong = (int) ((int) clan.clanThanThus.stream()
                            .filter(t -> t.getPetItem().id >= DI_LONG_1_ID && t.getPetItem().id <= DI_LONG_3_ID).count()
                            +
                            clan.items.stream().filter(i -> i.id == TRUNG_DI_LONG_ID).count());

                    int countHoaLong = (int) clan.clanThanThus.stream().filter(t -> t.getPetItem().id == HOA_LONG_ID)
                            .count();
                    int countHaiMa = (int) ((int) clan.clanThanThus.stream()
                            .filter(t -> t.getPetItem().id >= HAI_MA_1_ID && t.getPetItem().id <= HAI_MA_1_ID).count()
                            + clan.items.stream().filter(i -> i.id == TRUNG_HAI_MA_ID).count());
                    if (sell.id == TRUNG_DI_LONG_ID) {
                        if (countDiLong + countHoaLong == 2) {
                            p.endLoad(false);
                            p.sendYellowMessage("Gia tộc bạn đã có thần thú dị long không thể mua");
                            return;
                        }
                    } else if (sell.id == TRUNG_HAI_MA_ID) {
                        if (countHaiMa == 1) {
                            p.endLoad(false);
                            p.sendYellowMessage("Gia tộc bạn đã có thần thú hải mã không thể mua thêm");
                            return;
                        }
                    }
                }

                final Item item = sell.clone();
                item.quantity = num;
                for (short i = 0; i < item.option.size(); ++i) {
                    item.option.get(i).param = util.nextInt(
                            item.getOptionShopMin(item.option.get(i).id, item.option.get(i).param),
                            item.option.get(i).param);
                }
                if (sell.id == TRUNG_HAI_MA_ID || sell.id == Constants.TRUNG_DI_LONG_ID) {
                    item.isExpires = true;
                    if (util.debug) {
                        // TODO TIME TRUNG
                        item.expires = 60000 * 3L;

                    } else {
                        item.expires = 3 * 86400000L;
                    }
                    item.timeBuy = System.currentTimeMillis();
                }

                clan.addItem(item);
                clan.updateCoin(-buycoin);
                m = new Message(13);
                m.writer().writeInt(p.nj.xu);
                m.writer().writeInt(p.nj.yen);
                m.writer().writeInt(p.luong);
                m.writer().flush();
                p.sendMessage(m);
                m.cleanup();
                m = new Message(-24);
                m.writer().writeUTF("Gia tộc nhận được " + data.name);
                m.writer().flush();
                clan.sendMessage(m);
                m.cleanup();

            }
        } else if ((!data.isUpToUp && p.nj.getAvailableBag() >= num)
                || (data.isUpToUp && p.nj.getIndexBagid(sell.id, sell.isLock()) != -1)
                || (data.isUpToUp && p.nj.getAvailableBag() > 0)) {
            if (p.nj.xu < buycoin) {
                p.session.sendMessageLog("Không đủ xu");
                return;
            }
            if (p.nj.yen < buycoinlock) {
                p.session.sendMessageLog("Không đủ yên");
                return;
            }
            if (p.luong < buycoingold) {
                p.session.sendMessageLog("Không đủ lượng");
                return;
            }
            p.nj.upxu(-buycoin);
            p.nj.upyen(-buycoinlock);
            p.upluong(-buycoingold);
            for (int j = 0; j < num; ++j) {
                final Item item = new Item();
                item.id = sell.id;
                if (sell.isLock()) {
                    item.setLock(true);
                }
                item.sys = sell.sys;
                if (sell.isExpires) {
                    item.isExpires = true;
                    item.expires = util.TimeMillis(sell.expires);
                }
                item.sale = sell.sale;
                for (final Option Option : sell.option) {
                    final int idOp = Option.id;
                    final int par = util.nextInt(item.getOptionShopMin(idOp, Option.param), Option.param);
                    final Option option = new Option(idOp, par);
                    item.option.add(option);
                }
                if (data.isUpToUp) {
                    item.quantity = num;
                    p.nj.addItemBag(true, item);
                    break;
                }
                p.nj.addItemBag(false, item);
            }

            if (p.nj.getTaskId() == 3 && p.nj.getTaskIndex() == 0 && sell.id == 23) {
                p.nj.upMainTask();
            }

            m = new Message(13);
            m.writer().writeInt(p.nj.xu);
            m.writer().writeInt(p.nj.yen);
            m.writer().writeInt(p.luong);
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
        } else {
            p.session.sendMessageLog("Hành trang không đủ chỗ trống");
        }
    }

    public static void doConvertUpgrade(final User p, Message m) throws IOException {
        final byte index1 = m.reader().readByte();
        final byte index2 = m.reader().readByte();
        final byte index3 = m.reader().readByte();
        m.cleanup();
        final Item item1 = p.nj.getIndexBag(index1);
        final Item item2 = p.nj.getIndexBag(index2);
        final Item item3 = p.nj.getIndexBag(index3);
        if (item1 != null && item2 != null && item3 != null) {
            if (!ItemData.isTypeBody(item1.id) || !ItemData.isTypeBody(item2.id)
                    || (item3.id != 269 && item3.id != 270 && item3.id != 271)) {
                p.session.sendMessageLog("Chỉ chọn trang bị và Chuyển hóa");
                return;
            }
            final ItemData data1 = ItemData.ItemDataId(item1.id);
            final ItemData data2 = ItemData.ItemDataId(item2.id);
            if (item1.getUpgrade() == 0 || item2.getUpgrade() > 0 || (item3.id == 269 && item1.getUpgrade() > 10)
                    || (item3.id == 270 && item1.getUpgrade() > 13)) {
                p.session.sendMessageLog("Vật phẩm chuyển hóa không hợp lệ");
                return;
            }
            if (data1.level > data2.level || data1.type != data2.type) {
                p.session.sendMessageLog("Chỉ được chuyển hóa trang bị cùng loại và cùng cấp trở lên");
                return;
            }
            item1.setLock(true);
            item2.setLock(true);
            final byte upgrade = item1.getUpgrade();
            item1.upgradeNext((byte) (-item1.getUpgrade()));
            item2.upgradeNext(upgrade);
            m = new Message(-28);
            m.writer().writeByte(-88);
            m.writer().writeByte(index1);
            m.writer().writeByte(item1.getUpgrade());
            m.writer().writeByte(index2);
            m.writer().writeByte(item2.getUpgrade());
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
            p.nj.removeItemBag(index3, 1);
        }
    }

    public static void crystalCollect(final User p, Message m, final boolean isCoin) throws IOException {
        if (p.nj.isNhanban) {
            p.session.sendMessageLog("Bạn đang trong chế độ thứ thân không thể dùng được chức năng này");
            return;
        }
        if (m.reader().available() > 28) {
            util.Debug("Lơn hơn 28");
            return;
        }
        if (p.nj.getAvailableBag() == 0) {
            p.session.sendMessageLog("Hành trang không đủ chỗ trống");
            return;
        }
        int crys = 0;
        final byte[] arrItem = new byte[m.reader().available()];
        for (byte i = 0; i < arrItem.length; ++i) {
            arrItem[i] = -1;
            final byte index = m.reader().readByte();
            final Item item = p.nj.getIndexBag(index);
            if (item != null) {
                final ItemData data = ItemData.ItemDataId(item.id);
                if (data.type != 26 || item.id >= 12) {
                    p.session.sendMessageLog("Chỉ có thể dùng đá dưới 12 để nâng cấp");
                    return;
                }
                arrItem[i] = index;
                crys += GameScr.crystals[item.id];
            }
        }

        short id = 0;
        for (byte j = 0; j < GameScr.crystals.length; ++j) {
            if (crys > GameScr.crystals[j]) {
                id = (short) (j + 1);
            }
        }

        try {
            if (id >= 12) {
                id = 11;
            }
            final int percen = crys * 100 / GameScr.crystals[id];
            if (percen < 45) {
                p.session.sendMessageLog("Tỷ lệ phải từ 45% trở lên");
                return;
            }
            if (isCoin) {
                if (GameScr.coinUpCrystals[id] > p.nj.xu) {
                    return;
                }
                p.nj.upxu(-GameScr.coinUpCrystals[id]);
            } else {
                if (GameScr.coinUpCrystals[id] > p.nj.xu + p.nj.yen) {
                    return;
                }
                if (p.nj.yen >= GameScr.coinUpCrystals[id]) {
                    p.nj.upyen(-GameScr.coinUpCrystals[id]);
                } else {
                    final int coin = GameScr.coinUpCrystals[id] - p.nj.yen;
                    p.nj.upyen(-p.nj.yen);
                    p.nj.upxu(-coin);
                }
            }
            boolean suc = false;
            final Item item2 = new Item();
            if (util.nextInt(1, 100) <= percen) {
                suc = true;
                item2.id = id;
            } else {
                item2.id = (short) (id - 1);
            }
            item2.setLock(true);
            final int index2 = p.nj.getIndexBagNotItem();
            p.nj.ItemBag[index2] = item2;
            for (byte k = 0; k < arrItem.length; ++k) {
                if (arrItem[k] != -1) {
                    p.nj.ItemBag[arrItem[k]] = null;
                }
            }
            m = new Message(isCoin ? 19 : 20);
            m.writer().writeByte(suc ? 1 : 0);
            m.writer().writeByte(index2);
            m.writer().writeShort(item2.id);
            m.writer().writeBoolean(item2.isLock());
            m.writer().writeBoolean(item2.isExpires);
            if (isCoin) {
                m.writer().writeInt(p.nj.xu);
            } else {
                m.writer().writeInt(p.nj.yen);
                m.writer().writeInt(p.nj.xu);
            }
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            p.endLoad(true);
        }

    }

    public static void UpGrade(final User p, Message m) throws IOException {
        if (p.nj.isNhanban) {
            p.session.sendMessageLog("Bạn đang trong chế độ thứ thân không thể dùng được chức năng này");
            return;
        }
        final byte type = m.reader().readByte();
        final byte index = m.reader().readByte();
        final Item item = p.nj.getIndexBag(index);
        if (item == null || m.reader().available() > 18) {
            return;
        }
        if (item.getUpgrade() >= item.getUpMax()) {
            p.session.sendMessageLog("Trang bị đã đạt cấp tối đa");
            return;
        }
        final byte[] arrItem = new byte[m.reader().available()];
        int crys = 0;
        boolean keep = false;
        boolean da = false;
        for (byte i = 0; i < arrItem.length; ++i) {
            arrItem[i] = -1;
            final byte index2 = m.reader().readByte();
            final Item item2 = p.nj.getIndexBag(index2);
            if (item2 != null) {
                final ItemData data = ItemData.ItemDataId(item2.id);
                if (data.type == 26) {
                    arrItem[i] = index2;
                    crys += GameScr.crystals[item2.id];
                    da = true;
                } else {
                    if (data.type != 28) {
                        p.session.sendMessageLog("Chỉ có thể chọn đá và bảo hiểm");
                        return;
                    }
                    arrItem[i] = index2;
                    if (item2.id == 242 && item.getUpgrade() < 8) {
                        keep = true;
                    } else if (item2.id == 284 && item.getUpgrade() < 12) {
                        keep = true;
                    } else if (item2.id == 285 && item.getUpgrade() < 14) {
                        keep = true;
                    } else {
                        if (item2.id != 475) {
                            p.session.sendMessageLog("Bảo hiểm không hợp lệ");
                            return;
                        }
                        keep = true;
                    }
                }
            }
        }
        final ItemData data2 = ItemData.ItemDataId(item.id);
        int gold = 0;
        if (arrItem.length == 0 || data2.type > 10) {
            return;
        }
        if (!da) {
            p.session.sendMessageLog("Hãy chọn thêm đá");
            return;
        }
        int coins;
        int percent;
        if (data2.type == 1) {
            coins = GameScr.coinUpWeapons[item.getUpgrade()];
            percent = crys * 100 / GameScr.upWeapon[item.getUpgrade()];
            if (percent > GameScr.maxPercents[item.getUpgrade()]) {
                percent = GameScr.maxPercents[item.getUpgrade()];
            }
        } else if (data2.type % 2 == 0) {
            coins = GameScr.coinUpClothes[item.getUpgrade()];
            percent = crys * 100 / GameScr.upClothe[item.getUpgrade()];
            if (percent > GameScr.maxPercents[item.getUpgrade()]) {
                percent = GameScr.maxPercents[item.getUpgrade()];
            }
        } else {
            coins = GameScr.coinUpAdorns[item.getUpgrade()];
            percent = crys * 100 / GameScr.upAdorn[item.getUpgrade()];
            if (percent > GameScr.maxPercents[item.getUpgrade()]) {
                percent = GameScr.maxPercents[item.getUpgrade()];
            }
        }

        if (type == 1) {
            percent *= (int) 1.5;
            gold = GameScr.goldUps[item.getUpgrade()];
        }
        if (coins > p.nj.yen + p.nj.xu || gold > p.luong) {
            return;
        }
        for (byte j = 0; j < arrItem.length; ++j) {
            if (arrItem[j] != -1) {
                p.nj.ItemBag[arrItem[j]] = null;
            }
        }
        p.upluong(-gold);
        if (coins <= p.nj.yen) {
            p.nj.upyen(-coins);
        } else {
            final int coin = coins - p.nj.yen;
            p.nj.upyen(-p.nj.yen);
            p.nj.upxu(-coin);
        }
        if (item.getUpgrade() >= 15) {
            percent += 3;
        }
        final boolean suc = util.nextInt(1, 100) <= percent;
        m.cleanup();
        item.setLock(true);
        util.Debug("type " + type + " index " + index + " percen " + percent);
        if (suc) {
            item.upgradeNext((byte) 1);

            if (p.nj.getTaskId() == 12) {
                if (p.nj.getTaskIndex() == 1 && item.getData().isVuKhi()) {
                    p.nj.upMainTask();
                } else if (p.nj.getTaskIndex() == 2 && item.getData().isTrangSuc()) {
                    p.nj.upMainTask();
                } else if (p.nj.getTaskIndex() == 3 && item.getData().isTrangPhuc()) {
                    p.nj.upMainTask();
                }
            }
        } else if (!keep) {
            item.upgradeNext((byte) (-(item.getUpgrade() - KeepUpgrade(item.getUpgrade()))));
        }
        m = new Message(21);
        m.writer().writeByte(suc ? 1 : 0);
        m.writer().writeInt(p.luong);
        m.writer().writeInt(p.nj.xu);
        m.writer().writeInt(p.nj.yen);
        m.writer().writeByte(item.getUpgrade());
        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
    }

    public synchronized static void Split(final User p, Message m) throws IOException {
        if (p.nj.isNhanban) {
            p.session.sendMessageLog("Bạn đang trong chế độ thứ thân không thể dùng được chức năng này");
            return;
        }
        final byte index = m.reader().readByte();
        final Item item = p.nj.getIndexBag(index);
        if (item == null || item.getUpgrade() <= 0) {
            return;
        }
        final ItemData data = ItemData.itemDefault(item.id).getData();
        if (data.type > 10 && data.type != 14) {
            return;
        }
        int num = 0;
        if (data.type == 1) {
            for (byte i = 0; i < item.getUpgrade(); ++i) {
                num += GameScr.upWeapon[i];
            }
        } else if (data.type % 2 == 0 && data.type != 14) {
            for (byte i = 0; i < item.getUpgrade(); ++i) {
                num += GameScr.upClothe[i];
            }
        } else if (data.type == 14) {
            num = item.getUpgrade() * 6;
        } else {
            for (byte i = 0; i < item.getUpgrade(); ++i) {
                num += GameScr.upAdorn[i];
            }
        }

        num /= 2;
        int num2 = 0;
        final Item[] arrItem = new Item[24];
        int[] arrIndex = null;

        try {
            if (data.type != 14) {
                for (int n = GameScr.crystals.length - 1; n >= 0; --n) {
                    if (num >= GameScr.crystals[n]) {
                        arrItem[num2] = new Item();
                        arrItem[num2].id = (short) n;
                        arrItem[num2].setLock(item.isLock());
                        num -= GameScr.crystals[n];
                        ++n;
                        ++num2;
                    }
                }
            } else {

                if (num >= 24) {
                    num = 24;
                }
                for (int i = 0; i < num; i++) {
                    arrItem[i] = ItemData.itemDefault(item.id + 10);
                }
            }
            if (num2 > p.nj.getAvailableBag()) {
                p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                return;
            }
            arrIndex = new int[arrItem.length];
            for (byte j = 0; j < arrItem.length; ++j) {
                if (arrItem[j] != null) {
                    final int index2 = p.nj.getIndexBagNotItem();
                    p.nj.ItemBag[index2] = arrItem[j];
                    arrIndex[j] = index2;
                }
            }
        } finally {
            if (data.type == 14) {
                if (item.getUpgrade() != 0) {
                    val itemX = ItemData.itemDefault(685);
                    itemX.setUpgrade(0);
                    itemX.setLock(true);
                    p.nj.ItemBag[index] = itemX;
                    for (Option option : itemX.option) {
                        option.param = 0;
                    }
                }
            } else {
                item.upgradeNext((byte) (-item.getUpgrade()));
            }
        }

        m = new Message(22);
        m.writer().writeByte(num2);
        for (byte j = 0; j < num2; ++j) {
            if (arrItem[j] != null) {
                m.writer().writeByte(arrIndex[j]);
                m.writer().writeShort(arrItem[j].id);
            }
        }
        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
        p.sendInfo(false);
    }

    public static void LuckValue(final User p, Message m) throws IOException {
        byte index = m.reader().readByte();
        m.cleanup();
        if (index < 0 || index > 8) {
            index = 0;
        }
        if (p.nj.getAvailableBag() == 0) {
            p.session.sendMessageLog("Hành trang không đủ chỗ trống.");
            return;
        }
        if (p.nj.quantityItemyTotal(340) == 0) {
            p.session.sendMessageLog("Cần có phiếu may mắn.");
            return;
        }

        short[] itemIds = LAT_HINH_ID;
        // int MAX_LEVEL = p.nj.getLevel() - p.nj.getLevel() % 10 + 10;
        // if (MAX_LEVEL >= 100) {
        // MAX_LEVEL = 100;
        // }

        // if (MAX_LEVEL == 100) {
        // itemIds = LAT_HINH_LV100_ID;
        // } else if (MAX_LEVEL == 90) {
        // itemIds = LAT_HINH_LV90_ID;
        // } else if (MAX_LEVEL == 80) {
        // itemIds = LAT_HINH_LV80_ID;
        // } else if (MAX_LEVEL == 70) {
        // itemIds = LAT_HINH_LV70_ID;
        // } else if (MAX_LEVEL == 60) {
        // itemIds = LAT_HINH_LV60_ID;
        // } else if (MAX_LEVEL == 50) {
        // itemIds = LAT_HINH_LV50_ID;
        // } else if (MAX_LEVEL == 40) {
        // itemIds = LAT_HINH_LV40_ID;
        // } else if (MAX_LEVEL == 30) {
        // itemIds = LAT_HINH_LV30_ID;
        // } else if (MAX_LEVEL == 20) {
        // itemIds = LAT_HINH_LV20_ID;
        // } else if (MAX_LEVEL == 10) {
        // itemIds = LAT_HINH_LV10_ID;
        // } else {
        // itemIds = LAT_HINH_ID;
        // }

        final short id = itemIds[util.nextInt(itemIds.length - 1)];
        /**
         *
         */
        p.nj.removeItemBags(340, 1);
        ItemData data = ItemData.ItemDataId(id);

        if (data == null) {
            data = ItemData.ItemDataId(12);
        }

        Item item;
        if (data.type < 10) {
            if (data.type == 1) {
                item = ItemData.itemDefault(id);
                item.sys = SysClass(data.nclass);
            } else {
                final byte sys = (byte) util.nextInt(1, 3);
                item = ItemData.itemDefault(id, sys);
            }
        } else {
            item = ItemData.itemDefault(id);
        }
        if (id == 523 || id == 419) {
            item.isExpires = true;
            item.expires = util.TimeDay(GameScr.ArrdayLuck[util.nextInt(GameScr.ArrdayLuck.length)]);
        }
        if (data.type != 19) {
            p.nj.addItemBag(true, item);
        } else {
            item.quantity = GameScr.ArryenLuck[util.nextInt(GameScr.ArryenLuck.length)];
            p.nj.upyenMessage(item.quantity);
            p.sendYellowMessage("Bạn nhận được " + item.quantity + " yên");
        }
        m = new Message(-28);
        m.writer().writeByte(-72);
        for (byte i = 0; i < 9; ++i) {
            if (i == index) {
                m.writer().writeShort(id);
            } else {
                m.writer().writeShort(itemIds[util.nextInt(itemIds.length - 1)]);
            }
        }

        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
    }

    public static void LuyenThach(User p, Message m) throws IOException {
        byte[] arrItem = new byte[m.reader().available()];

        Item item = null;
        int checkTTS = 0;
        int checkTTT = 0;

        p.endLoad(true);

        if (arrItem.length == 4) {
            for (byte i = 0; i < arrItem.length; i++) {
                byte index2 = m.reader().readByte();
                item = p.nj.getIndexBag(index2);
                if (item.id == 455) {
                    checkTTS++;
                    checkTTT = 0;
                } else if (item.id == 456) {
                    checkTTT++;
                    checkTTS = 0;
                }
                p.nj.removeItemBag(index2, 1);
            }
            if (checkTTS > 0) {
                p.nj.addItemBag(false, ItemData.itemDefault(456));
            } else if (checkTTT > 0) {
                p.nj.addItemBag(false, ItemData.itemDefault(457));
            }
            return;

        } else if (arrItem.length == 9) {
            for (byte i = 0; i < arrItem.length; i++) {
                byte index2 = m.reader().readByte();
                if (i == 0) {
                    item = p.nj.getIndexBag(index2);
                }
                p.nj.removeItemBag(index2, 1);
            }

            if (item.id == 455) {
                p.nj.addItemBag(false, ItemData.itemDefault(456));
            } else if (item.id == 456) {
                p.nj.addItemBag(false, ItemData.itemDefault(457));
            }
            return;
        }

    }

    public static void TinhLuyen(final User p, final Message m) throws IOException {
        final byte index = m.reader().readByte();
        final Item it = p.nj.getIndexBag(index);
        if (it == null) {
            return;
        }
        final ItemData data = ItemData.ItemDataId(it.id);
        int tl = -1;
        for (byte i = 0; i < it.option.size(); ++i) {
            if (it.option.get(i).id == 85) {
                tl = it.option.get(i).param;
                if (tl >= 9) {
                    p.session.sendMessageLog("Vật phẩm đã được tinh luyên tối đa");
                    return;
                }
            }
        }
        if (tl == -1) {
            p.session.sendMessageLog("Vật phẩm không dùng cho tinh luyện");
            return;
        }
        int ttts = 0;
        int tttt = 0;
        int tttc = 0;
        final byte[] arit = new byte[m.reader().available()];
        for (byte j = 0; j < arit.length; ++j) {
            final byte ind = m.reader().readByte();
            final Item item = p.nj.getIndexBag(ind);
            if (item == null) {
                return;
            }
            if (item.id != 455 && item.id != 456 && item.id != 457) {
                p.session.sendMessageLog("Vật phẩm không dùng cho tinh luyện");
                return;
            }
            arit[j] = ind;
            if (item.id == 455) {
                ++ttts;
            } else if (item.id == 456) {
                ++tttt;
            } else if (item.id == 457) {
                ++tttc;
            }
        }
        int percent = 0;
        int yen = 0;
        switch (tl) {
            case 0: {
                percent = 60;
                yen = 150000;
                if (ttts != 3 || tttt != 0 || tttc != 0) {
                    p.session.sendMessageLog("Tinh luyện 1 cần dùng 3 Tử tinh thạch sơ");
                    return;
                }
                break;
            }
            case 1: {
                percent = 55;
                yen = 247500;
                if (ttts != 5 || tttt != 0 || tttc != 0) {
                    p.session.sendMessageLog("Tinh luyện 2 cần dùng 5 Tử tinh thạch sơ");
                    return;
                }
                break;
            }
            case 2: {
                percent = 50;
                yen = 408375;
                if (ttts != 9 || tttt != 0 || tttc != 0) {
                    p.session.sendMessageLog("Tinh luyện 3 cần dùng 9 Tử tinh thạch sơ");
                    return;
                }
                break;
            }
            case 3: {
                percent = 45;
                yen = 673819;
                if (ttts != 0 || tttt != 4 || tttc != 0) {
                    p.session.sendMessageLog("Tinh luyện 4 cần dùng 4 Tử tinh thạch trung");
                    return;
                }
                break;
            }
            case 4: {
                percent = 40;
                yen = 1111801;
                if (ttts != 0 || tttt != 7 || tttc != 0) {
                    p.session.sendMessageLog("Tinh luyện 5 cần dùng 7 Tử tinh thạch trung");
                    return;
                }
                break;
            }
            case 5: {
                percent = 35;
                yen = 2056832;
                if (ttts != 0 || tttt != 10 || tttc != 0) {
                    p.session.sendMessageLog("Tinh luyện 5 cần dùng 7 Tử tinh thạch trung");
                    return;
                }
                break;
            }
            case 6: {
                percent = 30;
                yen = 4010922;
                if (ttts != 0 || tttt != 0 || tttc != 5) {
                    p.session.sendMessageLog("Tinh luyện 7 cần dùng 5 Tử tinh thạch cao");
                    return;
                }
                break;
            }
            case 7: {
                percent = 25;
                yen = 7420021;
                if (ttts != 0 || tttt != 0 || tttc != 7) {
                    p.session.sendMessageLog("Tinh luyện 8 cần dùng 7 Tử tinh thạch cao");
                    return;
                }
                break;
            }
            case 8: {
                percent = 20;
                yen = 12243035;
                if (ttts != 0 || tttt != 0 || tttc != 9) {
                    p.session.sendMessageLog("Tinh luyện 9 cần dùng 9 Tử tinh thạch cao");
                    return;
                }
                break;
            }
        }
        if (yen > p.nj.yen && yen > p.nj.xu && yen > p.nj.xu + p.nj.yen) {
            p.session.sendMessageLog("Không đủ yên hoặc xu tinh luyện");
            return;
        }
        p.endLoad(true);
        if (p.nj.yen >= yen) {
            p.nj.upyenMessage(-yen);
        } else if (p.nj.xu >= yen) {
            p.nj.upxuMessage(-yen);
        } else {
            val preYen = p.nj.yen;
            p.nj.upyenMessage(-preYen);
            p.nj.upXuMessage(-(yen - preYen));
        }

        if (percent >= util.nextInt(80)) {
            for (byte k = 0; k < it.option.size(); ++k) {
                final Option option = it.option.get(k);
                option.param += ItemData.ThinhLuyenParam(it.option.get(k).id, tl);
            }
            p.requestItemInfoMessage(it, index, 3);
            p.sendYellowMessage("Tinh luyện thành công!");
        } else {
            p.sendYellowMessage("Tinh luyện thất bại!");
        }
        for (byte k = 0; k < arit.length; ++k) {
            p.nj.removeItemBag(arit[k], 1);
        }
    }

    public static void DichChuyen(final User p, final Message m) throws IOException {
        final byte index = m.reader().readByte();
        final Item item = p.nj.getIndexBag(index);
        if (item != null && ItemData.isTypeBody(item.id) && item.getUpgrade() > 11) {
            for (byte i = 0; i < item.option.size(); ++i) {
                if (item.option.get(i).id == 85) {
                    p.session.sendMessageLog("Vật phẩm đã được dịch chuyển");
                    return;
                }
            }
            final byte[] arrIndex = new byte[20];
            for (byte j = 0; j < arrIndex.length; ++j) {
                final byte index2 = m.reader().readByte();
                final Item item2 = p.nj.getIndexBag(index2);
                if (item2 == null || item2.id != 454) {
                    return;
                }
                arrIndex[j] = index2;
            }
            p.endLoad(true);
            final ItemData data = ItemData.ItemDataId(item.id);
            item.option.add(new Option(85, 0));
            switch (data.type) {
                case 0: {
                    if (item.sys == 1) {
                        item.option.add(new Option(96, 10));
                    } else if (item.sys == 2) {
                        item.option.add(new Option(95, 10));
                    } else if (item.sys == 3) {
                        item.option.add(new Option(97, 10));
                    }
                    item.option.add(new Option(79, 5));
                    break;
                }
                case 1: {
                    item.option.add(new Option(87, util.nextInt(450, 500)));
                    item.option.add(new Option(87 + item.sys, util.nextInt(450, 500)));
                    break;
                }
                case 2: {
                    item.option.add(new Option(80, 50));
                    item.option.add(new Option(91, 10));
                    break;
                }
                case 3: {
                    item.option.add(new Option(81, 5));
                    item.option.add(new Option(79, 5));
                    break;
                }
                case 4: {
                    item.option.add(new Option(86, 120));
                    item.option.add(new Option(94, util.nextInt(120, 124)));
                    break;
                }
                case 5: {
                    if (item.sys == 1) {
                        item.option.add(new Option(96, 5));
                    } else if (item.sys == 2) {
                        item.option.add(new Option(95, 5));
                    } else if (item.sys == 3) {
                        item.option.add(new Option(97, 5));
                    }
                    item.option.add(new Option(92, 10));
                    break;
                }
                case 6: {
                    item.option.add(new Option(83, util.nextInt(450, 500)));
                    item.option.add(new Option(82, util.nextInt(450, 500)));
                    break;
                }
                case 7: {
                    if (item.sys == 1) {
                        item.option.add(new Option(96, 5));
                    } else if (item.sys == 2) {
                        item.option.add(new Option(95, 5));
                    } else if (item.sys == 3) {
                        item.option.add(new Option(97, 5));
                    }
                    item.option.add(new Option(87 + item.sys, util.nextInt(500, 600)));
                    break;
                }
                case 8: {
                    item.option.add(new Option(82, util.nextInt(450, 500)));
                    item.option.add(new Option(84, util.nextInt(90, 100)));
                    break;
                }
                case 9: {
                    item.option.add(new Option(84, util.nextInt(90, 100)));
                    item.option.add(new Option(83, util.nextInt(450, 500)));
                    break;
                }
            }
            for (byte k = 0; k < arrIndex.length; ++k) {
                p.nj.removeItemBag(arrIndex[k], 1);
            }
            p.sendYellowMessage("Đã dịch chuyển trang bị");
            p.requestItemInfoMessage(item, index, 3);
        }
        util.Debug(index + " " + item.id);
    }

    @SneakyThrows
    public static void requestMapTemplate(User user, Message m) {

        final int templateId = m.reader().readUnsignedByte();
        m.cleanup();

        final Message ms = new Message(-28);
        String url = "res/map/" + templateId;
        if (templateId == 139) {
            url = "res/map_file_msg/map_back.bin";
        } else {
            ms.writer().writeByte(-109);
        }
        val data = loadFile(url).toByteArray();
        ms.writer().write(data);
        user.sendMessage(ms);

    }

    @SneakyThrows
    public static void ngocFeature(User p, Message m) {

        if (p.nj.get().isNhanban) {
            p.sendYellowMessage("Tính năng ko dành cho phân thân");
            return;
        }

        val type = m.reader().readByte();
        val indexUI = m.reader().readByte();

        if (type == 0) {
            // Kham ngoc
            val ngocIndex = m.reader().readByte();
            val ngocItem = p.nj.ItemBag[ngocIndex];
            val item = p.nj.ItemBag[indexUI];
            p.endLoad(true);
            if (!item.getData().isTrangSuc() &&
                    !item.getData().isTrangPhuc() &&
                    !item.getData().isVuKhi()) {
                p.sendYellowMessage("Trang bị không hỗ trợ");
                return;
            }

            if (item.ngocs != null && item.ngocs.stream().anyMatch(n -> n.id == ngocItem.id)) {
                p.sendYellowMessage("Ngọc đã được khảm vào trang bị rồi");
                return;
            }

            val yen = ngocItem.option.get(ngocItem.option.indexOf(new Option(ItemData.GIA_KHAM_OPTION_ID, 0))).param;

            if (p.nj.yen < yen) {
                p.sendYellowMessage("Không đủ yên để khảm");
                return;
            }

            p.nj.upyenMessage(-yen);

            val data2 = item.getData();

            int crys = 0;
            try {
                while (true) {
                    val index = m.reader().readByte();
                    val tone = p.nj.ItemBag[index];
                    p.nj.removeItemBag(index);
                    crys += GameScr.crystals[tone.id];
                }
            } catch (Exception e) {

            }

            int coins;
            int percent;
            if (data2.type == 1) {
                coins = GameScr.coinUpWeapons[ngocItem.getUpgrade()];
                percent = crys * 100 / GameScr.upWeapon[ngocItem.getUpgrade()];
                if (percent > GameScr.maxPercents[ngocItem.getUpgrade()]) {
                    percent = GameScr.maxPercents[ngocItem.getUpgrade()];
                }
            } else if (data2.type % 2 == 0) {
                coins = GameScr.coinUpClothes[ngocItem.getUpgrade()];
                percent = crys * 100 / GameScr.upClothe[ngocItem.getUpgrade()];
                if (percent > GameScr.maxPercents[ngocItem.getUpgrade()]) {
                    percent = GameScr.maxPercents[ngocItem.getUpgrade()];
                }
            } else {
                coins = GameScr.coinUpAdorns[ngocItem.getUpgrade()];
                percent = crys * 100 / GameScr.upAdorn[ngocItem.getUpgrade()];
                if (percent > GameScr.maxPercents[ngocItem.getUpgrade()]) {
                    percent = GameScr.maxPercents[ngocItem.getUpgrade()];
                }
            }

            if (coins <= p.nj.yen) {
                p.nj.upyen(-coins);
            } else {
                final int coin = coins - p.nj.yen;
                p.nj.upyen(-p.nj.yen);
                p.nj.upxu(-coin);
            }
            final boolean suc = util.nextInt(1, 100) <= percent;
            m.cleanup();
            item.setLock(true);
            ngocItem.setLock(true);

            if (suc) {
                item.ngocs.add(ngocItem);
                p.nj.removeItemBag(ngocIndex);
                p.sendYellowMessage("Khảm ngọc thành công");
            } else {
                p.sendYellowMessage("Khảm ngọc thất bại");
            }

            m = new Message(21);
            m.writer().writeByte(suc ? 1 : 0);
            m.writer().writeInt(p.luong);
            m.writer().writeInt(p.nj.xu);
            m.writer().writeInt(p.nj.yen);
            m.writer().writeByte(item.getUpgrade());
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();

            p.requestItemInfoMessage(item, indexUI, 3);

        } else if (type == 1) {
            int exp = 0;
            Item mainItem = p.nj.ItemBag[indexUI];
            final int i = mainItem.option.indexOf(new Option(EXP_ID, 0));
            p.endLoad(true);

            try {
                while (true) {
                    val index = m.reader().readByte();
                    Item item = p.nj.ItemBag[index];
                    p.nj.removeItemBag(index);

                    if (item.isTypeNgocKham()) {
                        if (item.getUpgrade() == 1) {
                            exp += 10;
                        } else if (item.getUpgrade() == 2) {
                            exp += 20;
                        } else if (item.getUpgrade() == 3) {
                            exp += 50;
                        } else if (item.getUpgrade() == 4) {
                            exp += 100;
                        } else if (item.getUpgrade() == 5) {
                            exp += 200;
                        } else if (item.getUpgrade() == 6) {
                            exp += 500;
                        } else if (item.getUpgrade() == 7) {
                            exp += 1000;
                        } else if (item.getUpgrade() == 8) {
                            exp += 2000;
                        } else if (item.getUpgrade() == 9) {
                            exp += 5000;
                        } else if (item.getUpgrade() == 10) {
                            exp += 10_000;
                        }

                    }
                }
            } catch (Exception e) {
            }

            int oldUpGrad = getNextUpgrade(mainItem.option.get(i).param);

            mainItem.option.get(i).param += exp;
            int nextUpgrade = getNextUpgrade(mainItem.option.get(i).param);

            upgradeNgoc(mainItem, oldUpGrad, nextUpgrade);

            mainItem.setUpgrade((byte) nextUpgrade);
            val m2 = new Message(-144);
            m2.writer().writeByte(indexUI);
            m2.writer().writeByte(mainItem.getUpgrade());
            p.sendMessage(m2);
            m2.cleanup();

        } else if (type == 2) {
            // Got ngoc

            try {

                Item item = p.nj.ItemBag[indexUI];
                if (item == null)
                    return;
                if (p.nj.xu < GameScr.xuGotNgoc.get((int) item.getUpgrade())) {
                    p.sendYellowMessage("Không đủ xu để gọt");
                    p.endLoad(true);
                    return;
                }

                p.nj.upxuMessage(-GameScr.xuGotNgoc.get((int) item.getUpgrade()));
                p.endLoad(true);
                for (Option option : item.option) {
                    if (option.param < -1) {
                        option.param += util.nextInt(ItemData.PARAMS.get(option.id) * 30 / 100,
                                ItemData.PARAMS.get(option.id) * 50 / 100);
                        if (option.param >= 0) {
                            option.param = -1;
                        }
                    }
                }

                val m2 = new Message(-144);
                m2.writer().writeByte(indexUI);
                m2.writer().writeByte(item.getUpgrade());
                p.sendMessage(m2);
                m2.cleanup();
            } catch (Exception e) {
                p.endLoad(true);
            }

        } else if (type == 3) {
            // DOSTH
            val item = p.nj.ItemBag[indexUI];
            val iter = item.ngocs.iterator();
            p.endLoad(true);

            while (iter.hasNext()) {
                Item ngoc = iter.next();
                p.nj.addItemBag(false, ngoc);
                iter.remove();
            }

            p.requestItemInfoMessage(item, indexUI, 3);
            p.sendYellowMessage("Tháo ngọc thành công");
        }

        p.endLoad(true);

    }

    public static void upgradeNgoc(Item mainItem, int oldUpGrad, int nextUpgrade) {
        for (int j = oldUpGrad; j < nextUpgrade; j++) {

            for (Option option : mainItem.option) {
                if (ItemData.PARAMS.containsKey(option.id)) {
                    option.param += (option.param / Math.abs(option.param))
                            * util.nextInt((int) (0.4 * ItemData.PARAMS.get(option.id)),
                                    (int) (0.7 * ItemData.PARAMS.get(option.id)));
                }
            }

            mainItem.option.stream().filter(o -> o.id == ItemData.GIA_KHAM_OPTION_ID)
                    .forEach(o -> o.param += 400000);
        }
    }

    private static final HashMap<Integer, Integer> xuGotNgoc;
    public static final HashMap<Integer, Integer> exps;

    static {
        xuGotNgoc = new HashMap<>();
        exps = new HashMap<>();
        xuGotNgoc.put(1, 5_000);
        xuGotNgoc.put(2, 40_000);
        xuGotNgoc.put(3, 135_000);
        xuGotNgoc.put(4, 320_000);
        xuGotNgoc.put(5, 625_000);
        xuGotNgoc.put(6, 1_080_000);
        xuGotNgoc.put(7, 1_715_000);
        xuGotNgoc.put(8, 2_560_000);
        xuGotNgoc.put(9, 3_645_000);
        xuGotNgoc.put(10, 5_000_000);

        exps.put(1, 0);
        exps.put(2, 210);
        exps.put(3, 510);
        exps.put(4, 1010);
        exps.put(5, 2010);
        exps.put(6, 5010);
        exps.put(7, 10_010);
        exps.put(8, 20_010);
        exps.put(9, 50_010);
        exps.put(10, 100_010);
    }

    private static int getNextUpgrade(int xExp) {

        if (xExp > 200 && xExp <= 500) {
            return 2;
        } else if (xExp > 500 && xExp <= 1_000) {
            return 3;
        } else if (xExp > 1_000 && xExp <= 2_000) {
            return 4;
        } else if (xExp > 2_000 && xExp <= 5_000) {
            return 5;
        } else if (xExp > 5000 && xExp <= 10_000) {
            return 6;
        } else if (xExp > 10_000 && xExp <= 20_000) {
            return 7;
        } else if (xExp > 20_000 && xExp <= 50_000) {
            return 8;
        } else if (xExp > 50_000 && xExp <= 100_000) {
            return 9;
        } else if (xExp > 100_000) {
            return 10;
        }

        return 1;
    }

    public static void requestRankedInfo(User p, String ninjaName) {
        try {
            final User user = KageTournament.gi().getUserByNinjaName(ninjaName);
            p.viewInfoPlayers(user);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static {
        GameScr.server = Server.getInstance();
        upClothe = new int[] { 4, 9, 33, 132, 177, 256, 656, 2880, 3968, 6016, 13440, 54144, 71680, 108544, 225280,
                1032192 };
        upAdorn = new int[] { 6, 14, 50, 256, 320, 512, 1024, 5120, 6016, 9088, 19904, 86016, 108544, 166912, 360448,
                1589248 };
        upWeapon = new int[] { 18, 42, 132, 627, 864, 1360, 2816, 13824, 17792, 26880, 54016, 267264, 315392, 489472,
                1032192, 4587520 };
        coinUpCrystals = new int[] { 10, 40, 160, 640, 2560, 10240, 40960, 163840, 655360, 1310720, 3932160, 11796480 };
        crystals = new int[] { 1, 4, 16, 64, 256, 1024, 4096, 16384, 65536, 262144, 1048576, 3096576 };
        coinUpClothes = new int[] { 120, 270, 990, 3960, 5310, 7680, 19680, 86400, 119040, 180480, 403200, 1624320,
                2150400, 3256320, 6758400, 10137600 };
        coinUpAdorns = new int[] { 180, 420, 1500, 7680, 9600, 15360, 30720, 153600, 180480, 272640, 597120, 2580480,
                3256320, 5007360, 10813440, 16220160 };
        coinUpWeapons = new int[] { 540, 1260, 3960, 18810, 25920, 40800, 84480, 414720, 533760, 806400, 1620480,
                8017920, 9461760, 14684160, 22026240, 33039360 };
        goldUps = new int[] { 1, 2, 3, 4, 5, 10, 15, 20, 50, 100, 150, 200, 300, 400, 500, 600 };
        maxPercents = new int[] { 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25, 20,
                15, 10, 5 };
        // maxPercents = new int[] { 80, 77, 74, 71, 68, 65, 62, 59, 56, 53, 50, 47, 44,
        // 44, 41, 38 };
        ArryenLuck = new int[] { 10_000, 20_000 };
        ArrdayLuck = new byte[] { 3, 7, 15, 30 };
        // TODO
        // Task ID,
    }

}
