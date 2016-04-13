package app.service;

import app.entity.Trader;
import app.repository.TraderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by terry.wu on 2016/4/12 0012.
 */
@Service
@CacheConfig(cacheNames = "trader")
public class TraderService {
    @Autowired
    TraderRepository traderRepository;

    @Cacheable(value="trader",key="#id")
    public Boolean exists(Long id){
        return  traderRepository.exists(id);
    }

    @CacheEvict(allEntries = true)
    public void save(Trader entity){
        traderRepository.save(entity);
    }

    @CacheEvict(value="trader",key="#id")
    public void delete(Long id){
        traderRepository.delete(id);
    }
}
