package app.service;

import app.entity.AccountData;
import app.entity.TraderSession;
import app.repository.AccountRepository;
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
@CacheConfig(cacheNames = "accountCache")
public class AccountService {

    @Autowired
    AccountRepository accountRepository;


    @Cacheable
    public  List<AccountData> findTrackAccount(){
        return accountRepository.findTrackAccount();
    }


}
