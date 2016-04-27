package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class YJBBalance implements Serializable{

/*    @JsonProperty(value = "money_type")
    public  String moneyType;

    @JsonProperty(value = "current_balance")
    public  Double currentBalance;*/
    @JsonProperty(value = "enable_balance")
    public  Double enableBalance;
    /*
    @JsonProperty(value = "market_value")
    public  Double marketValue;

    @JsonProperty(value = "asset_balance")
    public  Double assetBalance;*/

}
