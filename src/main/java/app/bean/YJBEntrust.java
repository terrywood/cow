package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class YJBEntrust implements Serializable{

    @JsonProperty(value = "entrust_no")
    public  String entrustNo;
    @JsonProperty(value = "report_time")
    public  String reportTime;
    @JsonProperty(value = "entrust_bs")
    public  String entrustBs;

    @JsonProperty(value = "stock_code")
    public  String stockCode;

    @JsonProperty(value = "stock_name")
    public  String stockName;

    @JsonProperty(value = "entrust_status")
    public  String entrustStatus;

    @JsonProperty(value = "entrust_price")
    public  Double entrustPrice;

    @JsonProperty(value = "business_price")
    public  Double businessPrice;

    @JsonProperty(value = "entrust_amount")
    public String entrustAmount;

    @JsonProperty(value = "business_amount")
    public String businessAmount;



}
