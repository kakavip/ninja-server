package interfaces;

import real.Ninja;
import real.User;

@FunctionalInterface
public interface UpdateEvent {
    void update(Ninja nj);
}
