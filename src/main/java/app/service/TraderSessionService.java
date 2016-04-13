package app.service;

import app.entity.TraderSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by terry.wu on 2016/4/12 0012.
 */
@Service
@CacheConfig(cacheNames = "traderSession")
public class TraderSessionService {
    @Autowired
    TraderSessionRepository traderSessionRepository;
    @Cacheable
    public TraderSession getSession(){
        return traderSessionRepository.findAll().iterator().next();
    }
    public Iterable<TraderSession> findAll(){
        return traderSessionRepository.findAll();
    }
    @CacheEvict(allEntries = true)
    public void save(TraderSession entity){
        traderSessionRepository.save(entity);
    }
    @CacheEvict(allEntries = true)
    public void delete(String id){
        traderSessionRepository.delete(id);
    }
}
