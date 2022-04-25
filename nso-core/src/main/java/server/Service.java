package server;

import lombok.SneakyThrows;
import lombok.val;
import interfaces.IBattle;
import interfaces.SendMessage;
import tournament.TournamentData;
import real.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import real.User;
import tasks.TaskList;
import tasks.TaskTemplate;
import threading.Manager;
import threading.Message;

public class Service {
    protected static Message messageSubCommand(final byte command) throws Exception {
        final Message message = new Message(-30);
        message.writer().writeByte(command);
        return message;

    }

    public static void openUIMenu(Ninja _ninja, String[] menu) {
        Message msg = null;
        try {
            msg = new Message((byte) 40);
            if (menu != null) {
                for (byte i = 0; i < menu.length; i = (byte) (i + 1)) {
                    if (menu[i] != null) {
                        msg.writer().writeUTF(menu[i]);
                    }
                }
            }
            _ninja.p.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    public static void openUIConfirm(Ninja _char, short npcTemplateId, String chat, String[] menu) {
        Message msg = null;
        try {
            msg = new Message((byte) 39);
            msg.writer().writeShort(npcTemplateId);
            msg.writer().writeUTF(chat);
            msg.writer().writeByte(menu.length);
            byte i;
            for (i = 0; i < menu.length; i = (byte) (i + 1)) {
                msg.writer().writeUTF(menu[i]);
            }
            _char.p.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    protected static Message messageNotLogin(final byte command) throws Exception {
        final Message message = new Message(-29);
        message.writer().writeByte(command);
        return message;
    }

    public static Message messageNotMap(final byte command) throws Exception {
        final Message message = new Message(-28);
        message.writer().writeByte(command);
        return message;
    }

    public static void evaluateCave(final Ninja nj) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-83));
            msg.writer().writeShort(nj.pointCave);
            msg.writer().writeShort(2);
            msg.writer().writeByte(0);
            msg.writer().writeShort(nj.pointCave / 10);
            nj.p.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static Message messageSubCommand2(int command) {
        Message message = new Message(-30);

        try {
            message.writer().writeByte(command - 128);
            return message;
        } catch (Exception e) {
            return message;
        }
    }

    @SneakyThrows
    public static void batDauTinhGio(SendMessage sendMes, int second) {
        if (sendMes == null)
            return;
        val message = messageSubCommand2(33);
        message.writer().writeInt(second);
        message.writer().flush();
        sendMes.sendMessage(message);
        message.cleanup();

    }

    public static void sendInfoChar(final User p, final User _p) {
        Message m = null;
        try {
            // m = messageSubCommand((byte)-120);
            m = new Message((byte) 116);
            m.writer().writeInt(p.nj.get().id);
            m.writer().writeUTF(p.nj.clan.clanName);
            if (!p.nj.clan.clanName.isEmpty()) {
                m.writer().writeByte(p.nj.clan.typeclan);
            }
            m.writer().writeBoolean(false);
            m.writer().writeByte(p.nj.get().getTypepk());
            m.writer().writeByte(p.nj.get().nclass);
            m.writer().writeByte(p.nj.gender);
            m.writer().writeShort(p.nj.get().partHead());
            m.writer().writeUTF(p.nj.name);
            m.writer().writeInt(p.nj.get().hp);
            m.writer().writeInt(p.nj.get().getMaxHP());
            m.writer().writeByte(p.nj.get().getLevel());
            m.writer().writeShort(p.nj.get().Weapon());
            m.writer().writeShort(p.nj.get().partBody());
            m.writer().writeShort(p.nj.get().partLeg());
            m.writer().writeByte(-1);
            m.writer().writeShort(p.nj.get().x);
            m.writer().writeShort(p.nj.get().y);
            m.writer().writeShort(p.nj.get().eff5buffHP());
            m.writer().writeShort(p.nj.get().eff5buffMP());
            m.writer().writeByte(0);
            m.writer().writeBoolean(p.nj.isHuman);
            m.writer().writeBoolean(p.nj.isNhanban);
            m.writer().writeShort(p.nj.get().partHead());
            m.writer().writeShort(p.nj.get().Weapon());
            m.writer().writeShort(p.nj.get().partBody());
            m.writer().writeShort(p.nj.get().partLeg());

            m.writer().writeShort(p.nj.get().ID_HAIR);
            m.writer().writeShort(p.nj.get().ID_Body);
            m.writer().writeShort(p.nj.get().ID_LEG);
            m.writer().writeShort(p.nj.get().ID_WEA_PONE);
            m.writer().writeShort(p.nj.get().ID_PP);
            m.writer().writeShort(p.nj.get().ID_NAME);
            m.writer().writeShort(p.nj.get().ID_HORSE);
            m.writer().writeShort(p.nj.get().ID_RANK);
            m.writer().writeShort(p.nj.get().ID_MAT_NA);
            m.writer().writeShort(p.nj.get().ID_Bien_Hinh);
            m.writer().flush();
            _p.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public static void sendclonechar(final User p, final User top) {
        try {
            Message m = new Message(3);
            m.writer().writeInt(p.nj.clone.id);
            m.writer().writeUTF("");
            m.writer().writeBoolean(false);
            m.writer().writeByte(p.nj.clone.getTypepk());
            m.writer().writeByte(p.nj.clone.nclass);
            m.writer().writeByte(p.nj.clone.chuThan.gender);
            m.writer().writeShort(p.nj.clone.partHead());
            m.writer().writeUTF(p.nj.clone.chuThan.name);
            m.writer().writeInt(p.nj.clone.hp);
            m.writer().writeInt(p.nj.clone.getMaxHP());
            m.writer().writeByte(p.nj.clone.getLevel());
            m.writer().writeShort(p.nj.clone.Weapon());
            m.writer().writeShort(p.nj.clone.partBody());
            m.writer().writeShort(p.nj.clone.partLeg());
            m.writer().writeByte(-1);
            m.writer().writeShort(p.nj.clone.x);
            m.writer().writeShort(p.nj.clone.y);
            m.writer().writeShort(p.nj.eff5buffHP());
            m.writer().writeShort(p.nj.eff5buffMP());
            m.writer().writeByte(0);
            m.writer().writeBoolean(p.nj.clone.isHuman);
            m.writer().writeBoolean(p.nj.clone.isNhanban);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(p.nj.clone.ID_HAIR);
            m.writer().writeShort(p.nj.clone.ID_Body);
            m.writer().writeShort(p.nj.clone.ID_LEG);
            m.writer().writeShort(p.nj.clone.ID_WEA_PONE);
            m.writer().writeShort(p.nj.clone.ID_PP);
            m.writer().writeShort(p.nj.clone.ID_NAME);
            m.writer().writeShort(p.nj.clone.ID_HORSE);
            m.writer().writeShort(p.nj.clone.ID_RANK);
            m.writer().writeShort(p.nj.clone.ID_MAT_NA);
            m.writer().writeShort(p.nj.clone.ID_Bien_Hinh);
            m.writer().flush();
            top.sendMessage(m);
            m.cleanup();
            if (p.nj.clone.mobMe != null) {
                m = new Message(-30);
                m.writer().writeByte(-68);
                m.writer().writeInt(p.nj.clone.id);
                m.writer().writeByte(p.nj.clone.mobMe.templates.id);
                m.writer().writeByte(p.nj.clone.mobMe.isIsboss() ? 1 : 0);
                m.writer().flush();
                top.sendMessage(m);
                m.cleanup();
            }
            p.nj.getPlace().sendCoat(p.nj.clone, top);
            p.nj.getPlace().sendGlove(p.nj.clone, top);
            p.nj.getPlace().sendMounts(p.nj.clone, top);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setHPMob(final Ninja nj, final int mobid, final int hp) {
        Message msg = null;
        try {
            msg = new Message(51);
            msg.writer().writeByte(mobid);
            msg.writer().writeInt(0);
            nj.p.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void CharViewInfo(final User p) {
        CharViewInfo(p, true);
    }

    public static void CharViewInfo(final User p, boolean sendEff) {
        Message msg = null;
        try {
            final Ninja c = p.nj;
            msg = messageSubCommand((byte) 115);
            msg.writer().writeInt(c.get().id);
            msg.writer().writeUTF(c.clan.clanName);
            if (!c.clan.clanName.isEmpty()) {
                msg.writer().writeByte(c.clan.typeclan);
            }
            msg.writer().writeByte(c.getTaskId());
            msg.writer().writeByte(c.gender);
            msg.writer().writeShort(c.get().partHead());
            msg.writer().writeByte(c.get().speed());
            msg.writer().writeUTF(c.name);
            msg.writer().writeByte(c.get().pk);
            msg.writer().writeByte(c.get().getTypepk());
            msg.writer().writeInt(c.get().getMaxHP());
            msg.writer().writeInt(c.get().hp);
            msg.writer().writeInt(c.get().getMaxMP());
            msg.writer().writeInt(c.get().mp);
            msg.writer().writeLong(c.get().getExp());
            msg.writer().writeLong(c.get().expdown);
            msg.writer().writeShort(c.get().eff5buffHP());
            msg.writer().writeShort(c.get().eff5buffMP());
            msg.writer().writeByte(c.get().nclass);
            msg.writer().writeShort(c.get().getPpoint());
            msg.writer().writeShort(c.get().getPotential0());
            msg.writer().writeShort(c.get().getPotential1());
            msg.writer().writeInt(c.get().getPotential2());
            msg.writer().writeInt(c.get().getPotential3());
            msg.writer().writeShort(c.get().getSpoint());
            msg.writer().writeByte(c.get().getSkills().size());
            for (short i = 0; i < c.get().getSkills().size(); ++i) {
                final Skill skill = c.get().getSkills().get(i);
                msg.writer().writeShort(SkillData.Templates(skill.id, skill.point).skillId);
            }
            msg.writer().writeInt(c.xu);
            msg.writer().writeInt(c.yen);
            msg.writer().writeInt(p.luong);
            msg.writer().writeByte(c.maxluggage);
            for (int j = 0; j < c.maxluggage; ++j) {
                final Item item = c.ItemBag[j];
                if (item != null) {
                    msg.writer().writeShort(item.id);
                    msg.writer().writeBoolean(item.isLock());
                    if (ItemData.isTypeBody(item.id) || ItemData.isTypeMounts(item.id)
                            || ItemData.isTypeNgocKham(item.id)) {
                        msg.writer().writeByte(item.getUpgrade());
                    }
                    msg.writer().writeBoolean(item.isExpires);
                    msg.writer().writeShort(item.quantity);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            for (int k = 0; k < 16; ++k) {
                final Item item = c.get().ItemBody[k];
                if (item != null) {
                    msg.writer().writeShort(item.id);
                    msg.writer().writeByte(item.getUpgrade());
                    msg.writer().writeByte(item.sys);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            msg.writer().writeBoolean(c.isHuman);
            msg.writer().writeBoolean(c.isNhanban);
            msg.writer().writeShort(c.get().partHead());
            msg.writer().writeShort(c.get().Weapon());
            msg.writer().writeShort(c.get().partBody());
            msg.writer().writeShort(c.get().partLeg());

            msg.writer().writeShort(c.get().ID_HAIR);
            msg.writer().writeShort(c.get().ID_Body);
            msg.writer().writeShort(c.get().ID_LEG);
            msg.writer().writeShort(c.get().ID_WEA_PONE);
            msg.writer().writeShort(c.get().ID_PP);
            msg.writer().writeShort(c.get().ID_NAME);
            msg.writer().writeShort(c.get().ID_HORSE);
            msg.writer().writeShort(c.get().ID_RANK);
            msg.writer().writeShort(c.get().ID_MAT_NA);
            msg.writer().writeShort(c.get().ID_Bien_Hinh);

            for (int k = 16; k < 32; ++k) {
                final Item item = c.get().ItemBody[k];
                if (item != null) {
                    msg.writer().writeShort(item.id);
                    msg.writer().writeByte(item.getUpgrade());
                    msg.writer().writeByte(item.sys);
                } else {
                    msg.writer().writeShort(-1);
                }

            }

            msg.writer().flush();
            p.sendMessage(msg);
            msg.cleanup();
            p.getMobMe();
            if (sendEff) {
                for (byte n = 0; n < c.get().getVeff().size(); ++n) {
                    p.addEffectMessage(c.get().getVeff().get(n));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void Mobstart(final User p, final int mobid, final int dame, final boolean flag) {
        Message msg = null;
        try {
            msg = new Message(-4);
            msg.writer().writeByte(mobid);
            msg.writer().writeInt(dame);
            msg.writer().writeBoolean(flag);
            msg.writer().flush();
            p.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void Mobstart(final User p, final int mobid, final int hp, final int dame, final boolean flag,
            final int levelboss, final int hpmax) {
        Message msg = null;
        try {
            msg = new Message(-1);
            msg.writer().writeByte(mobid);
            msg.writer().writeInt(hp);
            msg.writer().writeInt(dame);
            msg.writer().writeBoolean(flag);
            msg.writer().writeByte(levelboss);
            msg.writer().writeInt(hpmax);
            msg.writer().flush();
            p.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void HavePlayerAttack(Ninja _ninja, Ninja player, int dame) {
        Message msg = null;
        try {
            msg = new Message((byte) 62);
            msg.writer().writeInt(player.id);
            msg.writer().writeInt(player.hp);
            msg.writer().writeInt(dame);
            msg.writer().writeInt(player.mp);
            msg.writer().writeInt(0);
            _ninja.p.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    public static void PlayerAttack(final User p, final Mob[] mob, final Body b) {
        Message msg = null;
        try {
            msg = new Message(60);
            msg.writer().writeInt(b.id);
            msg.writer().writeByte(b.getCSkill());
            for (byte i = 0; i < mob.length; ++i) {
                if (mob[i] != null) {
                    msg.writer().writeByte(mob[i].id);
                }
            }
            msg.writer().flush();
            p.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    @SneakyThrows
    public static void sendBattleList(User p) {
        val m = new Message(-156);
        val battles = Battle.battles.entrySet()
                .stream()
                .filter(e -> e.getValue().getState() == Battle.CHIEN_DAU_STATE
                        || e.getValue().getState() == Battle.DOI_1_PHUT_STATE)
                .collect(Collectors.toList());
        m.writer().writeByte(battles.size());
        battles.forEach(e -> {
            val key = e.getKey();
            val battle = e.getValue();
            try {
                m.writer().writeByte(key);
                m.writer().writeUTF(battle.getTeam1Name());
                m.writer().writeUTF(battle.getTeam2Name());
            } catch (IOException ex) {
            }
        });
        m.writer().flush();
        p.sendMessage(m);
        m.cleanup();

    }

    ///////////////////////////////
    public static void PlayerAttack(Ninja _ninja, int charID, byte skillId, Mob[] arrMob, Ninja[] arrNinja) {
        Message msg = null;
        try {
            msg = new Message((byte) 4);
            msg.writer().writeInt(charID);
            msg.writer().writeByte(skillId);
            byte num = 0;
            byte i;
            for (i = 0; i < arrMob.length; i = (byte) (i + 1)) {
                if (arrMob[i] != null) {
                    num = (byte) (num + 1);
                }
            }
            msg.writer().writeByte(num);
            for (i = 0; i < arrMob.length; i = (byte) (i + 1)) {
                if (arrMob[i] != null) {
                    msg.writer().writeByte((arrMob[i]).templates.id);
                }
            }
            for (i = 0; i < arrNinja.length; i = (byte) (i + 1)) {
                if (arrNinja[i] != null) {
                    msg.writer().writeInt((arrNinja[i]).id);
                }
            }
            _ninja.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    public static void PlayerAttack(Ninja _ninja, int charID, byte skill, Mob[] arrMob) {
        Message msg = null;
        try {
            msg = new Message((byte) 60);
            msg.writer().writeInt(charID);
            msg.writer().writeByte(skill);
            byte i;
            for (i = 0; i < arrMob.length; i = (byte) (i + 1)) {
                if (arrMob[i] != null) {
                    msg.writer().writeByte((arrMob[i]).templates.id);
                }
            }
            _ninja.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    public static void PlayerAttack(Ninja _ninja, int charID, short skill, Ninja[] arrNinja) {
        Message msg = null;
        try {
            msg = new Message((byte) 61);
            msg.writer().writeInt(charID);
            msg.writer().writeByte(skill);
            byte i;
            for (i = 0; i < arrNinja.length; i = (byte) (i + 1)) {
                if (arrNinja[i] != null) {
                    msg.writer().writeInt((arrNinja[i]).id);
                }
            }
            _ninja.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    @SneakyThrows
    public static void sendBattleResult(Ninja n, IBattle battle) {
        val m = messageNotMap((byte) (46 - 126));
        m.writer().writeUTF(battle.getResult(n));
        val reward = battle.getRewards(n) != null && battle.getRewards(n).length > 0 && n.getClanBattle() == null;
        m.writer().writeBoolean(reward);
        n.sendMessage(m);
        m.cleanup();
    }

    @SneakyThrows
    public static void sendThongBao(SendMessage n, String message) {
        val m = messageNotMap((byte) (46 - 126));
        m.writer().writeUTF(message);
        m.writer().writeBoolean(false);
        n.sendMessage(m);
        m.cleanup();
    }

    @SneakyThrows
    public static void sendChallenges(List<TournamentData> tournaments, SendMessage p) {
        val m = new Message(-135);
        m.writer().writeByte(tournaments.size());
        for (TournamentData tournament : tournaments) {
            m.writer().writeUTF(tournament.getName());
            m.writer().writeInt(tournament.getRanked());
            m.writer().writeUTF(tournament.getStatus());
        }
        p.sendMessage(m);
        m.cleanup();
    }

    public static void openUISay(Ninja _ninja, short npcTemplateId, String chat) {
        Message msg = null;
        try {
            msg = new Message((byte) 38);
            msg.writer().writeShort(npcTemplateId);
            msg.writer().writeUTF(chat);
            _ninja.p.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    public static void finishTask(Ninja _ninja) {
        Message msg = null;
        try {
            msg = new Message((byte) 49);
            _ninja.p.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    public static void updateTask(Ninja _ninja) {
        Message msg = null;
        try {
            msg = new Message((byte) 50);
            msg.writer().writeShort(_ninja.taskCount);
            _ninja.p.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    public static void nextTask(Ninja _ninja) {
        Message msg = null;
        try {
            msg = new Message((byte) 48);
            _ninja.p.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    public static void getTask(Ninja _ninja) {
        Message msg = null;
        try {
            if (TaskList.taskTemplates.length <= _ninja.getTaskId())
                return;

            TaskTemplate taskTemplate = TaskList.taskTemplates[_ninja.getTaskId()];
            msg = new Message((byte) 47);
            msg.writer().writeShort(taskTemplate.getTaskId());
            msg.writer().writeByte(_ninja.getTaskIndex());
            msg.writer().writeUTF(taskTemplate.getName());
            msg.writer().writeUTF(taskTemplate.getDetail());
            msg.writer().writeByte(taskTemplate.subNames.length);
            for (byte b = 0; b < taskTemplate.subNames.length; b = (byte) (b + 1)) {
                msg.writer().writeUTF(taskTemplate.getSubNames()[b]);
            }
            msg.writer().writeShort(_ninja.taskCount);
            for (short i = 0; i < taskTemplate.getCounts().length; i = (short) (i + 1)) {
                msg.writer().writeShort(taskTemplate.getCounts()[i]);
            }
            _ninja.p.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (msg != null)
                msg.cleanup();
        }
    }

    @SneakyThrows
    public static void showWait(String s, SendMessage sendMsg) {
        final Message message = messageSubCommand2(54);
        message.writer().writeUTF(s);
        sendMsg.sendMessage(message);
    }

    public static void endWait(SendMessage sendMessage) {
        final Message message = messageSubCommand2(39);
        sendMessage.sendMessage(message);
    }

    public static void sendBallEffect(Ninja ninja) {
        val m = messageSubCommand2(70);
        ninja.sendMessage(m);
    }

    @SneakyThrows
    public static void sendThieuDot(Collection<? extends SendMessage> sendMessages, int mobId) {
        val m = messageSubCommand2(55);
        m.writer().writeByte(mobId);
        for (SendMessage sendMessage : sendMessages) {
            sendMessage.sendMessage(m);
        }
    }

    public static void createCacheItem() {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            dos.writeByte(Manager.vsItem);
            dos.writeByte(Manager.itemOptionCaches.length);
            for (short i = 0; i < Manager.itemOptionCaches.length; ++i) {
                dos.writeUTF(Manager.itemOptionCaches[i].name);
                dos.writeByte(Manager.itemOptionCaches[i].type);
            }
            dos.writeShort(Manager.itemCaches.length);
            for (short j = 0; j < Manager.itemCaches.length; ++j) {
                dos.writeByte(Manager.itemCaches[j].type);
                dos.writeByte(Manager.itemCaches[j].gender);
                dos.writeUTF(Manager.itemCaches[j].name);
                dos.writeUTF(Manager.itemCaches[j].description);
                dos.writeByte(Manager.itemCaches[j].level);
                dos.writeShort(Manager.itemCaches[j].iconID);
                dos.writeShort(Manager.itemCaches[j].part);
                dos.writeBoolean(Manager.itemCaches[j].isUpToUp);
            }
            byte[] ab = bas.toByteArray();
            GameScr.saveFile("cache/item", ab);
            dos.close();
            bas.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createCacheMap() {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            dos.writeByte(Manager.vsMap);
            dos.writeByte(Manager.mapCaches.length);
            for (short i = 0; i < Manager.mapCaches.length; ++i) {
                dos.writeUTF(Manager.mapCaches[i].mapName);
            }
            dos.writeByte(Manager.npcCaches.length);
            for (byte j = 0; j < Manager.npcCaches.length; ++j) {
                dos.writeUTF(Manager.npcCaches[j].name);
                dos.writeShort(Manager.npcCaches[j].headId);
                dos.writeShort(Manager.npcCaches[j].bodyId);
                dos.writeShort(Manager.npcCaches[j].legId);
                dos.writeByte(Manager.npcCaches[j].menu.length);
                for (short k = 0; k < Manager.npcCaches[j].menu.length; ++k) {
                    dos.writeByte(Manager.npcCaches[j].menu[k].length);
                    for (short m = 0; m < Manager.npcCaches[j].menu[k].length; ++m) {
                        dos.writeUTF(Manager.npcCaches[j].menu[k][m]);
                    }
                }
            }
            dos.writeByte(Manager.mobCaches.length);
            for (short l = 0; l < Manager.mobCaches.length; ++l) {
                dos.writeByte(Manager.mobCaches[l].type);
                dos.writeUTF(Manager.mobCaches[l].name);
                dos.writeInt(Manager.mobCaches[l].hp);
                dos.writeByte(Manager.mobCaches[l].rangeMove);
                dos.writeByte(Manager.mobCaches[l].speed);
            }
            byte[] ab = bas.toByteArray();
            GameScr.saveFile("cache/map", ab);
            dos.close();
            bas.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openMenuBox(User p) {
        Message m = null;
        try {
            p.menuCaiTrang = 0;
            p.openUI(4);
            m = new Message(31);
            m.writer().writeInt(p.nj.xuBox);
            m.writer().writeByte(p.nj.ItemBox.length);
            for (Item item : p.nj.ItemBox) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }

    public static void openMenuBST(User p) {
        Message m = null;
        try {
            p.menuCaiTrang = 1;
            Service.sendTileAction(p, (byte) 4, "Bộ Sưu tập", "Sử dụng");
            m = new Message(31);
            m.writer().writeInt(p.nj.xuBox);
            m.writer().writeByte(p.nj.ItemBST.length);
            for (Item item : p.nj.ItemBST) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }

    public static void openMenuCaiTrang(User p) {
        Message m = null;
        try {
            p.menuCaiTrang = 2;
            Service.sendTileAction(p, (byte) 4, "Cải trang", "Sử dụng");
            m = new Message(31);
            m.writer().writeInt(p.nj.xuBox);
            m.writer().writeByte(p.nj.ItemCaiTrang.length);
            for (Item itemCT : p.nj.ItemCaiTrang) {
                if (itemCT != null) {
                    m.writer().writeShort(itemCT.id);
                    m.writer().writeBoolean(itemCT.isLock());
                    if (ItemData.isTypeBody(itemCT.id) || ItemData.isTypeNgocKham(itemCT.id)) {
                        m.writer().writeByte(itemCT.getUpgrade());
                    }
                    m.writer().writeBoolean(itemCT.isExpires);
                    m.writer().writeShort(itemCT.quantity);
                } else {
                    m.writer().writeShort(-1);
                }
            }
            m.writer().flush();
            p.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }

    public static void sendTileAction(User p, byte typeUI, String title, String action) {
        Message m = null;
        try {
            m = new Message(30);
            m.writer().writeByte(typeUI);
            m.writer().writeUTF(title);
            m.writer().writeUTF(action);
            m.writer().flush();
            p.sendMessage(m);
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }
}
