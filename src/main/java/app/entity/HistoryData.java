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
public class HistoryData {
    @Id
    @JsonProperty(value = "DelegateID")
    public Long delegateID;
    @JsonProperty(value = "ListID")
    public Long listID;

    @JsonProperty(value = "AddTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    public Date addTime;
    @JsonProperty(value = "ClearFlag")
    public String clearFlag;

    @JsonProperty(value = "Type")
    public String type; // 1 buy ; 2 sell

    @JsonProperty(value = "TransactionAmount")
    public Integer transactionAmount;
    @JsonProperty(value = "TransactionUnitPrice")
    public Float transactionUnitPrice;

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
