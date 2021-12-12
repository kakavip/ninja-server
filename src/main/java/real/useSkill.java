package real;

import java.io.IOException;

import lombok.val;
import patch.MessageSubCommand;
import threading.Message;

public class useSkill {

    public static void useSkill(Body body, short idSkill) throws IOException {

        final Skill skill = body.getSkill(idSkill);


        if (skill != null && System.currentTimeMillis() > body.CSkilldelay) {
            final SkillData data = SkillData.Templates(idSkill);
            if (data.type != 0) {
                body.CSkilldelay = System.currentTimeMillis() + 500L;
                if (data.type == 2) {
                    useSkillBuff(body, idSkill);
                } else {
                    body.setCSkill(idSkill);
                }
            }
        }
    }

    private static void useSkillBuff(Body body, final int skilltemp) throws IOException {

        val p = body.c.p;

        final Skill skill = body.getSkill(skilltemp);
        final SkillTemplates temp = SkillData.Templates(skill.id, skill.point);

        if (body.mp < temp.manaUse) {
            p.getMp();
            return;
        }
        if (skill.coolDown > System.currentTimeMillis()) {
            return;
        }
        body.upMP(-temp.manaUse);
        skill.coolDown = System.currentTimeMillis() + temp.coolDown;
        int param = 0;
        switch (skilltemp) {
            case 6: {
                p.setEffect(15, 0, body.getPramSkill(53) * 1000, 0);
                break;
            }
            case 13: {
                p.setEffect(9, 0, 30000, body.getPramSkill(51));
                break;
            }
            case 15: {
                p.setEffect(16, 0, 5000, body.getPramSkill(52));
                break;
            }
            case 31: {
                p.setEffect(10, 0, 90000, body.getPramSkill(30));
                break;
            }
            case 33: {
                p.setEffect(17, 0, 5000, body.getPramSkill(56));
                break;
            }
            case 47: {
                param = body.getPramSkill(27);
                param += param * body.getPramSkill(66) / 100;
                p.setEffect(8, 0, 5000, param);
                if (body.party != null) {
                    for (User p2 : p.nj.getPlace().getUsers()) {
                        if (p2.nj.id != p.nj.id) {
                            final Ninja n = p2.nj;
                            if (n.party == body.party && Math.abs(body.x - n.x) <= temp.dx && Math.abs(body.y - n.y) <= temp.dy) {
                                n.p.setEffect(8, 0, 5000, body.getPramSkill(43) + body.getPramSkill(43) * body.getPramSkill(66) / 100);
                            }
                        }
                    }
                }
                break;
            }
            case 51: {
                param = body.getPramSkill(45);
                param += body.getPramSkill(66);
                p.setEffect(19, 0, 90000, param);

                if (body.party != null) {
                    for (User p2 : p.nj.getPlace().getUsers()) {
                        if (p2.nj.id != p.nj.id) {
                            final Ninja n = p2.nj;
                            if (n.party == body.party && Math.abs(p.nj.x - n.x) <= temp.dx && Math.abs(p.nj.y - n.y) <= temp.dy) {
                                n.p.setEffect(19, 0, 90000, param);
                            }
                        }
                    }
                }
                break;
            }
            case 52: {
                p.setEffect(20, 0, body.getPramSkill(54) * 1000, body.getPramSkill(66));
                if (body.party != null) {
                    for (User p2 : p.nj.getPlace().getUsers()) {
                        if (p2.nj.id != p.nj.id) {
                            final Ninja n = p2.nj;
                            if (n.party == body.party && Math.abs(body.x - n.x) <= temp.dx && Math.abs(body.y - n.y) <= temp.dy) {
                                n.p.setEffect(20, 0, body.getPramSkill(54) * 1000, body.getPramSkill(66));
                            }
                        }
                    }
                }
                break;
            }
            case 58: {
                p.setEffect(11, 0, body.getPramSkill(64), Short.MAX_VALUE);
                break;
            }
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72: {
                if (p.nj.timeRemoveClone <= System.currentTimeMillis() && p.nj.quantityItemyTotal(545) <= 0) {
                    p.sendYellowMessage("Không có đủ " + ItemData.ItemDataId(545).name);
                    break;
                }
                p.nj.clone.open(System.currentTimeMillis() + 60000 * p.nj.getPramSkill(68), p.nj.getPramSkill(71));
                if (p.nj.quantityItemyTotal(545) > 0) {
                    p.nj.removeItemBags(545, 1);
                    break;
                }
                break;
            }
            case 22: {
                // Send bu nhin
                p.nj.getPlace().addBuNhin(new BuNhin(p.nj.name, p.nj.x, p.nj.y, temp.options.get(0).param * 1000, p.nj.id, p.nj.hp));
                break;
            }
        }
    }

    public static void useSkillSupport(final User p, final int skilltemp, final int type, final Ninja n) throws IOException {
        final Skill skill = p.nj.get().getSkill(skilltemp);
        final SkillTemplates temp = SkillData.Templates(skill.id, skill.point);
        switch (skilltemp) {
            case 49: {
                if (n.isDie) {
                    n.p.liveFromDead();
                    n.p.setEffect(11, 0, 5000, p.nj.get().getPramSkill(28));
                    break;
                }
                break;
            }
        }
    }

    public static void buffLive(final User p, final Message m) throws IOException {
        final int idP = m.reader().readInt();
        final Ninja nj = p.nj.getPlace().getNinja(idP);
        m.cleanup();
        final Skill skill = p.nj.get().getSkill(p.nj.get().getCSkill());
        if (nj != null && nj.isDie && skill.id == 49) {
            final SkillTemplates temp = SkillData.Templates(skill.id, skill.point);
            if (p.nj.get().mp < temp.manaUse) {
                MessageSubCommand.sendMP(p.nj);
                return;
            }
            if (skill.coolDown > System.currentTimeMillis() || Math.abs(p.nj.get().x - nj.x) > temp.dx || Math.abs(p.nj.get().y - nj.y) > temp.dy) {
                return;
            }
            p.nj.get().upMP(-temp.manaUse);
            skill.coolDown = System.currentTimeMillis() + temp.coolDown;
            nj.p.liveFromDead();
            nj.p.setEffect(11, 0, 5000, p.nj.get().getPramSkill(28));
        }
    }
}
