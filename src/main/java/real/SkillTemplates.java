package real;

import java.util.ArrayList;
import java.util.List;

public class SkillTemplates
{
    public short skillId;
    public byte point;
    public int level;
    public short manaUse;
    public int coolDown;
    public short dx;
    public short dy;
    public byte maxFight;
    public List<Option> options;
    
    public SkillTemplates() {
        this.options = new ArrayList<>();
    }
}
