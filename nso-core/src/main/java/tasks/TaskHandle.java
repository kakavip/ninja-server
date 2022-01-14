package tasks;

import lombok.SneakyThrows;
import lombok.val;
import real.*;
import server.GameCanvas;
import server.GameScr;
import server.Service;
import server.util;
import threading.Map;
import threading.Server;

import static tasks.TaskList.taskTemplates;

public class TaskHandle {

    public static void Task(Ninja ninja, short npcTemplateId) {
        // Nhan nhiem vu
        switch (ninja.getTaskId()) {

            case 0:
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 0),
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            case 1:
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 7),
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            case 2:
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 27),
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            case 3:
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 30),
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            case 4:
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 35),
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            case 5:
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 38),
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            case 6:
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 41),
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            case 7:
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 44),
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            case 8:
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 110),
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            case 9: {
                Service.openUIConfirm(ninja, npcTemplateId, "Nâng cao thể lực bản thân con có muốn nhận không",
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            }
            case 10: {
                final TaskTemplate taskTemplate = taskTemplates[ninja.getTaskId()];
                Service.openUIConfirm(ninja, npcTemplateId, "Bài học đầu tiên của con là "
                        + String.join(", ", taskTemplate.getSubNames()) + ".",
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
                break;
            }
            default:
                final TaskTemplate taskTemplate = taskTemplates[ninja.getTaskId()];
                Service.openUIConfirm(ninja, npcTemplateId, taskTemplate.getNpcTalk(),
                        new String[] { Text.get(0, 10), Text.get(0, 11) });
        }
    }

    public static void doTask(Ninja ninja, short npcTemplateId, byte menuId, byte optionId) {
        if (ninja.getTaskId() == 1) {
            if (ninja.getTaskIndex() == 0) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 12),
                        new String[] { Talk.getTask(0, 17), Talk.getTask(0, 18), Talk.getTask(0, 19) });
            } else if (ninja.getTaskIndex() == 1) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 13),
                        new String[] { Talk.getTask(0, 19), Talk.getTask(0, 20), Talk.getTask(0, 18) });
            } else if (ninja.getTaskIndex() == 2) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 14),
                        new String[] { Talk.getTask(0, 18), Talk.getTask(0, 17), Talk.getTask(0, 21) });
            } else if (ninja.getTaskIndex() == 3) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 15),
                        new String[] { Talk.getTask(0, 22), Talk.getTask(0, 18), Talk.getTask(0, 23) });
            } else if (ninja.getTaskIndex() == 4) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 16),
                        new String[] { Talk.getTask(0, 20), Talk.getTask(0, 23), Talk.getTask(0, 19) });
            }
        } else if (ninja.getTaskId() == 7) {
            if (ninja.getTaskIndex() == 1) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 49),
                        new String[] { Talk.getTask(0, 46), Talk.getTask(0, 47), Talk.getTask(0, 48) });
            } else if (ninja.getTaskIndex() == 2) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 50),
                        new String[] { Talk.getTask(0, 51), Talk.getTask(0, 52), Talk.getTask(0, 53) });
            } else if (ninja.getTaskIndex() == 3) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 54),
                        new String[] { Talk.getTask(0, 55), Talk.getTask(0, 56), Talk.getTask(0, 57) });
            } else if (ninja.getTaskIndex() == 4) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 58),
                        new String[] { Talk.getTask(0, 59), Talk.getTask(0, 60), Talk.getTask(0, 61) });
            } else if (ninja.getTaskIndex() == 5) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 62),
                        new String[] { Talk.getTask(0, 63), Talk.getTask(0, 64), Talk.getTask(0, 65) });
            } else if (ninja.getTaskIndex() == 6) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 67),
                        new String[] { Talk.getTask(0, 68), Talk.getTask(0, 69), Talk.getTask(0, 70) });
            } else if (ninja.getTaskIndex() == 7) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 71),
                        new String[] { Talk.getTask(0, 72), Talk.getTask(0, 73), Talk.getTask(0, 74) });
            } else if (ninja.getTaskIndex() == 8) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 75),
                        new String[] { Talk.getTask(0, 76), Talk.getTask(0, 77), Talk.getTask(0, 78) });
            } else if (ninja.getTaskIndex() == 9) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 79),
                        new String[] { Talk.getTask(0, 80), Talk.getTask(0, 81), Talk.getTask(0, 82) });
            } else if (ninja.getTaskIndex() == 10) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 83),
                        new String[] { Talk.getTask(0, 84), Talk.getTask(0, 85), Talk.getTask(0, 86) });
            } else if (ninja.getTaskIndex() == 11) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 88),
                        new String[] { Talk.getTask(0, 89), Talk.getTask(0, 90), Talk.getTask(0, 91) });
            } else if (ninja.getTaskIndex() == 12) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 92),
                        new String[] { Talk.getTask(0, 93), Talk.getTask(0, 94), Talk.getTask(0, 95) });
            } else if (ninja.getTaskIndex() == 13) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 96),
                        new String[] { Talk.getTask(0, 97), Talk.getTask(0, 98), Talk.getTask(0, 99) });
            } else if (ninja.getTaskIndex() == 14) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 100),
                        new String[] { Talk.getTask(0, 101), Talk.getTask(0, 102), Talk.getTask(0, 103) });
            } else if (ninja.getTaskIndex() == 15) {
                Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 104),
                        new String[] { Talk.getTask(0, 105), Talk.getTask(0, 106), Talk.getTask(0, 107) });
            }
        }
    }

    @SneakyThrows
    public static void finishTask(Ninja ninja, short npcTemplateId) {
        switch (ninja.getTaskId()) {
            case 0:
                Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 9));
                ninja.upyenMessage(10000L);
                ninja.p.updateExp(200L, false);
                break;
            case 1:
                if (ninja.getAvailableBag() < 1) {
                    GameCanvas.startOKDlg(ninja.p.session, Text.get(0, 15));
                    return;
                }
                Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 26));
                ninja.upyenMessage(100L);
                ninja.p.updateExp(400L, false);
                val item = ItemData.itemDefault(194);
                item.option.add(new Option(0, util.nextInt(10, 15)));
                item.option.add(new Option(8, util.nextInt(5, 10)));
                ninja.addItemBag(false, item);
                break;
            case 2:
                Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 28));
                ninja.upyenMessage(200L);
                ninja.p.updateExp(800L, false);
                break;
            case 3:
                Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 34));
                ninja.upyenMessage(300L);
                ninja.p.updateExp(1500L, false);
                break;
            case 4:
                if (ninja.getAvailableBag() < 1) {
                    GameCanvas.startOKDlg(ninja.p.session, Text.get(0, 15));
                    return;
                }
                Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 37));
                ninja.upyenMessage(400L);
                ninja.p.updateExp(2000L, false);

                ninja.addItemBag(false, ItemData.itemDefault(198 - ninja.gender));
                break;
            case 5:
                if (ninja.getAvailableBag() < 2) {
                    GameCanvas.startOKDlg(ninja.p.session, Text.get(0, 15));
                    return;
                }
                Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 40));
                ninja.upyenMessage(500L);
                ninja.p.updateExp(4000L, false);
                ninja.addItemBag(false, ItemData.itemDefault(13));
                val i1 = ItemData.itemDefault(18);
                i1.setLock(true);
                ninja.addItemBag(false, i1);
                break;
            case 6:
                Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 43));
                ninja.upyenMessage(600L);
                ninja.p.updateExp(5000L, false);
                break;
            case 7:
                if (ninja.getAvailableBag() < 1) {
                    GameCanvas.startOKDlg(ninja.p.session, Text.get(0, 15));
                    return;
                }
                Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 43));
                ninja.upyenMessage(700L);
                ninja.p.updateExp(8000L, false);
                val i2 = ItemData.itemDefault(205);
                i2.setLock(true);
                ninja.addItemBag(false, i2);
                break;
            case 8:
                if (ninja.getAvailableBag() < 2) {
                    GameCanvas.startOKDlg(ninja.p.session, Text.get(0, 15));
                    return;
                }
                Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 115));
                ninja.upyenMessage(800L);
                ninja.p.updateExp(24080L, false);
                ninja.addItemBag(false, ItemData.itemDefault(37));
                val i3 = ItemData.itemDefault(225);
                i3.setLock(true);
                ninja.addItemBag(false, i3);
                break;
            default:
                if (ninja.getTaskId() < taskTemplates.length) {
                    val task = taskTemplates[ninja.getTaskId()];
                    if (task != null && task.getItemsPick().length != 0) {
                        for (int itemId : task.getItemsPick()) {
                            ninja.removeAllItemInBag(itemId);
                            ninja.p.sendInfo(false);
                        }
                        val bagNull = ninja.getAvailableBag();
                        if (bagNull >= task.getRewards().length) {
                            int[][] rewards = task.getRewards();
                            for (int i = 0, rewardsLength = rewards.length; i < rewardsLength; i++) {
                                int[] reward = rewards[i];
                                if (ninja.getTaskId() == 24 && ninja.getTaskIndex() == 1) {
                                    if (i == 0) {
                                        continue;
                                    }
                                }

                                if (reward != null && reward.length > 1) {
                                    val itemId = reward[0];
                                    if (itemId == 12) {
                                        ninja.upyenMessage(reward[1]);
                                    } else if (itemId == TaskTemplate.LUONG_ID) {
                                        ninja.p.upluongMessage(reward[1]);
                                    } else if (itemId == TaskTemplate.XU_ID) {
                                        ninja.upxuMessage(reward[1]);
                                    } else if (itemId == TaskTemplate.EXP_ID) {
                                        ninja.p.updateExp(reward[1], false);
                                    } else {
                                        val itemUp = ItemData.itemDefault(itemId);
                                        itemUp.setLock(true);
                                        itemUp.quantity = reward[1];
                                        ninja.addItemBag(itemUp.getData().isUpToUp, itemUp);
                                    }
                                }
                            }
                        } else {
                            //
                            ninja.p.sendYellowMessage("Hành trang không đủ ô trống để nhận vật phẩm");
                            return;
                        }
                    }
                }

        }
        ninja.upMainTask();
        ninja.clearTask();
    }

    public static void getTask(Ninja ninja, byte npcTemplateId, byte menuId, byte optionId) {
        if (isTaskNPC(ninja, npcTemplateId) && Map.isNPCNear(ninja, npcTemplateId)) {
            if (menuId == 0 && ninja.getTaskIndex() == -1) {
                ninja.setTaskIndex(0);
                Service.getTask(ninja);
                switch (ninja.getTaskId()) {
                    case 0:
                        Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 8));
                        break;
                    case 1:
                        Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 10));
                        break;
                    case 2:
                        Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 28));
                        if (ninja.ItemBody[1] != null) {
                            ninja.upMainTask();
                        }
                        break;
                    case 3:
                        Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 31));
                        break;
                    case 4:
                        Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 36));
                        break;
                    case 5:
                        Service.openUISay(ninja, npcTemplateId, Talk.getTask(0, 39));
                        break;
                    case 6:
                        Service.openUISay(ninja, (short) npcTemplateId, Talk.getTask(0, 42));
                        break;
                    case 7:
                        Service.openUISay(ninja, (short) npcTemplateId, Talk.getTask(0, 45));
                        break;
                    case 8:
                        Service.openUISay(ninja, (short) npcTemplateId, Talk.getTask(0, 111));
                        break;

                    default:
                        if (taskTemplates.length > ninja.getTaskId()) {
                            val task = taskTemplates[ninja.getTaskId()];
                            val items = task.getReceiveItems();
                            if (items.length > ninja.getTaskIndex()) {
                                for (int[] item : items) {
                                    if (item != null && item.length == 2) {
                                        final int itemId = item[0];
                                        val quantity = item[1];
                                        val i = ItemData.itemDefault(itemId);
                                        i.setLock(true);
                                        i.quantity = quantity;

                                        ninja.addItemBag(true, i);
                                    } else {
                                        util.Debug("Không có item nào để nhận");
                                    }
                                }

                            } else {
                                util.Debug("Không có item nào để nhận");
                            }
                        }

                }
                requestLevel(ninja);
            } else if (ninja.getTaskId() == 1) {
                if (ninja.getTaskIndex() == 0) {
                    if (menuId == 1) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, npcTemplateId, Talk.getTask(0, 13),
                                new String[] { Talk.getTask(0, 19), Talk.getTask(0, 20), Talk.getTask(0, 18) });
                    } else {
                        Service.openUIConfirm(ninja, npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 12) }),
                                new String[] { Talk.getTask(0, 17), Talk.getTask(0, 18), Talk.getTask(0, 19) });
                    }
                } else if (ninja.getTaskIndex() == 1) {
                    if (menuId == 0) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 14),
                                new String[] { Talk.getTask(0, 18), Talk.getTask(0, 17), Talk.getTask(0, 21) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 13) }),
                                new String[] { Talk.getTask(0, 19), Talk.getTask(0, 20), Talk.getTask(0, 18) });
                    }
                } else if (ninja.getTaskIndex() == 2) {
                    if (menuId == 1) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 15),
                                new String[] { Talk.getTask(0, 22), Talk.getTask(0, 18), Talk.getTask(0, 23) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 14) }),
                                new String[] { Talk.getTask(0, 18), Talk.getTask(0, 17), Talk.getTask(0, 21) });
                    }
                } else if (ninja.getTaskIndex() == 3) {
                    if (menuId == 2) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 16),
                                new String[] { Talk.getTask(0, 20), Talk.getTask(0, 23), Talk.getTask(0, 19) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 15) }),
                                new String[] { Talk.getTask(0, 22), Talk.getTask(0, 18), Talk.getTask(0, 23) });
                    }
                } else if (ninja.getTaskIndex() == 4) {
                    if (menuId == 0) {
                        ninja.upMainTask();
                        Service.openUISay(ninja, (short) npcTemplateId, Talk.getTask(0, 25));
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 16) }),
                                new String[] { Talk.getTask(0, 20), Talk.getTask(0, 23), Talk.getTask(0, 19) });
                    }
                }
            } else if (ninja.getTaskId() == 7) {
                if (ninja.getTaskIndex() == 1) {
                    if (menuId == 1) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 50),
                                new String[] { Talk.getTask(0, 51), Talk.getTask(0, 52), Talk.getTask(0, 53) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 49) }),
                                new String[] { Talk.getTask(0, 46), Talk.getTask(0, 47), Talk.getTask(0, 48) });
                    }
                } else if (ninja.getTaskIndex() == 2) {
                    if (menuId == 0) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 54),
                                new String[] { Talk.getTask(0, 55), Talk.getTask(0, 56), Talk.getTask(0, 57) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 50) }),
                                new String[] { Talk.getTask(0, 51), Talk.getTask(0, 52), Talk.getTask(0, 53) });
                    }
                } else if (ninja.getTaskIndex() == 3) {
                    if (menuId == 1) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 58),
                                new String[] { Talk.getTask(0, 59), Talk.getTask(0, 60), Talk.getTask(0, 61) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 54) }),
                                new String[] { Talk.getTask(0, 55), Talk.getTask(0, 56), Talk.getTask(0, 57) });
                    }
                } else if (ninja.getTaskIndex() == 4) {
                    if (menuId == 1) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 62),
                                new String[] { Talk.getTask(0, 63), Talk.getTask(0, 64), Talk.getTask(0, 65) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 58) }),
                                new String[] { Talk.getTask(0, 59), Talk.getTask(0, 60), Talk.getTask(0, 61) });
                    }
                } else if (ninja.getTaskIndex() == 5) {
                    if (menuId == 2) {
                        ninja.upMainTask();
                        Service.openUISay(ninja, (short) npcTemplateId, Talk.getTask(0, 66));
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 62) }),
                                new String[] { Talk.getTask(0, 63), Talk.getTask(0, 64), Talk.getTask(0, 65) });
                    }
                } else if (ninja.getTaskIndex() == 6) {
                    if (menuId == 2) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 71),
                                new String[] { Talk.getTask(0, 72), Talk.getTask(0, 73), Talk.getTask(0, 74) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 67) }),
                                new String[] { Talk.getTask(0, 68), Talk.getTask(0, 69), Talk.getTask(0, 70) });
                    }
                } else if (ninja.getTaskIndex() == 7) {
                    if (menuId == 0) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 75),
                                new String[] { Talk.getTask(0, 76), Talk.getTask(0, 77), Talk.getTask(0, 78) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 71) }),
                                new String[] { Talk.getTask(0, 72), Talk.getTask(0, 73), Talk.getTask(0, 74) });
                    }
                } else if (ninja.getTaskIndex() == 8) {
                    if (menuId == 2) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 79),
                                new String[] { Talk.getTask(0, 80), Talk.getTask(0, 81), Talk.getTask(0, 82) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 75) }),
                                new String[] { Talk.getTask(0, 76), Talk.getTask(0, 77), Talk.getTask(0, 78) });
                    }
                } else if (ninja.getTaskIndex() == 9) {
                    if (menuId == 2) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 83),
                                new String[] { Talk.getTask(0, 84), Talk.getTask(0, 85), Talk.getTask(0, 86) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 79) }),
                                new String[] { Talk.getTask(0, 80), Talk.getTask(0, 81), Talk.getTask(0, 82) });
                    }
                } else if (ninja.getTaskIndex() == 10) {
                    if (menuId == 1) {
                        ninja.upMainTask();
                        Service.openUISay(ninja, (short) npcTemplateId, Talk.getTask(0, 87));
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 83) }),
                                new String[] { Talk.getTask(0, 84), Talk.getTask(0, 85), Talk.getTask(0, 86) });
                    }
                } else if (ninja.getTaskIndex() == 11) {
                    if (menuId == 0) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 92),
                                new String[] { Talk.getTask(0, 93), Talk.getTask(0, 94), Talk.getTask(0, 95) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 88) }),
                                new String[] { Talk.getTask(0, 89), Talk.getTask(0, 90), Talk.getTask(0, 91) });
                    }
                } else if (ninja.getTaskIndex() == 12) {
                    if (menuId == 1) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 96),
                                new String[] { Talk.getTask(0, 97), Talk.getTask(0, 98), Talk.getTask(0, 99) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 92) }),
                                new String[] { Talk.getTask(0, 93), Talk.getTask(0, 94), Talk.getTask(0, 95) });
                    }
                } else if (ninja.getTaskIndex() == 13) {
                    if (menuId == 2) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 100),
                                new String[] { Talk.getTask(0, 101), Talk.getTask(0, 102), Talk.getTask(0, 103) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 96) }),
                                new String[] { Talk.getTask(0, 97), Talk.getTask(0, 98), Talk.getTask(0, 99) });
                    }
                } else if (ninja.getTaskIndex() == 14) {
                    if (menuId == 2) {
                        ninja.upMainTask();
                        Service.openUIConfirm(ninja, (short) npcTemplateId, Talk.getTask(0, 104),
                                new String[] { Talk.getTask(0, 105), Talk.getTask(0, 106), Talk.getTask(0, 107) });
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 100) }),
                                new String[] { Talk.getTask(0, 101), Talk.getTask(0, 102), Talk.getTask(0, 103) });
                    }
                } else if (ninja.getTaskIndex() == 15) {
                    if (menuId == 1) {
                        ninja.upMainTask();
                        Service.openUISay(ninja, (short) npcTemplateId, Talk.getTask(0, 108));
                    } else {
                        Service.openUIConfirm(ninja, (short) npcTemplateId,
                                String.format(Talk.getTask(0, 24), new Object[] { Talk.getTask(0, 104) }),
                                new String[] { Talk.getTask(0, 105), Talk.getTask(0, 106), Talk.getTask(0, 107) });
                    }
                }
            }
        }
    }

    public static boolean isTaskNPC(Ninja ninja, short npcTemplateId) {
        try {
            byte npcID = Server.getInstance().manager.tasks[ninja.getTaskId()][ninja.getTaskIndex() + 1];
            if (npcID == -1) {
                npcID = getHieuTruong(ninja.nclass);
            }
            return (npcID == npcTemplateId);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte getHieuTruong(int nClass) {

        if (nClass == 1 || nClass == 2) {
            return 9;
        } else if (nClass == 3 || nClass == 4) {
            return 10;
        } else if (nClass == 5 || nClass == 6) {
            return 11;
        }

        return 0;
    }

    public static boolean isFinishTask(Ninja ninja) {
        try {
            return ((taskTemplates[ninja.getTaskId()]).subNames.length == ninja.getTaskIndex() + 1);
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public static boolean isMobTask(Ninja ninja, Mob mob) {
        switch (mob.templates.id) {
            case 0:
                if (ninja.getTaskId() == 2 && ninja.getTaskIndex() == 1) {
                    return true;
                }
                break;
            case 1:
                if (ninja.getTaskId() == 3 && ninja.getTaskIndex() == 2) {
                    return true;
                }
                break;
            case 2:
                if (ninja.getTaskId() == 3 && ninja.getTaskIndex() == 3) {
                    return true;
                }
                break;
            case 5: {
                if (ninja.getTaskId() == 10 && ninja.getTaskIndex() == 0) {
                    return true;
                }
                break;
            }
            case 6: {
                if (ninja.getTaskId() == 10 && ninja.getTaskIndex() == 1) {
                    return true;
                }
                break;
            }
            case 7: {
                if (ninja.getTaskId() == 10 && ninja.getTaskIndex() == 2) {
                    return true;
                }
                break;
            }
            default:
                if (taskTemplates.length > ninja.getTaskId()) {
                    val task = taskTemplates[ninja.getTaskId()];
                    if (ninja.getTaskIndex() == -1) {
                        return false;
                    }

                    val mobTask = task.getMobs();
                    if (mobTask != null && ninja.getTaskIndex() < mobTask.length
                            && mobTask[ninja.getTaskIndex()].length >= 2) {
                        val mobId = mobTask[ninja.getTaskIndex()][0];
                        if (mobId != mob.templates.id) {
                            return false;
                        }
                        val lvBoss = mobTask[ninja.getTaskIndex()][1];
                        if ((lvBoss == 1 || lvBoss == 2) && mob.lvboss == lvBoss
                                && task.getItemsPick()[ninja.getTaskIndex()] == -1) {
                            return true;
                        } else if (lvBoss == 0
                                && task.getItemsPick()[ninja.getTaskIndex()] == -1) {
                            return true;
                        }
                    }
                } else {
                    util.Debug("Không tăng nhiêm vụ mob id = " + mob.templates.id);
                }
        }
        return false;
    }

    public static void requestLevel(Ninja ninja) {
        switch (ninja.getTaskId()) {
            case 4:
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 5) {
                    ninja.upMainTask();
                }
                break;
            case 5:
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 7) {
                    ninja.upMainTask();
                }
                break;
            case 6:
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 8) {
                    ninja.upMainTask();
                }
                break;
            case 7:
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 8) {
                    ninja.upMainTask();
                }
                break;
            case 8:
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 9) {
                    ninja.upMainTask();
                }
                break;
            case 9: {
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 10) {
                    ninja.upMainTask();
                }
            }
            case 11:
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 11) {
                    ninja.upMainTask();
                }
                break;
            case 12:
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 12) {
                    ninja.upMainTask();
                }
                break;
            case 13:
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 14) {
                    ninja.upMainTask();
                }
                break;
            case 14:
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 16) {
                    ninja.upMainTask();
                }
                break;

            case 15:
                if (ninja.getTaskIndex() == 0 && ninja.getLevel() >= 19) {
                    ninja.upMainTask();
                }
                break;

            default:
                if (taskTemplates.length > ninja.getTaskId()) {
                    val task = taskTemplates[ninja.getTaskId()];
                    if (ninja.getTaskIndex() == 0 && task.getMinLevel() <= ninja.getLevel()
                            || task.getBypass() == true) {
                        ninja.upMainTask();
                    }
                }
        }
        inMap(ninja);
    }

    // DROP ITEM HANDLE
    public static short itemDrop(Ninja ninja, Mob mob) {
        switch (ninja.getTaskId()) {
            case 4:
                if (ninja.getTaskIndex() == 1 && mob.templates.id == 3) {
                    return 209;
                }
                if (ninja.getTaskIndex() == 2 && mob.templates.id == 4) {
                    return 210;
                }
                break;
            case 5:
                if (ninja.getTaskIndex() == 1 && mob.templates.id == 54) {
                    return 211;
                }
                break;
            default:
                if (ninja.getTaskId() < taskTemplates.length) {
                    val task = taskTemplates[ninja.getTaskId()];
                    val itemsPick = task.getItemsPick();
                    val dropMobs = task.getMobs();
                    val index = ninja.getTaskIndex();
                    if (itemsPick.length > index && dropMobs.length > index && index != -1) {
                        val itemId = itemsPick[index];
                        val mobDrop = dropMobs[index];
                        if (mobDrop.length == 2) {
                            if (mobDrop[0] == mob.templates.id) {
                                if (348 == itemId) {
                                    return -1;
                                }
                                return (short) itemId;
                            }
                        } else {
                            util.Debug("Mob không đủ length");
                        }
                    } else {
                        util.Debug("Index out of bound");
                    }
                }
        }
        return -1;
    }

    public static boolean itemPick(Ninja ninja, short itemTemplateId) {
        switch (ninja.getTaskId()) {
            case 4:
                if (ninja.getTaskIndex() == 1 && itemTemplateId == 209) {
                    return true;
                }
                if (ninja.getTaskIndex() == 2 && itemTemplateId == 210) {
                    return true;
                }
                break;
            case 5:
                if (ninja.getTaskIndex() == 1 && itemTemplateId == 211) {
                    return true;
                }
                break;
            default:
                if (taskTemplates.length > ninja.getTaskId()) {
                    val task = taskTemplates[ninja.getTaskId()];
                    if (ninja.getTaskIndex() != -1 && ninja.getTaskIndex() < task.getCounts().length) {
                        val count = task.getCounts()[ninja.getTaskIndex()];
                        if (count == -1) {
                            return false;
                        }
                        val items = task.getItemsPick();
                        if (items.length > ninja.getTaskIndex()) {
                            final boolean canPick = itemTemplateId == items[ninja.getTaskIndex()];
                            util.Debug("Can pick " + canPick);
                            return canPick;
                        } else {
                            util.Debug("Không có item nhặt trong danh sách");
                        }
                    }

                }
        }
        return false;
    }

    public static void inMap(Ninja ninja) {
        switch (ninja.getTaskId()) {
            case 6:
                if (ninja.getTaskIndex() == 1 && ninja.getPlace().map.id == 2) {
                    ninja.upMainTask();
                    break;
                }
                if (ninja.getTaskIndex() == 2 && ninja.getPlace().map.id == 71) {
                    ninja.upMainTask();
                    break;
                }
                if (ninja.getTaskIndex() == 3 && ninja.getPlace().map.id == 26) {
                    ninja.upMainTask();
                }
                break;
        }
    }

    public static boolean isLockChangeMap(final short mapID, final byte taskId) {
        switch (taskId) {
            case 0:
            case 1:
            case 2: {
                return mapID != 22;
            }
            case 3:
            case 4: {
                return mapID != 21 && mapID != 22 && mapID != 23;
            }
            case 5: {
                return mapID != 6 && mapID != 20 && mapID != 21 && mapID != 22 && mapID != 23;
            }
            case 6: {
                return mapID != 2 && mapID != 6 && mapID != 20 && mapID != 21 && mapID != 22 && mapID != 23
                        && mapID != 25 && mapID != 26 && mapID != 69 && mapID != 70 && mapID != 71;
            }
            case 7:
            case 8: {
                return mapID != 1 && mapID != 2 && mapID != 6 && mapID != 20 && mapID != 21 && mapID != 22
                        && mapID != 23 && mapID != 25 && mapID != 26 && mapID != 27 && mapID != 69 && mapID != 70
                        && mapID != 71 && mapID != 72;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean isLockChangeMap2(final short mapID, final byte taskId) {
        switch (taskId) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                return mapID == 1 || mapID == 10 || mapID == 17 || mapID == 27 || mapID == 32 || mapID == 38
                        || mapID == 43 || mapID == 48 || mapID == 72;
            }
            default: {
                return false;
            }
        }
    }

    @SneakyThrows
    public static void useItemUpdate(Ninja ninja, int itemId) {
        if (ninja.getTaskId() == 3 && ninja.getTaskIndex() == 1 && itemId == 23) {
            ninja.upMainTask();
        } else if (ninja.getTaskId() == 23 && ninja.getTaskIndex() == 1 && itemId == 230) {
            // 1787,432, 1861,432
            if (ninja.x >= 1787 && ninja.x <= 1861 && ninja.y == 432) {
                if (ninja.party == null) {
                    val area = Server.getMapById(78).getFreeArea();
                    if (area != null) {
                        ninja.getPlace().leave(ninja.p);
                        area.EnterMap0(ninja);
                        area.refreshMobs();
                        ninja.removeItemBags(230, 1);
                        ninja.p.expiredTime = System.currentTimeMillis() + 600000L;
                        Service.batDauTinhGio(ninja, 600);
                    } else {
                        ninja.p.sendYellowMessage("Địa đạo không còn khu trống");
                    }
                } else {
                    ninja.p.sendYellowMessage("Chỉ có thể vào địa đạo một mình");
                }
            } else {
                ninja.p.sendYellowMessage(
                        "Không đúng địa điểm để vào địa đạo, hãy tìm nơi có nhiều đầu lâu nhất để sử mở khoá địa đạo");
            }
        } else if (itemId == 234 && ninja.getTaskId() == 24 && ninja.getTaskIndex() == 1) {

            if (ninja.x >= 1216 && ninja.x <= 1321 && ninja.y == 432) {
                // TODO NHAN THUONG
                // Nhiem vu dao bau vat
                if (util.percent(100, 50)) {
                    ninja.removeItemBags(itemId, 1);
                    ninja.upMainTask();
                    if (taskTemplates.length > ninja.getTaskId()) {
                        val task = taskTemplates[ninja.getTaskId()];
                        if (task.getRewards().length > 0) {
                            final int[] reward = task.getRewards()[0];
                            if (reward.length > 0) {
                                val i = ItemData.itemDefault(reward[0]);
                                i.setLock(true);
                                ninja.addItemBag(false, i);

                            }
                        }
                    }
                } else {
                    ninja.p.sendYellowMessage("Báu vật đang ở đây đào sâu thêm xíu nữa");
                }
            } else {
                Service.sendThongBao(ninja, "Hãy đến khu rừng có cua biển để tìm kho báu theo bức hình");
            }
        } else if (itemId == 219) {
            if (ninja.getTaskIndex() == 1) {
                val taskId = ninja.getTaskId();
                if (taskId == 19 || taskId == 35) {
                    val mapId = ninja.getPlace().map.id;
                    if ((mapId == 63 && taskId == 19 && ninja.x >= 1691 && ninja.x <= 1885 && ninja.y == 336)
                            || (mapId == 24 && taskId == 35 && ninja.x >= 155 && ninja.x <= 589 && ninja.y == 384)) {
                        // Nhiem vu muc nuoc lang chai
                        val lastHp = ninja.hp;
                        Service.showWait("Đang lấy nước vui lòng đợi", ninja);
                        Thread.sleep(2000L);
                        Service.endWait(ninja);
                        if (ninja.hp == ninja.getMaxHP()) {
                            ninja.removeItemBags(219, 1);
                            val it = ItemData.itemDefault(220);
                            it.setLock(true);
                            ninja.addItemBag(true, it);
                            ninja.upMainTask();
                        }
                    } else {
                        ninja.p.sendYellowMessage("Không đúng vị trí để lấy nước hãy đến "
                                + (taskId == 19 ? "Hang Ha" : "Đỉnh IChidai") + " để múc nước");
                    }
                }
            }
        } else if (itemId == 266) {
            if (ninja.getTaskIndex() == 1 && ninja.getTaskId() == 32) {
                if (ninja.x >= 83 && ninja.x <= 277 && ninja.y == 360) {
                    // TODO CAU VAT PHAM
                    Service.showWait("Đang đào", ninja);
                    Thread.sleep(2000L);
                    Service.endWait(ninja);

                    if (util.percent(100, 20)) {
                        val task = taskTemplates[ninja.getTaskId()];
                        ninja.removeItemBags(266, 1);
                        val it = ItemData.itemDefault(task.getItemsPick()[ninja.getTaskIndex()]);
                        it.setLock(true);
                        ninja.addItemBag(false, it);
                        ninja.upMainTask();
                    } else {
                        ninja.p.sendYellowMessage(
                                (util.nextInt(0, 1) == 0) ? "Cần cù bù siêng năng lần này thất bại rồi"
                                        : "Lần sau không câu được thì đốt làng");
                    }
                } else {
                    ninja.p.sendYellowMessage("Hãy tìm vật phẩm ở cái hồ dưới chân ta");
                }
            }
        } else if (itemId == 288 && ninja.getTaskId() == 40 && ninja.getTaskIndex() == 2) {
            if (ninja.getAvailableBag() != -1) {
                ninja.removeItemBags(288, 1);
                // That thu bao
                ninja.upMainTask();
            }
        } else if (itemId >= 222 && itemId <= 228) {
            int count = 0;
            for (Item item : ninja.ItemBag) {

                if (item != null && item.getData().type == 22) {
                    count++;
                }
            }

            if (count == 7) {
                if (ninja.p.nj.getAvailableBag() == 0) {
                    ninja.p.session.sendMessageLog("Hành trang không đủ chỗ trống");
                } else {
                    Service.sendBallEffect(ninja);
                    ninja.p.nj.removeItemBags(222, 1);
                    ninja.p.nj.removeItemBags(223, 1);
                    ninja.p.nj.removeItemBags(224, 1);
                    ninja.p.nj.removeItemBags(225, 1);
                    ninja.p.nj.removeItemBags(226, 1);
                    ninja.p.nj.removeItemBags(227, 1);
                    ninja.p.nj.removeItemBags(228, 1);

                    switch (GameScr.SysClass(ninja.c.nclass)) {
                        case 1:
                            ninja.p.nj.addItemBag(false, ItemData.itemDefault(420));
                            break;
                        case 2:
                            ninja.p.nj.addItemBag(false, ItemData.itemDefault(421));
                            break;
                        case 3:
                            ninja.p.nj.addItemBag(false, ItemData.itemDefault(422));
                            break;
                    }
                }
            } else {
                ninja.p.sendYellowMessage("Cần phải hội tụ 7 viên ngọc thì mới có tác dụng.");
            }
        }
    }

    @SneakyThrows
    public static boolean npcTalk(Ninja ninja, int index, short npcId) {
        index++;
        if (ninja.getTaskId() == 8) {
            if (ninja.getTaskIndex() == 1 && npcId == 9 && index == 3) {
                ninja.upMainTask();
                Service.openUISay(ninja, npcId,
                        "Ngươi thấy ta nóng bỏng không nếu thấy vậy hãy theo học trường của ta");
                return true;
            } else if (ninja.getTaskIndex() == 2 && npcId == 10 && index == 3) {
                ninja.upMainTask();
                Service.openUISay(ninja, npcId,
                        "Trường ta hơi lạnh nếu thích ngươi có thể theo học để có thể giữ cái đầu lạnh");
                return true;
            } else if (ninja.getTaskIndex() == 3 && npcId == 11 && index == 3) {
                ninja.upMainTask();
                Service.openUISay(ninja, npcId,
                        "Trường ta rất khoẻ vì theo hệ gió nên không gặp tình trạng yếu mà hay ra gió");
                return true;
            }
        } else if (ninja.getTaskId() == 0) {
            if (ninja.getTaskIndex() == 0 && npcId == 3 && index == 2) {
                Service.openUISay(ninja, npcId, "Ta bán HP MP cho mọi người");
                ninja.upMainTask();
                return true;
            } else if (ninja.getTaskIndex() == 1 && npcId == 4 && index == 2) {
                Service.openUISay(ninja, npcId, "Ta bán thức ăn");
                ninja.upMainTask();
                return true;
            } else if (ninja.getTaskIndex() == 2 && npcId == 6 && index == 8) {
                Service.openUISay(ninja, npcId, "Ta bán nâng cấp trang bị");
                ninja.upMainTask();
                return true;
            } else if (ninja.getTaskIndex() == 3 && npcId == 24 && index == 5) {
                Service.openUISay(ninja, npcId, "Gì liên quan tới tiền lại gặp ta");
                ninja.upMainTask();
                return true;
            } else if (ninja.getTaskIndex() == 4 && npcId == 5 && index == 3) {
                Service.openUISay(ninja, npcId, "Giữ đồ và khiên về khi bị chết do ta đảm nhận");
                ninja.upMainTask();
                return true;
            } else if (ninja.getTaskIndex() == 5 && npcId >= 7 && npcId <= 8 && index == 0) {
                Service.openUISay(ninja, npcId, "Ngựa ta đi rất nhanh");
                ninja.upMainTask();
                return true;
            }
        }
        return false;
    }
}
