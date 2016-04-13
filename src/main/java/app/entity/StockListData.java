package app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class StockListData {
    @Id
    @JsonProperty(value = "ListID")
    public Long listID;
    @JsonProperty(value = "Market")
    public String market; //1 shanhai  2 shenzhen
    @JsonProperty(value = "AccountID")
    public String accountID;
    @JsonProperty(value = "ActionAmount")
    public Integer actionAmount;
    @JsonProperty(value = "FirstTradingTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    public Date firstTradingTime;
    @JsonProperty(value = "LastTradingTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    public Date lastTradingTime;
    @JsonProperty(value = "StockCode")
    public String stockCode;
    @JsonProperty(value = "StockName")
    public String stockName;
    @JsonProperty(value = "TodaySellAmount")
    public Integer todaySellAmount;
    @JsonProperty(value = "TotalBuyAmount")
    public Integer totalBuyAmount;
    @JsonProperty(value = "UnitPrice")
    public Float unitPrice;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    @PrePersist
    void prePersist(){
        this.setLastUpdateTime( new Date() );
    }
    @PreUpdate
    void preUpdate(){
        this.setLastUpdateTime( new Date() );
    }
}
