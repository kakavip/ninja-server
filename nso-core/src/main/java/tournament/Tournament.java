package tournament;

import boardGame.Place;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import patch.Constants;
import patch.Mapper;
import real.*;
import server.SQLManager;
import server.util;
import threading.Map;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static tournament.Tournament.RegisterResult.ALREADY_REGISTER;
import static tournament.Tournament.RegisterResult.SUCCESS;

public abstract class Tournament {

    @Nullable
    public abstract Ninja findNinjaById(int ninjaId);

    public enum RegisterResult {
        SUCCESS,
        ALREADY_REGISTER,
        LOSE,
        DEFAULT
    }

    protected int tournamentId;
    @NotNull
    protected final List<@NotNull User> participants;
    @NotNull
    public final Map map;
    public final int maxArea;
    public static long lastTimeReward = -1;

    @Nullable
    public static Ninja findNinjaGById(int ninjaId) {
        final Ninja ninjaById = GeninTournament.gi().findNinjaById(ninjaId);
        if (ninjaById == null) {
            return KageTournament.gi().findNinjaById(ninjaId);
        } else {
            return ninjaById;
        }
    }

    protected Tournament(int maxArea, int id) {
        this.participants = new CopyOnWriteArrayList<>();
        this.maxArea = maxArea;
        map = new TournamentMap(111, null, maxArea);
        this.tournamentId = id;
        loadTournamentFromDb();

    }

    public synchronized List<TournamentData> getTopTen() {
        return participants.stream()
                .sorted(Comparator.comparingInt(n -> n.nj.getTournamentData().getRanked()))
                .map(p -> p.nj.getTournamentData())
                .limit(10)
                .collect(Collectors.toList());
    }

    @NotNull
    public User getUserByNinjaName(String playerName) {
        return Objects.requireNonNull(this.participants.stream()
                .filter(p -> p != null && p.nj != null && p.nj.name != null && p.nj.name.equals(playerName)).findFirst()
                .orElse(null));
    }

    public synchronized void updateRanked(final @NotNull Ninja norm, final @NotNull Ninja ai, boolean isWin) {
        synchronized (this.participants) {
            TournamentData normData = null;
            TournamentData aiData = null;
            // Find AI data
            for (User participant : this.participants) {
                if (participant.nj.equals(ai)) {
                    aiData = participant.nj.getTournamentData();
                    break;
                }
            }
            // Find Norm data
            for (User participant : this.participants) {
                if (participant.nj.equals(norm)) {
                    normData = participant.nj.getTournamentData();
                    break;
                }
            }

            if (aiData != null && normData != null) {
                if (isWin) {

                    if (aiData.getRanked() < normData.getRanked()) {
                        norm.p.sendYellowMessage(
                                "Bạn đã chiến thắng " + ai.name + " và tranh được hạng " + aiData.getRanked());
                        // Cuop rank
                        if (aiData.getRanked() == 1) {
                            try {
                                this.notifyKTG(norm.name);
                            } catch (Exception e) {
                            }
                        }

                        final Integer temp = aiData.getRanked();
                        aiData.setRanked(normData.getRanked());
                        normData.setRanked(temp);
                    } else {
                        // Khong lam gif
                        norm.p.sendYellowMessage("Bạn đã chiến thắng " + ai.name);
                    }

                } else {
                    normData.setCanGoNext(false);
                    norm.p.sendYellowMessage("Bạn đã thua " + ai.name);

                }
            }

            sorting();
        }

    }

    public void sorting() {
        synchronized (this.participants) {
            for (int i = 0; i < participants.size(); i++) {
                User participant = participants.get(i);
                final Integer currentRank = participant.nj.getTournamentData().getRanked();

                List<User> users = this.participants;
                for (int j = i + 1; j < users.size(); j++) {
                    User user = users.get(j);
                    final Integer otherRanked = user.nj.getTournamentData().getRanked();
                    if (currentRank.equals(otherRanked)) {
                        user.nj.getTournamentData().setRanked(currentRank + 1);
                    }
                }
            }
        }
    }

