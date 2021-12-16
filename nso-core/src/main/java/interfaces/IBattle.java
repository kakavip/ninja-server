package interfaces;


import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IBattle extends Expireable, UpdateEvent {

    /**
     * Chien truong
     */
    byte CAN_CU_DIA_BACH = 98;
    byte BACH_DAI_ID = 99;
    byte HANH_LANG_GIUA = 100;
    byte HANH_LANG_TREN = 101;
    byte HANH_LANG_DUOI = 102;
    byte HAC_DAI_ID = 103;
    byte CAN_CU_DIA_HAC = 104;

    /**
     * Gia tộc chiến
     */

    byte KHU_BAO_DANH = 117;
    byte BAO_DANH_GT_BACH = 118;
    byte BAO_DANH_GT_HAC = 119;
    byte SANH_1 = 120;
    byte HANH_LANG_1 = 121;
    byte HANH_LANG_2 = 122;
    byte HANH_LANG_3 = 123;
    byte SANH_2 = 124;

    /**
     * Chien truong keo
     *
     */


    byte INITIAL_STATE = -1;
    byte WAITING_STATE = 0;
    byte START_STATE = 1;
    byte END_STATE = 3;
    byte DAT_CUOC_STATE = 4;


    long MAX_TIME_DURATION = 3600000L;


    int[] idBachMobs = new int[]{
            97, 98,
    };
int[] idHacMobs = new int[]{
            96, 99
    };


    boolean isExpired();

    @NotNull
    List<@Nullable IGlobalBattler> getTopBattlers();

    @NotNull
    short[] getRewards(final @NotNull IGlobalBattler battler);

    void reset();

    void start();

    void close();

    @NotNull
    String getResult(@NotNull final IGlobalBattler battler);


    int getState();

    void updateBattler(IGlobalBattler battler, boolean isHuman, Object other);

    long getTimeInSeconds();

    @SneakyThrows
    boolean enter(IGlobalBattler member, int type);

    void setState(byte state);

}
