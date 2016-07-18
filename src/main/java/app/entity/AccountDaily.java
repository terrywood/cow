package app.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "account_daily",indexes = {
        @Index(name = "account_daily_unique_idx", unique = true, columnList = "accountid,day") })
public class AccountDaily implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private Long accountID;

    private Date day;

    private Float equity;

}

