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
public class CowResult {
    /*
    * {"code":0,"message":"买入：委托成功，资金冻结958.06元","result":1,"delegateID":"62904736","isPlan":"0"}
    * */
    @JsonProperty(value = "code")
    public  String code;
    @JsonProperty(value = "message")
    public  String  message;
    public  String  result;
    public  String  delegateID;
    public  String  isPlan;
    public  String  innercode;
    public  Integer  maxBuy;
    public  Integer  maxSell;
    public  String  nowv;
}
