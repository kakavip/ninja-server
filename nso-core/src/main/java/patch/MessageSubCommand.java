package patch;

import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import real.*;
import server.Service;
import threading.Message;

import java.util.Collection;
import java.util.List;

public class MessageSubCommand {
    /**
     * case 0
     * send charId int
     * send cHP int
     * send cMaxHP int
     * level unsignByte
     */
    @SneakyThrows
    public static void sendHPMaXHPLevel(@Nullable final Ninja ninja) {
        if (ninja == null) return;

        val m = Service.messageSubCommand2(0);
        m.writer().writeInt(ninja.hp);
        m.writer().writeInt(ninja.getMaxHP());
        m.writer().writeByte(ninja.getLevel());
        ninja.sendMessage(m);
        m.cleanup();

    }

    @SneakyThrows
    public static void sendLock(@Nullable final Ninja ninja) {
        if (ninja == null) return;

        val m = Service.messageSubCommand2(5);
        m.writer().writeInt(ninja.xu);
        m.writer().writeInt(ninja.yen);
        m.writer().writeInt(ninja.p.luong);
        m.writer().writeInt(ninja.hp);
        m.writer().writeInt(ninja.mp);
        m.writer().writeByte(1);
    }

    /**
     * case 6
     * Send hp
     */
    @SneakyThrows
    public static void sendHP(@Nullable final Ninja ninja) {
        if (ninja == null) return;

        val m = Service.messageSubCommand2(6);
        m.writer().writeInt(ninja.hp);
        ninja.sendMessage(m);
        m.cleanup();

    }

    /**
     * case 7
     *
     * @param ninja mp int
     */
    @SneakyThrows
    public static void sendMP(@Nullable final Ninja ninja) {
        if (ninja == null) return;

        val m = Service.messageSubCommand2(7);
        m.writer().writeInt(ninja.mp);
        ninja.sendMessage(m);
        m.cleanup();
        ninja.p.sendYellowMessage("Không đủ MP để sử dụng");
    }

    /**
     * case 8
     *Read char info
     *
     */

    /**
     * case 9
     * Update peer hp max hp
     * int char in map_back id
     * cHP char hp
     * cMaxHP max hp char
     */

    @SneakyThrows
    public static void sendMP(@Nullable Body ninja, @NotNull List<@Nullable User> ninjas) {
        if (ninja == null) return;
        val m = Service.messageSubCommand2(9);
        m.writer().writeInt(ninja.id);
        m.writer().writeInt(ninja.hp);
        m.writer().writeInt(ninja.getMaxHP());
        m.writer().flush();
        for (User ninja1 : ninjas) {
            if (ninja1 == null) continue;
            ninja1.sendMessage(m);
        }
        m.cleanup();

    }

    /**
     * case 11
     * int id char in map_back
     * hp int
     * max hp
     * eff5buffhp
     * eff5buffmp
     * wp short
     */

    @SneakyThrows
    public static void sendHPWP(final @Nullable Body ninja, @NotNull List<@Nullable User> ninjas) {
        if (ninja == null) return;
        val m = Service.messageSubCommand2(11);
        m.writer().writeInt(ninja.id);
        m.writer().writeInt(ninja.hp);
        m.writer().writeInt(ninja.getMaxHP());
        m.writer().writeInt(ninja.eff5buffHP());
        m.writer().writeInt(ninja.eff5buffMP());
        m.writer().writeInt(ninja.Weapon());
        for (User ninja1 : ninjas) {
            if (ninja1 != null) {
                ninja1.sendMessage(m);
            }
        }
        m.cleanup();

    }

    /**
     * case 12
     * int id char in map_back
     * hp int
     * max hp
     * eff5buffhp
     * eff5buffmp
     * head short
     */

    @SneakyThrows
    public static void sendHPHead(@Nullable final Body ninja, @NotNull List<@Nullable User> ninjas) {
        if (ninja == null) return;
        val m = Service.messageSubCommand2(12);
        m.writer().writeInt(ninja.id);
        m.writer().writeInt(ninja.hp);
        m.writer().writeInt(ninja.getMaxHP());
        m.writer().writeInt(ninja.eff5buffHP());
        m.writer().writeInt(ninja.eff5buffMP());
        m.writer().writeInt(ninja.partHead());

        for (val ninja1 : ninjas) {
            if (ninja1 == null) continue;
            ninja1.sendMessage(m);
        }
        m.cleanup();

    }

