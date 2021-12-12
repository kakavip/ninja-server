package tournament;

import real.Cave;
import real.MapTemplate;
import threading.Map;
import threading.Server;

import java.util.HashMap;

import static interfaces.IBattle.*;

public class TournamentMap extends Map {
    public TournamentMap(int id, Cave cave, int maxArea) {
        this.timeMap = -1L;
        this.id = id;
        this.template = MapTemplate.arrTemplate[id];
        this.cave = cave;
        this.area = new TournamentPlace[maxArea];
        this.levelToMobId = new HashMap<>();

        for (byte i = 0; i < maxArea; ++i) {
            this.area[i] = new TournamentPlace(this, i);

            if (id == CAN_CU_DIA_BACH ||
                    id == CAN_CU_DIA_HAC ||
                    id == BACH_DAI_ID ||
                    id == HAC_DAI_ID ||
                    id == HANH_LANG_TREN ||
                    id == HANH_LANG_DUOI ||
                    id == HANH_LANG_GIUA
            ) {
                this.area[i].battle = Server.getInstance().globalBattle;
            }
        }
        setName(template.name);

        this.initMob();
        this.runing = true;
        lastTimeActive = System.currentTimeMillis();
        this.start();
    }

}
