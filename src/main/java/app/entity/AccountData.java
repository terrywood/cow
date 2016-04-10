package app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

/**
 * Created by Riky on 2016/4/10.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountData {
    private  String title;
    private  String yieldUrl;
    @JsonProperty(value = "BackgroundUrl")
    public String backgroundUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    public Date today;


}
