package app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Riky on 2016/4/10.
 */
@Data
public class AccountData {
    private  String title;
    private  String yieldUrl;
    @JsonProperty(value = "BackgroundUrl")
    public String backgroundUrl;


}
