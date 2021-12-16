package real;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class Option implements Serializable
{
    public int id;
    public int param;
    
    public Option(final int id, final int par) {
        this.id = id;
        this.param = par;
    }

    public Option() {
    }

    @Override
    public boolean equals(Object  o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return id == option.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
