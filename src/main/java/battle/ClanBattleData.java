package battle;

import lombok.*;
import real.Battle;

import java.io.Serializable;
import java.util.List;

import static battle.ClanBattle.*;
import static interfaces.IBattle.WAITING_STATE;

@Getter
@Setter
@Builder
public class ClanBattleData implements Serializable {

    private int finalXu;
    private int tocTruongId1;
    private int tocTruongId2;

    private long tick;
    private byte state;

    private List<Integer> bachGiaIds;
    private List<Integer> hacGiaIds;

    public ClanBattleData() {

    }

    public ClanBattleData(int finalXu, int tocTruongId1, int tocTruongId2, long tick, byte state, List<Integer> bachGiaIds, List<Integer> hacGiaIds) {
        this.finalXu = finalXu;
        this.tocTruongId1 = tocTruongId1;
        this.tocTruongId2 = tocTruongId2;
        this.tick = tick;
        this.state = state;

        this.bachGiaIds = bachGiaIds;
        this.hacGiaIds = hacGiaIds;
    }


    public boolean isExpired() {
        long time = -1;
        if (state == WAITING_STATE) {
            time = WATING_TIME;
        } else if (state == START_STATE) {
            time = WATING_TIME;
        } else if (state == END_STATE) {
            time = -1;
        } else if (state == DAT_CUOC_STATE) {
            time = Battle.MATCHING_WAIT_FOR_INFORMATION;
        }

        return System.currentTimeMillis() - tick > time;
    }
}
