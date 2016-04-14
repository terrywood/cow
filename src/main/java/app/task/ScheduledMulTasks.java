package app.task;

import app.repository.HistoryDataRepository;
import app.service.StockListService;
import app.service.TraderService;
import app.service.TraderSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@Transactional
public class ScheduledMulTasks {
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


    String[] accounts = new String[]{"605166", "607955", "773183"};


    public void initMethod() {
        try {
            // create an array of URIs to perform GETs on
            String[] urisToGet = {
                    "https://trade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878002&dse_sessionId=",
                    "https://trade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878003&dse_sessionId="
            };
            // create a thread for each URI
            GetThread[] threads = new GetThread[urisToGet.length];
            for (int i = 0; i < threads.length; i++) {
             /*   HttpGet httpget = new HttpGet(urisToGet[i]);
                httpget.addHeader("Cookie",gfCookie);*/
                threads[i] = new GetThread(i + 1);
            }
            // start the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].start();
            }
            // join the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }
    }


    static class GetThread extends Thread {

        private final int id;
        // private double price;
        private Map data;

        public Map getData() {
            return data;
        }

        /*     public double getPrice(){
                 return  price;
             }
     */
        public GetThread(int id) {

            this.id = id;
        }

        /**
         * Executes the GetMethod and prints some status information.
         */
        @Override
        public void run() {

        }

    }


}
