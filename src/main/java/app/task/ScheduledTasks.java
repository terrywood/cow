package app.task;

import app.bean.Account;
import app.bean.StockList;
import app.entity.DelegateData;
import app.entity.HistoryData;
import app.entity.StockListData;
import app.repository.HistoryDataRepository;
import app.service.StockListService;
import app.service.TraderService;
import app.service.TraderSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class ScheduledTasks {
    @Autowired
    ObjectMapper jacksonObjectMapper;
    @Autowired
    StockListService stockListService;
    @Autowired
    HistoryDataRepository historyDataRepository;
    @Autowired
    TraderService traderService;
    @Autowired
    TraderSessionService traderSessionService;
    /*guo jin*/
    // String  userId = "605166";
    //*terry*/
  //String userId = "607955";
    //阿勤
    String userId = "773183";
    public void stockListItem(StockListData stockList) throws IOException {
        URL url = new URL("https://swww.niuguwang.com/tr/201411/stocklistitem.ashx?id=" + stockList.getListID() + "&s=xiaomi&version=3.4.4&packtype=1");
        StockList bean = jacksonObjectMapper.readValue(url, StockList.class);
        for (HistoryData data : bean.getHistoryData()) {
            if (!historyDataRepository.exists(data.getDelegateID())) {
                String type = data.getType();
                Float price =  data.getTransactionUnitPrice() ;
                String result = String.valueOf(price);
                int amount = 100;
            /*    if (type.equals("1")) {
                    price = data.getTransactionUnitPrice() * 1.025f;
                    result = String.format("%.2f", price);
                } else {
                    price = data.getTransactionUnitPrice() * 0.97f;
                    result = String.format("%.2f", price);
                }*/
                //call 券商API
                if(traderService.findOne(data.getDelegateID())==null){
                    traderService.trading(stockList.getMarket(), data.getDelegateID(), stockList.getStockCode(), amount, result, type, false);
                }
                historyDataRepository.save(data);

            }
        }

    }

    /* ObjectMapper mapper = new ObjectMapper();
       mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
       mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
       mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
       Account user = mapper.readValue(url, Account.class);*//*

*/

   /* @Scheduled(cron = "0/10 * 9-15 * * ?")
    public void balanceTest() {
        System.out.println("balanceTest----------------------"+ new Date());
    }*/

    @Scheduled(fixedDelay = 1)
    public void init() {

        if (isTradeDayTimeByMarket()) {
            long times = System.currentTimeMillis();
            URL url = null;
            try {
                url = new URL("https://swww.niuguwang.com/tr/201411/account.ashx?aid=" + userId + "&s=xiaomi&version=3.4.4&packtype=1");
                Account bean = jacksonObjectMapper.readValue(url, Account.class);
                //List list = bean.getStockListData();

                for (DelegateData data : bean.getDelegateData()) {
                    int amount = 100;
                    if(traderService.findOne(data.getDelegateID())==null){
                        traderService.trading(data.getMarket(), data.getDelegateID(), data.getStockCode(), amount, data.getDelegateUnitPrice(), data.getDelegateType(), true);
                    }
                }
                //handle clear stock
                List<StockListData> list = stockListService.findByAccountID(userId);
                for (StockListData entity : list) {
                    Long entityId = entity.getListID();
                    boolean isDelete = true;
                    for (StockListData data : bean.getStockListData()) {
                        Long id = data.getListID();
                        if (entityId.equals(id)) {
                            isDelete = false;
                            break;
                        }
                    }
                    if (isDelete) {
                        stockListItem(entity);
                        stockListService.delete(entityId);
                    }
                }
                //handle update

                for (StockListData data : bean.getStockListData()) {
                    Long id = data.getListID();
                    StockListData entity = stockListService.findOne(id);
                    if (entity == null) {
                        stockListItem(data);
                        stockListService.save(data);
                    } else {
                        long lastTrading = data.getLastTradingTime().getTime();
                        if (entity.getLastTradingTime().getTime() != lastTrading) {
                            stockListItem(data);
                            stockListService.save(data);
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(10000); //sleep 10 sec
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

        } else {

            System.out.println(new Date() + "----------------- is not trade Day");
            try {
                Thread.sleep(1000 * 60 * 10); //sleep 10 min
                //Thread.sleep(1000); //sleep 10 min
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isTradeDayTimeByMarket() {
  /*   if (1 == 1) {
            try {
                Thread.sleep(2000); //sleep 5 sec
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }*/

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
