package app.service;

import app.bean.XueHistories;
import app.bean.XueSellRebalancing;
import app.repository.XueHistoriesRepository;
import app.repository.XueSellRebalancingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by terry.wu on 2016/4/12 0012.
 */
@Service
public class XueService {
    private static final Logger log = LoggerFactory.getLogger(XueService.class);

    @Autowired
    XueSellRebalancingRepository xueSellRebalancingRepository;

    @Autowired
    XueHistoriesRepository xueHistoriesRepository;

    @Cacheable(value="findXueSellRebalancingByPK",key = "#id" ,unless="#result == null")
    public XueSellRebalancing findXueSellRebalancingByPK(Long id){
        return  xueSellRebalancingRepository.findOne(id);
    }

    @Cacheable(value="findHistoriesByRBID",key = "#rbid")
    public List<XueHistories> findHistoriesByRBID(Long rbid){
        return  xueHistoriesRepository.findByRebalancing_id(rbid);
    }

    public void saveXueSellRebalancing(XueSellRebalancing entity){
        xueSellRebalancingRepository.save(entity);
        //xueHistoriesRepository.save(entity.getXueHistories());
    }
     /*
   @Caching(put = { @CachePut(value="movieFindCache", key="#entity.listID") },
           evict ={ @CacheEvict(value = "findByAccountID", key = "#entity.accountID") }
   )
 public StockListData update(StockListData entity){
        stockListRepository.save(entity);
        return  entity;
    }

    @CacheEvict(value = "findByAccountID",key = "#entity.accountID")
    public void save(StockListData entity){
        stockListRepository.save(entity);
    }

    @CacheEvict(value = { "movieFindCache", "findByAccountID" }, allEntries = true)
    public void delete(Long listID){
        stockListRepository.delete(listID);
    }*/
}
