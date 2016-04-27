package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by terry.wu on 2016/4/18 0018.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class YJBAccount implements Serializable{

    @JsonProperty(value = "stock_code")
    public  String stockCode;
    @JsonProperty(value = "enable_amount")
    public  Integer enableAmount;

    /*
    @JsonProperty(value = "stock_name")
    public  String stockName;
    @JsonProperty(value = "current_amount")
    public  Integer currentAmount;
    @JsonProperty(value = "last_price")
    public  Double lastPrice;
    @JsonProperty(value = "cost_price")
    public  Double costPrice;
    @JsonProperty(value = "keep_cost_price")
    public  Double keepCostPrice;
    @JsonProperty(value = "income_balance")
    public  Double incomeBalance;
    @JsonProperty(value = "market_value")
    public  Double marketBalue;*/
}
