package app.service;

import app.entity.TraderSession;
import app.repository.TraderSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<TraderSession> iterable =traderSessionRepository.findAllData();
        if(iterable.size()>0){
           return  iterable.get(0);
        }
        return null;
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
