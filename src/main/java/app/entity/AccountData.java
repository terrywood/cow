package app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class AccountData implements Serializable {
    @Id
    @JsonProperty(value = "AccountID")
    private Long accountID;
    private String yieldUrl;
    private String retracement;
    private Float equity;
    @JsonProperty(value = "WinRatio")
    private String winRatio; //胜率
    @JsonProperty(value = "UserName")
    private String userName;
    @JsonProperty(value = "LogoPhotoUrl")
    private String logoPhotoUrl;
    @JsonProperty(value = "TradeNumber")
    private Integer tradeNumber;
    @JsonProperty(value = "TotalAssets")
    private Float totalAssets;
    @JsonProperty(value = "TodayProfit")
    private Float todayProfit;
    @JsonProperty(value = "Profit")
    private Float profit;
    @JsonProperty(value = "MonthYield")
    private Float monthYield;
    @JsonProperty(value = "MonthTradeNumber")
    private Float monthTradeNumber;
    @JsonProperty(value = "AvaliableAsset")
    private Float avaliableAsset;
    @JsonProperty(value = "FirstTradeTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    public Date firstTradingTime;
    @JsonProperty(value = "LastTradeTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    public Date lastTradingTime;

    public int status;
}

