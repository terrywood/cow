package app.service;

import app.entity.Trader;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by terry.wu on 2016/4/15 0015.
 */
public interface TraderService {


    //Boolean exists(Long id);
    void save(Trader entity);
    Trader findOne(Long id);
    void trading(String market, Long id, String code, Integer amount, String price, String type, Boolean fast);
}