    /**
     * case 15
     * int id char in map_back
     * hp int
     * max hp
     * eff5buffhp
     * eff5buffmp
     * head short
     */

    @SneakyThrows
    public static void sendHPLeg(@Nullable Body ninja, @NotNull List<@Nullable User> ninjas) {
        if (ninja == null) return;
        val m = Service.messageSubCommand2(15);
        m.writer().writeInt(ninja.id);
        m.writer().writeInt(ninja.hp);
        m.writer().writeInt(ninja.getMaxHP());
        m.writer().writeInt(ninja.eff5buffHP());
        m.writer().writeInt(ninja.eff5buffMP());
        m.writer().writeInt(ninja.partLeg());

        for (User ninja1 : ninjas) {
            if (ninja1 == null) continue;
            ninja1.sendMessage(m);
        }
        m.cleanup();

    }

    /**
     * case 16
     * int id char in map_back
     * hp int
     * max hp
     * eff5buffhp
     * eff5buffmp
     */

    @SneakyThrows
    public static void sendHPBuffHPMP(@Nullable Ninja ninja, @NotNull List<@Nullable Ninja> ninjas) {
        if (ninja == null) return;

        val m = Service.messageSubCommand2(16);
        m.writer().writeInt(ninja.id);
        m.writer().writeInt(ninja.hp);
        m.writer().writeInt(ninja.getMaxHP());
        m.writer().writeInt(ninja.eff5buffHP());
        m.writer().writeInt(ninja.eff5buffMP());
        m.writer().writeInt(ninja.partLeg());

        for (Ninja ninja1 : ninjas) {
            if (ninja1 == null) continue;
            ninja1.sendMessage(m);
        }
        m.cleanup();

    }

    /**
     * case 17
     * int id char in map_back
     * hp int
     * max hp
     * eff5buffhp
     * eff5buffmp
     */

    @SneakyThrows
    public static void sendHP(@Nullable Body body, @NotNull Collection<@Nullable User> users) {
        if (body == null) return;
        val m = Service.messageSubCommand2(17);
        m.writer().writeInt(body.id);
        m.writer().writeInt(body.hp);
        for (User user : users) {
            if (user == null) continue;

            val ninja = user.nj;
            if (body.id == ninja.id) {
                sendHP(ninja);
            } else {
                user.sendMessage(m);
            }
        }
        m.cleanup();

    }

    /**
     * case 18
     * int id char in map_back
     * hp int
     * max hp
     * eff5buffhp
     * eff5buffmp
     */

    @SneakyThrows
    public static void sendHPMaxHPCXCY(@Nullable final Ninja ninja, final @NotNull List<@Nullable Ninja> ninjas) {
        if (ninja == null) return;

        val m = Service.messageSubCommand2(18);
        m.writer().writeInt(ninja.id);
        m.writer().writeInt(ninja.hp);
        m.writer().writeInt(ninja.getMaxHP());
        m.writer().writeInt(ninja.x);
        m.writer().writeInt(ninja.y);
        for (Ninja ninja1 : ninjas) {
            if (ninja1 == null) continue;
            ninja1.sendMessage(m);
        }
        m.cleanup();


    }

    /**
     * case 21 bag sort, case 22 boxsort
     * int id char in map_back
     * hp int
     * max hp
     * eff5buffhp
     * eff5buffmp
     *
     */

    /**
     * case 27
     * Send effect skill
     * byte template id
     * int timeStart
     * int timeLength
     * short param
     */

    @SneakyThrows
    public static void sendEffect(final @Nullable Ninja ninja, @NotNull Effect effect, short x, short y) {
        if (ninja == null) return;
        ninja.addEffect(effect);
        val m = Service.messageSubCommand2(27);
        m.writer().writeByte(effect.template.id);
        m.writer().writeInt(effect.timeStart);
        m.writer().writeInt(effect.timeLength);
        m.writer().writeShort(effect.param);

        if (x != -1 && y != -1) {
            m.writer().writeShort(ninja.x);
            m.writer().writeShort(ninja.y);
        }
        ninja.sendMessage(m);
    }

