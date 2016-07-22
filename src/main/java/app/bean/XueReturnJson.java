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
public class XueReturnJson {
    @JsonProperty(value = "updated_at")
    public  long updated_at;


    @JsonProperty(value = "view_rebalancing")
    public List<Func302> func302;
}
