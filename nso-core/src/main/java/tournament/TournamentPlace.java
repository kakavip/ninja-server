package tournament;

import boardGame.Place;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import patch.MessageSubCommand;
import real.Body;
import real.Effect;
import real.Ninja;
import real.User;
import server.Service;
import server.util;
import threading.Map;
import threading.Message;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TournamentPlace extends Place {

    public TournamentPlace(Map map, byte id) {
        super(map, id);
    }

    @Override
    public void leave(User p) {
        if (p.session == null)
            return;
        super.leave(p);
    }

    @Override
    public void Enter(User p) throws IOException {

        p.nj.upHP(p.nj.getMaxHP());
        p.nj.upMP(p.nj.getMaxMP());
        MessageSubCommand.sendHP(p.nj, this.getUsers());
        MessageSubCommand.sendMP(p.nj, this.getUsers());

        if (state == State.INITIAL || state == State.WAIT_10_SECS) {
            tick = System.currentTimeMillis();
            state = State.WAIT_10_SECS;
            p.nj.updateEffect(new Effect(14, 0, 10000, 0));
        }
        if (this.getUsers().size() >= 2)
            return;
        Place.Enter(p, this);
        Service.batDauTinhGio(p.nj, 10);
    }

    @Override
    public void wakeUpDieReturn(User p) {
        return;
    }

    private enum State {
        WAIT_10_SECS,
        KICK_10_MIN,
        INITIAL
    }

    private State state = State.INITIAL;

    @Override
    public void update() {
        synchronized (this) {
            if (state == State.WAIT_10_SECS) {
                if (System.currentTimeMillis() - tick > $10_SEC) {
                    tick = System.currentTimeMillis();
                    state = State.KICK_10_MIN;
                    Service.batDauTinhGio(findNormal(), $10MIN / 1000);
                }
            } else if (state == State.KICK_10_MIN) {
                if (System.currentTimeMillis() - tick > $10MIN) {
                    try {
                        state = State.INITIAL;
                        tick = -1;
                        final Tournament tour = Tournament.getTypeTournament(ninjaAI.getLevel());
                        tour.leave(norm, ninjaAI);
                        this.ninjaAI = null;
                        this.norm = null;
                        this.getUsers().clear();

                    } catch (Exception e) {

                    } finally {
                        this.getUsers().clear();
                    }
                }
            }

            try {
                super.update();
                updateAI();
            } catch (Exception e) {

            }
        }
    }

    @Nullable
    protected Ninja ninjaAI;
    @Nullable
    protected Ninja norm;

    private Ninja findAI() {
        synchronized (this) {
            if (ninjaAI == null) {
                ninjaAI = getUsers()
                        .stream().filter(u -> u.session == null)
                        .map(p -> p.nj)
                        .findFirst().orElse(null);
            }
        }
        return ninjaAI;
    }

    private Ninja findNormal() {
        synchronized (this) {
            if (norm == null)
                norm = getUsers()
                        .stream().filter(u -> u.session != null)
                        .map(p -> p.nj)
                        .findFirst()
                        .orElse(null);
        }
        return norm;
    }

    public static int TIME_CONTROL_MOVE = 1000;

    protected int maxRightBound() {
        return 661;
    }

    protected int minLeftBound() {
        return 0;
    }

    private void updateAI() {

        try {
            // AI
            final Ninja ninjaAI = findAI();
            final Ninja ninjaNorm = findNormal();
            if (ninjaAI == null || ninjaNorm == null)
                return;

            if (handleDie(ninjaAI, ninjaNorm))
                return;

            val currentTime = System.currentTimeMillis();
            if (ninjaAI.lastTimeMove == -1 || currentTime - ninjaAI.lastTimeMove >= util
                    .nextInt(20 * TIME_CONTROL_MOVE / 100, TIME_CONTROL_MOVE)) {
                val normalX = ninjaNorm.x;
                boolean negative = util.nextInt(0, 1) == 1;
                val randomX = util.nextInt(ninjaAI.getMyCSkillObject().getTemplate().dx + 60);
                short aiX = (short) (normalX + (negative ? -randomX : randomX));

                if (aiX <= minLeftBound()) {
                    aiX = (short) (minLeftBound() + 20);
                } else if (aiX > maxRightBound()) {
                    aiX = (short) (maxRightBound() - 30);
                }

                if (!ninjaAI.isDie) {
                    moveMessage(ninjaAI, aiX, ninjaAI.y);
                }
            }

            val data = ByteBuffer.allocate(4).putInt(ninjaNorm.id).array();
            val message = new Message((byte) 61, data);
            val cloneMessage = new Message((byte) 61, data);
            ninjaAI.upMP(ninjaAI.getMaxMP());
            try {
                if (!ninjaAI.isDie) {
                    attackNinja(ninjaAI, cloneMessage);
                    if (ninjaAI.clone != null && ninjaAI.clone.isIslive()) {
                        attackNinja(ninjaAI.clone, message);
                    }
                    ninjaAI.lastTimeMove = currentTime;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                message.cleanup();
            }
        } catch (Exception e) {

        }
    }

    public boolean handleDie(Ninja ninjaAI, Ninja ninjaNorm) {
        if (ninjaAI.isDie) {
            final Tournament tour = Tournament.getTypeTournament(ninjaAI.getLevel());
            tour.updateRanked(ninjaNorm, ninjaAI, true);
            kickToHome(ninjaAI, ninjaNorm, tour);
            this.getUsers().clear();
            return true;
        } else if (ninjaNorm.isDie) {
            final Tournament tour = Tournament.getTypeTournament(ninjaNorm.getLevel());
            tour.updateRanked(ninjaNorm, ninjaAI, false);
            kickToHome(ninjaAI, ninjaNorm, tour);
            this.getUsers().clear();
            return true;
        }
        return false;
    }

    protected void kickToHome(@Nullable final Ninja ninjaAI, @Nullable final Ninja ninjaNorm,
            @Nullable final Tournament tour) {
        if (tour == null || ninjaAI == null || ninjaNorm == null) {
            if (ninjaNorm != null) {
                this.leave(ninjaNorm.p);
            }
            getUsers().clear();
            return;
        }
        this.ninjaAI = null;
        this.norm = null;
        this.state = State.INITIAL;
        this.tick = -1;
        Service.batDauTinhGio(ninjaNorm.p, 0);
        tour.leave(ninjaNorm, ninjaAI);
    }

    @Override
    public void attackNinja(@Nullable final Body body, @Nullable final Message m) throws IOException {
        if (body == null || m == null)
            return;
        if (state != State.INITIAL && state != State.WAIT_10_SECS) {
            super.attackNinja(body, m);
        }
    }

    @Override
    protected boolean resetPlaceIfInBattle(final User p) {
        return false;
    }

    private final int $10MIN = 600_000;
    private final int $10_SEC = 10_000;

    private long tick = -1;

    @Override
    public void removeUser(final @Nullable User p) {
        super.removeUser(p);
        if (getUsers().size() == 0) {
            tick = -1;
            state = State.INITIAL;
        }
    }

    @Override
    protected void FireNinjaMessage(final int ninjaId, final int type) {
        try {
            Ninja ninja = Tournament.findNinjaGById(ninjaId);
            if (ninja == null)
                return;
            long reduceTime = 0;
            try {
                reduceTime = ninja.get().getPramSkill(37) * 100L + ninja.get().getFireReduceTime();
            } catch (Exception e) {

            }
            long time = 0;
            switch (type) {
                case -1: {
                    ninja.isFire = false;
                    break;
                }
                case 0: {
                    time = 2000L - reduceTime;
                    break;
                }
                case 1: {
                    time = 4000 - reduceTime;
                    break;
                }
                case 2: {
                    time = 5000 - reduceTime;
                    break;
                }
            }

            if (time > 0) {
                ninja.isFire = true;
                ninja.timeFire = System.currentTimeMillis() + time;
                ninja.p.setEffect(5, 0, (int) time, 10);
                MessageSubCommand.sendEffectToOther(ninja, ninja.getEffId(5), this.getUsers(), -1, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void IceNinjaMessage(final int ninjaId, final int type) {
        try {
            Ninja ninja = Tournament.findNinjaGById(ninjaId);
            if (ninja == null)
                return;
            long reduceIceTime = 0;

            try {
                reduceIceTime = ninja.get().getPramSkill(38) * 100L + ninja.get().getIceReduceTime();
            } catch (Exception e) {

            }
            long time = 0;

            switch (type) {
                case -1: {
                    ninja.isIce = false;
                    break;
                }
                case 0: {
                    time = 2000L - reduceIceTime;
                    break;
                }
                case 1: {
                    time = 3000L - reduceIceTime;
                    break;
                }
                case 2: {
                    time = 2000 - reduceIceTime;
                    break;
                }
                case 3: {
                    time = 5000 - reduceIceTime;
                    break;
                }
            }

            if (time > 0) {
                ninja.isIce = true;
                ninja.timeIce = System.currentTimeMillis() + time;
                ninja.p.setEffect(6, 0, (int) time, 10);
                MessageSubCommand.sendEffectToOther(ninja, ninja.getEffId(6), this.getUsers(), -1, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void WindNinjaMessage(final int ninjaId, final int type) {
        try {
            Ninja ninja = Tournament.findNinjaGById(ninjaId);
            if (ninja == null)
                return;
            long reduceTime = 0;
            try {
                reduceTime = ninja.get().getPramSkill(39) * 100L + ninja.get().getWindReduceTime();
            } catch (Exception e) {

            }
            long time = 0;

            switch (type) {
                case -1: {
                    ninja.isWind = false;
                    break;
                }
                case 0: {
                    time = 1000L - reduceTime;

                    break;
                }
                case 1: {
                    time = 2000 - reduceTime;
                    break;
                }
                case 3: {
                    time = 5000 - reduceTime;
                    break;
                }
            }

            if (time > 0) {
                ninja.isWind = true;
                ninja.timeWind = System.currentTimeMillis() + time;
                ninja.p.setEffect(7, 0, (int) time, 10);
                MessageSubCommand.sendEffectToOther(ninja, ninja.getEffId(7), this.getUsers(), -1, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
