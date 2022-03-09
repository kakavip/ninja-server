package event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventData implements Serializable {
    private Map<String, Integer> annual;

    public EventData() {
        annual = new HashMap<String, Integer>();
    }
}
