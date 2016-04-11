package app.task;

import app.bean.Account;
import app.bean.StockList;
import app.entity.HistoryData;
import app.entity.StockListData;
import app.service.HistoryDataRepository;
import app.service.StockListRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Transactional
public class ScheduledTasks {

   // private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    ConcurrentMap<Long,Date> map =new ConcurrentHashMap();
    //@Scheduled(fixedRate = 1000)
    public void reportCurrentTime() {
      //  System.out.println("The time is now " + dateFormat.format(new Date()));
    }
    @Autowired
    ObjectMapper jacksonObjectMapper;
    @Autowired
    StockListRepository stockListRepository;
    @Autowired
    HistoryDataRepository historyDataRepository;
    boolean loop = true;
    /*guo jin*/
   // String  userId = "605166";
    /*terry*/
    String  userId = "607955";
    public void stockListItem(Long listId) throws IOException {
        URL url = new URL("https://swww.niuguwang.com/tr/201411/stocklistitem.ashx?id="+listId+"&s=xiaomi&version=3.4.4&packtype=1");
        StockList bean = jacksonObjectMapper.readValue(url, StockList.class);
        historyDataRepository.save( bean.getHistoryData());

        for(HistoryData data : bean.getHistoryData()){

        }


        //historyDataRepository
    }

    /* ObjectMapper mapper = new ObjectMapper();
       mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
       mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
       mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
       Account user = mapper.readValue(url, Account.class);*/

    @PostConstruct
    public void init() throws IOException {
        URL url = new URL("https://swww.niuguwang.com/tr/201411/account.ashx?aid="+userId+"&s=xiaomi&version=3.4.4&packtype=1");
        while (loop){
            long times = System.currentTimeMillis();
            Account bean = jacksonObjectMapper.readValue(url, Account.class);
            //List list = bean.getStockListData();
            for(StockListData data :bean.getStockListData()){
                Long id = data.getListID();
                if(map.containsKey(id)){
                    Date date =map.get(id);
                    Date lastTrading = data.getLastTradingTime();
                    if(!date.equals(lastTrading)){
                        stockListRepository.save(data);
                        stockListItem(id);
                        map.put(id,lastTrading);
                    }
                }else{
                    stockListRepository.save(data);
                    stockListItem(id);
                    Date lastTrading = data.getLastTradingTime();
                    map.put(id,lastTrading);
                }
            }
          /*  System.out.println("use ["+(times-System.currentTimeMillis())+"] ms");
            try {
                Thread.sleep(5000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }

    }
}
