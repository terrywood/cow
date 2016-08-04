package app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by terry.wu on 2016/4/18 0018.
 */
@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XueSellRebalancing implements Serializable {
    @Id
    private Long  id;
    private Long prev_bebalancing_id;
    private Long created_at;
    private String status;
    private Double cash;

    @JsonProperty(value = "rebalancing_histories")
    @OneToMany(mappedBy="rebalancing", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<XueHistories> xueHistories = new ArrayList<>();

}
