package app.task;

import app.bean.Account;
import app.bean.StockList;
import app.entity.HistoryData;
import app.entity.StockListData;
import app.service.HistoryDataRepository;
import app.service.StockListRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ScheduledTasks {


    ConcurrentMap<Long, Date> map = new ConcurrentHashMap();

    @Autowired
    ObjectMapper jacksonObjectMapper;
    @Autowired
    StockListRepository stockListRepository;
    @Autowired
    HistoryDataRepository historyDataRepository;

    //@Scheduled(cron = "0 0/5 9,16 * * ?")


     boolean debug = true;
    /*guo jin*/
    // String  userId = "605166";
    /*terry*/
    String userId = "607955";

    public void stockListItem(Long listId) throws IOException {
        URL url = new URL("https://swww.niuguwang.com/tr/201411/stocklistitem.ashx?id=" + listId + "&s=xiaomi&version=3.4.4&packtype=1");
        StockList bean = jacksonObjectMapper.readValue(url, StockList.class);
        for (HistoryData data : bean.getHistoryData()) {
            HistoryData historyData = historyDataRepository.findOne(data.getDelegateID());
            if (historyData == null) {
                historyDataRepository.save(data);
                //TODO call 券商API
            }
        }


        //historyDataRepository
    }

    /* ObjectMapper mapper = new ObjectMapper();
       mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
       mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
       mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
       Account user = mapper.readValue(url, Account.class);*/


    @Scheduled(fixedDelay = 1)
    public void init() throws IOException {
        if(isTradeDayTimeByMarket()){
            //long times = System.currentTimeMillis();
            URL url = new URL("https://swww.niuguwang.com/tr/201411/account.ashx?aid=" + userId + "&s=xiaomi&version=3.4.4&packtype=1");
            Account bean = jacksonObjectMapper.readValue(url, Account.class);
            //List list = bean.getStockListData();
            for (StockListData data : bean.getStockListData()) {
                Long id = data.getListID();
                if (map.containsKey(id)) {
                    Date date = map.get(id);
                    Date lastTrading = data.getLastTradingTime();
                    if (!date.equals(lastTrading)) {
                        stockListRepository.save(data);
                        stockListItem(id);
                        map.put(id, lastTrading);
                    }/* else {
                        System.out.println("no update by "+userId);
                    }*/
                } else {
                    stockListRepository.save(data);
                    stockListItem(id);
                    Date lastTrading = data.getLastTradingTime();
                    map.put(id, lastTrading);
                }
            }
           // System.out.println("use [" + (times - System.currentTimeMillis()) + "] ms");
        }else{
            System.out.println(new Date() + "-----------------no  trade Day");
            try {
                Thread.sleep(10*60*1000l); //sleep 10 min
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isTradeDayTimeByMarket() {
        if(debug)return true;
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        if (week == 1 || week == 7) {
            return false;
        }
        if (hour < 9 || hour > 15) {
            return false;
        }
        if (hour == 9 && minute < 15) {
            return false;
        }
        if (hour == 15 && minute >= 30) {
            return false;
        }
        return true;
    }

}
