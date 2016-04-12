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
    public String getSession(){
        System.out.println("getSession function");
        TraderSession session = traderSessionRepository.findAll().iterator().next();
        return session.getSid();
    }

    public Iterable<TraderSession> findAll(){
        System.out.println("findAll function");
        return traderSessionRepository.findAll();
    }

    @CacheEvict
    public void save(TraderSession entity){
        System.out.println("save function");
        traderSessionRepository.save(entity);
    }
    @CacheEvict
    public void delete(Long id){
        System.out.println("delete function");
        traderSessionRepository.delete(id);
    }
}
