package real;

public class Skill {

    public byte id;
    public byte point;
    public long coolDown;
    public byte type;

    public Skill() {
    }

    public Skill(int id) {
        this.id = (byte) id;
    }

    public SkillTemplates getTemplate() {
        return SkillData.Templates(id, point);
    }

    public SkillData skillData() {
        return SkillData.Templates(id);
    }
}
