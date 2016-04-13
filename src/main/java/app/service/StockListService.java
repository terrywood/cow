package app.service;

import app.bean.StockList;
import app.entity.StockListData;
import app.entity.Trader;
import app.repository.StockListRepository;
import app.repository.TraderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by terry.wu on 2016/4/12 0012.
 */
@Service
public class StockListService {
    @Autowired
    StockListRepository stockListRepository;

  /*
     @Cacheable()
    public Boolean exists(Long id){
        return  stockListRepository.exists(id);
    }*/

    @Cacheable(value="movieFindCache")
    public StockListData findOne(Long listID){
        System.out.println("findOne["+listID+"]");
        return  stockListRepository.findOne(listID);
    }

    @Cacheable(value="findByAccountID")
    public List<StockListData> findByAccountID(String accountID){
        System.out.println("findByAccountID["+accountID+"]");
        return  stockListRepository.findByAccountID(accountID);
    }

    @CacheEvict(value = { "movieFindCache", "findByAccountID" }, allEntries = true)
    public void save(StockListData entity){
        stockListRepository.save(entity);
    }

    @CacheEvict(value = { "movieFindCache", "findByAccountID" }, allEntries = true)
    public void delete(Long listID){
        System.out.println("delete id ->"+listID);
        stockListRepository.delete(listID);
    }
}
