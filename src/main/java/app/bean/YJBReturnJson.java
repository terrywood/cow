package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by terry.wu on 2016/4/18 0018.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class YJBReturnJson {
    @JsonProperty(value = "function_id")
    public  String functionId;
    @JsonProperty(value = "msg_info")
    public  String msgInfo;
    @JsonProperty(value = "msg_no")
    public  String msgNo;
    @JsonProperty(value = "Func302")
    public List<Func302> func302;
}
