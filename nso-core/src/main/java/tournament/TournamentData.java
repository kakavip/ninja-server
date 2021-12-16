package tournament;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TournamentData implements Serializable {

    private String name;
    private Integer ranked;
    private String status;
    private int ninjaID;
    private boolean canGoNext = true;

    public TournamentData() {
    }

    public TournamentData(String name, int ranked) {
        this.name = name;
        this.ranked = ranked;
        this.status = "Có thể thách đấu";
    }

    public void setCanGoNext(boolean canGoNext) {
        this.canGoNext = canGoNext;
    }
}
