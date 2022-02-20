package battlewithmaster;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tournament.Tournament;
import tournament.TournamentPlace;
import real.Body;
import real.Ninja;
import real.User;
import server.Service;
import threading.Map;
import threading.Message;

import java.io.IOException;

public class BattleWithMasterPlace extends TournamentPlace {
    public BattleWithMasterPlace(final @NotNull Map map, byte id) {
        super(map, id);
    }

    @Override
    protected int maxRightBound() {
        return 1140;
    }

    @Override
    protected int minLeftBound() {
        return 780;
    }

    @Override
    public void Enter(final @Nullable User p) throws IOException {
        if (p == null)
            return;
        super.Enter(p);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public boolean canAttackNinja(Body body, Ninja other) {
        return true;
    }

    @Override
    public boolean canAttackNinja(final @Nullable Body body, @Nullable Message m) {
        return true;
    }

    /**
     * @param ninjaAI
     * @param ninjaNorm
     * @return true someone in map die
     */
    @Override
    public boolean handleDie(final @Nullable Ninja ninjaAI, final @Nullable Ninja ninjaNorm) {
        if (ninjaAI == null || ninjaNorm == null)
            return false;
        if (ninjaAI.isDie) {
            ninjaNorm.upMainTask();
            kickToHome(ninjaAI, ninjaNorm, null);
            this.getUsers().clear();
            return true;
        } else if (ninjaNorm.isDie) {
            ninjaNorm.p.sendYellowMessage("Hãy cố gắng thử lại lần nữa");
            kickToHome(ninjaAI, ninjaNorm, null);
            return true;
        }

        return false;
    }

    @SneakyThrows
    @Override
    protected void kickToHome(final @Nullable Ninja ninjaAI, final @Nullable Ninja ninjaNorm,
            final @Nullable Tournament tour) {
        if (ninjaNorm == null || ninjaAI == null)
            return;
        ninjaNorm.getPlace().gotoHaruna(ninjaNorm.p);
        getUsers().clear();
        Service.batDauTinhGio(ninjaNorm.p, 0);
        this.ninjaAI = null;
        this.norm = null;
    }
}
