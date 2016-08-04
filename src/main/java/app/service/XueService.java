package app.service;

import app.bean.XueHistories;
import app.bean.XueSellRebalancing;
import app.repository.XueHistoriesRepository;
import app.repository.XueSellRebalancingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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

    @Cacheable(value="xueSellRebalancingByPK",key = "#id" ,unless="#result == null")
    public XueSellRebalancing findXueSellRebalancingByPK(Long id){
        return  xueSellRebalancingRepository.findXueSellRebalancingByPK(id);
    }


   /* public Boolean existsSellRebalancingRepository(Long id){
        return  xueSellRebalancingRepository.exists(id);
    }
*/

/*    @Cacheable(value="findHistoriesByRBID",key = "#rbid")
    public List<XueHistories> findHistoriesByRBID(Long rbid){
        return  xueHistoriesRepository.findByRebalancing_id(rbid);
    }
    */

    @CacheEvict(value="xueSellRebalancingByPK", allEntries = true)
    public void saveXueSellRebalancing(XueSellRebalancing entity){
        for(XueHistories obj : entity.getXueHistories()){
            obj.setRebalancing(entity);
        }
        xueSellRebalancingRepository.save(entity);
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
