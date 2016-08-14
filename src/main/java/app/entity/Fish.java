package app.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
@Data
@Entity
public class Fish {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;
    private String name;
    private Date date;
    private String work;
    private String edge;
    //private String code;
    private String symbol;

    public Fish(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }
    public Fish() {
    }
}
