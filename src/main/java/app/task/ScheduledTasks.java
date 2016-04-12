package app.task;

import app.bean.Account;
import app.bean.StockList;
import app.entity.HistoryData;
import app.entity.StockListData;
import app.entity.Trader;
import app.service.HistoryDataRepository;
import app.service.StockListRepository;
import app.service.TraderRepository;
import app.service.TraderSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Transactional
public class ScheduledTasks {
    ConcurrentMap<Long, Date> map = new ConcurrentHashMap();
    @Autowired
    ObjectMapper jacksonObjectMapper;
    @Autowired
    StockListRepository stockListRepository;
    @Autowired
    HistoryDataRepository historyDataRepository;
    @Autowired
    TraderRepository traderRepository;

   /*
     @Scheduled(cron = "0 0/5 9,16 * * ?")
     boolean debug = true;*/
    /*guo jin*/
    // String  userId = "605166";
    /*terry*/
    //String userId = "607955";

    //阿勤
    String userId = "773183";

    public void trading (String code, Integer amount,String price){


    }


    public void stockListItem(StockListData stockList) throws IOException {
        URL url = new URL("https://swww.niuguwang.com/tr/201411/stocklistitem.ashx?id=" + stockList.getListID() + "&s=xiaomi&version=3.4.4&packtype=1");
        StockList bean = jacksonObjectMapper.readValue(url, StockList.class);
        for (HistoryData data : bean.getHistoryData()) {
            if (!historyDataRepository.exists(data.getDelegateID())) {
                //TODO call 券商API
                String type = data.getType();
                Float price = null;
                String result = null;
                int amount =100;
                if(type.equals("1")){
                     price = data.getTransactionUnitPrice()*1.025f;
                     result = String .format("%.2f",price);
                }else{
                   price = data.getTransactionUnitPrice()*0.97f;
                   result = String .format("%.2f",price);
                }
                Trader trader = new Trader();
                trader.setType(type);
                trader.setDelegateID(data.getDelegateID());
                trader.setTransactionAmount(amount);
                trader.setTransactionUnitPrice(Float.valueOf(result));
                trader.setCode(stockList.getStockCode());
                historyDataRepository.save(data);
                traderRepository.save(trader);
            }
        }

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
                        stockListItem(data);
                        stockListRepository.save(data);
                        map.put(id, lastTrading);
                    }/* else {
                        System.out.println("no update by "+userId);
                    }*/
                } else {
                    stockListItem(data);
                    stockListRepository.save(data);
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
        //if(debug)return true;
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        if (week == 1 || week == 7) {
            return false;
        }
        if (hour < 9 || hour >= 15) {
            return false;
        }
        if (hour == 9 && minute < 15) {
            return false;
        }
        return true;
    }

}
