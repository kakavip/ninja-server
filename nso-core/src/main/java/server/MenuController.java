package server;

import boardGame.Place;
import lombok.SneakyThrows;
import lombok.val;
import patch.*;
import clan.ClanTerritory;
import clan.ClanTerritoryData;
import interfaces.IBattle;
import tournament.GeninTournament;
import tournament.KageTournament;
import tournament.Tournament;
import tournament.TournamentData;
import real.*;
import tasks.TaskHandle;
import tasks.TaskList;
import tasks.Text;
import threading.Manager;
import threading.Map;
import threading.Message;
import threading.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static patch.Constants.TOC_TRUONG;
import static patch.ItemShinwaManager.*;
import static patch.TaskOrder.*;
import static tournament.Tournament.*;
import static real.User.TypeTBLOption.*;

public class MenuController {

    public static final String MSG_HANH_TRANG = "Hành trang không đủ chỗ trống";
    public static final int MIN_EVENT_MENU_ID = 1000;

    Server server;

    public MenuController() {
        this.server = Server.getInstance();
    }

    public void sendMenu(final User p, final Message m) throws IOException {
        final byte npcId = m.reader().readByte();
        byte menuId = m.reader().readByte();
        final byte optionId = m.reader().readByte();

        if (p.typemenu == -1) {
            p.typemenu = (short) npcId;
        }

        val ninja = p.nj;

        if (TaskHandle.isTaskNPC(ninja, npcId) && Map.isNPCNear(ninja, npcId)) {
            // TODO SELECT MENU TASK
            menuId = (byte) (menuId - 1);
            if (ninja.getTaskIndex() == -1) {

                if (menuId == -1) {
                    TaskHandle.Task(ninja, (short) npcId);
                    return;
                }
            } else if (TaskHandle.isFinishTask(ninja)) {
                if (menuId == -1) {
                    TaskHandle.finishTask(ninja, (short) npcId);
                    return;
                }
            } else if (ninja.getTaskId() == 1) {
                if (menuId == -1) {
                    TaskHandle.doTask(ninja, (short) npcId, menuId, optionId);
                    return;
                }
            } else if (ninja.getTaskId() == 7) {
                if (menuId == -1) {
                    TaskHandle.doTask(ninja, (short) npcId, menuId, optionId);
                    return;
                }
            } else if (ninja.getTaskId() == 8 || ninja.getTaskId() == 0) {
                boolean npcTalking = TaskHandle.npcTalk(ninja, menuId, npcId);
                if (npcTalking) {
                    return;
                }

            } else if (ninja.getTaskId() == 13) {
                if (menuId == -1) {
                    if (ninja.getTaskIndex() == 1) {
                        // OOka
                        final Map map = Server.getMapById(56);
                        val place = map.getFreeArea();
                        val npc = Ninja.getNinja("Thầy Ookamesama");
                        npc.p = new User();
                        npc.p.nj = npc;
                        npc.isNpc = true;
                        npc.setTypepk(Constants.PK_DOSAT);
                        p.nj.enterSamePlace(place, npc);
                        return;
                    } else if (ninja.getTaskIndex() == 2) {
                        // Haru
                        final Map map = Server.getMapById(0);
                        val place = map.getFreeArea();
                        val npc = Ninja.getNinja("Thầy Kazeto");
                        if (npc == null) {
                            System.out.println("KO THẦY ĐỐ MÀY LÀM NÊN");
                            return;
                        }
                        npc.p = new User();
                        npc.isNpc = true;
                        npc.p.nj = npc;
                        npc.setTypepk(Constants.PK_DOSAT);
                        p.nj.enterSamePlace(place, npc);
                        return;
                    } else if (ninja.getTaskIndex() == 3) {
                        final Map map = Server.getMapById(73);

                        val npc = Ninja.getNinja("Cô Toyotomi");
                        if (npc == null) {
                            System.out.println("KO THẦY ĐỐ MÀY LÀM NÊN");
                            return;
                        }
                        npc.isNpc = true;
                        npc.p = new User();
                        npc.setTypepk(Constants.PK_DOSAT);
                        npc.p.nj = npc;
                        val place = map.getFreeArea();
                        p.nj.enterSamePlace(place, npc);
                        return;
                    }
                } else if (ninja.getTaskId() == 15 && ninja.getTaskIndex() >= 1) {
                    if (menuId == -1) {
                        // Nhiem vu giao thu
                        if (ninja.getTaskIndex() == 1 && npcId == 14) {
                            p.nj.removeItemBags(214, 1);
                        } else if (ninja.getTaskIndex() == 2 && npcId == 15) {
                            p.nj.removeItemBags(214, 1);
                        } else if (ninja.getTaskIndex() == 3 && npcId == 16) {
                            p.nj.removeItemBags(214, 1);
                        }
                    }

                }
            }
        }

        m.cleanup();
        Label_6355: {
            label: switch (p.typemenu) {
                case 0: {
                    if (menuId == 0) {
                        // Mua vu khi
                        p.openUI(2);
                        break;
                    }
                    switch (menuId) {
                        case 1:
                            if (optionId == 0) {
                                // Thanh lap gia toc
                                if (!p.nj.clan.clanName.isEmpty()) {
                                    p.nj.getPlace().chatNPC(p, (short) npcId,
                                            "Con đã có gia tộc rồi, không thể thành lập thêm gia tộc nữa.");
                                    break label;
                                }
                                if (p.luong < ClanManager.LUONG_CREATE_CLAN) {
                                    p.nj.getPlace().chatNPC(p, (short) npcId,
                                            "Hành trang phải có ít nhất 1.500 lượng thì mới có thể lập gia tộc.");
                                    break label;
                                }
                                this.sendWrite(p, (short) 50, "Tên gia tộc");
                            } else if (optionId == 1) {
                                // Lanh địa gia tộc
                                if (p.nj.getLevel() < 40) {
                                    p.nj.getPlace().chatNPC(p, (short) npcId,
                                            "Trình độ từ cấp 40 trở lên mới được tham gia.");
                                    return;
                                }
                                if (p.getClanTerritoryData() == null) {
                                    if (p.nj.clan.typeclan == TOC_TRUONG) {

                                        if (p.nj.getAvailableBag() == 0) {
                                            p.sendYellowMessage("Hành trang không đủ chỗ trống để nhận chìa khoá");
                                            return;
                                        }
                                        val clan = ClanManager.getClanByName(p.nj.clan.clanName);
                                        if (clan.openDun <= 0) {
                                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                                    "Số lần đi lãnh địa gia tộc đã hết vui lòng dùng thẻ bài hoặc đợi vào tuần sau");
                                            return;
                                        }

                                        val clanTerritory = new ClanTerritory(clan);
                                        Server.clanTerritoryManager.addClanTerritory(clanTerritory);
                                        p.setClanTerritoryData(new ClanTerritoryData(clanTerritory, p.nj));
                                        Server.clanTerritoryManager.addClanTerritoryData(p.getClanTerritoryData());

                                        clanTerritory.clanManager.openDun--;
                                        if (clanTerritory == null) {
                                            p.sendYellowMessage("Có lỗi xảy ra");
                                            return;
                                        }
                                        val area = clanTerritory.getEntrance();
                                        if (area != null) {
                                            val item = ItemData.itemDefault(260);
                                            p.nj.addItemBag(false, item);
                                            if (p.getClanTerritoryData().getClanTerritory() != null) {

                                                if (p.getClanTerritoryData().getClanTerritory() != null) {
                                                    p.getClanTerritoryData().getClanTerritory().enterEntrance(p.nj);
                                                }

                                                clanTerritory.clanManager
                                                        .informAll("Tộc trưởng đã mở lãnh địa gia tộc");
                                            } else {
                                                p.sendYellowMessage("Có lỗi xảy ra");
                                            }
                                        } else {
                                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                                    "Hiện tại lãnh địa gia tộc không còn khu trống");
                                        }

                                    } else {
                                        p.sendYellowMessage(
                                                "Chỉ những người ưu tú được tộc trưởng mời mới có thể vào lãnh địa gia tộc");
                                    }
                                } else {
                                    val data = p.getClanTerritoryData();
                                    if (data != null) {
                                        val teri = data.getClanTerritory();
                                        if (teri != null) {
                                            teri.enterEntrance(p.nj);
                                        }
                                    }
                                }

                            } else if (optionId == 2) {

                                if (p.nj.quantityItemyTotal(262) < 500) {
                                    p.nj.getPlace().chatNPC(p, (short) npcId,
                                            "Con hãy đem 500 đồng tiền gia tộc đến đây để đổi túi quà.");
                                    return;
                                } else {
                                    Item itemup = ItemData.itemDefault(263);
                                    p.nj.addItemBag(true, itemup);
                                    p.nj.removeItemBags(262, 500);
                                }

                            } else if (optionId == 3) {
                                server.manager.sendTB(p, "Hướng Dẫn", "Lập gia tộc bạn cần có 1.500 lượng."
                                        + "\n\nVào thứ 2 hàng tuần gia tộc phải trả một mức phí tùy theo cấp độ của gia tộc để duy trì gia tộc."
                                        + "\n\nMức nợ ngân quỹ tối đa của mỗi gia tộc = 3 lần số phí duy trì gia tộc. Nếu số nợ ngân quỹ lớn hơn 3 lần số phí duy trì thì gia tộc số bị giải tán."
                                        + "\n\nPhí trục xuất thành viên:\n- Thành viên thường: 10.000 xu.\n- Ưu tú: 20.000 xu.\n- Trưởng lão: 50.000 xu.\n- Tộc phó: 100.000 xu."
                                        + "\n\nPhí tự ý rời gia tộc:\n- Thành viên thường: 10.000 xu.\n- Ưu tú: 20.000 xu.\n- Trưởng lão: 50.000 xu.\n- Tộc phó: 100.000 xu.");
                                break;
                            }
                            break label;
                        case 2:
                            if (menuId != 2) {
                                break label;
                            }
                            if (p.nj.isNhanban) {
                                p.session.sendMessageLog("Chức năng này không dành cho phân thân");
                                return;
                            }
                            if (optionId == 0) {
                                Service.evaluateCave(p.nj);
                                break label;
                            }
                            Cave cave = null;
                            if (p.nj.caveID != -1) {
                                if (Cave.caves.containsKey(p.nj.caveID)) {
                                    cave = Cave.caves.get(p.nj.caveID);
                                    p.nj.getPlace().leave(p);
                                    cave.map[0].area[0].EnterMap0(p.nj);
                                }
                            } else if (p.nj.party != null && p.nj.party.cave == null && p.nj.party.master != p.nj.id) {
                                p.session.sendMessageLog("Chỉ có nhóm trưởng mới được phép mở cửa hang động");
                                return;
                            }
                            if (cave == null) {
                                if (p.nj.nCave <= 0) {
                                    p.nj.getPlace().chatNPC(p, (short) npcId,
                                            "Số lần vào hang động cảu con hôm nay đã hết hãy quay lại vào ngày mai.");
                                    return;
                                }
                                if (optionId == 1) {
                                    if (p.nj.getLevel() < 30 || p.nj.getLevel() > 39) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Trình độ của con không thích hợp để vào cửa này.");
                                        return;
                                    }
                                    if (p.nj.party != null) {
                                        synchronized (p.nj.party.ninjas) {
                                            for (byte i = 0; i < p.nj.party.ninjas.size(); ++i) {
                                                if (p.nj.party.ninjas.get(i).getLevel() < 30
                                                        || p.nj.party.ninjas.get(i).getLevel() > 39) {
                                                    p.session.sendMessageLog(
                                                            "Thành viên trong nhóm trình độ không phù hợp");
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    if (p.nj.party != null) {
                                        if (p.nj.party.cave == null) {
                                            cave = new Cave(3);
                                            p.nj.party.openCave(cave, p.nj.name);
                                        } else {
                                            cave = p.nj.party.cave;
                                        }
                                    } else {
                                        cave = new Cave(3);
                                    }
                                    p.nj.caveID = cave.caveID;
                                }
                                if (optionId == 2) {
                                    if (p.nj.getLevel() < 40 || p.nj.getLevel() > 49) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Trình độ của con không thích hợp để vào cửa này.");
                                        return;
                                    }
                                    if (p.nj.party != null) {
                                        synchronized (p.nj.party) {
                                            for (byte i = 0; i < p.nj.party.ninjas.size(); ++i) {
                                                if (p.nj.party.ninjas.get(i).getLevel() < 40
                                                        || p.nj.party.ninjas.get(i).getLevel() > 49) {
                                                    p.session.sendMessageLog(
                                                            "Thành viên trong nhóm trình độ không phù hợp");
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    if (p.nj.party != null) {
                                        if (p.nj.party.cave == null) {
                                            cave = new Cave(4);
                                            p.nj.party.openCave(cave, p.nj.name);
                                        } else {
                                            cave = p.nj.party.cave;
                                        }
                                    } else {
                                        cave = new Cave(4);
                                    }
                                    p.nj.caveID = cave.caveID;
                                }
                                if (optionId == 3) {
                                    if (p.nj.getLevel() < 50 || p.nj.getLevel() > 59) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Trình độ của con không thích hợp để vào cửa này.");
                                        return;
                                    }
                                    if (p.nj.party != null) {
                                        synchronized (p.nj.party.ninjas) {
                                            for (byte i = 0; i < p.nj.party.ninjas.size(); ++i) {
                                                if (p.nj.party.ninjas.get(i).getLevel() < 50
                                                        || p.nj.party.ninjas.get(i).getLevel() > 59) {
                                                    p.session.sendMessageLog(
                                                            "Thành viên trong nhóm trình độ không phù hợp");
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    if (p.nj.party != null) {
                                        if (p.nj.party.cave == null) {
                                            cave = new Cave(5);
                                            p.nj.party.openCave(cave, p.nj.name);
                                        } else {
                                            cave = p.nj.party.cave;
                                        }
                                    } else {
                                        cave = new Cave(5);
                                    }
                                    p.nj.caveID = cave.caveID;
                                }
                                if (optionId == 4) {
                                    if (p.nj.getLevel() < 60 || p.nj.getLevel() > 69) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Trình độ của con không thích hợp để vào cửa này.");
                                        return;
                                    }
                                    if (p.nj.party != null) {
                                        synchronized (p.nj.party.ninjas) {
                                            for (byte i = 0; i < p.nj.party.ninjas.size(); ++i) {
                                                if (p.nj.party.ninjas.get(i).getLevel() < 60
                                                        || p.nj.party.ninjas.get(i).getLevel() > 69) {
                                                    p.session.sendMessageLog(
                                                            "Thành viên trong nhóm trình độ không phù hợp");
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    if (p.nj.party != null) {
                                        if (p.nj.party.cave == null) {
                                            cave = new Cave(6);
                                            p.nj.party.openCave(cave, p.nj.name);
                                        } else {
                                            cave = p.nj.party.cave;
                                        }
                                    } else {
                                        cave = new Cave(6);
                                    }
                                    p.nj.caveID = cave.caveID;
                                }
                                if (optionId == 5) {
                                    if (p.nj.getLevel() < 70) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Trình độ của con không thích hợp để vào cửa này.");
                                        return;
                                    }
                                    if (p.nj.party != null) {
                                        synchronized (p.nj.party.ninjas) {
                                            for (byte i = 0; i < p.nj.party.ninjas.size(); ++i) {
                                                if (p.nj.party.ninjas.get(i).getLevel() < 70) {
                                                    p.session.sendMessageLog(
                                                            "Thành viên trong nhóm trình độ không phù hợp");
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    if (p.nj.party != null) {
                                        if (p.nj.party.cave == null) {
                                            cave = new Cave(7);
                                            p.nj.party.openCave(cave, p.nj.name);
                                        } else {
                                            cave = p.nj.party.cave;
                                        }
                                    } else {
                                        cave = new Cave(7);
                                    }
                                    p.nj.caveID = cave.caveID;
                                }
                                if (optionId == 6) {
                                    if (p.nj.getLevel() < 90) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Trình độ của con không thích hợp để vào cửa này.");
                                        return;
                                    }

                                    if (p.nj.party != null && p.nj.party.getKey() != null
                                            && p.nj.party.getKey().get().getLevel() >= 90) {
                                        synchronized (p.nj.party.ninjas) {
                                            for (byte i = 0; i < p.nj.party.ninjas.size(); ++i) {
                                                if (p.nj.party.ninjas.get(i).getLevel() < 90
                                                        || p.nj.party.ninjas.get(i).getLevel() > 151) {
                                                    p.session.sendMessageLog(
                                                            "Thành viên trong nhóm trình độ không phù hợp");
                                                    return;
                                                }
                                            }
                                        }
                                    }

                                    if (p.nj.party != null) {
                                        if (p.nj.party.cave == null) {
                                            cave = new Cave(9);
                                            p.nj.party.openCave(cave, p.nj.name);
                                        } else {
                                            cave = p.nj.party.cave;
                                        }
                                    } else {
                                        cave = new Cave(9);
                                    }
                                    p.nj.caveID = cave.caveID;
                                }
                                if (cave != null) {
                                    final Ninja c = p.nj;
                                    --c.nCave;
                                    p.nj.pointCave = 0;
                                    p.nj.getPlace().leave(p);
                                    cave.map[0].area[0].EnterMap0(p.nj);
                                }
                            }
                            p.setPointPB(p.nj.pointCave);
                            break label;
                        case 3:
                            if (optionId == 0) {
                                // Thach dau loi dai
                                this.sendWrite(p, (short) 2, "Nhập tên nhân vật");
                                break;
                            } else if (optionId == 1) {
                                // Xem thi dau
                                Service.sendBattleList(p);
                            }
                            break label;
                        case 4:
                            Random generator = new Random();
                            int value = generator.nextInt(3);
                            if (value == 0) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Ở chỗ ta có rất nhiều binh khí có giá trị");
                            }
                            if (value == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hãy chọn cho mình một món bình khí đi.");
                            }
                            if (value == 2) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Haha, nhà ngươi cần vũ khí gì?");
                            }
                            break label;
                    }
                }
                case 1: {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            p.openUI(21 - p.nj.gender);
                            break;
                        }
                        if (optionId == 1) {
                            p.openUI(23 - p.nj.gender);
                            break;
                        }
                        if (optionId == 2) {
                            p.openUI(25 - p.nj.gender);
                            break;
                        }
                        if (optionId == 3) {
                            p.openUI(27 - p.nj.gender);
                            break;
                        }
                        if (optionId == 4) {
                            p.openUI(29 - p.nj.gender);
                            break;
                        }
                    } else if (menuId == 1) {
                        Random generator = new Random();
                        int value = generator.nextInt(3);
                        if (value == 0) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Giáp, giày giá rẻ đây!");
                        }
                        if (value == 1) {
                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                    "Không mặc giáp mua từ ta, ra khỏi trường ngươi sẽ gặp nguy hiểm.");
                        }
                        if (value == 2) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Ngươi cần giày, giáp sắt, quần áo?");
                        }
                        break label;
                    }
                    break;
                }
                case 2: {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            p.openUI(16);
                            break;
                        } else if (optionId == 1) {
                            p.openUI(17);
                            break;
                        } else if (optionId == 2) {
                            p.openUI(18);
                            break;
                        } else if (optionId == 3) {
                            p.openUI(19);
                            break;
                        }
                    } else if (menuId == 1) {
                        if (optionId == 4) {
                            // Nang mat thuong
                            final val item = p.nj.get().ItemBody[14];
                            if (item != null && item.getUpgrade() != 0) {
                                nangMat(p, item, false);
                            } else {
                                p.sendYellowMessage("Hãy sử dụng Nguyệt Nhãn để sử dụng được chức năng này.");
                            }

                        } else if (optionId == 5) {
                            // Nang mắt vip
                            final val item = p.nj.get().ItemBody[14];
                            if (item != null && item.getUpgrade() != 0) {
                                nangMat(p, item, true);
                            } else {
                                p.sendYellowMessage("Hãy sử dụng Nguyệt Nhãn để sử dụng được chức năng này.");
                            }
                        } else if (optionId == 6) {
                            final List<int[]> data = MenuController.nangCapMat.keySet().stream()
                                    .map(s -> nangCapMat.get(s)).collect(Collectors.toList());

                            String s = "Sử dụng vật phẩm sự kiện để có thể nhận mắt 1\n";
                            for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
                                int[] datum = data.get(i);
                                s += "-Nâng cấp mắt " + (i + 2) + " dùng " + datum[0] + " viên đá danh vọng cấp "
                                        + (i + 2) + " nâng thường " + datum[1] + " xu xác suất " + datum[2] + "%, VIP "
                                        + datum[1] + " xu " + datum[3] + " lượng xác suất " + datum[4] + "% \n\n";
                            }
                            Service.sendThongBao(p.nj, s);
                        }
                    }
                    break;
                }
                case 3: {
                    if (menuId == 0) {
                        p.openUI(7);
                        break;
                    }
                    if (menuId == 1) {
                        p.openUI(6);
                        break;
                    }
                    if (menuId == 2) {
                        Random generator = new Random();
                        int value = generator.nextInt(3);
                        if (value == 0) {
                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                    "Không mang theo HP, MP bên mình, con sẽ gặp nguy hiểm.");
                        }
                        if (value == 1) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Đi đường cần mang theo ít dược phẩm");
                        }
                        if (value == 2) {
                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                    "Mua ngay HP, MP từ ta, được chế tạo từ loại thảo dược quý hiếm nhất.");
                        }
                        break label;
                    }
                    break;
                }
                case 4: {
                    switch (menuId) {
                        case 0: {
                            p.openUI(9);
                            break;
                        }
                        case 1: {
                            p.openUI(8);
                            break;
                        }
                        case 2: {
                            Random generator = new Random();
                            int value = generator.nextInt(3);
                            if (value == 0) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Thức ăn của ta là ngon nhất rồi!");
                            }
                            if (value == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hahaha, chắc ngươi đi đường cũng mệt rồi.");
                            }
                            if (value == 2) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Ăn xong bảo đảm ngươi sẽ quay lại lần sau.");
                            }
                            break;
                        }
                        case 3: {
                            switch (optionId) {
                                case 0: {
                                    // Đăng kí thien dia bang
                                    RegisterResult result = null;
                                    if (p.nj.get().getLevel() <= 80) {
                                        result = GeninTournament.gi().register(p);

                                    } else if (p.nj.get().getLevel() > 80
                                            && p.nj.get().getLevel() <= Manager.MAX_LEVEL) {
                                        result = KageTournament.gi().register(p);
                                    }

                                    if (result != null) {
                                        if (result == RegisterResult.SUCCESS) {
                                            p.nj.getPlace().chatNPC(p, (short) 4, "Bạn đã đăng kí thành công");
                                        } else if (result == RegisterResult.ALREADY_REGISTER) {
                                            p.nj.getPlace().chatNPC(p, (short) 4, "Bạn đã đăng kí thành công rồi");
                                        } else if (result == RegisterResult.LOSE) {
                                            p.nj.getPlace().chatNPC(p, (short) 4, "Bạn đã thua không thể đăng kí được");
                                        }
                                    } else {

                                    }
                                    break;
                                }
                                case 1: {
                                    // Chinh phuc thien dia bang
                                    try {
                                        final List<TournamentData> tournaments = getTypeTournament(p.nj.getLevel())
                                                .getChallenges(p);
                                        Service.sendChallenges(tournaments, p);
                                    } catch (Exception e) {

                                    }

                                    break;
                                }
                                case 2: {
                                    // Thien bang
                                    sendThongBaoTDB(p, KageTournament.gi(), "Thiên bảng\n");
                                    break;
                                }
                                case 3: {
                                    // Dia bang
                                    sendThongBaoTDB(p, GeninTournament.gi(), "Địa bảng\n");
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
                case 5: {
                    switch (menuId) {
                        case 0: {
                            p.openUI(4);
                            break;
                        }
                        case 1: {
                            p.nj.mapLTD = p.nj.getPlace().map.id;
                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                    "Tốt lắm, ngươi đã chọn nơi này làm nơi trở về khi bị trọng thương");
                            break;
                        }
                        case 2: {
                            if (optionId == 0) {
                                // if (p.nj.isNhanban) {
                                // p.session.sendMessageLog("Chức năng này không dành cho phân thân");
                                // return;
                                // }

                                // if (p.nj.getEffId(34) == null) {
                                // p.nj.getPlace().chatNPC(p, (short) 5, "Phải dùng thí luyện thiếp mới có thể
                                // vào được");
                                // return;
                                // }
                                if (p.nj.getLevel() < 60) {
                                    p.session.sendMessageLog("Trình độ 60 mới có thể sử dụng chức năng này");
                                    return;
                                }
                                final Manager manager = this.server.manager;
                                final Map ma = Manager.getMapid(139);
                                for (final Place area : ma.area) {
                                    if (area.getNumplayers() < ma.template.maxplayers) {
                                        p.nj.getPlace().leave(p);
                                        area.EnterMap0(p.nj);
                                        return;
                                    }
                                }
                                break;
                            } else if (optionId == 1) {
                                p.nj.getPlace().chatNPC(p, (short) 5,
                                        "Sử dụng vật phẩm Thí luyện thiếp để có thể tham gia phiêu lưu tại Vùng Đất Ma Quỷ. Thí luyện thiếp được bán tại NPC Goosho. Nhẫn Thuật 80 sẽ xuất hiện tai Vùng Đất Ma Quỷ.");
                                break;
                            }
                        }
                        case 3: {
                            Random generator = new Random();
                            int value = generator.nextInt(3);
                            if (value == 0) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hãy an tâm giao đồ cho ta nào!");
                            }
                            if (value == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Ta giữ đồ chưa hề để thất lạc bao giờ.");
                            }
                            if (value == 2) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Trên người của ngươi toàn đồ là những đồ có giá trị, sao không cất bớt ở đây?");
                            }
                            break;
                        }
                    }
                    break;
                }
                case 6: {
                    switch (menuId) {
                        case 0: {
                            if (optionId == 0) {
                                p.openUI(10);
                                break;
                            }
                            if (optionId == 1) {
                                p.openUI(31);
                                break;
                            }
                            break;
                        }
                        case 1: {
                            if (optionId == 0) {
                                p.openUI(12);
                                break;
                            }
                            if (optionId == 1) {
                                p.openUI(11);
                                break;
                            }
                            break;
                        }
                        case 2: {
                            p.openUI(13);
                            break;
                        }
                        case 3: {
                            p.openUI(33);
                            break;
                        }
                        case 4: {
                            // Luyen ngoc
                            p.openUI(46);
                            break;
                        }
                        case 5: {
                            // Kham ngoc
                            p.openUI(47);
                            break;
                        }
                        case 6: {
                            // Got ngoc
                            p.openUI(49);
                            break;
                        }
                        case 7: {
                            // Thao ngoc
                            p.openUI(50);
                            break;
                        }
                        case 8: {
                            Random generator = new Random();
                            int value = generator.nextInt(3);
                            if (value == 0) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Đảm bảo sau khi nâng cấp đồ của ngươi sẽ tốt hơn hẳn");
                            }
                            if (value == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Nâng cấp trang bị: Uy tín, giá cả phải chăng.");
                            }
                            if (value == 2) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Ngươi muốn cải tiến trang bị?");
                            }
                            break;
                        }
                    }
                    break;
                }
                case 7: {
                    if (menuId == 0) {
                        Random generator = new Random();
                        int value = generator.nextInt(3);
                        if (value == 0) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Nhà ngươi muốn đi đâu?");
                        }
                        if (value == 1) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Đi xe kéo của ta, an toàn là số một.");
                        }
                        if (value == 2) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Ngựa của ta rất khỏe, có thể chạy ngàn dặm");
                        }
                        break;
                    }
                    if (menuId > 0 && menuId <= Map.arrLang.length) {
                        final Map ma = Manager.getMapid(Map.arrLang[menuId - 1]);
                        if (TaskHandle.isLockChangeMap2((short) ma.id, p.nj.getTaskId())) {
                            GameCanvas.startOKDlg(p.session, Text.get(0, 84));
                            return;
                        }
                        for (final Place area : ma.area) {
                            if (area.getNumplayers() < ma.template.maxplayers) {
                                p.nj.getPlace().leave(p);
                                area.EnterMap0(p.nj);
                                return;
                            }
                        }
                        break;
                    }
                    break;
                }
                case 8: {
                    if (menuId >= 0 && menuId < Map.arrTruong.length) {
                        final Map ma = Manager.getMapid(Map.arrTruong[menuId]);
                        if (TaskHandle.isLockChangeMap2((short) ma.id, p.nj.getTaskId())) {
                            GameCanvas.startOKDlg(p.session, Text.get(0, 84));
                            return;
                        }
                        for (final Place area : ma.area) {
                            if (area.getNumplayers() < ma.template.maxplayers) {
                                p.nj.getPlace().leave(p);
                                area.EnterMap0(p.nj);
                                return;
                            }
                        }
                    } else {
                        Random generator = new Random();
                        int value = generator.nextInt(3);
                        if (value == 0) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Nhà ngươi muốn đi đâu?");
                        }
                        if (value == 1) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Đi xe kéo của ta, an toàn là số một.");
                        }
                        if (value == 2) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Ngựa của ta rất khỏe, có thể chạy ngàn dặm");
                        }
                        break;
                    }
                    break;
                }
                // Toyotomi
                case 9:
                    // Ookamesama
                case 10:
                case 11: {
                    java.util.Map<Integer, List<Integer>> classesByNpcId = new HashMap<>();
                    classesByNpcId.put(11, new ArrayList<>(Arrays.asList(5, 6)));
                    classesByNpcId.put(10, new ArrayList<>(Arrays.asList(3, 4)));
                    classesByNpcId.put(9, new ArrayList<>(Arrays.asList(1, 2)));

                    java.util.Map<Integer, List<String>> randomMessagesByNpcId = new HashMap<>();
                    randomMessagesByNpcId.put(11,
                            new ArrayList<>(Arrays.asList(
                                    "Một học sinh trường gió chúng ta có thể chấp hai học sinh các trường kia.",
                                    "So với các trường khác, trường Gió của chúng ta là tốt nhất",
                                    "Ngươi may mắn mới gặp được ta đó, ta vốn là Thần Gió mà!")));
                    randomMessagesByNpcId.put(10,
                            new ArrayList<>(Arrays.asList("Tập trung học tốt nhé con.",
                                    "Học, để thành tài, để thành người tốt, chứ  không phải để ganh đua với đời.",
                                    "Con có cảm thấy lạnh không?")));
                    randomMessagesByNpcId.put(9,
                            new ArrayList<>(Arrays.asList("Ngươi muốn trở thành hỏa Ninja thì học, không thì cút!",
                                    "Trường ta dạy kiếm, và phi tiêu, chẳng dạy các vũ khí vô danh khác.",
                                    "Theo học ở là đây là vinh hạnh của ngươi, biết chứ?")));

                    if (menuId == 0) {
                        if (optionId == 0) {
                            this.server.manager.sendTB(p, "Top đại gia", BXHManager.getStringBXH(0));
                        } else if (optionId == 1) {
                            this.server.manager.sendTB(p, "Top cao thủ", BXHManager.getStringBXH(1));
                        } else if (optionId == 2) {
                            this.server.manager.sendTB(p, "Top Gia Tộc", BXHManager.getStringBXH(2));
                        } else if (optionId == 3) {
                            this.server.manager.sendTB(p, "Top hang động", BXHManager.getStringBXH(3));
                        }
                        break;
                    }
                    if (menuId == 1) {
                        if (p.nj.get().nclass > 0) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Con đã vào lớp từ trước rồi mà!");
                            break;
                        }
                        if (p.nj.get().ItemBody[1] != null) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Con cần tháo vũ khí ra để đến đây nhập học nhé");
                            break;
                        }
                        if (p.nj.getAvailableBag() < 3) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Hành trang phải có đủ 2 ô để nhận đồ con nhé");
                            break;
                        }
                        if (p.nj.getLevel() < 10) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Trình độ của con chưa đủ");
                            break;
                        }

                        if (!(p.nj.getTaskId() == 9 && p.nj.getTaskIndex() == -1) && !p.nj.isNhanban) {
                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                    "Con hãy hoàn thành hết nhiệm vụ ở làng Tone trước khi nhận lớp");
                            break;
                        }

                        p.Admission(classesByNpcId.get((int) npcId).get((int) optionId).byteValue());
                        p.nj.getPlace().chatNPC(p, (short) npcId, "Hãy chăm chỉ luyện tập để lên cấp con nhé");
                        break;
                    }
                    if (menuId == 2) {
                        if (!classesByNpcId.get((int) npcId).contains((int) p.nj.get().nclass)) {
                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                    "Con không phải là học sinh của trường này, không thể tẩy điểm ở đây.");
                            break;
                        }
                        if (optionId == 0) {
                            if (p.nj.timesResetPpoint == 0) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Số lần tẩy điểm tiềm năng của con đã hết.");
                                return;
                            } else {
                                p.nj.timesResetPpoint -= 1;
                                p.restPpoint(p.nj.get());
                                if (p.nj.timesResetPpoint == 0) {
                                    p.nj.getPlace().chatNPC(p, (short) npcId,
                                            "Ta đã giúp con tẩy điểm tiềm năng. Đây là lần cuối con được tẩy tiềm năng, hãy sử dụng cho thật tốt điểm tiềm năng nhé.");
                                } else {
                                    p.nj.getPlace().chatNPC(p, (short) npcId,
                                            "Ta đã giúp con tẩy điểm tiềm năng. Con vẫn còn có thể tẩy thêm được "
                                                    + p.nj.timesResetPpoint + " lần tẩy tiềm năng nữa.");
                                }
                            }
                        }
                        if (optionId == 1) {
                            if (p.nj.timesResetSpoint == 0) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Số lần tẩy điểm kỹ năng của con đã hết.");
                                return;
                            } else {
                                p.nj.timesResetSpoint -= 1;
                                p.restSpoint();
                                if (p.nj.timesResetSpoint == 0) {
                                    p.nj.getPlace().chatNPC(p, (short) npcId,
                                            "Ta đã giúp con tẩy điểm kỹ năng. Đây là lần cuối con được tẩy kỹ năng, hãy sử dụng cho thật tốt điểm kỹ năng nhé.");
                                } else {
                                    p.nj.getPlace().chatNPC(p, (short) npcId,
                                            "Ta đã giúp con tẩy điểm kỹ năng. Con vẫn còn có thể tẩy thêm được "
                                                    + p.nj.timesResetSpoint + " lần tẩy kỹ năng nữa.");
                                }
                            }
                        }
                        break;
                    }
                    if (menuId == 3) {
                        int value = util.nextInt(randomMessagesByNpcId.get((int) npcId).size());
                        p.nj.getPlace().chatNPC(p, (short) npcId, randomMessagesByNpcId.get((int) npcId).get(value));
                        break;
                    }

                    break;
                }
                case 12: {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            server.manager.sendTB(p, "Trưởng làng",
                                    "Dùng các phím Q,W,E,A,D: Di chuyển nhân vật\nHoặc các phím Lên,Trái,Phải: Di chuyển nhân vật\nPhím Spacebar hoặc phím Enter: Tấn công hoặc hành động\nPhím F1: Menu,Phím F2: Đổi mục tiêu, phím 6,7: Dùng bình HP,MP\nPhím 0: Chat,Phím P: Đổi kỹ năng,Phím 1,2,3,4,5: Sử dụng kỹ năng được gán trước trong mục Kỹ Năng");
                            break;
                        }
                        if (optionId == 1) {
                            server.manager.sendTB(p, "Trưởng làng",
                                    "Kiếm, Kunai, Đao: Ưu tiên tăng sức mạnh(sức đánh) --> thể lực(HP) --> Thân pháp(Né đòn, chính xác) --> Charka(MP).\n\nTiêu, Cung, Quạt: Ưu tiên tăng Charka(Sức đánh, MP) -->thể lực(HP)--> Thân pháp(Né đòn, chính xác). Không tăng SM.");
                            break;
                        }
                        if (optionId == 2) {
                            server.manager.sendTB(p, "Trưởng làng",
                                    "Pk thường: trạng thái hòa bình.\n\nPk phe: không đánh được người cùng nhóm hay cùng bang hội. Giết người không lên điểm hiếu chiến.\n\nPk đồ sát: có thể đánh tất cả người chơi. Giết 1 người sẽ lên 1 điểm hiếu chiến.\n\nĐiểm hiếu chiến cao sẽ không sử dụng bình HP, MP, Thức ăn.\n\nTỷ thí: chọn người chơi, chọn tỷ thí, chờ người đó đồng ý.\n\nCừu Sát: Chọn người chơi khác, chọn cừu sát, điểm hiếu chiến tăng 2.");
                            break;
                        }
                        if (optionId == 3) {
                            server.manager.sendTB(p, "Trưởng làng",
                                    "Bạn có thể tạo một nhóm tối đa 6 người chơi.\n\nNhững người trong cùng nhóm sẽ được nhận thêm x% điểm kinh nghiệm từ người khác.\n\nNhững người cùng nhóm sẽ cùng được vật phẩm, thành tích nếu cùng chung nhiệm vụ.\n\nĐể mời người vào nhóm, chọn người đó, và chọn mời vào nhóm. Để quản lý nhóm, chọn Menu/Tính năng/Nhóm.");
                            break;
                        }
                        if (optionId == 4) {
                            server.manager.sendTB(p, "Trưởng làng",
                                    "Đá dùng để nâng cấp trang bị. Bạn có thể mua từ cửa hàng hoặc nhặt khi đánh quái.Nâng cấp đá nhằm mục đích nâng cao tỉ lệ thành công khi nâng cấp trang bị cấp cao.Để luyện đá, bạn cần tìm Kenshinto.\n\nĐể đảm bảo thành công 100%, 4 viên đá cấp thấp sẽ luyện thành 1 viên đá cấp cao hơn.");
                            break;
                        }
                        if (optionId == 5) {
                            server.manager.sendTB(p, "Trưởng làng",
                                    "Nâng cấp trang bị nhằm mục đích gia tăng các chỉ số cơ bản của trang bị. Có các cấp trang bị sau +1, +2, +3... tối đa +16.Để thực hiện, bạn cần gặp NPC Kenshinto. Sau đó, tiến hành chọn vật phẩm và số lượng đá đủ để nâng cấp. Lưu ý, trang bị cấp độ 5 trở lên nâng cấp thất bại sẽ bị giảm cấp độ.\n\nBạn có thể tách một vật phẩm đã nâng cấp và thu lại 50% số đá đã dùng để nâng cấp trang bị đó.");
                            break;
                        }
                        if (optionId == 6) {
                            server.manager.sendTB(p, "Trưởng làng",
                                    "Khi tham gia các hoạt động trong game bạn sẽ nhận được điểm hoạt động. Qua một ngày điểm hoạt động sẽ bị trừ dần (nếu từ 1-49 trừ 1, 50-99 trừ 2, 100-149 trừ 3...). Mỗi tuần bạn sẽ có cơ hội đổi Yên sang Xu nếu có đủ điểm hoạt động theo vêu cầu của NPC Okanechan.\n\nMột tuần một lần duy nhất được đối tối đa 70.000 Yên = 70.000 xu.");
                            break;
                        }
                    }
                    if (menuId == 1) {
                        Random generator = new Random();
                        int value = generator.nextInt(3);
                        if (value == 0) {
                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                    "Làng Tone là ngôi làng cổ xưa, đã có từ rất lâu.");
                        }
                        if (value == 1) {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Đi thưa, về trình, nhé các con");
                        }
                        if (value == 2) {
                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                    "Ta là Tajima, mọi việc ở đây đều do ta quản lý.");
                        }
                        break;
                    }
                    if (menuId == 3) {
                        if (p.nj.timeRemoveClone > System.currentTimeMillis()) {
                            p.toNhanBan();
                            break;
                        }
                        break;
                    }
                    if (menuId == 6) {
                        p.session.sendMessageLog("Chức năng đang được cập nhật.");
                    } else {
                        if (menuId != 4) {
                            if (menuId == 2) {
                                p.nj.clearTask();
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Ta đã huỷ hết nhiệm vụ và vật phẩm nhiệm vụ của con lần sau làm nhiệm vụ tốt hơn nhé");
                                Service.getTask(p.nj);
                                break;
                            }
                            p.nj.getPlace().chatNPC(p, (short) npcId,
                                    "Con đang thực hiện nhiệm vụ trừ gian diệt ác, hãy chọn Menu/Nhiệm vụ để biết mình đang làm đến đâu");
                            break;
                        }
                        if (!p.nj.clone.isDie && p.nj.timeRemoveClone > System.currentTimeMillis()) {
                            p.exitNhanBan(false);
                            p.nj.clone.open(p.nj.timeRemoveClone, p.nj.getPramSkill(71));
                            break;
                        }
                        break;
                    }
                }
                    break;

                case 14:
                case 15:
                case 16: {
                    boolean hasItem = false;
                    for (Item item : p.nj.ItemBag) {
                        if (item != null && item.id == 214) {
                            hasItem = true;
                            break;
                        }
                    }
                    if (hasItem) {
                        p.nj.removeItemBags(214, 1);
                        p.nj.getPlace().chatNPC(p, npcId, "Ta rất vui vì cô béo còn quan tâm đến ta.");
                        p.nj.upMainTask();
                    } else {
                        if (p.nj.getTaskId() == 20 && p.nj.getTaskIndex() == 1 && npcId == 15) {
                            p.nj.getPlace().leave(p);
                            final Map map = Server.getMapById(74);
                            val place = map.getFreeArea();
                            synchronized (place) {
                                p.expiredTime = System.currentTimeMillis() + 60000L;
                            }
                            Service.batDauTinhGio(p, 600);
                            place.refreshMobs();
                            place.EnterMap0(p.nj);
                        } else {
                            p.nj.getPlace().chatNPC(p, npcId, "Không có thư để con giao");
                        }
                    }
                    break;
                }
                case 17: {
                    Ninja jaien = Ninja.getNinja("Jaian");
                    jaien.p = new User();
                    jaien.p.nj = jaien;
                    Place place = p.nj.getPlace();
                    jaien.upHP(jaien.getMaxHP());
                    jaien.isDie = false;

                    jaien.x = place.map.template.npc[0].x;
                    jaien.id = -p.nj.id;
                    jaien.y = place.map.template.npc[0].y;
                    place.Enter(jaien.p);
                    Place.sendMapInfo(jaien.p, place);
                    break;
                }
                case 19: {
                    if (menuId == 0) {
                        if (p.nj.exptype == 0) {
                            p.nj.exptype = 1;
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Đã tắt không nhận kinh nghiệm");
                            break;
                        }
                        p.nj.exptype = 0;
                        p.nj.getPlace().chatNPC(p, (short) npcId, "Đã bật không nhận kinh nghiệm");
                        break;
                    } else {
                        if (menuId == 1) {
                            p.passold = "";
                            this.sendWrite(p, (short) 51, "Nhập mật khẩu cũ");
                            break;
                        }
                        break;
                    }
                }
                case 22: {
                    if (menuId != 0) {
                        break;
                    }
                    if (p.nj.clan.clanName.isEmpty()) {
                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                "Con cần phải có gia tộc thì mới có thể điểm danh được nhé");
                        break;
                    }
                    if (p.nj.ddClan) {
                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                "Hôm nay con đã điểm danh rồi nhé, hãy quay lại đây vào ngày mai");
                        break;
                    }
                    p.nj.ddClan = true;
                    final ClanManager clan = ClanManager.getClanByName(p.nj.clan.clanName);
                    if (clan == null) {
                        p.nj.getPlace().chatNPC(p, (short) npcId, "Gia tộc lỗi");
                        return;
                    }
                    p.upExpClan(util.nextInt(1, 10 + clan.getLevel()));
                    p.upluongMessage(5 * clan.getLevel());
                    p.nj.upyenMessage(5000 * clan.getLevel());
                    p.nj.getPlace().chatNPC(p, (short) npcId, "Điểm danh mỗi ngày sẽ nhận được các phần quà giá trị");
                    break;
                }
                case 25: {
                    switch (menuId) {
                        case 0: {
                            switch (optionId) {
                                case 0: {
                                    // Nhiem vu hang ngay
                                    if (p.nj.getTasks()[NHIEM_VU_HANG_NGAY] == null && p.nj.nvhnCount < 20) {
                                        val task = createTask(p.nj.getLevel());
                                        if (task != null) {
                                            p.nj.addTaskOrder(task);
                                        } else {
                                            p.nj.getPlace().chatNPC(p, (short) 25,
                                                    "Nhiệm vụ lần này có chút trục trặc chắc con không làm được rồi ahihi");
                                        }
                                    } else if (p.nj.nvhnCount >= 20) {
                                        p.nj.getPlace().chatNPC(p, (short) 25,
                                                "Nhiệm vụ hôm nay con đã làm hết quay lại vào ngày hôm sau");
                                    } else {
                                        p.nj.getPlace().chatNPC(p, (short) 25,
                                                "Nhiệm vụ lần trước ta giao cho con vẫn chưa hoàn thành");
                                    }
                                    break;
                                }
                                case 1: {
                                    // Huy nhiem vu
                                    p.nj.huyNhiemVu(NHIEM_VU_HANG_NGAY);
                                    break;
                                }
                                case 2: {
                                    // Hoan thanh
                                    if (!p.nj.hoanThanhNhiemVu(NHIEM_VU_HANG_NGAY)) {
                                        p.nj.getPlace().chatNPC(p, (short) 25,
                                                "Hãy hoàn thành nhiệm vụ để được nhận thưởng");
                                    } else {
                                        int luck = util.nextInt(0, 100);
                                        if (luck <= 30) {
                                            p.upluongMessage(util.nextInt(p.nj.getLevel(), p.nj.getLevel() * 2));
                                        } else if (luck <= 45) {
                                            long currentLvExps = Level.getLevel(p.nj.getLevel()).exps;

                                            p.updateExp((long) currentLvExps * util.nextInt(1, 3) / 100, true);
                                            ;
                                        } else {

                                            p.nj.upyenMessage(
                                                    Math.min(p.nj.getLevel(), Manager.MAX_LEVEL_RECEIVE_YEN_COEF)
                                                            * Manager.YEN_NVHN_COEF *
                                                            util.nextInt(90,
                                                                    100)
                                                            / 100);
                                        }
                                        if ((p.nj.getTaskId() == 30 && p.nj.getTaskIndex() == 1)
                                                || (p.nj.getTaskId() == 39 && p.nj.getTaskIndex() == 3)) {
                                            p.nj.upMainTask();
                                        }

                                        p.ticketGold += 1;
                                        p.nj.getPlace().chatNPC(p, (short) 25,
                                                "Con được tặng một vé lượng. Hãy tiếp tục cố gắng nhé.");
                                    }
                                    break;
                                }

                                case 3: {
                                    // Di toi
                                    if (p.nj.getTasks() != null && p.nj.getTasks()[NHIEM_VU_HANG_NGAY] != null) {
                                        val task = p.nj.getTasks()[NHIEM_VU_HANG_NGAY];
                                        val map = Server.getMapById(task.getMapId());
                                        p.nj.setMapid(map.id);
                                        for (Npc npc : map.template.npc) {
                                            if (npc.id == 13) {
                                                p.nj.x = npc.x;
                                                p.nj.y = npc.y;
                                                p.nj.getPlace().leave(p);
                                                map.getFreeArea().Enter(p);
                                                break;
                                            }
                                        }
                                        p.nj.getPlace().chatNPC(p, (short) 25,
                                                "Nhiệm vụ lần này gặp lỗi con hãy đi up level lên đi rồi nhận lại nhiệm vụ từ ta");
                                    } else {
                                        p.nj.getPlace().chatNPC(p, (short) 25,
                                                "Hãy nhận nhiệm vụ từ ta để có thể chuyển map");
                                    }
                                }
                            }
                            break;
                        }
                        case 1: {
                            // Ta thu
                            switch (optionId) {
                                case 0: {
                                    // Nhan nhiem vu
                                    if (p.nj.getTasks()[NHIEM_VU_TA_THU] == null) {
                                        if (p.nj.taThuCount > 0) {
                                            val task = createBeastTask(p.nj.getLevel());
                                            if (task != null) {
                                                p.nj.addTaskOrder(task);
                                            } else {
                                                p.nj.getPlace().chatNPC(p, (short) 25, "Nhiệm vụ ngày hôm nay đã hêt");
                                            }
                                        } else {
                                            p.nj.getPlace().chatNPC(p, (short) 25, "Nhiệm vụ ngày hôm nay đã hêt");
                                        }
                                    } else {
                                        p.nj.getPlace().chatNPC(p, (short) 25,
                                                "Nhiệm vụ lần trước ta giao cho con vẫn chưa hoàn thành");
                                    }
                                    break;
                                }
                                case 1: {
                                    p.nj.huyNhiemVu(NHIEM_VU_TA_THU);
                                    break;
                                }
                                case 2: {
                                    if (!p.nj.hoanThanhNhiemVu(NHIEM_VU_TA_THU)) {
                                        p.nj.getPlace().chatNPC(p, (short) 25,
                                                "Hãy hoàn thành nhiệm vụ để được nhận thưởng");
                                    } else {
                                        val i = ItemData.itemDefault(251);
                                        i.quantity = p.nj.get().getLevel() >= 60 ? 5 : 2;
                                        p.nj.addItemBag(true, i);
                                        if ((p.nj.getTaskId() == 30 && p.nj.getTaskIndex() == 2)
                                                || (p.nj.getTaskId() == 39 && p.nj.getTaskIndex() == 1)) {

                                            int luck = util.nextInt(0, 100);
                                            if (luck <= 30) {
                                                p.upluongMessage(util.nextInt(p.nj.getLevel(), p.nj.getLevel() * 2));
                                            } else if (luck < 60) {
                                                long currentLvExps = Level.getLevel(p.nj.getLevel()).exps;

                                                p.updateExp((long) currentLvExps * util.nextInt(3, 7) / 100, true);
                                            } else {
                                                p.nj.upyenMessage(
                                                        Math.min(p.nj.getLevel(), Manager.MAX_LEVEL_RECEIVE_YEN_COEF)
                                                                * Manager.YEN_NVHN_COEF *
                                                                util.nextInt(90,
                                                                        100)
                                                                / 100);
                                            }

                                            p.nj.upMainTask();
                                        }
                                    }
                                    break;
                                }

                                // NOTE fix error when go to map for nvhn pb hack
                                case 3: {
                                    // Di toi
                                    if (p.nj.getTasks() != null && p.nj.getTasks()[NHIEM_VU_HANG_NGAY] != null) {
                                        val task = p.nj.getTasks()[NHIEM_VU_HANG_NGAY];
                                        val map = Server.getMapById(task.getMapId());
                                        p.nj.setMapid(map.id);
                                        for (Npc npc : map.template.npc) {
                                            if (npc.id == 13) {
                                                p.nj.x = npc.x;
                                                p.nj.y = npc.y;
                                                p.nj.getPlace().leave(p);
                                                map.getFreeArea().Enter(p);
                                                break;
                                            }
                                        }
                                        p.nj.getPlace().chatNPC(p, (short) 25,
                                                "Nhiệm vụ lần này gặp lỗi con hãy đi up level lên đi rồi nhận lại nhiệm vụ từ ta");
                                    } else {
                                        p.nj.getPlace().chatNPC(p, (short) 25,
                                                "Hãy nhận nhiệm vụ từ ta để có thể chuyển map");
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        case 2: {
                            // Chien truong
                            switch (optionId) {
                                case 0: {
                                    // Bạch giả
                                    p.nj.enterChienTruong(IBattle.CAN_CU_DIA_BACH);
                                    break;
                                }
                                case 1: {
                                    // Hắc giả
                                    p.nj.enterChienTruong(IBattle.CAN_CU_DIA_HAC);
                                    break;
                                }
                                case 2: {
                                    Service.sendBattleResult(p.nj, Server.getInstance().globalBattle);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
                case 26: {
                    if (menuId == 0) {
                        p.openUI(14);
                        break;
                    }
                    if (menuId == 1) {
                        p.openUI(15);
                        break;
                    }
                    if (menuId == 2) {
                        p.openUI(32);
                        break;
                    }
                    if (menuId == 3) {
                        p.openUI(34);
                        break;
                    }
                    break;
                }
                case 30: {
                    switch (menuId) {
                        case 0: {
                            p.openUI(38);
                            break;
                        }
                        case 1: {
                            this.sendWrite(p, (short) 53, "Mã quà tặng:");
                            break;
                        }
                        case 2: {
                            if (optionId == 0 || optionId == 1) {
                                this.server.manager.rotationluck[0].luckMessage(p);
                                break;
                            }
                            if (optionId == 2) {
                                this.server.manager.sendTB(p, "Vòng xoay vip", "Tham gia đi xem luật lm gì");
                                break;
                            }
                            break;
                        }
                        case 3: {
                            if (optionId == 0 || optionId == 1) {
                                this.server.manager.rotationluck[1].luckMessage(p);
                                break;
                            }
                            if (optionId == 2) {
                                this.server.manager.sendTB(p, "Vòng xoay thường", "Tham gia đi xem luật lm gì");
                                break;
                            }
                            break;
                        }
                        case 4: {
                            p.typemenu = 30_4;
                            doMenuArray(p, new String[] { "Tài", "Xỉu" });
                            break;
                        }
                    }
                    break;
                }
                case 32: {
                    switch (menuId) {
                        case 0: {
                            switch (optionId) {
                                case 0: {
                                    // Tham gia chiến trường kẹo
                                    Server.candyBattleManager.enter(p.nj);
                                    break;
                                }
                                case 1: {
                                    // Hướng dẫn chiến trường kẹo
                                    Service.sendThongBao(p.nj, "Chiến trường kẹo:\n"
                                            + "\t- 20 ninja sẽ chia làm 2 đội Kẹo Trăng và Kẹo Đen.\n"
                                            + "\t- Mỗi đội chơi sẽ có nhiệm vụ tấn công giở kẹo của đối phương, nhặt kẹo và sau đó chạy về bỏ vào giỏ kẹo của bên đội mình.\n"
                                            + "\t- Trong khoảng thời gian ninja giữ kẹo sẽ bị mất một lượng HP nhất định theo thời gian.\n"
                                            + "\t- Giữ càng nhiều thì nguy hiểm càng lớn.\n"
                                            + "\t- Còn 10 phú cuối cùng sẽ xuất hiện Phù Thuỷ");
                                    break;
                                }
                            }
                            break;
                        }
                        case 1: {
                            // Option 1
                            val clanManager = ClanManager.getClanByName(p.nj.clan.clanName);
                            if (clanManager != null) {
                                // Có gia tọc và khong battle
                                if (clanManager.getClanBattle() == null) {
                                    // Chua duoc moi battle
                                    if (p.nj.getClanBattle() == null) {
                                        // La toc truong thach dau
                                        if (p.nj.clan.typeclan == TOC_TRUONG) {
                                            if (clanManager.getClanBattleData() == null
                                                    || (clanManager.getClanBattleData() != null
                                                            && clanManager.getClanBattleData().isExpired())) {
                                                sendWrite(p, (byte) 4, "Nhập vào gia tộc muốn chiến đấu");
                                            } else {
                                                if (clanManager.restore()) {
                                                    enterClanBattle(p, clanManager);
                                                } else {
                                                    p.nj.getPlace().chatNPC(p, (short) 32, "Không hỗ trợ");
                                                }
                                            }
                                        } else {
                                            // Thử tìm battle data
                                            p.nj.getPlace().chatNPC(p, (short) 32, "Không hỗ trợ");
                                        }
                                    }
                                } else {
                                    enterClanBattle(p, clanManager);
                                }
                            }
                            break;
                        }
                        case 4: {
                            if (optionId == 0) {
                                p.openUI(43);
                            } else if (optionId == 1) {
                                p.openUI(44);
                                break;
                            } else if (optionId == 2) {
                                p.openUI(45);
                                break;
                            }
                            break;
                        }
                    }
                    break;
                }
                case 33: {
                    if (p.typemenu != 33) {
                        break;
                    }
                    switch (this.server.manager.EVENT) {
                        case 1: {
                            switch (menuId) {
                                case 0: {
                                    if (p.nj.quantityItemyTotal(432) < 1 || p.nj.quantityItemyTotal(428) < 3
                                            || p.nj.quantityItemyTotal(429) < 2 || p.nj.quantityItemyTotal(430) < 3) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Hành trang của con không có đủ nguyên liệu");
                                        break;
                                    }
                                    if (p.nj.getAvailableBag() == 0) {
                                        p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                                        break;
                                    }
                                    final Item it = ItemData.itemDefault(434);
                                    p.nj.addItemBag(true, it);
                                    p.nj.removeItemBags(432, 1);
                                    p.nj.removeItemBags(428, 3);
                                    p.nj.removeItemBags(429, 2);
                                    p.nj.removeItemBags(430, 3);
                                    break;
                                }
                                case 1: {
                                    if (p.nj.quantityItemyTotal(433) < 1 || p.nj.quantityItemyTotal(428) < 2
                                            || p.nj.quantityItemyTotal(429) < 3 || p.nj.quantityItemyTotal(431) < 2) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Hành trang của con không có đủ nguyên liệu");
                                        break;
                                    }
                                    if (p.nj.getAvailableBag() == 0) {
                                        p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                                        break;
                                    }
                                    final Item it = ItemData.itemDefault(435);
                                    p.nj.addItemBag(true, it);
                                    p.nj.removeItemBags(433, 1);
                                    p.nj.removeItemBags(428, 2);
                                    p.nj.removeItemBags(429, 3);
                                    p.nj.removeItemBags(431, 2);
                                    break;
                                }
                            }
                            break Label_6355;
                        }
                        case 2: {
                            switch (menuId) {
                                case 0: {
                                    if (p.nj.quantityItemyTotal(304) < 1 || p.nj.quantityItemyTotal(298) < 1
                                            || p.nj.quantityItemyTotal(299) < 1 || p.nj.quantityItemyTotal(300) < 1
                                            || p.nj.quantityItemyTotal(301) < 1) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Hành trang của con không có đủ nguyên liệu");
                                        break;
                                    }
                                    if (p.nj.getAvailableBag() == 0) {
                                        p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                                        break;
                                    }
                                    final Item it = ItemData.itemDefault(302);
                                    p.nj.addItemBag(true, it);
                                    p.nj.removeItemBags(304, 1);
                                    p.nj.removeItemBags(298, 1);
                                    p.nj.removeItemBags(299, 1);
                                    p.nj.removeItemBags(300, 1);
                                    p.nj.removeItemBags(301, 1);
                                    break;
                                }
                                case 1: {
                                    if (p.nj.quantityItemyTotal(305) < 1 || p.nj.quantityItemyTotal(298) < 1
                                            || p.nj.quantityItemyTotal(299) < 1 || p.nj.quantityItemyTotal(300) < 1
                                            || p.nj.quantityItemyTotal(301) < 1) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Hành trang của con không có đủ nguyên liệu");
                                        break;
                                    }
                                    if (p.nj.getAvailableBag() == 0) {
                                        p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                                        break;
                                    }
                                    final Item it = ItemData.itemDefault(303);
                                    p.nj.addItemBag(true, it);
                                    p.nj.removeItemBags(305, 1);
                                    p.nj.removeItemBags(298, 1);
                                    p.nj.removeItemBags(299, 1);
                                    p.nj.removeItemBags(300, 1);
                                    p.nj.removeItemBags(301, 1);
                                    break;
                                }
                                case 2: {
                                    if (p.nj.yen < 10000 || p.nj.quantityItemyTotal(292) < 3
                                            || p.nj.quantityItemyTotal(293) < 2 || p.nj.quantityItemyTotal(294) < 3) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Hành trang của con không có đủ nguyên liệu hoặc yên");
                                        break;
                                    }
                                    if (p.nj.getAvailableBag() == 0) {
                                        p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                                        break;
                                    }
                                    final Item it = ItemData.itemDefault(298);
                                    p.nj.addItemBag(true, it);
                                    p.nj.upyenMessage(-10000L);
                                    p.nj.removeItemBags(292, 3);
                                    p.nj.removeItemBags(293, 2);
                                    p.nj.removeItemBags(294, 3);
                                    break;
                                }
                                case 3: {
                                    if (p.nj.yen < 10000 || p.nj.quantityItemyTotal(292) < 2
                                            || p.nj.quantityItemyTotal(295) < 3 || p.nj.quantityItemyTotal(294) < 2) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Hành trang của con không có đủ nguyên liệu hoặc yên");
                                        break;
                                    }
                                    if (p.nj.getAvailableBag() == 0) {
                                        p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                                        break;
                                    }
                                    final Item it = ItemData.itemDefault(299);
                                    p.nj.addItemBag(true, it);
                                    p.nj.upyenMessage(-10000L);
                                    p.nj.removeItemBags(292, 2);
                                    p.nj.removeItemBags(295, 3);
                                    p.nj.removeItemBags(294, 2);
                                    break;
                                }
                                case 4: {
                                    if (p.nj.yen < 10000 || p.nj.quantityItemyTotal(292) < 2
                                            || p.nj.quantityItemyTotal(295) < 3 || p.nj.quantityItemyTotal(297) < 3) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Hành trang của con không có đủ nguyên liệu hoặc yên");
                                        break;
                                    }
                                    if (p.nj.getAvailableBag() == 0) {
                                        p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                                        break;
                                    }
                                    final Item it = ItemData.itemDefault(300);
                                    p.nj.addItemBag(true, it);
                                    p.nj.upyenMessage(-10000L);
                                    p.nj.removeItemBags(292, 2);
                                    p.nj.removeItemBags(295, 3);
                                    p.nj.removeItemBags(297, 3);
                                    break;
                                }
                                case 5: {
                                    if (p.nj.yen < 10000 || p.nj.quantityItemyTotal(292) < 2
                                            || p.nj.quantityItemyTotal(296) < 2 || p.nj.quantityItemyTotal(297) < 3) {
                                        p.nj.getPlace().chatNPC(p, (short) npcId,
                                                "Hành trang của con không có đủ nguyên liệu hoặc yên");
                                        break;
                                    }
                                    if (p.nj.getAvailableBag() == 0) {
                                        p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                                        break;
                                    }
                                    final Item it = ItemData.itemDefault(301);
                                    p.nj.addItemBag(true, it);
                                    p.nj.upyenMessage(-10000L);
                                    p.nj.removeItemBags(292, 2);
                                    p.nj.removeItemBags(296, 2);
                                    p.nj.removeItemBags(297, 3);
                                    break;
                                }
                            }
                            break Label_6355;
                        }
                        default: {
                            p.nj.getPlace().chatNPC(p, (short) npcId, "Hiện tại chưa có sự kiện diễn ra");
                            break Label_6355;
                        }
                    }
                }
                case 92: {
                    p.typemenu = ((menuId == 0) ? 94 : 93);
                    this.doMenuArray(p, new String[] { "Thông tin", "Luật chơi" });
                    break;
                }
                case 93: {
                    if (menuId == 0) {
                        this.server.manager.rotationluck[0].luckMessage(p);
                        break;
                    }
                    if (menuId == 1) {
                        this.server.manager.sendTB(p, "Vòng xoay vip", "Tham gia đi xem luật lm gì");
                        break;
                    }
                    break;
                }
                case 94: {
                    if (menuId == 0) {
                        this.server.manager.rotationluck[1].luckMessage(p);
                        break;
                    }
                    if (menuId == 1) {
                        this.server.manager.sendTB(p, "Vòng xoay thường", "Tham gia đi xem luật lm gì");
                        break;
                    }
                    break;
                }
                case 95: {
                    break;
                }
                case 120: {
                    if (menuId > 0 && menuId < 7) {
                        p.Admission(menuId);
                        break;
                    }
                    break;
                }
                case 23: {
                    // Matsurugi
                    if (ninja.getTaskId() == 23 && ninja.getTaskIndex() == 1 && menuId == 0) {
                        boolean hasItem = false;
                        for (Item item : p.nj.ItemBag) {
                            if (item != null && item.id == 230) {
                                hasItem = true;
                                break;
                            }
                        }

                        if (!hasItem) {
                            val i = ItemData.itemDefault(230);
                            i.setLock(true);
                            p.nj.addItemBag(false, i);
                            p.nj.getPlace().chatNPC(p, 23,
                                    "Ta hi vọng đây là lần cuối ta giao chìa khoá cho con ta nghĩ lần này con sẽ làm được. ");
                        } else {
                            p.nj.getPlace().chatNPC(p, 23, "Con đã có chìa khoá rồi không thể nhận thêm được");
                        }
                    } else {
                        p.nj.getPlace().chatNPC(p, 23, "Ta không quen biết con con đi ra đi");
                    }
                    break;
                }
                case 20: {
                    // Soba
                    if (menuId == 0) {
                        if (!ninja.hasItemInBag(266)) {
                            if (ninja.getTaskId() == 32 && ninja.getTaskIndex() == 1) {
                                val item = ItemData.itemDefault(266);
                                item.setLock(true);
                                ninja.addItemBag(false, item);
                            }
                        } else {
                            ninja.p.sendYellowMessage("Con đã có cần câu không thể nhận thêm");
                        }
                    } else {
                        ninja.getPlace().chatNPC(ninja.p, 20, "Làng ta rất thanh bình con có muốn sống ở đây không");
                    }
                    break;
                }
                case 28: {
                    // Shinwa
                    switch (menuId) {
                        case 0: {
                            final List<ItemShinwa> itemShinwas = items.get((int) optionId);
                            Message mess = new Message(103);
                            mess.writer().writeByte(optionId);
                            if (itemShinwas != null) {
                                mess.writer().writeInt(itemShinwas.size());
                                for (ItemShinwa item : itemShinwas) {
                                    val itemStands = item.getItemStand();
                                    mess.writer().writeInt(itemStands.getItemId());
                                    mess.writer().writeInt(itemStands.getTimeEnd());
                                    mess.writer().writeShort(itemStands.getQuantity());
                                    mess.writer().writeUTF(itemStands.getSeller());
                                    mess.writer().writeInt(itemStands.getPrice());
                                    mess.writer().writeShort(itemStands.getItemTemplate());
                                }
                            } else {
                                mess.writer().writeInt(0);
                            }
                            mess.writer().flush();
                            p.sendMessage(mess);
                            mess.cleanup();
                            break;
                        }
                        case 1: {
                            // Sell item
                            p.openUI(36);
                            break;
                        }
                        case 2: {
                            // Get item back

                            for (ItemShinwa itemShinwa : items.get(-2)) {
                                if (p.nj.getAvailableBag() == 0) {
                                    p.sendYellowMessage("Hành trang không đủ ô trống để nhận thêm");
                                    break;
                                }
                                if (itemShinwa != null) {
                                    if (p.nj.name.equals(itemShinwa.getSeller())) {
                                        itemShinwa.item.quantity = itemShinwa.getQuantity();
                                        p.nj.addItemBag(true, itemShinwa.item);
                                        items.get(-2).remove(itemShinwa);
                                        deleteItem(itemShinwa);
                                    }
                                }
                            }

                            break;
                        }
                    }
                    break;
                }
                case 27: {
                    // Cam chia khoa co quan
                    if (Arrays.stream(p.nj.ItemBag)
                            .anyMatch(item -> item != null && (item.id == 231 || item.id == 260))) {
                        p.nj.removeItemBags(231, 1);
                        p.nj.removeItemBags(260, 1);
                        p.getClanTerritoryData().getClanTerritory().plugKey(p.nj.getMapid(), p.nj);

                    } else {
                        p.sendYellowMessage("Không có chìa khoá để cắm");
                    }
                    break;
                }

                case 24: {
                    switch (menuId) {
                        case 0: {
                            p.typemenu = 24_0;
                            doMenuArray(p, new String[] { "Đổi lượng ra yên", "Đổi lượng ra xu" });
                            break;
                        }
                        case 1: {
                            this.sendWrite(p, (short) 24_1, "Số lượng");
                            break;
                        }
                        case 2: {
                            p.typemenu = 24_2;
                            doMenuArray(p, new String[] { "Cấp 10", "Cấp 20", "Cấp 30", "Cấp 40", "Cấp 50", "Cấp 70",
                                    "Cấp 90", "Cấp 130" });
                            break;
                        }
                        case 3: {
                            Random generator = new Random();
                            int value = generator.nextInt(3);
                            if (value == 0) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Online mỗi ngày tham gia các hoạt động để tích lũy điểm hoạt động con nhé.");
                            }
                            if (value == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Ta là hiện thân của thần tài sẽ mang tài lộc đến cho mọi người.");
                            }
                            if (value == 2) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Con hãy chăm đánh quái, làm nhiệm vụ để có thêm nhiều yên hơn");
                            }
                            break;
                        }
                        case 4: {
                            // this.sendWrite(p, (short) 24_4, "Nhập mã");
                            this.sendWrite(p, (short) 53, "Mã quà tặng:");
                            break;
                        }
                        case 5: {
                            if (p.ticketGold <= 0) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Ngươi đã hết vé tặng lượng. Hãy tích cực tham gia hoạt động hằng ngày để có thể tăng số lượng vé.");
                            } else {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Ngươi đang có " + p.ticketGold
                                                + " vé tặng lượng. Mỗi vé có thể tặng 100 lượng.");
                                this.sendWrite(p, (short) 24_8, "Nhập tên người nhận");
                            }

                            break;
                        }
                    }
                    break;
                }
                case 24_0: {
                    switch (menuId) {
                        case 0:
                            this.sendWrite(p, (short) 24_0_0, "Số lượng");
                            break;
                        case 1:
                            this.sendWrite(p, (short) 24_0_1, "Số lượng");
                            break;
                    }
                    break;
                }
                case 24_2: {
                    switch (menuId) {
                        case 0:
                            if (p.nj.getLevel() < 10) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Trình độ của con không đủ để nhận thưởng.");
                            } else if (p.nj.reward10 == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Con đã nhận phần thưởng này rồi. Mỗi người chỉ được nhận 1 lần.");
                            } else if (p.nj.getAvailableBag() < 3) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hành trang không đủ chỗ trống");
                            } else {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Hãy luyện tập chăm chỉ để tăng cấp và nhận phần thưởng con nhé");
                                p.nj.reward10 = 1;
                                p.nj.upyenMessage(100000);
                                p.nj.upxuMessage(100000);
                            }
                            break;
                        case 1:
                            if (p.nj.getLevel() < 20) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Trình độ của con không đủ để nhận thưởng.");
                            } else if (p.nj.reward20 == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Con đã nhận phần thưởng này rồi. Mỗi người chỉ được nhận 1 lần.");
                            } else if (p.nj.getAvailableBag() < 3) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hành trang không đủ chỗ trống");
                            } else {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Hãy luyện tập chăm chỉ để tăng cấp và nhận phần thưởng con nhé");
                                p.nj.reward20 = 1;
                                p.nj.upyenMessage(100000);
                                p.nj.upxuMessage(100000);
                            }
                            break;
                        case 2:
                            if (p.nj.getLevel() < 30) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Trình độ của con không đủ để nhận thưởng.");
                            } else if (p.nj.reward30 == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Con đã nhận phần thưởng này rồi. Mỗi người chỉ được nhận 1 lần.");
                            } else if (p.nj.getAvailableBag() < 3) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hành trang không đủ chỗ trống");
                            } else {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Hãy luyện tập chăm chỉ để tăng cấp và nhận phần thưởng con nhé");
                                p.nj.reward30 = 1;
                                p.nj.upyenMessage(100000);
                                p.nj.upxuMessage(100000);
                            }
                            break;
                        case 3:
                            if (p.nj.getLevel() < 40) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Trình độ của con không đủ để nhận thưởng.");
                            } else if (p.nj.reward40 == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Con đã nhận phần thưởng này rồi. Mỗi người chỉ được nhận 1 lần.");
                            } else if (p.nj.getAvailableBag() < 3) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hành trang không đủ chỗ trống");
                            } else {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Hãy luyện tập chăm chỉ để tăng cấp và nhận phần thưởng con nhé");
                                p.nj.reward40 = 1;
                                p.nj.upyenMessage(100000);
                                p.nj.upxuMessage(100000);
                            }
                            break;
                        case 4:
                            if (p.nj.getLevel() < 50) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Trình độ của con không đủ để nhận thưởng.");
                            } else if (p.nj.reward50 == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Con đã nhận phần thưởng này rồi. Mỗi người chỉ được nhận 1 lần.");
                            } else if (p.nj.getAvailableBag() < 3) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hành trang không đủ chỗ trống");
                            } else {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Hãy luyện tập chăm chỉ để tăng cấp và nhận phần thưởng con nhé");
                                p.nj.reward50 = 1;
                                p.nj.upyenMessage(100000);
                                p.nj.upxuMessage(100000);
                            }
                            break;
                        case 5:
                            if (p.nj.getLevel() < 70) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Trình độ của con không đủ để nhận thưởng.");
                            } else if (p.nj.reward70 == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Con đã nhận phần thưởng này rồi. Mỗi người chỉ được nhận 1 lần.");
                            } else if (p.nj.getAvailableBag() < Manager.IDS_THUONG_LV70.length + 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hành trang không đủ chỗ trống");
                            } else {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Hãy luyện tập chăm chỉ để tăng cấp và nhận phần thưởng con nhé");
                                p.nj.reward70 = 1;
                                p.upluongMessage(5000);
                                ;
                                p.nj.upyenMessage(1000000);
                                p.nj.upxuMessage(1000000);

                                for (short itemId : Manager.IDS_THUONG_LV70) {
                                    if (itemId != -1) {
                                        Item it = ItemData.itemDefault(itemId);
                                        p.nj.addItemBag(true, it);
                                    }
                                }
                            }
                            break;
                        case 6:
                            if (p.nj.getLevel() < 90) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Trình độ của con không đủ để nhận thưởng.");
                            } else if (p.nj.reward90 == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Con đã nhận phần thưởng này rồi. Mỗi người chỉ được nhận 1 lần.");
                            } else if (p.nj.getAvailableBag() < Manager.IDS_THUONG_LV90.length + 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hành trang không đủ chỗ trống");
                            } else {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Hãy luyện tập chăm chỉ để tăng cấp và nhận phần thưởng con nhé");
                                p.nj.reward90 = 1;
                                p.upluongMessage(10000);
                                ;
                                p.nj.upyenMessage(1500000);
                                p.nj.upxuMessage(1500000);

                                for (short itemId : Manager.IDS_THUONG_LV90) {
                                    if (itemId != -1) {
                                        Item it = ItemData.itemDefault(itemId);
                                        p.nj.addItemBag(true, it);
                                    }
                                }
                            }
                            break;
                        case 7:
                            if (p.nj.getLevel() < 130) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Trình độ của con không đủ để nhận thưởng.");
                            } else if (p.nj.reward130 == 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Con đã nhận phần thưởng này rồi. Mỗi người chỉ được nhận 1 lần.");
                            } else if (p.nj.getAvailableBag() < Manager.IDS_THUONG_LV130.length + 1) {
                                p.nj.getPlace().chatNPC(p, (short) npcId, "Hành trang không đủ chỗ trống");
                            } else {
                                p.nj.getPlace().chatNPC(p, (short) npcId,
                                        "Hãy luyện tập chăm chỉ để tăng cấp và nhận phần thưởng con nhé");
                                p.nj.reward130 = 1;
                                p.upluongMessage(2000);
                                ;
                                p.nj.upyenMessage(2000000);
                                p.nj.upxuMessage(2000000);

                                for (short itemId : Manager.IDS_THUONG_LV130) {
                                    if (itemId != -1) {
                                        Item it = ItemData.itemDefault(itemId);
                                        p.nj.addItemBag(true, it);
                                    }
                                }
                            }
                            break;
                    }
                    break;
                }
                case 30_4: {
                    p.nj.typebet = menuId; // tai = 0, xiu =1
                    this.server.manager.rotationluck[2].luckMessage(p);
                    break;
                }
                case 36_4_0: {
                    if (p.luong < 10_000) {
                        p.sendYellowMessage("Con cần có đủ 10.000 lượng để chuyển phái.");
                        break;
                    }

                    byte nClass = (byte) (menuId + 1);

                    if (p.nj.get().getNClass() == nClass) {
                        p.session.sendMessageLog("Con đang ở lớp này rồi, hãy chuyến tới lớp khác nếu muốn.");
                        break;
                    }

                    p.convertNClass(nClass);
                    p.upluongMessage(-10_000);
                    break;
                }
                case 572: {
                    switch (menuId) {
                        case 0: {
                            p.typeTBLOption = $240;
                            break;
                        }
                        case 1: {
                            p.typeTBLOption = $480;
                            break;
                        }
                        case 2: {
                            p.typeTBLOption = ALL_MAP;
                            break;
                        }
                        case 3: {
                            p.typeTBLOption = PICK_ALL;
                            break;
                        }
                        case 4: {
                            p.typeTBLOption = USEFUL;
                            break;
                        }
                        case 5: {
                            p.activeTBL = !p.activeTBL;
                            break;
                        }
                    }
                    break;
                }
                default: {
                    p.nj.getPlace().chatNPC(p, (short) npcId, "Chức năng này đang cập nhật nhé");
                    break;
                }
            }
        }
        util.Debug("byte1 " + npcId + " byte2 " + menuId + " byte3 " + optionId);
    }

    private void sendThongBaoTDB(User p, Tournament tournaments, String type) {
        val stringBuilder = new StringBuilder();
        stringBuilder.append(type);
        for (TournamentData tournament : tournaments.getTopTen()) {
            stringBuilder.append(tournament.getRanked())
                    .append(".")
                    .append(tournament.getName())
                    .append("\n");
        }
        Service.sendThongBao(p, stringBuilder.toString());
    }

    public static java.util.Map<Byte, int[]> nangCapMat = new TreeMap<>();

    static {
        nangCapMat.put((byte) 1, new int[] { 500, 2_000_000, 80, 200, 100 });
        nangCapMat.put((byte) 2, new int[] { 400, 3_000_000, 75, 300, 85 });
        nangCapMat.put((byte) 3, new int[] { 300, 5_000_000, 65, 500, 75 });
        nangCapMat.put((byte) 4, new int[] { 250, 7_500_000, 55, 700, 65 });
        nangCapMat.put((byte) 5, new int[] { 200, 8_500_000, 45, 900, 55 });
        nangCapMat.put((byte) 6, new int[] { 175, 10_000_000, 30, 1000, 45 });
        nangCapMat.put((byte) 7, new int[] { 150, 12_000_000, 25, 1200, 30 });
        nangCapMat.put((byte) 8, new int[] { 100, 15_000_000, 20, 1200, 25 });
        nangCapMat.put((byte) 9, new int[] { 50, 20_000_000, 15, 1500, 20 });
    }

    private void nangMat(User p, Item item, boolean vip) throws IOException {

        if (item.id < 694) {
            int toneCount = (int) Arrays.stream(p.nj.ItemBag).filter(i -> i != null && i.id == item.id + 11)
                    .map(i -> i.quantity).reduce(0, Integer::sum);
            if (toneCount >= nangCapMat.get(item.getUpgrade())[0]) {

                if (vip && nangCapMat.get(item.getUpgrade())[3] > p.luong) {
                    p.sendYellowMessage("Không đủ lượng nâng cấp vật phẩm");
                    return;
                }
                if (p.nj.xu < nangCapMat.get(item.getUpgrade())[1]) {
                    p.sendYellowMessage("Không đủ xu để nâng cấp");
                    return;
                }
                val succ = util.percent(100, nangCapMat.get(item.getUpgrade())[vip ? 2 : 4]);
                if (succ) {
                    p.nj.get().ItemBody[14] = ItemData.itemDefault(item.id + 1);

                    p.nj.removeItemBags(item.id + 11, nangCapMat.get(item.getUpgrade())[0]);
                    p.sendInfo(false);
                    p.sendYellowMessage(
                            "Nâng cấp mắt thành công bạn nhận được mắt " + p.nj.get().ItemBody[14].getData().name
                                    + p.nj.get().ItemBody[14].getUpgrade() + " đã mặc trên người");
                } else {
                    p.sendYellowMessage("Nâng cấp mắt thất bại");
                }

                if (vip) {
                    p.removeLuong(nangCapMat.get(item.getUpgrade())[3]);
                }

                p.nj.upxuMessage(-nangCapMat.get(item.getUpgrade())[1]);

            } else {
                p.sendYellowMessage("Không đủ " + nangCapMat.get(item.getUpgrade())[0] + " đá danh vọng cấp "
                        + (item.getUpgrade() + 1) + " để nâng cấp");
            }
        } else {
            p.sendYellowMessage("Mắt được nâng cấp tối đa");
        }
    }

    private void enterClanBattle(User p, ClanManager clanManager) {
        val battle = clanManager.getClanBattle();
        p.nj.setClanBattle(battle);
        if (!clanManager.getClanBattle().enter(p.nj, p.nj.getPhe() == Constants.PK_TRANG ? IBattle.BAO_DANH_GT_BACH
                : IBattle.BAO_DANH_GT_HAC)) {
            p.nj.changeTypePk(Constants.PK_NORMAL);
        }
    }

    public void openUINpc(final User p, Message m) throws IOException {
        final short idnpc = m.reader().readShort();
        m.cleanup();
        p.nj.menuType = 0;
        p.typemenu = idnpc;

        if (idnpc == 33 && server.manager.EVENT != 0) {

            val itemNames = new String[EventItem.entrys.length + 1];

            for (int i = 0; i < itemNames.length - 1; i++) {
                itemNames[i] = "Làm " + EventItem.entrys[i].getOutput().getItemData().name;
            }

            itemNames[EventItem.entrys.length] = "Hướng dẫn";
            createMenu(33, itemNames,
                    "Sự kiện tết nguyên đán đã chính thức bắt đầu. Nhanh tay thu thập đủ các nguyên liệu làm bánh để nhận được những vật phẩm vô cùng độc đáo...",
                    p);
        }
        if (idnpc == 24 && p.nj.getLevel() > 1) {
            this.doMenuArray(p, new String[] { "Đổi lượng", "Đổi yên qua xu", "Nhận thưởng thăng cấp", "Nói chuyện",
                    "Mã quà tặng", "Tặng lượng" });
        } else if (idnpc == 30 && p.nj.getLevel() > 1) {
            this.doMenuArray(p, new String[] { "Lật hình", "Mã quà tặng", "Vòng quay VIP", "Vòng quay thường",
                    "Tài Xỉu" });
        } else if (idnpc == 0 && (p.nj.getPlace().map.isGtcMap() ||
                p.nj.getPlace().map.loiDaiMap())) {
            if (p.nj.hasBattle() || p.nj.getClanBattle() != null) {
                createMenu(idnpc, new String[] { "Đặt cược", "Rời khỏi đây" },
                        "Con có 5 phút để xem thông tin đối phương", p);
            }

        } else if (idnpc == Manager.ID_EVENT_NPC) {
            createMenu(Manager.ID_EVENT_NPC, Manager.MENU_EVENT_NPC,
                    Manager.EVENT_NPC_CHAT[util.nextInt(0, Manager.EVENT_NPC_CHAT.length - 1)],
                    p);
        } else if (idnpc == 32 && p.nj.getPlace().map.id == IBattle.BAO_DANH_GT_BACH
                || p.nj.getPlace().map.id == IBattle.BAO_DANH_GT_HAC) {
            createMenu(idnpc, new String[] { "Tổng kết", "Rời khỏi đây" }, "", p);
        } else {
            val ninja = p.nj;
            val npcTemplateId = idnpc;
            p.nj.menuType = 0;

            String[] captions = null;
            if (TaskHandle.isTaskNPC(ninja, npcTemplateId)) {
                captions = new String[1];
                p.nj.menuType = 1;
                if (ninja.getTaskIndex() == -1) {
                    captions[0] = (TaskList.taskTemplates[ninja.getTaskId()]).name;
                } else if (TaskHandle.isFinishTask(ninja)) {
                    captions[0] = Text.get(0, 12);
                } else if (ninja.getTaskIndex() >= 0 && ninja.getTaskIndex() <= 4 &&
                        ninja.getTaskId() == 1) {
                    captions[0] = (TaskList.taskTemplates[ninja.getTaskId()]).name;
                } else if (ninja.getTaskIndex() >= 1 && ninja.getTaskIndex() <= 15 &&
                        ninja.getTaskId() == 7) {
                    captions[0] = (TaskList.taskTemplates[ninja.getTaskId()]).name;
                } else if (ninja.getTaskIndex() >= 1 && ninja.getTaskIndex() <= 3 &&
                        ninja.getTaskId() == 13) {
                    captions[0] = (TaskList.taskTemplates[ninja.getTaskId()]).name;
                } else if (ninja.getTaskId() >= 11) {
                    captions[0] = TaskList.taskTemplates[ninja.getTaskId()].getMenuByIndex(ninja.getTaskIndex());
                }
            }
            if (ninja.getTaskId() == 23 && idnpc == 23 && ninja.getTaskIndex() == 1) {
                captions = new String[1];
                captions[0] = "Nhận chìa khoá";
            } else if (ninja.getTaskId() == 32 && idnpc == 20 && ninja.getTaskIndex() == 1) {
                captions = new String[1];
                captions[0] = "Nhận cần câu";
            }
            Service.openUIMenu(ninja, captions);
        }
    }

    @SneakyThrows
    public void selectMenuNpc(final User p, final Message m) throws IOException {

        val idNpc = (short) m.reader().readByte();
        val index = m.reader().readByte();
        if (idNpc == 0 && p.nj.getTaskId() != 13) {
            if (index == 0) {
                server.menu.sendWrite(p, (short) 3, "Nhập số tiền cược");
            } else if (index == 1) {
                if (p.nj.getBattle() != null) {
                    p.nj.getBattle().setState(Battle.BATTLE_END_STATE);
                }
            }
        } else if (idNpc == Manager.ID_EVENT_NPC) {
            // 0: nhận lượng, 1: tắt exp, 2: bật up exp, 3: nhận thưởng level 70, 4: nhận
            // thưởng level 90, 5: nhận thưởng lv 130
            short featureCode = Manager.ID_FEATURES[index];
            switch (featureCode) {
                case 1: {
                    p.nj.get().exptype = 0;
                    break;
                }
                case 2: {
                    p.nj.get().exptype = 1;
                    break;
                }
                case 3: {
                    if (p.luong >= 10_000) {
                        if (p.nj.maxluggage >= Manager.MAX_LUGGAGE) {
                            p.sendYellowMessage("Hành trang của bạn đã đạt số lượng ô tối đa.");
                            break;
                        }

                        synchronized (p.nj) {
                            p.nj.maxluggage = Manager.MAX_LUGGAGE;
                        }

                        p.upluongMessage(-10_000);
                        p.session.sendMessageLog("Vui lòng thoát game để hệ thống cập nhật 120 ô hành trang.");
                    } else {
                        p.sendYellowMessage("Ta cũng cần ăn cơm đem 10.000 lượng đến đây ta thông hành trang cho");
                    }
                    break;
                }
                case 4: {

                    if (p.nj.get().getNClass() <= 0 || p.nj.get().getNClass() > 6) {
                        p.session.sendMessageLog("Con cần vào lớp để sử dụng tính năng này.");
                        break;
                    }

                    p.typemenu = 36_4_0;
                    doMenuArray(p, new String[] {
                            "Kiếm", "Phi tiêu", "Kunai", "Cung", "Đao", "Quạt" });
                    break;
                }
                case 5: {
                    p.passold = "";
                    this.sendWrite(p, (short) 51, "Nhập mật khẩu cũ");
                    break;
                }
                default:
                    p.nj.getPlace().chatNPC(p, idNpc, "Ta đứng đây từ " + (util.nextInt(0, 1) == 1 ? "chiều" : "trưa"));
            }
        } else if (idNpc == 33 && server.manager.EVENT != 0) {
            if (EventItem.entrys.length == 0) {
                return;
            }

            if (index < EventItem.entrys.length) {
                EventItem entry = EventItem.entrys[index];
                if (entry != null) {
                    this.sendWrite(p, (short) (MIN_EVENT_MENU_ID + index), "Nhập số lượng");
                }
            } else {
                String huongDan = "";
                for (EventItem entry : EventItem.entrys) {
                    String s = "";
                    Recipe[] inputs = entry.getInputs();
                    for (int i = 0, inputsLength = inputs.length; i < inputsLength; i++) {
                        Recipe input = inputs[i];
                        val data = input.getItem().getData();
                        s += input.getCount() + " " + data.name;
                        if (inputsLength != inputs.length - 1) {
                            s += ",";
                        }

                    }
                    huongDan += "Để làm " + entry.getOutput().getItem().getData().name + " cần\n\t" + s;
                    if (entry.getCoin() > 0) {
                        huongDan += ", " + entry.getCoin() + " xu";
                    }

                    if (entry.getCoinLock() > 0) {
                        huongDan += ", " + entry.getCoinLock() + " yên";
                    }

                    if (entry.getGold() > 0) {
                        huongDan += ", " + entry.getGold() + " lượng";
                    }
                    huongDan += "\n";

                }

                Service.sendThongBao(p.nj, huongDan);
            }

        } else if (idNpc == 32 && p.nj.getPlace().map.isGtcMap()) {
            if (index == 0) {
                // Tong ket
                Service.sendBattleResult(p.nj, p.nj.getClanBattle());
            } else if (index == 1) {

                // Roi khoi day
                p.nj.changeTypePk(Constants.PK_NORMAL);
                p.nj.getPlace().gotoHaruna(p);
            }
        } else {
            TaskHandle.getTask(p.nj, (byte) idNpc, index, (byte) -1);
        }
        m.cleanup();
    }

    public static void lamSuKien(User p, EventItem entry) throws IOException {
        lamSuKien(p, entry, 1);
    }

    public static void lamSuKien(User p, EventItem entry, int quantity) throws IOException {
        boolean enough = true;
        for (Recipe input : entry.getInputs()) {
            int id = input.getId();
            enough = p.nj.enoughItemId(id, input.getCount() * quantity);
            if (!enough) {
                p.nj.getPlace().chatNPC(p, (short) 33, "Con không đủ " + input.getItemData().name + " để làm sự kiện");
                break;
            }
        }
        if (enough && p.nj.xu >= entry.getCoin() * quantity && p.nj.yen >= entry.getCoinLock() * quantity
                && p.luong >= entry.getGold() * quantity) {
            for (Recipe input : entry.getInputs()) {
                p.nj.removeItemBags(input.getId(), input.getCount() * quantity);
            }

            Item it = entry.getOutput().getItem();
            it.quantity = quantity;

            p.nj.addItemBag(true, it);
            p.nj.upxuMessage(-entry.getCoin() * quantity);
            p.nj.upyenMessage(-entry.getCoinLock() * quantity);
            p.upluongMessage(-entry.getGold() * quantity);
        }
    }

    private boolean receiverSingleItem(User p, short idItem, int count) {
        if (!p.nj.checkHanhTrang(count)) {
            p.sendYellowMessage(MSG_HANH_TRANG);
            return true;
        }
        for (int i = 0; i < count; i++) {
            p.nj.addItemBag(false, ItemData.itemDefault(idItem));
        }
        return false;
    }

    private boolean nhanQua(User p, short[] idThuong) {
        if (p.nj.getAvailableBag() == 0) {
            p.sendYellowMessage("Hành trang phải đủ " + idThuong.length + " ô để nhận vật phẩm");
            return true;
        }
        for (short i : idThuong) {
            if (i == 12) {
                val quantity = util.nextInt(10_000_000, 15_000_000);
                p.nj.upyen(quantity);
            } else {
                Item item = ItemData.itemDefault(i);
                p.nj.addItemBag(false, item);
            }
        }
        return false;
    }

    @SneakyThrows
    public static void createMenu(int idNpc, String[] menu, String npcChat, User p) {
        val m = new Message(39);
        m.writer().writeShort(idNpc);
        m.writer().writeUTF(npcChat);
        m.writer().writeByte(menu.length);
        for (String s : menu) {
            m.writer().writeUTF(s);
        }

        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
    }

    public static void doMenuArray(final User p, final String[] menu) throws IOException {
        final Message m = new Message(63);
        for (byte i = 0; i < menu.length; ++i) {
            m.writer().writeUTF(menu[i]);
        }
        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();
    }

    public void sendWrite(final User p, final short type, final String title) {
        try {
            final Message m = new Message(92);
            m.writer().writeUTF(title);
            m.writer().writeShort(type);
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
        } catch (IOException ex) {
        }
    }

}
