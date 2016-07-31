package app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.TABLE;

/**
 * Created by terry.wu on 2016/4/12 0012.
 */
@Entity
@Data
public class Trader implements Serializable {

    @Id
    public Long delegateID;
    @LastModifiedDate
    @Column(name = "last_update_time")
    private Date lastUpdateTime;
    @JsonProperty(value = "Type")
    public String type; // 1 buy ; 2 sell
    public Integer transactionAmount;
    public Double transactionUnitPrice;
    public String code;
    public Boolean fast;
    @Column(length = 1000)
    public String remark;
    @PrePersist
    void prePersist(){
        this.setLastUpdateTime( new Date() );
    }
    @PreUpdate
    void preUpdate(){
        this.setLastUpdateTime( new Date() );
    }
}
