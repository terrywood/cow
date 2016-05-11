package app.service;

import app.entity.AccountData;
import app.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * Created by terry.wu on 2016/4/12 0012.
 */
@Service
@CacheConfig(cacheNames = "holidayCache")
public class HolidayService {
    private static final Logger log = LoggerFactory.getLogger(HolidayService.class);
    @Cacheable
    public boolean isTradeDayTimeByMarket() {
        boolean ret = true;
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        if (week == 1 || week == 7) {
            ret = false;
        }else if (hour < 9 || hour >= 15) {
            ret = false;
        }else  if (hour == 9 && minute < 15) {
            ret = false;
        }
        log.info("check isTradeDayTimeByMarket ["+ret+"]");
        return ret;
    }
}
