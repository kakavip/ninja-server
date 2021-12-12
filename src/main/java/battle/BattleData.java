package battle;

import lombok.Getter;
import lombok.Setter;
import patch.Constants;

import java.io.Serializable;

@Getter
@Setter
public class BattleData implements Serializable {

    private int point;
    private short phe;

    public BattleData() {
        point = 0;
        phe = Constants.PK_NORMAL;
    }
}
