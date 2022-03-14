package server;

import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import patch.Constants;
import patch.ItemShinwaManager;
import patch.ItemShinwaManager.ItemShinwa;
import patch.MessageSubCommand;
import interfaces.ISoloer;
import tournament.Tournament;
import real.*;
import threading.Map;
import threading.Message;
import io.Session;
import io.ISessionHandler;
import threading.Server;

import java.io.IOException;

import static patch.ItemShinwaManager.returnXuToSeller;

@SuppressWarnings("ALL")
public class Controller implements ISessionHandler {
    Server server;

    public Controller() {
        this.server = Server.getInstance();
    }

    @Override
    public void onConnectOK(final Session conn) {
    }

    @Override
    public void onConnectionFail(final Session conn) {
    }

    @Override
    public void onDisconnected(final Session conn) {
        PlayerManager.getInstance().kickSession(conn);
    }

    @SneakyThrows
    @Override
    public void onMessage(final Session conn, final Message message) {
        final ServerController ctl = this.server.controllerManager;
        try {
            final User p = conn.user;

            switch (message.getCommand()) {
                case -30: {
                    if (p != null) {
                        p.messageSubCommand(message);
                        break;
                    }
                    break;
                }
                case -29: {
                    ctl.processGameMessage(conn, message);
                    break;
                }
                case -28: {
                    if (p != null) {
                        p.messageNotMap(message);
                        break;
                    }
                    break;
                }
                case -27: {
                    conn.hansakeMessage();
                    break;
                }
                case -23: {
                    if (p != null) {
                        p.nj.getPlace().Chat(p, message.reader().readUTF());
                        message.cleanup();
                        break;
                    }
                    break;
                }
                case -22: {
                    if (p != null) {
                        p.privateChat(message);
                        break;
                    }
                    break;
                }
                case -21: {
                    if (p != null) {
                        this.server.manager.chatKTG(p, message);
                        break;
                    }
                    break;
                }
                case -20: {
                    if (p != null && p.nj != null) {
                        p.chatParty(message);
                        break;
                    }
                    break;
                }
                case -19: {
                    if (p != null && !p.nj.isDie) {
                        final ClanManager clan = ClanManager.getClanByName(p.nj.clan.clanName);
                        if (clan != null) {
                            clan.chat(p, message);
                        }
                        break;
                    }
                    break;
                }
                case -17: {
                    if (p != null && !p.nj.isDie) {
                        p.nj.getPlace().changeMap(p);
                        message.cleanup();
                        break;
                    }
                    break;
                }
                case -14: {
                    if (p != null && p.nj != null && !p.nj.isDie) {
                        p.nj.getPlace().pickItem(p, message);
                        break;
                    }
                    break;
                }
                case -12: {
                    if (p != null && !p.nj.isDie) {
                        final byte index = message.reader().readByte();
                        p.nj.getPlace().leaveItemBackground(p, index);
                        break;
                    }
                    break;
                }
                case -10: {
                    if (p != null && p.nj.isDie && !p.nj.isNhanban) {
                        p.nj.getPlace().wakeUpDieReturn(p);
                        break;
                    }
                    break;
                }
                case -9: {
                    if (p != null && p.nj.isDie && !p.nj.isNhanban) {
                        p.nj.getPlace().DieReturn(p);
                        break;
                    }
                    break;
                }
                case 1: {
                    if (p != null && p.nj != null && !p.nj.isDie) {
                        p.nj.getPlace().moveMessage(p.nj, message.reader().readShort(), message.reader().readShort());
                        break;
                    }
                    break;
                }
                case 4:
                case 73: {
                    attackPlayerVsMob(message, p);
                    break;
                }
                case 11: {
                    if (p != null && !p.nj.isDie) {
                        p.useItem(message);
                        break;
                    }
                    break;
                }
                case 12: {
                    if (p != null && p.nj != null && !p.nj.isDie) {
                        useItem.useItemChangeMap(p, message);
                        break;
                    }
                    break;
                }
                case 13: {
                    if (p != null && !p.nj.isDie) {
                        GameScr.buyItemStore(p, message);
                        break;
                    }
                    break;
                }
                case 14: {
                    if (p != null) {
                        p.SellItemBag(message);
                        break;
                    }
                    break;
                }
                case 15: {
                    if (p != null) {
                        p.itemBodyToBag(message);
                        break;
                    }
                    break;
                }
                case 16: {
                    if (p != null) {
                        p.itemBoxToBag(message);
                        break;
                    }
                    break;
                }
                case 17: {
                    if (p != null) {
                        p.itemBagToBox(message);
                        break;
                    }
                    break;
                }
                case 19: {
                    if (p != null) {
                        GameScr.crystalCollect(p, message, true);
                        break;
                    }
                    break;
                }
                case 20: {
                    if (p != null) {
                        GameScr.crystalCollect(p, message, false);
                        break;
                    }
                    break;
                }
                case 21: {
                    if (p != null) {
                        GameScr.UpGrade(p, message);
                        break;
                    }
                    break;
                }
                case 22: {
                    if (p != null) {
                        GameScr.Split(p, message);
                        break;
                    }
                    break;
                }
                case 28: {
                    if (p != null && !p.nj.isDie) {
                        p.nj.getPlace().selectUIZone(p, message);
                        break;
                    }
                    break;
                }
                case 29: {
                    if (p != null) {
                        this.server.menu.sendMenu(p, message);
                        break;
                    }
                    break;
                }
                case 36: {
                    if (p != null && !p.nj.isDie) {
                        p.nj.getPlace().openUIZone(p);
                        break;
                    }
                    break;
                }
                case 40: {
                    if (p != null && !p.nj.isDie) {
                        this.server.menu.openUINpc(p, message);
                        break;
                    }
                    break;
                }
                case 41:
                case 74: {
                    if (p != null && p.nj != null && !p.nj.isDie && message.reader().available() > 0) {

                        useSkill.useSkill(p.nj.get(), message.reader().readShort());
                        break;
                    }
                    break;
                }
                case 42: {
                    if (p != null) {
                        p.requestItemInfo(message);
                        break;
                    }
                    break;
                }
                case 43: {
                    if (p != null && !p.nj.isDie) {
                        p.requestTrade(message);
                        break;
                    }
                    break;
                }
                case 44: {
                    if (p != null && !p.nj.isDie) {
                        p.startTrade(message);
                        break;
                    }
                    break;
                }
                case 45: {
                    if (p != null) {
                        p.lockTrade(message);
                        break;
                    }
                    break;
                }
                case 46: {
                    if (p != null) {
                        p.agreeTrade();
                        break;
                    }
                    break;
                }
                case 47: {
                    if (p != null) {
                        server.menu.selectMenuNpc(p, message);
                        break;
                    }
                    break;
                }
                case 56: {
                    if (p != null) {
                        p.closeTrade();
                        break;
                    }
                    break;
                }
                case 57: {
                    if (p != null) {
                        p.closeLoad();
                        break;
                    }
                    break;
                }
                case 59: {
                    if (p != null) {
                        p.addFriend(message);
                        break;
                    }
                    break;
                }
                case 60: {
                    if (p != null && !p.nj.isDie) {
                        val cloneMessage = message.cloneMessage();
                        p.nj.getPlace().FightMob(p.nj.get(), message);
                        if (p.nj.get().isHuman && p.nj.clone != null && p.nj.clone.isIslive()) {
                            p.nj.getPlace().FightMob(p.nj.clone, (Message) cloneMessage);
                        }
                        break;
                    }
                    break;
                }

                case 61: {
                    if (p != null && !p.nj.isDie) {
                        if (p.nj.getPlace().canAttackNinja(p.nj.get(), message.cloneMessage())) {
                            val cloneMessage = message.cloneMessage();
                            p.nj.getPlace().attackNinja(p.nj.get(), message);
                            if (p.nj.get().isHuman && p.nj.clone != null && p.nj.clone.isIslive()) {
                                p.nj.getPlace().attackNinja(p.nj.clone, (Message) cloneMessage);
                            }
                            break;
                        }
                    }
                    break;
                }
                case 65: {
                    if (p != null && !p.nj.isDie) {
                        ISoloer soloer = p.nj.getPlace().getNinja(message.reader().readInt());
                        p.nj.requestSolo(soloer);
                        message.cleanup();
                    }
                    break;
                }
                case 66: {
                    if (p != null && !p.nj.isDie) {
                        p.nj.acceptSolo();
                    }
                    break;
                }
                case 67: {
                    if (p != null && !p.nj.isDie) {
                        p.nj.endSolo();
                    }
                    break;
                }

                case 79: {
                    if (p != null) {
                        p.addParty(message);
                        break;
                    }
                    break;
                }
                case 80: {
                    if (p != null) {
                        p.addPartyAccept(message);
                        break;
                    }
                    break;
                }
                case 83: {
                    if (p != null && p.nj != null && p.nj.get().party != null) {
                        p.nj.get().party.exitParty(p.nj);
                        break;
                    }
                    break;
                }
                case 92:
                    handleInputMessage(message, p);
                    break;
                case 93: {
                    if (p != null) {
                        final String playername = message.reader().readUTF();
                        p.viewPlayerMessage(playername);
                        break;
                    }
                    break;
                }
                case 94: {
                    if (p != null) {
                        p.viewOptionPlayers(message);
                        break;
                    }
                    break;
                }
                case 99: {
                    val parnerId = message.reader().readInt();
                    this.goToWaitingBetRoom(p.nj.getPlace().getNinja(parnerId), p.nj);
                    break;
                }
                case 100: {
                    // Xem thi đấu lôi đài
                    int battleId = message.reader().readByte();
                    this.moveToBattleMap(battleId, p);
                    message.cleanup();
                    break;
                }
                case 104: {
                    p.requestItemShinwaInfo(message);
                    break;
                }
                case 108: {
                    if (p != null) {
                        p.itemMonToBag(message);
                        break;
                    }
                    break;
                }
                case 110: {
                    GameScr.LuyenThach(p, message);
                    break;
                }
                case 111: {
                    if (p != null) {
                        GameScr.TinhLuyen(p, message);
                        break;
                    }
                    break;
                }
                case 112: {
                    if (p != null) {
                        GameScr.DichChuyen(p, message);
                        break;
                    }
                    break;
                }
                case 125: {
                    if (p == null) {
                        break;
                    }
                    final byte b = message.reader().readByte();
                    if (b == 1) {
                        GameCanvas.getImgEffect(p.session, message.reader().readShort());
                        break;
                    }
                    if (b == 2) {
                        GameCanvas.getDataEffect(p.session, message.reader().readShort());
                        break;
                    }
                    break;
                }

                case 25: {
                    // Send player info
                    if (p != null) {
                        p.nj.getPlace().sendPlayersInfo(p.nj, message);
                    }
                    break;
                }
                case 102: {
                    // Shinwa
                    val indexUI = message.reader().readByte();
                    val price = message.reader().readInt();
                    val item = p.nj.ItemBag[indexUI];

                    // int FEE = 50_000;
                    int FEE_GOLD = 25;
                    if (item != null && p.luong >= FEE_GOLD) {
                        if (item.isExpires)
                            return;
                        ItemShinwa itemShinwa = new ItemShinwa(item, p.nj.name, price);
                        ItemShinwaManager.add(itemShinwa);
                        p.nj.removeItemBag(indexUI);
                        // p.nj.upxuMessage(-FEE);
                        p.upluongMessage(-FEE_GOLD);
                        Service.CharViewInfo(p, false);
                        p.endLoad(true);
                        Service.CharViewInfo(p, false);
                        p.endLoad(true);
                        break;
                    } else {
                        p.sendYellowMessage(
                                "Không đủ " + String.format("%,d", FEE_GOLD) + " lượng.");
                        p.endLoad(true);
                    }
                    break;
                }
                case 105: {
                    // Buy item shinwa
                    val itemId = message.reader().readInt();
                    val itemShinwa = ItemShinwaManager.findItemById(itemId);
                    boolean canBuy = !ItemShinwaManager.items.get(-1).contains(itemShinwa);

                    if (!itemShinwa.isExpired() && itemShinwa.getPrice() <= p.nj.getXu() && canBuy) {
                        // final byte bagNull = p.nj.getAvailableBag();
                        if (p.nj.getAvailableBag() == 0) {
                            p.session.sendMessageLog("Hành trang không đủ chổ trống");
                            return;
                        }
                        p.nj.upxuMessage(-itemShinwa.getPrice());
                        p.nj.addItemBag(false, itemShinwa.getItem());
                        returnXuToSeller(itemShinwa);

                        p.endLoad(true);
                        break;
                    }
                    if (p.nj.xu < itemShinwa.getPrice()) {
                        // Khong du xu
                        p.sendYellowMessage("Không đủ xu để mua item");
                        p.endLoad(true);
                        return;
                    } else {
                        p.sendYellowMessage("Lỗi không xác định !");
                        p.endLoad(true);
                    }
                    break;
                }
                case 106: {
                    p.acceptInviteGT(message.reader().readInt());
                    break;
                }
                case 124: {
                    // Luyen ngoc
                    GameScr.ngocFeature(p, message);
                    break;
                }

                case 121: {
                    val index = message.reader().readByte();
                    val ninjaName = message.reader().readUTF();

                    if (index == 0) {
                        GameScr.requestRankedInfo(p, ninjaName);
                    } else {
                        // Thach dau
                        if (p.nj.getTournamentData().isCanGoNext() || util.debug) {
                            if (p.nj.name.equals(ninjaName)) {
                                if (p.nj.getTournamentData().getRanked() == 1) {
                                    p.sendYellowMessage("Bạn đã chinh phục thành công top 1 rồi.");
                                } else {
                                    p.sendYellowMessage("Bạn không thể chiến đấu với mình");
                                }
                                break;
                            }

                            final Tournament tournament = Tournament.getTypeTournament(p.nj.getLevel());
                            if (!tournament.checkBusy(ninjaName)) {
                                tournament.enter(p.nj, ninjaName);
                            } else {
                                p.sendYellowMessage(
                                        "Đối thủ đang thi đấu với một người khác vui lòng đợi trong giây lát");
                            }

                        } else {
                            p.nj.getPlace().chatNPC(p, 4,
                                    "Thất bại là mẹ thành công ta biết con hơi buồn nhưng một sự thật đáng buồn là con hãy quay lại vào ngày hôm sau");
                        }
                    }

                    break;
                }
                case 113: {
                    //
                    if (p != null && p.nj != null && p.nj.candyBattle != null) {
                        val id = message.reader().readShort();
                        System.out.println(id);
                        p.nj.candyBattle.catKeo(p.nj, id);
                    }
                    break;
                }

                default: {
                    util.Debug("NOT MATCH " + message.getCommand());
                }
            }
            message.cleanup();
        } catch (Exception ex) {
            System.out.println("ERROR Process message");
            ex.printStackTrace();
        }
    }