    public static void sendEffect(@Nullable Ninja ninja, @NotNull Effect effect) {
        if (ninja == null) return;
        sendEffect(ninja, effect, (short) -1, (short) -1);
    }

    /**
     * type 11, 12: ẩn thân
     * type 14, bị trói x y
     * type 1: bỏng
     * type 2: đóng bảng
     * type 3: choáng
     *
     * @param ninja
     * @param effect
     * @param x
     * @param y
     */
    @SneakyThrows
    public static void sendEffectToOther(@Nullable final Ninja ninja, @NotNull final Effect effect, @NotNull Collection<@Nullable User> users, int x, int y) {
        if (ninja == null) return;
        val m = Service.messageSubCommand2(30);
        m.writer().writeByte(effect.template.id);
        m.writer().writeInt(effect.timeStart);
        m.writer().writeInt(effect.timeLength);
        m.writer().writeShort(effect.param);

        if (x != -1 && y != -1) {
            m.writer().writeShort(ninja.x);
            m.writer().writeShort(ninja.y);
        }

        for (User ninja1 : users) {
            if (ninja1 == null) continue;
            ninja1.sendMessage(m);
        }
        m.cleanup();

    }

    /**
     * Update effect to area
     *
     * @param ninja
     * @param effect
     * @param ninjas
     */
    @SneakyThrows
    public static void updateEffect(@Nullable final Ninja ninja, @NotNull final Effect effect, @NotNull final List<@Nullable Ninja> ninjas) {
        if (ninja == null) return;
        val m = Service.messageSubCommand2(31);
        m.writer().writeInt(ninja.id);
        m.writer().writeByte(effect.template.id);
        m.writer().writeInt(effect.timeStart);
        m.writer().writeInt(effect.timeLength);
        m.writer().writeInt(effect.param);
        for (Ninja n : ninjas) {
            if (n == null) continue;
            n.sendMessage(m);
        }
        m.cleanup();

    }

    /**
     * Remove effect to area
     * 17 bom máu
     * 0 thưc ăn, 11 tang hinh 12 ẩn thân, nửa giây hồi phục MP
     * 23 hiệu ứng tăng máu
     * 2 hiệu ứng đóng băng,
     * 4, 13: giảm trừ máu, tăng tấn công , 17 bom máu
     */

    @SneakyThrows
    public static void removeEffect(@Nullable final Ninja ninja, @NotNull final Effect effect, @NotNull List<@Nullable Ninja> ninjas) {
        if (ninja == null) return;

        val m = Service.messageSubCommand2(32);
        m.writer().writeInt(ninja.id);
        m.writer().writeByte(effect.template.id);

        if (effect.template.type == 0) {
            m.writer().writeInt(ninja.hp);
            m.writer().writeInt(ninja.mp);
        } else if (effect.template.type == 11) {
            m.writer().writeShort(ninja.x);
            m.writer().writeShort(ninja.y);
        } else if (effect.template.type == 12) {
            m.writer().writeInt(ninja.hp);
            m.writer().writeInt(ninja.mp);
        } else if (effect.template.type == 4 || effect.template.type == 13 || effect.template.type == 17) {
            m.writer().writeInt(ninja.hp);
        } else if (effect.template.type == 23) {
            m.writer().writeInt(ninja.hp);
            m.writer().writeInt(ninja.getMaxHP());
        }

        for (Ninja ninja1 : ninjas) {
            if (ninja1 == null) continue;
            ninja1.sendMessage(m);
        }
        m.cleanup();
    }


    @SneakyThrows
    public static void sendBuNhin(@Nullable BuNhin buNhin, @NotNull Collection<@Nullable User> users) {
        if (buNhin == null) return;
        val m = new Message(75);
        m.writer().writeUTF(buNhin.name);
        m.writer().writeShort(buNhin.x);
        m.writer().writeShort(buNhin.y);
        for (User user : users) {
            if (user == null) continue;
            user.sendMessage(m);
        }
        m.cleanup();
    }

    @SneakyThrows
    public static void removeBuNhin(int i, @NotNull Collection<@Nullable User> users) {
        val m = new Message(77);
        m.writer().writeShort(i);

        for (User user : users) {
            if (user == null) continue;
            user.sendMessage(m);
        }
        m.cleanup();
    }
}
