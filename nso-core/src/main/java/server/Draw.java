package server;

import real.Ninja;
import real.ClanManager;
import real.Item;
import real.ItemData;
import real.PlayerManager;
import java.io.IOException;

import patch.EventItem;
import threading.Message;
import real.User;
import threading.Server;

public class Draw {

    private static final Server server;

    public static void Draw(final User p, final Message m) throws IOException {
        final short menuId = m.reader().readShort();
        final String str = m.reader().readUTF();
        m.cleanup();
        util.Debug("menuId " + menuId + " str " + str);
        byte b = -1;
        try {
            b = m.reader().readByte();
        } catch (IOException ex) {
        }
        m.cleanup();
        switch (menuId) {
            case 1: {
                if (p.nj.quantityItemyTotal(279) <= 0) {
                    break;
                }
                final Ninja c = PlayerManager.getInstance().getNinja(str);
                if (c.getPlace() != null && !c.getPlace().map.isLangCo() && c.getPlace().map.getXHD() == -1) {
                    p.nj.getPlace().leave(p);
                    p.nj.get().x = c.get().x;
                    p.nj.get().y = c.get().y;
                    c.getPlace().Enter(p);
                    return;
                }
                p.sendYellowMessage("Vị trí người này không thể đi tới");
                break;
            }
            case 24_0_0: {
                try {
                    long luong = Integer.parseInt(str);
                    if (luong <= 0) {
                        p.session.sendMessageLog("Phải nhập số lớn hơn 0.");
                        break;
                    }
                    if (p.luong < luong) {
                        p.session.sendMessageLog("Số lượng trong hành trang của bạn phải lớn hơn " + luong + " lượng.");
                        break;
                    } else if (p.nj.yen + 10_000 * luong > 2_000_000_000) {
                        p.session.sendMessageLog("Số yên trong hành trang của bạn đã đạt mức tối đa.");
                        break;
                    } else {
                        p.upluongMessage(-luong);
                        p.nj.upyenMessage(10_000 * luong);
                        break;
                    }
                } catch (NumberFormatException ex) {
                    p.session.sendMessageLog("Sai định dạng.");
                    break;
                }
            }
            case 24_0_1: {
                try {
                    long luong = Integer.parseInt(str);
                    if (luong <= 0) {
                        p.session.sendMessageLog("Phải nhập số lớn hơn 0.");
                        break;
                    }
                    if (p.luong < luong) {
                        p.session.sendMessageLog("Số lượng trong hành trang của bạn phải lớn hơn " + luong + " lượng.");
                        break;
                    } else if (p.nj.xu + 10_000 * luong > 2_000_000_000) {
                        p.session.sendMessageLog("Số xu trong hành trang của bạn đã đạt mức tối đa.");
                        break;
                    } else {
                        p.upluongMessage(-luong);
                        p.nj.upxuMessage(10_000 * luong);
                        break;
                    }
                } catch (NumberFormatException ex) {
                    p.session.sendMessageLog("Sai định dạng.");
                    break;
                }
            }
            case 24_1: {

                try {
                    long yen = Integer.parseInt(str);
                    if (yen <= 0) {
                        p.session.sendMessageLog("Phải nhập số lớn hơn 0.");
                        break;
                    }
                    long maxYenTransfer = p.nj.nActPoint * 10_000_000L;
                    if (p.nj.yen < yen) {
                        p.session.sendMessageLog("Số yên trong hành trang của bạn phải lớn hơn " + yen + " yên.");
                        break;
                    } else if (p.nj.xu + yen > 2_000_000_000) {
                        p.session.sendMessageLog("Số xu trong hành trang của bạn đã đạt mức tối đa.");
                        break;
                    } else if (yen > maxYenTransfer) {
                        p.session.sendMessageLog("Bạn chỉ có thể chuyển " + maxYenTransfer + " yên.");
                        break;
                    } else {
                        p.nj.upNActPoint(-((int) (yen / 10_000_000L) + 1));
                        p.nj.upyenMessage(-yen);
                        p.nj.upxuMessage(yen);
                        break;
                    }
                } catch (NumberFormatException ex) {
                    p.session.sendMessageLog("Sai định dạng.");
                    break;
                }
            }

            case 24_7_0: {
                p.cardCode = str;
                if (str.length() > 0 && util.CheckString(str, "^\\d+")) {
                    Draw.server.menu.sendWrite(p, (short) 24_7_1, "Nhập Seri");
                }
                break;
            }

            case 24_7_1: {
                p.cardSeri = str;
                if (str.length() > 0 && util.CheckString(str, "^\\d+")) {
                    p.submitCardDcoin();
                }
                break;
            }

            case 24_8: {
                p.nameUS = str;
                Ninja user_gift_gold = PlayerManager.getInstance().getNinja(p.nameUS);
                if (user_gift_gold == null) {
                    p.session.sendMessageLog("Người chơi không có online. Không thể nhận lượng.");
                    break;
                }

                Draw.server.menu.sendWrite(p, (short) 24_8_1, "Nhập số lượng vé");
                break;
            }
            case 24_8_1: {
                try {
                    int nTicket = Integer.parseInt(str);
                    if (nTicket <= 0) {
                        p.session.sendMessageLog("Phải nhập số lớn hơn 0.");
                        break;
                    }
                    if (nTicket > 10_000) {
                        p.session.sendMessageLog("Chỉ có thể tặng tối đa 10.000 vé lượng 1 lần.");
                        break;
                    }
                    Ninja user_gift_gold = PlayerManager.getInstance().getNinja(p.nameUS);
                    if (user_gift_gold == null) {
                        p.session.sendMessageLog("Người chơi không có online. Không thể nhận lượng.");
                        break;
                    } else {
                        p.sendGold(nTicket);
                        break;
                    }
                } catch (NumberFormatException ex) {
                    p.session.sendMessageLog("Sai định dạng.");
                    break;
                }
            }
            case 28_3: {
                // 11 for linh tinh
                Draw.server.menu.showShinwaItems(p, 11, str);
                break;
            }
            case 50: {
                ClanManager.createClan(p, str);
                break;
            }
            case 51: {
                p.passnew = "";
                p.passold = str;
                p.changePassword();
                Draw.server.menu.sendWrite(p, (short) 52, "Nhập mật khẩu mới");
                break;
            }
            case 52: {
                p.passnew = str;
                p.changePassword();
                break;
            }
            case 53: {
                p.giftcode = str;
                p.giftcode();
                break;
            }
            case 100: {
                final String num = str.replaceAll(" ", "").trim();
                if (num.length() > 10 || !util.checkNumInt(num) || b < 0
                        || b >= Draw.server.manager.rotationluck.length) {
                    return;
                }
                final int xujoin = Integer.parseInt(num);
                if (b != 2) {
                    Draw.server.manager.rotationluck[1 - b].joinLuck(p, xujoin);
                } else {
                    Draw.server.manager.rotationluck[2].joinLuck(p, xujoin);
                }
                break;
            }
            case 101: {
                if (b < 0 || b >= Draw.server.manager.rotationluck.length) {
                    return;
                }
                if (b != 2) {
                    Draw.server.manager.rotationluck[1 - b].luckMessage(p);
                } else {
                    Draw.server.manager.rotationluck[2].luckMessage(p);
                }
                break;
            }
            case 102: {
                p.typemenu = 92;
                MenuController.doMenuArray(p, new String[] { "Vòng xoay thường", "Vòng xoay VIP", "Tài Xỉu" });
                break;
            }
            case 600:
            case 601:
            case 610:
            case 611: {
                try {
                    int nTicket = Integer.parseInt(str);
                    if (nTicket > 10 || nTicket <= 0) {
                        p.session.sendMessageLog("Một lần nhập chỉ từ 0 -> 10.");
                        break;
                    }

                    long amount = nTicket * 100_000_000;
                    if (menuId % 2 == 0) {
                        // gui
                        if (menuId % 100 == 0) {
                            // yen
                            if (p.nj.yen < amount) {
                                p.session.sendMessageLog("Bạn không đủ " + amount + " yên để kí gửi.");
                                break;
                            }

                            p.nj.upyenMessage(-amount);
                            p.nj.upTicketYen(nTicket);
                        } else {
                            // xu
                            if (p.nj.xu < amount) {
                                p.session.sendMessageLog("Bạn không đủ " + amount + " xu để kí gửi.");
                                break;
                            }
                            p.nj.upxuMessage(-amount);
                            p.nj.upTicketXu(nTicket);
                        }
                    } else {
                        // rut
                        if (menuId % 100 == 1) {
                            // yen
                            if (nTicket > p.nj.ticketYen) {
                                p.session.sendMessageLog("Bạn không đủ " + nTicket + " vé yên để rút.");
                                break;
                            }

                            if (p.nj.yen + amount > 2_000_000_000 || p.nj.yen + amount < 0) {
                                p.session.sendMessageLog(
                                        "Yên trong hành trang của bạn quá lớn, không thể rút " + amount + " yên.");
                                break;
                            }

                            p.nj.upyenMessage(amount);
                            p.nj.upTicketYen(-nTicket);
                        } else {
                            // xu
                            if (nTicket > p.nj.ticketXu) {
                                p.session.sendMessageLog("Bạn không đủ " + nTicket + " vé xu để rút.");
                                break;
                            }

                            if (p.nj.xu + amount > 2_000_000_000 || p.nj.xu + amount < 0) {
                                p.session.sendMessageLog(
                                        "Xu trong hành trang của bạn quá lớn, không thể rút " + amount + " xu.");
                                break;
                            }

                            p.nj.upxuMessage(amount);
                            p.nj.upTicketXu(-nTicket);

                        }
                    }
                } catch (NumberFormatException ex) {
                    p.session.sendMessageLog("Sai định dạng.");
                    break;
                }

                break;
            }

            default: {
                if (menuId >= MenuController.MIN_EVENT_MENU_ID && menuId <= 3 * MenuController.MIN_EVENT_MENU_ID) {
                    if (menuId <= MenuController.MIN_EVENT_MENU_ID + EventItem.entrys.length) {
                        // create vpsk with more quantities
                        int index = menuId - MenuController.MIN_EVENT_MENU_ID;

                        try {
                            int quantity = Integer.parseInt(str);
                            if (quantity > 5000 || quantity <= 0) {
                                p.session.sendMessageLog("Một lần nhập chỉ từ 0 -> 5000.");
                                break;
                            }

                            EventItem entry = EventItem.entrys[index];
                            MenuController.lamSuKien(p, entry, quantity);
                        } catch (NumberFormatException ex) {
                            p.session.sendMessageLog("Sai định dạng.");
                            break;
                        }
                    } else if (menuId < MenuController.MIN_EVENT_MENU_ID * 2) {
                        int itemId = menuId - MenuController.MIN_EVENT_MENU_ID;

                        p.nameUG = str;
                        String partnerName = p.nameUG;
                        Ninja user_gift = PlayerManager.getInstance().getNinja(partnerName);
                        if (user_gift == null) {
                            p.sendYellowMessage("Người chơi không có online. Không thể nhận quà.");
                            break;
                        }
                        if (user_gift.gender != 0) {
                            p.sendYellowMessage("Bạn chỉ có thể tặng cho nhân vật nữ.");
                            break;
                        }
                        if (user_gift.name.equals(p.nj.name)) {
                            p.sendYellowMessage("Bạn không thể tặng cho chính mình.");
                            break;
                        }

                        Draw.server.menu.sendWrite(p, (short) (MenuController.MIN_EVENT_MENU_ID * 2 + itemId),
                                "Nhập số lượng");
                        break;
                    } else {
                        // give gift item
                        int itemId = menuId - 2 * MenuController.MIN_EVENT_MENU_ID;

                        int quantity;
                        try {
                            quantity = Integer.parseInt(str);
                            if (quantity > 100 || quantity <= 0) {
                                p.session.sendMessageLog("Một lần nhập chỉ từ 0 -> 100.");
                                break;
                            }
                        } catch (NumberFormatException ex) {
                            p.session.sendMessageLog("Sai định dạng.");
                            break;
                        }

                        util.Debug("Số lượng: " + quantity);

                        String partnerName = p.nameUG;
                        Ninja user_gift = PlayerManager.getInstance().getNinja(partnerName);
                        if (user_gift == null) {
                            p.sendYellowMessage("Người chơi không có online. Không thể nhận quà.");
                            break;
                        }
                        if (user_gift.gender != 0) {
                            p.sendYellowMessage("Bạn chỉ có thể tặng cho nhân vật nữ.");
                            break;
                        }
                        if (user_gift.name.equals(p.nj.name)) {
                            p.sendYellowMessage("Bạn không thể tặng cho chính mình.");
                            break;
                        }

                        EventItem entry = EventItem.getEventItemFromOutputItemId(itemId);
                        if (p.nj.quantityItemyTotal(itemId) < quantity) {
                            p.sendYellowMessage("Bạn không có đủ " + quantity + " "
                                    + entry.getOutput().getItemData().name + " để tặng");
                            break;
                        }

                        if (user_gift.getAvailableBag() <= quantity) {
                            p.sendYellowMessage("Hành trang đối phương không đủ chỗ trống để nhận " + quantity + " "
                                    + entry.getOutput().getItemData().name);
                            break;
                        }
                        if (p.nj.getAvailableBag() <= quantity) {
                            p.sendYellowMessage("Hành trang của bạn không đủ chỗ trống để tặng " + quantity + " "
                                    + entry.getOutput().getItemData().name);
                            break;
                        }

                        for (int i = 0; i < quantity; i++) {
                            p.updateExp(entry.getOutput().getExp(), false);
                            user_gift.p.updateExp(entry.getOutput().getExp(), false);

                            final short[] arId = entry.getOutput().getIdItems();
                            final short idI = arId[util.nextInt(arId.length)];
                            if (idI != -1) {
                                p.nj.randomItem(false, idI);
                                user_gift.randomItem(false, idI);
                            }
                        }

                        user_gift.p.session.sendMessageLog("Bạn vừa được " + p.nj.name + " tặng " + quantity + " "
                                + entry.getOutput().getItemData().name);

                        p.nj.updateEventData(itemId, quantity);
                        p.nj.removeItemBags(itemId, quantity);
                        break;
                    }

                }

                break;
            }
        }
    }

    static {
        server = Server.getInstance();
    }
}
