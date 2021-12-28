package server;

import real.Ninja;
import real.ClanManager;
import real.PlayerManager;
import java.io.IOException;
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
            case 24_1: {

                try {
                    long yen = Integer.parseInt(str);
                    if (p.nj.yen < yen) {
                        p.session.sendMessageLog("Số yên trong hành trang của bạn phải lớn hơn " + yen + " yên.");
                        break;
                    } else if (p.nj.xu + yen > 2000000000) {
                        p.session.sendMessageLog("Số xu trong hành trang của bạn đã đạt mức tối đa.");
                        break;
                    } else {
                        p.nj.upyenMessage(-yen);
                        p.nj.upxuMessage(yen);
                        break;
                    }
                } catch (NumberFormatException ex) {
                    p.session.sendMessageLog("Sai định dạng.");
                    break;
                }
            }

            case 24_4: {
                p.cardCode = str;
                p.cardDCoin();
                break;
            }
            case 24_6_1_0:
                p.nameUS = str;
                Ninja user_gift_coin = PlayerManager.getInstance().getNinja(str);
                if (user_gift_coin != null) {
                    server.menu.sendWrite(p, (short) -1004_2, "Số lượng");
                } else {
                    p.session.sendMessageLog("Người chơi không có trên mạng. Không thể nhận kim cương");
                }
                break;
            case 24_6_1_1:
                p.diamond_send = str;
                p.sendDiamond();
                break;
            case 24_8:
                p.nameUS = str;
                Ninja user_gift_gold = PlayerManager.getInstance().getNinja(str);
                if (user_gift_gold == null) {
                    p.session.sendMessageLog("Người chơi không có trên mạng. Không thể nhận kim cương");
                    break;
                }
                if (user_gift_gold != null) {
                    if (p.luong < 10) {
                        p.session.sendMessageLog("Không đủ lượng");
                        break;
                    } else {
                        p.sendGold();
                    }
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
                Draw.server.manager.rotationluck[b].joinLuck(p, xujoin);
                break;
            }
            case 101: {
                if (b < 0 || b >= Draw.server.manager.rotationluck.length) {
                    return;
                }
                Draw.server.manager.rotationluck[b].luckMessage(p);
                break;
            }
            case 102: {
                p.typemenu = 92;
                MenuController.doMenuArray(p, new String[] { "Vòng xoay vip", "Vòng xoay thường" });
                break;
            }
        }
    }

    static {
        server = Server.getInstance();
    }
}