    /**
     * @param u
     * @return true if register success
     */
    public RegisterResult register(final @Nullable User u) {
        if (u == null)
            return RegisterResult.DEFAULT;

        synchronized (this.participants) {
            User user = participants.stream()
                    .filter(p -> p != null && p.nj != null &&
                            p.nj.id == u.nj.id)
                    .findFirst().orElse(null);
            if (user == null) {
                TournamentData data;

                data = new TournamentData(u.nj.name, getNextRank());
                user = createUser(data);
                user.nj.setTournamentData(data);
                u.nj.setTournamentData(data);
                this.participants.add(user);
                flush();

                return SUCCESS;
            } else {
                restoreNinjaTournament(u.nj);
                return ALREADY_REGISTER;
            }
        }
    }

    public static Tournament getTypeTournament(int level) {
        if (level <= 80) {
            return GeninTournament.gi();
        }
        return KageTournament.gi();
    }

    @NotNull
    public synchronized List<@NotNull TournamentData> getChallenges(User myAccount) {
        return this.participants
                .stream()
                .filter(p -> !p.nj.isBusy)
                .map(u -> u.nj.getTournamentData())
                .filter(r -> {
                    if (myAccount.nj.getTournamentData().getRanked() > 4) {
                        val minRank = myAccount.nj.getTournamentData().getRanked() - 10;
                        val maxRank = myAccount.nj.getTournamentData().getRanked();
                        return r.getRanked() >= minRank && r.getRanked() <= maxRank;
                    } else {
                        return r.getRanked() <= 5;
                    }
                })
                .limit(10)
                .sorted((u1, u2) -> -u2.getRanked() + u1.getRanked())
                .collect(Collectors.toList());
    }

    public void restoreNinjaTournament(final @NotNull Ninja nj) {
        this.getTournaments().stream()
                .filter(t -> t.getName().equals(nj.name))
                .findFirst()
                .ifPresent(nj::setTournamentData);
    }

    public void loadTournamentFromDb() {
        synchronized (this.participants) {
            this.participants.clear();
            SQLManager.executeQuery("SELECT * from `tournament`where id = " + this.tournamentId, red -> {

                while (red.next()) {
                    val tournaments = Mapper.converter.readValue(red.getString("tournaments"),
                            new TypeReference<List<TournamentData>>() {
                            });
                    tournaments.stream().forEach(tournament -> {
                        final User user = createUser(tournament);
                        if (user.nj != null && user.nj.name != null) {
                            this.participants.add(user);
                        }
                    });
                }

                red.close();
            });

            sorting();
        }
    }

    public int getNextRank() {
        synchronized (this.participants) {
            return this.participants.stream()
                    .max(Comparator.comparingInt(p -> p.nj.getTournamentData()
                            .getRanked()))
                    .map(p -> p.nj.getTournamentData().getRanked()).orElse(0) + 1;
        }
    }

    @NotNull
    private User createUser(final @NotNull TournamentData tournament) {
        final User user = new User();
        final Ninja ninja = Ninja.getNinja(tournament.getName());
        ninja.clone = CloneChar.getClone(ninja);
        user.username = "";
        user.nj = ninja;
        ninja.p = user;
        ninja.setTournamentData(tournament);
        return user;
    }

    @SneakyThrows
    public void reset() {
        synchronized (this.participants) {
            for (User participant : this.participants) {
                final Ninja ninja = PlayerManager.getInstance().getNinja(participant.nj.name);
                if (ninja != null) {
                    ninja.setTournamentData(null);
                }
            }

            this.participants.clear();

            flush();
        }
    }