    private void attackPlayerVsMob(Message message, User p) throws IOException {
        if (p != null && !p.nj.isDie) {
            Ninja _ninja = p.nj;

            if (p != null && _ninja != null && _ninja.getCSkill() != -1) {
                if (!_ninja.canUseVukhi()) {
                    p.sendYellowMessage("Vũ khí không hợp lệ");
                    return;
                }

                val template = _ninja.getCSkillTemplate();

                if (_ninja.mp < _ninja.getCSkillTemplate().manaUse) {
                    p.nj.getPlace().updateMp(p);
                    MessageSubCommand.sendMP(p.nj);
                    return;
                }
                if (_ninja.getSkills().size() > 0) {
                    byte size = message.reader().readByte();

                    if (size >= 0 && size <= template.maxFight) {
                        Mob[] arrMob = new Mob[size];
                        Ninja[] arrNinja = new Ninja[_ninja.getCSkillTemplate().maxFight];
                        try {
                            byte i;
                            for (i = 0; i < arrMob.length &&
                                    i < template.maxFight; i = (byte) (i + 1)) {

                                arrMob[i] = _ninja.getPlace().getMob(message.reader().readUnsignedByte());
                                if (arrMob[i] == null)
                                    continue;
                            }
                            for (i = 0; i < arrNinja.length &&
                                    i < template.maxFight; i = (byte) (i + 1)) {
                                arrNinja[i] = _ninja.getPlace().getNinja(message.reader().readInt());
                            }
                        } catch (Exception exception) {
                        }
                        _ninja.getPlace().PlayerAttack(_ninja, arrMob, arrNinja);
                    }
                }
            }
            return;
        }
    }

