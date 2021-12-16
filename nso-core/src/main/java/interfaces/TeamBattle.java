package interfaces;

import boardGame.Place;
import org.jetbrains.annotations.NotNull;
import threading.Message;
import real.Battle;
import real.Effect;
import real.Ninja;

import java.util.List;

public interface TeamBattle extends SendMessage {

    int MASTER_SINGLE = -100;

    void enterSamePlace(Place place, TeamBattle other);

    void changeTypePk(short typePk, TeamBattle notifier);

    void notifyMessage(String message);

    void upXuMessage(long xu);

    int getMaster();

    void sendMessage(Message message);

    @NotNull
    List<@NotNull Ninja> getNinjas();

    Battle getBattle();

    void setBattle(Battle battle);

    void updateEffect(Effect effect);

    int getCurrentMapId();

    @NotNull
    String getTeamName();

    boolean hasBattle();

    default void clearBattle() {
        this.setBattle(null);
    }

    boolean loose();

    int getMapId();

    short getKeyLevel();

    int getXu();
}
