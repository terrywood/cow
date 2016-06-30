package app.task;

import app.bean.Account;
import app.bean.StockList;
import app.entity.AccountData;
import app.entity.DelegateData;
import app.entity.HistoryData;
import app.entity.StockListData;
import app.repository.HistoryDataRepository;
import app.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
@Transactional
public class ScheduledTasks implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
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
    @Autowired
    AccountService accountService;
    @Autowired
    HolidayService holidayService;


    public void stockListItem(StockListData stockList) throws IOException {
        int amount = 100;
        URL url = new URL("https://swww.niuguwang.com/tr/201411/stocklistitem.ashx?id=" + stockList.getListID() + "&s=xiaomi&version=3.4.4&packtype=1");
        StockList bean = jacksonObjectMapper.readValue(url, StockList.class);
        for (HistoryData data : bean.getHistoryData()) {
            if (!historyDataRepository.exists(data.getDelegateID())) {
                String type = data.getType();
                Float price =  data.getTransactionUnitPrice() ;
                String result = String.valueOf(price);

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

    @Override
    public void afterPropertiesSet() throws Exception {
       // ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        /*guo jin*/
        // String  userId = "605166";
        //*terry*/
       // String userId = "607955";
        //阿勤
      //  String userId = "773183";
      /*  List<AccountData> list  = accountRepository.findTrackAccount();
        for(AccountData accountData :list){
            String userId = accountData.getAccountID().toString();
            log.info("track account Id ["+userId+"]");
            service.scheduleAtFixedRate(new Work(userId),10,1, TimeUnit.MILLISECONDS);
        }
       */
        //service.scheduleWithFixedDelay(new Work("607955"),10,5, TimeUnit.SECONDS);
    }
 /*   class Work implements Runnable{
        String userId ;
        public Work(String userId) {
            this.userId = userId;
        }
        @Override
        public void run() {
            init(userId);
        }
    }*/
    @Scheduled(fixedDelay = 1)
    public  void init() {
        //log.info("userID:"+userId);
        if (holidayService.isTradeDayTimeByMarket()) {
            List<AccountData> array  = accountService.findTrackAccount();
            for(AccountData accountData :array){
                String userId = accountData.getAccountID().toString();
                int amount = 100;
                //long times = System.currentTimeMillis();
                URL url = null;
                try {
                    url = new URL("https://swww.niuguwang.com/tr/201411/account.ashx?aid=" + userId + "&s=xiaomi&version=3.4.4&packtype=1");
                    Account bean = jacksonObjectMapper.readValue(url, Account.class);
                    //List list = bean.getStockListData();
                    for (DelegateData data : bean.getDelegateData()) {
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
                            System.out.println(" test 111111");
                            stockListItem(data);
                            stockListService.save(data);
                        } else {
                            long lastTrading = data.getLastTradingTime().getTime();
                            if (entity.getLastTradingTime().getTime() != lastTrading) {
                                stockListItem(data);
                                stockListService.update(data);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info("connect swww.niuguwang.com time out sleep 10 sec");
                    try {
                        Thread.sleep(10000); //sleep 10 sec
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else {
            log.info("now is not trade Day");
            try {
                Thread.sleep(1000 * 60 * 10); //sleep 10 min
                //Thread.sleep(1000); //sleep 10 min
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }




}
