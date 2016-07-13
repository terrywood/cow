package app.bean;

import app.entity.AccountData;
import app.entity.DelegateData;
import app.entity.StockListData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Created by Riky on 2016/4/10.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AccountRaw {
    private List<StockListData> stockListData;
    private List<DelegateData> delegateData;
    private List<AccountData> accountData;
}
