package app.service;

import app.bean.AccountRaw;
import app.entity.AccountDaily;
import app.entity.AccountData;
import app.repository.AccountDailyRepository;
import app.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by terry.wu on 2016/4/12 0012.
 */
@Service
@CacheConfig(cacheNames = "accountCache")
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountDailyRepository accountDailyRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Cacheable
    public  List<AccountData> findTrackAccount(){
        return accountRepository.findTrackAccount();
    }
    public  List<AccountData> findAll(){
        return accountRepository.findAll();
    }

    /*guo jin*/
    // String  userId = "605166";
    //*terry*/
    // String userId = "607955";
    //阿勤
    //  String userId = "773183";

    @PostConstruct
    public void initMethod() throws IOException {
        //String[] userId = new String[]{"607955","605166","773183"};
        String users ="5750720,4904532,3832417,6026528,6026380,6028641,6026400,5975300,5736249,3803325,5061951,5701284,12153,98077,4096,16236,111517,5984990,5835775,4457629,4890303,5802162,10100,565784,773183,131984,605166";
        for(String str : users.split(",")){
            updateAccount(str);
        }



    }

    public void updateAccount(String aid) throws IOException {
        URL url = new URL("https://swww.niuguwang.com/tr/201411/account.ashx?aid=" + aid + "&s=xiaomi&version=3.4.4&packtype=1");
        //ObjectMapper mapper = new ObjectMapper();
        Date today = DateUtils.truncate(new Date(), Calendar.DATE);
        AccountRaw callback = objectMapper.readValue(url, AccountRaw.class);
        AccountData data = callback.getAccountData().get(0);
        AccountData accountData = accountRepository.getOne(Long.valueOf(aid));
        if(accountData !=null){
            data.setStatus(accountData.getStatus());
        }
        accountRepository.save(data);
        Long _aid =Long.valueOf(aid);
        AccountDaily accountDaily =accountDailyRepository.findByDayAndAccountID(today,_aid);
        if(accountDaily==null){
            accountDaily = new AccountDaily();
        }
        accountDaily.setAccountID(_aid);
        accountDaily.setDay(today);
        accountDaily.setEquity(data.getEquity());
        accountDailyRepository.save(accountDaily);
    }

}
