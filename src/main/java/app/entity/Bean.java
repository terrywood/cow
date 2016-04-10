package app.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by Riky on 2016/4/10.
 */
@Data
public class Bean {
    public Double totalAssets;
    public Double totalMarketAssets;
    private List<AccountData> accountData;


}
