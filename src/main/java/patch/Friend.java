package patch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Friend implements Serializable {
    private String name;
    private Boolean agree;

    public Friend(String name, Boolean agree) {
        this.name = name;
        this.agree = agree;
    }

    public Friend(){

    }
}
