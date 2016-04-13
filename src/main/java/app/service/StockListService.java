package app.service;

import app.bean.StockList;
import app.entity.StockListData;
import app.entity.Trader;
import app.repository.StockListRepository;
import app.repository.TraderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by terry.wu on 2016/4/12 0012.
 */
@Service
@CacheConfig(cacheNames = "stockList")
public class StockListService {
    @Autowired
    StockListRepository stockListRepository;

  /*
     @Cacheable()
    public Boolean exists(Long id){
        return  stockListRepository.exists(id);
    }*/

    @Cacheable
    public StockListData findOne(Long id){
        return  stockListRepository.findOne(id);
    }

    @Cacheable
    public List<StockListData> findByAccountID(String accountID){
        return  stockListRepository.findByAccountID(accountID);
    }

    @CacheEvict(allEntries = true)
    public void save(StockListData entity){
        stockListRepository.save(entity);
    }

    @CacheEvict(allEntries = true)
    public void delete(Long id){
        System.out.println("delete id ->"+id);
        stockListRepository.delete(id);
    }
}
