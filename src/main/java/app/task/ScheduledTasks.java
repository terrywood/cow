package app.task;

import app.bean.Account;
import app.bean.StockList;
import app.entity.DelegateData;
import app.entity.HistoryData;
import app.entity.StockListData;
import app.entity.Trader;
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

//@Component
//@Transactional
public class ScheduledTasks {
    /*
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

    *//*
      @Scheduled(cron = "0 0/5 9,16 * * ?")
      boolean debug = true;*//*
    *//*guo jin*//*
    // String  userId = "605166";
    *//*terry*//*
    //String userId = "607955";
    //阿勤
    String userId = "773183";

    public void trading(String market, Long id, String code, Integer amount, String price, String type, Boolean fast) {
        if (!traderService.exists(id)) {
          *//*  TraderSession entity = traderSessionService.getSession();

            String account = null;
            if (market.equals("2")) {
                account = entity.getSzAccount();
            } else {
                account = entity.getShAccount();
            }
            String httpUrl = "https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.StockUF2Control&method=entrust&dse_sessionId=" + entity.getSid();
            try {
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Cookie", entity.getCookie());
                StringBuffer params = new StringBuffer();
                params.append("dse_sessionId").append("=").append(entity.getSid())
                        .append("&").append("stock_code").append("=").append(code)
                        .append("&").append("exchange_type").append("=").append(market)
                        .append("&").append("stock_account").append("=").append(account)
                        .append("&").append("entrust_amount").append("=").append(amount)
                        .append("&").append("entrust_price").append("=").append(price)
                        .append("&").append("entrust_prop").append("=").append("0")
                        .append("&").append("entrust_bs").append("=").append(type);
                byte[] bytes = params.toString().getBytes();
                connection.getOutputStream().write(bytes);
                connection.connect();
                String result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }*//*

            Trader trader = new Trader();
            trader.setType(type);
            trader.setDelegateID(id);
            trader.setTransactionAmount(amount);
            trader.setTransactionUnitPrice(Float.valueOf(price));
            trader.setCode(code);
            trader.setFast(fast);
            traderService.save(trader);


        }


    }


    public void stockListItem(StockListData stockList) throws IOException {
        URL url = new URL("https://swww.niuguwang.com/tr/201411/stocklistitem.ashx?id=" + stockList.getListID() + "&s=xiaomi&version=3.4.4&packtype=1");
        StockList bean = jacksonObjectMapper.readValue(url, StockList.class);
        for (HistoryData data : bean.getHistoryData()) {
            if (!historyDataRepository.exists(data.getDelegateID())) {
                String type = data.getType();
                Float price = null;
                String result = null;
                int amount = 100;
                if (type.equals("1")) {
                    price = data.getTransactionUnitPrice() * 1.025f;
                    result = String.format("%.2f", price);
                } else {
                    price = data.getTransactionUnitPrice() * 0.97f;
                    result = String.format("%.2f", price);
                }
                //call 券商API
                trading(stockList.getMarket(), data.getDelegateID(), stockList.getStockCode(), amount, result, type, false);
                historyDataRepository.save(data);

            }
        }

    }

    *//* ObjectMapper mapper = new ObjectMapper();
       mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
       mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
       mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
       Account user = mapper.readValue(url, Account.class);*//*

*//*
    @Scheduled(cron = "0/1 * * * * ?")
    public void connectGf() {
        System.out.println("run connectGf");
        //TraderSession entity = traderSessionService.getSession();
        TraderSession entity = new TraderSession();
       entity.setCookie("name=value; JSESSIONID=7FBD68852BD89E79C5D1102E2B8B64F0; dse_sessionId=64D3943AF312E53AC58207CC87615941; userId=J*1C*8F*106*C1*F1*28*C6r*96k1p*B3*BBG*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00");
        entity.setSid("64D3943AF312E53AC58207CC87615941");
        if (entity != null) {
            String httpUrl ="https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.StockUF2Control&method=queryFund&_dc="+System.currentTimeMillis()+"&dse_sessionId="+entity.getSid();
               try {
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Cookie", entity.getCookie());
                connection.connect();
                 String result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                //Map map = jacksonObjectMapper.readValue(connection.getInputStream(), Map.class);
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*//*



    @Scheduled(fixedDelay = 1)
    public void init() {
        if (isTradeDayTimeByMarket()) {
          //  long times = System.currentTimeMillis();
            URL url = null;
            try {
                url = new URL("https://swww.niuguwang.com/tr/201411/account.ashx?aid=" + userId + "&s=xiaomi&version=3.4.4&packtype=1");
                Account bean = jacksonObjectMapper.readValue(url, Account.class);
                //List list = bean.getStockListData();
                for (DelegateData data : bean.getDelegateData()) {
                    int amount =100;
                    trading(data.getMarket(), data.getDelegateID(), data.getStockCode(), amount, data.getDelegateUnitPrice(), data.getDelegateType(), true);
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
                        //System.out.println("null");
                        stockListItem(data);
                        stockListService.save(data);
                    } else {
                        //System.out.println("yes");
                        long lastTrading = data.getLastTradingTime().getTime();
                      //  long lastTrading2 = entity.getLastTradingTime().getTime();
                      //  System.out.println("lastTrading["+lastTrading+"] - lastTrading2["+lastTrading2+"]");
                        if (entity.getLastTradingTime().getTime() != lastTrading) {
                            stockListItem(data);
                            stockListService.save(data);
                        }
                    }
                }


               // System.out.println("use [" + (times - System.currentTimeMillis()) + "] ms");
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
                Thread.sleep(1000*60*10); //sleep 10 min
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isTradeDayTimeByMarket() {
     *//*   if (1 == 1) {
            try {
                Thread.sleep(2000); //sleep 5 sec
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }*//*
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
*/
}
