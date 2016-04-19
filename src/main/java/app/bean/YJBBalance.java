package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class YJBBalance {
    @JsonProperty(value = "function_id")
    public  String functionId;
    @JsonProperty(value = "msg_info")
    public  String msgInfo;
    @JsonProperty(value = "msg_no")
    public  String msgNo;
}
