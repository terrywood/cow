package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XueHistories implements Serializable {
    @Id
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "rebalancing_id",referencedColumnName = "id")
    private XueSellRebalancing rebalancing;

    private String stock_id;
    private String stock_name;
    private String stock_symbol;

    private Double prev_weight_adjusted;
    private Double weight;
    private Double price;



}
