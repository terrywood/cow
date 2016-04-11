package app.bean;

import app.entity.HistoryData;
import app.entity.StockListData;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Created by Riky on 2016/4/10.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockList {
    private List<HistoryData> historyData;
}
