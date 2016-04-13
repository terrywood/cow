package app.task;

import app.bean.Account;
import app.bean.StockList;
import app.entity.HistoryData;
import app.entity.StockListData;
import app.entity.Trader;
import app.entity.TraderSession;
import app.service.HistoryDataRepository;
import app.service.StockListRepository;
import app.service.TraderRepository;
import app.service.TraderSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
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
    @Autowired
    TraderSessionService traderSessionService;

   /*
     @Scheduled(cron = "0 0/5 9,16 * * ?")
     boolean debug = true;*/
    /*guo jin*/
    // String  userId = "605166";
    /*terry*/
    String userId = "607955";

    //阿勤
    //String userId = "773183";

    public void trading (String market,Long id,String code, Integer amount,String price,String type){
        TraderSession entity = traderSessionService.getSession();
        //https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.StockUF2Control&method=entrust&dse_sessionId=A084EE8300CE52072EA3FEE36654ABD8
       String account = null;
       if(market.equals("2")){
           account = entity.getSzAccount();
       }else {
           account = entity.getShAccount();
       }

     /*   String httpUrl ="https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.StockUF2Control&method=entrust&dse_sessionId="+entity.getSid();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url .openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Cookie",  entity.getCookie());
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
           // InputStream inStream=connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Trader trader = new Trader();
        trader.setType(type);
        trader.setDelegateID(id);
        trader.setTransactionAmount(amount);
        trader.setTransactionUnitPrice(Float.valueOf(price));
        trader.setCode(code);
        traderRepository.save(trader);
    }


    public void stockListItem(StockListData stockList) throws IOException {
        URL url = new URL("https://swww.niuguwang.com/tr/201411/stocklistitem.ashx?id=" + stockList.getListID() + "&s=xiaomi&version=3.4.4&packtype=1");
        StockList bean = jacksonObjectMapper.readValue(url, StockList.class);
        for (HistoryData data : bean.getHistoryData()) {
            if (!historyDataRepository.exists(data.getDelegateID())) {
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
                //call 券商API
                trading(stockList.getMarket(),data.getDelegateID() ,stockList.getStockCode(),amount,result,type);
                historyDataRepository.save(data);

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
            //System.out.println(new Date() + "-----------------no  trade Day");
            TraderSession entity = traderSessionService.getSession();
            long dc = System.currentTimeMillis()/100l;
            String httpUrl ="https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.StockUF2Control&method=queryFund&dse_sessionId="+entity.getSid()+"&_dc="+dc;
            try {
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url .openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Cookie",  entity.getCookie());
                connection.connect();
                // String result = IOUtils.toString(connection.getInputStream(), Consts.UTF_8);
                Map map = jacksonObjectMapper.readValue(connection.getInputStream(), Map.class);
                System.out.println(map);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(5*60*1000l); //sleep 5 min
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
