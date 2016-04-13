package app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by terry.wu on 2016/4/13 0013.
 * <p>
 * AccountID: "607955",
 * DelegateAmount: "100",
 * DelegateID: "58374525",
 * DelegateState: "0",
 * DelegateTime: "04-13 11:30",
 * DelegateTotalPrice: "3210.06",
 * DelegateType: "1",
 * DelegateUnitPrice: "32.05",
 * ExchangeCode: "101",
 * ExtensionTime: "04-13 15:00",
 * InnerCode: "7352",
 * Market: "1",
 * StockCode: "603828",
 * StockName: "柯利达"
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class DelegateData {
    @Id
    @JsonProperty(value = "DelegateID")
    public Long delegateID;
    @JsonProperty(value = "DelegateAmount")
    public Integer delegateAmount;
    @JsonProperty(value = "DelegateTime")
    @JsonFormat(pattern = "MM-dd HH:mm")
    public Date delegateTime;
    @JsonProperty(value = "DelegateType")
    public String delegateType; // 1 buy ; 2 sell
    @JsonProperty(value = "DelegateUnitPrice")
    public String delegateUnitPrice;
    @JsonProperty(value = "Market")
    public String market; // 1 sh ; 2 sz
    @JsonProperty(value = "StockCode")
    public String stockCode;
    @JsonProperty(value = "StockName")
    public String stockName;
}