    private void moveToBattleMap(int battleId, User p) {
        if (Battle.battles.containsKey(battleId)) {

            val battle = Battle.battles.get(battleId);
            p.nj.isBattleViewer = true;
            battle.addViewerIfNotInMatch(p.nj);
            p.nj.enterSamePlace(battle.getPlace(), null);

        }
    }

    private void handleInputMessage(Message message, User p) throws CloneNotSupportedException, IOException {
        // Input
        var cloneMessage = message.cloneMessage();
        var menuId = message.reader().readShort();
        val str = message.reader().readUTF();

        if (menuId == 1) {
            // Teleport
            teleport(p, str);
        } else if (menuId == 2) {
            var inputNinja = p.nj.getPlace().getNinja(str);
            if (inputNinja != null) {
                if (p.nj.party != null && p.nj.party.getNinja(inputNinja.id) != null) {
                    // Cung team
                    p.nj.getPlace().chatNPC(p, (short) 0, "Đối thủ cùng team với nhà người tính troll ta à");
                    return;
                }
                // Request battle
                p.nj.getPlace().chatNPC(p, (short) 0, "Ta đã gửi lời khiêu chiến của con đến đối thủ rồi");
                sendRequestBattleToAnother(inputNinja, p.nj, -157);
            } else {
                p.nj.getPlace().chatNPC(p, (short) 0, "Ta không tìm thấy đối thủ của con trong khu này");
            }
        } else if (menuId == 3) {
            // Đặt cược lôi đài
            if (p.nj.party != null) {
                if (p.nj.party.master != p.nj.id) {
                    p.sendYellowMessage("Chỉ có trưởng nhóm mới có thể đặt cược được");
                }
            }
            if (p.nj.hasBattle()) {
                p.nj.getBattle().setXu(Long.parseLong(str), p.nj.party != null ? p.nj.party : p.nj);
            } else {
                p.nj.getClanBattle().setXu(p.nj, Integer.parseInt(str.replace(",", "").replace(".", "").trim()));
            }

        } else if (menuId == 4) {
            final ClanManager clan = ClanManager.getClanByName(str);
            if (clan != null) {
                val tocTruong = clan.members.stream().filter(m -> {
                    val ninja = m.getNinja();
                    if (ninja == null)
                        return false;
                    if (ninja.clan.typeclan == Constants.TOC_TRUONG)
                        return true;
                    return false;
                }).map(c -> c.getNinja()).findFirst().orElse(null);
                if (tocTruong != null) {
                    final Ninja n = p.nj.getPlace().getNinja(tocTruong.name);
                    if (n != null) {
                        p.nj.getPlace().chatNPC(p, (short) 32,
                                "Ta đã gửi lời mời thách đấu của ngươi đến tộc trưởng gia tộc " + str);
                        sendRequestBattleToAnother(tocTruong, p.nj, -150);
                    } else {
                        p.nj.getPlace().chatNPC(p, (short) 32, "Tộc trưởng không cùng khu với bạn");
                    }
                } else {
                    p.sendYellowMessage("Tộc trưởng gia tộc bạn thách đấu không online");
                }
            } else {
                p.sendYellowMessage("Không tìm thấy gia tộc trên hệ thống");
            }
        } else if (menuId == 5) {
            try {
                val luong = Integer.parseInt(str);
                if (p.luong >= luong) {
                    int convertedCoin = luong * 12_000;

                    if (convertedCoin + p.nj.xu <= 2_000_000_000) {
                        p.upluongMessage(-luong);
                        p.nj.upxuMessage(convertedCoin);
                    } else {
                        p.sendYellowMessage("Sau khi đổi sẽ bị tràn xu nên không thể đổi được");
                    }

                } else {
                    p.sendYellowMessage("Không đủ lượng để đổi");
                }

            } catch (Exception e) {

            }
        } else if (p != null) {
            Draw.Draw(p, (Message) cloneMessage);
            return;
        }
        return;
    }

    private void teleport(User p, String str) throws IOException {
        Ninja ninja = PlayerManager.getInstance().getNinja(str);
        if (ninja == null) {
            p.sendYellowMessage("Người chơi đã offline");
            return;
        }
        final Map map = Server.getMapById(ninja.getMapid());
        if (map != null && (map.isLangCo() || Map.isCaveMap(map.id) || map.VDMQ() || map.loiDaiMap())) {
            p.sendYellowMessage("Con không thể di chuyển đến khu vực cấm này nếu đi vào sẽ phải có bình oxy mới được");
            return;
        }

        p.nj.getPlace().leave(p);
        ninja.getPlace().Enter(p);
        p.nj.x = ninja.x;
        p.nj.y = ninja.y;
        ninja.getPlace().sendXYPlayer(p);
    }

    private void sendRequestBattleToAnother(Ninja friendNinja, Ninja p, int id) throws IOException {

        val m = new Message(id);
        m.writer().writeInt(p.id);
        friendNinja.p.sendMessage(m);
        m.cleanup();
    }

    private void goToWaitingBetRoom(Ninja partner, Ninja me) throws IOException {
        Battle battle = new Battle(partner, me);
        battle.tick();
        battle.enter();
    }
}
