package app.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 code,代码
 name,名称
 industry,所属行业
 area,地区
 pe,市盈率
 outstanding,流通股本
 totals,总股本(万)
 totalAssets,总资产(万)
 liquidAssets,流动资产
 fixedAssets,固定资产
 reserved,公积金
 reservedPerShare,每股公积金
 eps,每股收益
 bvps,每股净资
 pb,市净率
 timeToMarket,上市日期
 */
@Data
@Entity
public class Stock {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String code;
    private String name;
    private String industry;
    private String area;
    private String timeToMarket;
    private Double pe;
    private Double outstanding;
    private Double totals;
    private Double totalAssets;
    private Double liquidAssets;
    private Double fixedAssets;
    private Double reserved;
    private Double reservedPerShare;
    private Double esp;
    private Double bvps;
    private Double pb;

}
