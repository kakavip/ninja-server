package patch;

import boardGame.Place;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import interfaces.UpdateEvent;
import threading.Message;
import lombok.SneakyThrows;
import lombok.val;
import interfaces.ISolo;
import real.Body;
import real.Ninja;

public class Solo implements ISolo {

    @Nullable
    private Body body1;
    @Nullable
    private Body body2;

    int checkWin = 0;
    public long startTime;

    // 30 seconds
    public static final long SOLO_DURATION = 60 * 1000;

    private boolean matching = false;
    private UpdateEvent runnable;
    private Place place;
    @SneakyThrows
    @Override
    public void showTiThi() {
        if (body1 == null || body2 == null) {
            throw new RuntimeException("Body 1 and body 2 not set");
        }
        val ms = new Message(66);
        val ds = ms.writer();
        ds.writeInt(body1.id);
        ds.flush();

        if (body1.c.getPlace() != null) {
            body1.c.getPlace().sendMyMessage(body1.c.p, ms);
        }
        ds.writeInt(body2.id);
        ds.flush();
        if (body2.c.getPlace() != null) {
            body2.c.getPlace().sendMyMessage(body2.c.p, ms);
        }
        ms.cleanup();
    }


    @Override
    public UpdateEvent getRunnable() {
        if (this.runnable == null) {
            this.runnable = (u) -> {
                if (this.isExpired()) {
                    this.endSolo();
                } else {
                    if (body1 == null || body2 == null) return;
                    if (body1.typeSolo == 1 && body2.typeSolo == 1) {
                        if (body2.isDie || body2.hp <= 0.1 * body2.getMaxHP()) {
                            updateLoser(body2);
                        } else if (body1.isDie || body1.hp <= 0.1 * body1.getMaxHP()) {
                            updateLoser(body1);
                        }
                    } else {
                        endSolo();
                    }
                }
            };
        }
        return this.runnable;
    }


    @Override
    public void setBody(final @NotNull  Body body1,final @NotNull Body body2) {
        this.body1 = body1;
        this.body2 = body2;
        this.body1.setSolo(this);
        this.body2.setSolo(this);
        this.place = (this.body1.c.getPlace());

    }


    public void start() {
        if (!this.matching) {
            this.tick();
            matching = true;
            if (body1 != null) {
                body1.typeSolo = 1;
            }
            if (body2 != null) {
                body2.typeSolo = 1;
            }
            showTiThi();
            if (((Ninja) body1).getPlace() != null) {
                ((Ninja) body1).getPlace().addRunner(this.getRunnable());
            }
        } else {
            if (body1 != null) {
                body1.c.p.sendYellowMessage("Trận đấu đã bắt đầu");
            }
            if (body2 != null) {
                body2.c.p.sendYellowMessage("Trận đấu đã bắt đầu");
            }
        }
    }

    @Override
    public void tick() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isExpired() {
        try {
            val expiredTime = System.currentTimeMillis() - this.startTime >= Solo.SOLO_DURATION;
            boolean diffPlace = false;
            if (body2 != null) {
                if (body1 != null) {
                    diffPlace = body1.c.getPlace() != body2.c.getPlace();
                }
            }
            return expiredTime || diffPlace || !this.matching;
        } catch (Exception e) {
            return true;
        }
    }

    @SneakyThrows
    private void updateLoser(@Nullable final Body body) {
        if (body == null) return;
        if (body == body2) {
            checkWin = 1;
        } else {
            checkWin = 2;
        }
        Thread.sleep(200L);
        body.isDie = false;
        if (body2 != null) {
            body.hp = body2.getMaxHP();
        }
        body.mp = body2.getMaxMP();

        val m = new Message(-10);
        body.c.sendMessage(m);
        m.cleanup();

        val m2 = new Message(88);
        m2.writer().writeInt(body.id);
        m2.writer().writeInt(body.x);
        m2.writer().writeInt(body.y);
        m2.writer().flush();
        m2.cleanup();

        if (body.c.getPlace() != null) {
            body.c.getPlace().sendMyMessage(body.c.p, m2);
        }
        endSolo();
    }

    @SneakyThrows
    @Override
    public void endSolo() {
        try {

            if (body1 == null || body2 == null) return;

            body1.typeSolo = 0;
            body2.typeSolo = 0;

            val ms = new Message(67);
            val ds = ms.writer();
            if (checkWin == 2) {
                ds.writeInt(body1.id);
                ds.writeInt(body2.id);
                ds.writeInt(body2.hp);
            } else if (checkWin == 1) {
                ds.writeInt(body2.id);
                ds.writeInt(body1.id);
                ds.writeInt(body1.hp);
            } else {
                ds.writeInt(body2.id);
                ds.writeInt(body1.id);


            }

            ds.flush();
            if (body1.c.getPlace() != null) {
                body1.c.getPlace().sendMessage(ms);
            }
            if (body2.c.getPlace() != null) {
                body2.c.getPlace().sendMessage(ms);
            }
            ms.cleanup();
        } catch (Exception e) {
            System.out.println("END SOLO");
        } finally {
            if (this.body1 != null) {
                this.body1.setSolo(null);
            }
            if (this.body2 != null) {
                this.body2.setSolo(null);
            }
            this.matching = false;
            this.body1 = this.body2 = null;
            place.removeRunner(this.runnable);
        }
    }

}