    public void flush() {
        try {
            SQLManager.executeUpdate("UPDATE `tournament` set tournaments = '"
                    + Mapper.converter.writeValueAsString(getTournaments()) + "' where id=" + tournamentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<TournamentData> getTournaments() {
        synchronized (this.participants) {
            return this.participants.stream().map(p -> p.nj.getTournamentData()).collect(Collectors.toList());
        }
    }

    public abstract int[][] getRewardItems();

    public abstract void notifyKTG(String name) throws IOException;

    public boolean checkBusy(String ninjaName) {
        synchronized (this.participants) {

            for (User participant : this.participants) {
                if (participant.nj != null && participant.nj.name != null && participant.nj.name.equals(ninjaName)) {
                    return participant.nj.isBusy;
                }
            }
        }
        return false;
    }

    /**
     * @param ninja       real entity
     * @param ninjaAIName not real entity
     */

    public void enter(final @Nullable Ninja ninja, final @NotNull String ninjaAIName) {
        if (ninja == null)
            return;
        synchronized (this.participants) {
            if (ninja.party != null) {
                ninja.p.sendYellowMessage("Bạn chỉ có thể tham gia một mình");
                return;
            }
            val ninjaAI = this.participants.stream()
                    .filter(p -> p.nj.name != null && p.nj.name.equals(ninjaAIName))
                    .map(u -> u.nj).findFirst().orElse(null);
            final Place area = map.getFreeArea();
            if (ninjaAI == null)
                return;

            ninjaAI.upHP(ninjaAI.getMaxHP());
            ninjaAI.upMP(ninjaAI.getMaxMP());

            ninjaAI.isDie = false;

            ninja.changeTypePk(Constants.PK_TRANG);
            ninjaAI.changeTypePk(Constants.PK_DEN);
            if (ninjaAI.clone != null) {
                ninjaAI.clone.open(util.TimeMinutes(20), ninjaAI.getPramSkill(71));
            }
            ninjaAI.isBusy = true;

            for (User participant : this.participants) {
                if (participant.nj.name != null && participant.nj.name.equals(ninja.name)) {
                    participant.nj.isBusy = true;
                    break;
                }
            }

            ninja.enterSamePlace(area, ninjaAI);
        }
    }

    @SneakyThrows
    public void leave(final @Nullable Ninja ninja, final @Nullable Ninja ninjaAI) {
        if (ninja == null || ninjaAI == null)
            return;
        synchronized (this.participants) {
            try {
                ninja.changeTypePk(Constants.PK_NORMAL);
            } finally {
                ninja.getPlace().DieReturn(ninja.p);
                ninja.getPlace().removeUser(ninjaAI.p);
                ninjaAI.isBusy = false;
                ninjaAI.isDie = false;
                for (User participant : this.participants) {
                    if (participant.nj.name.equals(ninja.name)) {
                        participant.nj.isBusy = false;
                        break;
                    }
                }
                ninjaAI.upHP(ninjaAI.getMaxHP());
                ninjaAI.upMP(ninjaAI.getMaxMP());
            }
        }

    }

    public void rewardNinja() {
        loadTournamentFromDb();
        synchronized (this.participants) {
            for (User participant : this.participants) {
                int rank = participant.nj.getTournamentData().getRanked();
                if (rank >= 1 && rank <= 10) {
                    final Ninja ninja = PlayerManager.getInstance().getNinja(participant.nj.name);
                    if (ninja != null) {
                        participant.nj = ninja;
                        ninja.setTournamentRank(this, rank);
                    } else {
                        Ninja.setTournamentRankInDB(this, participant.nj.name, rank);
                    }
                }
            }
        }
    }

    public boolean rewardNinja(Ninja nj) {
        int[][] rewards = this.getRewardItems();

        byte nullbag = nj.getAvailableBag();
        if (nullbag < rewards.length) {
            return false;
        }

        int rank = nj.getTournamentRank(this);
        if (!(rank >= 1 && rank <= 10)) {
            return false;
        }
        for (int[] reward : rewards) {
            if (reward[0] == 12) {
                nj.upyenMessage((long) (11 - rank) * reward[1]);
                break;
            }

            Item item = ItemData.itemDefault(reward[0]);
            item.quantity = (11 - rank) * reward[1];
            if (item.getData().type == 26) {
                nj.addItemBag(false, item);
            } else {
                nj.addItemBag(true, item);
            }
        }

        nj.resetTournamentRank(this);
        return true;

    }

    @SneakyThrows
    public void close() {
        this.flush();
        synchronized (this.participants) {
            this.participants.clear();
            this.map.close();
        }

    }

    public synchronized static void closeAll() {
        KageTournament.gi().close();
        GeninTournament.gi().close();
    }
}
