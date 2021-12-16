package real;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

public class Effect {

    public static final String TEMPLATE_ID = "templateId";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String ICON_ID = "iconId";
    public static final String TEMPLATE = "template";
    public static final String TIME_REMOVE = "timeRemove";
    public static final String PARAM = "param";
    public static final String TIME_LENGTH = "timeLength";
    public static final String TIME_START = "timeStart";
    public static final String TIME_SAVE = "saveTime";


    public int timeStart;
    public int timeLength;
    public int param;
    public long timeRemove;
    public static long A_SECOND = 1000;

    @NotNull
    public EffectData template;

    public Effect(final int id, final int param) {
        this.template = EffectData.entrys.get(id);
        this.param = param;
    }

    Effect() {

    }

    public Effect(final int id, final int timeStart, final int timeLength, final int param) {
        this.template = EffectData.entrys.get(id);
        this.timeStart = timeStart;
        this.timeLength = timeLength;
        this.param = param;
        this.timeRemove = System.currentTimeMillis() - timeStart + timeLength;
    }

    @NotNull
    public Object toJSONObject() {
        JSONObject object = new JSONObject();
        object.put(TIME_START, this.timeStart);
        object.put(TIME_LENGTH, this.timeLength);
        object.put(PARAM, this.param);
        object.put(TIME_REMOVE, this.timeRemove);
        val templateObject = new JSONObject();
        templateObject.put(TEMPLATE_ID, this.template.id);
        templateObject.put(TYPE, this.template.type);
        templateObject.put(NAME, this.template.name);
        templateObject.put(ICON_ID, this.template.iconId);
        object.put(TEMPLATE, templateObject);
        object.put(TIME_SAVE, System.currentTimeMillis());
        return object;
    }

    public static boolean isPermanentEffect(final @NotNull Effect effect) {
        val type = effect.template.type;
        return type == 0 || type == 18 || type == 25 || type == 26  || type == 28;
    }

    public boolean isExpired() {
        return this.getRemainingTimeInSecond() <= 0;
    }

    public int getRemainingTimeInSecond() {
        return (int) ((this.timeRemove - System.currentTimeMillis()) / A_SECOND);
    }

    public static Effect fromJSONObject(final  @NotNull JSONObject jsonObject) {
        Effect object = new Effect();
        val effTemplate = new EffectData();
        object.template = effTemplate;
        JSONObject temp = (JSONObject) jsonObject.get(TEMPLATE);

        effTemplate.id = Byte.parseByte(temp.get(TEMPLATE_ID).toString());
        effTemplate.type = Byte.parseByte(temp.get(TYPE).toString());
        effTemplate.name = temp.get(NAME).toString();
        effTemplate.iconId = Short.parseShort(temp.get(ICON_ID).toString());

        object.template = effTemplate;
        object.param = Integer.parseInt(jsonObject.get(PARAM).toString());
        object.timeStart = Integer.parseInt(jsonObject.get(TIME_START).toString());
        object.timeLength = Integer.parseInt(jsonObject.get(TIME_LENGTH).toString());
        object.timeRemove = Long.parseLong(jsonObject.get(TIME_REMOVE).toString());

        return object;
    }

}
