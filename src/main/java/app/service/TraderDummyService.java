package app.service;

import app.entity.Trader;
import app.repository.TraderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@EnableAspectJAutoProxy(proxyTargetClass = true)
@Lazy(value = false)
public class TraderDummyService  implements TraderService, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(TraderDummyService.class);
    @Autowired
    TraderRepository traderRepository;
    @Override
    public void afterPropertiesSet() throws Exception {

    }

   /* @Override
    @CacheEvict(value = "traderCache", allEntries = true)
    public void save(Trader entity) {
        traderRepository.save(entity);
    }*/

    @Override
    @Cacheable(value = "traderCache",key = "#id" ,unless="#result == null")
    public Trader findOne(Long id) {
        log.info("get Trader by db ---------->> [" + id + "]");
        return traderRepository.findOne(id);
    }

    @Override
    @CacheEvict(value = "traderCache",key = "#id")
    public void trading(String market, Long id, String code, Integer amount, String price, String type, Boolean fast) {
        Trader trader = new Trader();
        trader.setType(type);
        trader.setDelegateID(id);
        trader.setTransactionAmount(amount);
        trader.setTransactionUnitPrice(Float.valueOf(price));
        trader.setCode(code);
        trader.setFast(fast);
        trader.setRemark("dummy");
        traderRepository.save(trader);
    }
}
