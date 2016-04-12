package app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by terry.wu on 2016/4/12 0012.
 */
@Entity
@Data
public class Trader {
    @Id
    public Long delegateID;
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
    @JsonProperty(value = "Type")
    public String type; // 1 buy ; 2 sell
    public Integer transactionAmount;
    public Float transactionUnitPrice;
    public String code;

}
