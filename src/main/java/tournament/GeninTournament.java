package tournament;

import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import patch.Mapper;
import real.Ninja;
import threading.Server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class GeninTournament extends Tournament {
    public int[][] GENIN_REWARDS;

    protected GeninTournament() {
        super(100, 1);
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream("application.properties")) {
            properties.load(inputStream);
            GENIN_REWARDS = Mapper.converter.readValue(properties.getProperty("reward-dia-bang"), new TypeReference<int[][]>() {
            });
        } catch (Exception e) {
            Server.getInstance().stop();
            System.out.println("TOURNAMENT ERR");
        }
    }

    private static volatile Tournament instance = null;

    public static Tournament gi() {
        if (instance == null) {
            synchronized (GeninTournament.class) {
                if (instance == null) {
                    instance = new GeninTournament();
                }
            }
        }
        return instance;
    }

    @Override
    public @NotNull Ninja findNinjaById(int ninjaId) {
        return this.participants.stream().filter(p-> p.nj.id == ninjaId).map(p-> p.nj).findFirst().orElse(null);
    }

    @Override
    public int[][] getRewardItems() {
        return GENIN_REWARDS;
    }
}
