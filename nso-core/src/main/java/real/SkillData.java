package real;

import lombok.val;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SkillData {
    public int id;
    public byte nclass;
    public String name;
    public byte maxPoint;
    public byte type;
    public short iconId;
    public String desc;
    public ArrayList<SkillTemplates> templates;
    public static Map<Integer, SkillData> entrys;

    public SkillData() {
        this.name = null;
        this.templates = new ArrayList<>();
    }

    public static SkillTemplates Templates(final int id, final int point) {
        val temp = entrys.get(id);
        for (final SkillTemplates data : temp.templates) {
            if (data.point == point) {
                return data;
            }
        }
        return null;
    }

    public static SkillData Templates(final int id) {
        return entrys.get(id);
    }

    public static JSONObject ObjectSkill(final Skill skill) {
        final JSONObject put = new JSONObject();
        put.put("id", skill.id);
        put.put("point", skill.point);
        return put;
    }

    static {
        SkillData.entrys = new HashMap<>();
    }
}
