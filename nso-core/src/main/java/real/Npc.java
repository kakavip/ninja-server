package real;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Npc {

    public byte type;
    public short x;
    public short y;
    public byte id;
    public short head;
    public short body;
    public short leg;
    public String name;

    public Npc() {
    }

    @NotNull
    public static final Map<@NotNull Byte,@NotNull Npc> npcTemplates = new HashMap<>();
}
