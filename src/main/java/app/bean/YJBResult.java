package app.bean;

/**
 * Created by terry.wu on 2016/4/18 0018.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class YJBResult {
    @JsonProperty(value = "login_type")
    public  String loginType;

    @JsonProperty(value = "returnJson")
    public  YJBReturnJson returnJson;
}
