package candybattle;

import lombok.val;
import patch.Constants;
import interfaces.IGlobalBattler;
import real.Ninja;

import java.util.ArrayList;
import java.util.List;

public class CandyBattleManager {
    private List<CandyBattle> battles;

    public CandyBattleManager() {
        battles = new ArrayList<>();
    }

    public synchronized void enter(IGlobalBattler battler) {
        if (battles.size() == 0) {
            val battle = new CandyBattle();
            battles.add(battle);
            if (!battle.enter(battler, CandyBattle.PHONG_CHO)) {
                battler.changeTypePk(Constants.PK_NORMAL);
            }else{
                ((Ninja) battler).candyBattle = battle;
            }
        } else {
            boolean enter = false;
            for (CandyBattle battle : this.battles) {
                if (!battle.enough()) {
                    if (battle.enter(battler, CandyBattle.PHONG_CHO)) {
                        enter = true;
                        if (battler instanceof Ninja) {
                            ((Ninja) battler).candyBattle = battle;
                        }
                        break;
                    }
                }
            }
            if (!enter) {
                val battle = new CandyBattle();
                battles.add(battle);
                if (!battle.enter(battler, CandyBattle.PHONG_CHO)) {
                    battler.changeTypePk(Constants.PK_NORMAL);
                }else{
                    ((Ninja) battler).candyBattle = battle;
                }
            }
        }
    }

    public synchronized void close() {
        for (CandyBattle battle : this.battles) {
            try {
                battle.close();
            } catch (Exception e) {

            }
        }
        this.battles.clear();
    }

    public synchronized void remove(CandyBattle candyBattle) {
        this.battles.remove(candyBattle);
    }
}
