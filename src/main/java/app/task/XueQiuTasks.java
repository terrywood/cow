package app.task;

import app.repository.HistoryDataRepository;
import app.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class XueQiuTasks  {
    private static final Logger log = LoggerFactory.getLogger(XueQiuTasks.class);
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

   // @Scheduled(fixedDelay = 1)
    public  void init() {
        if (holidayService.isTradeDayTimeByMarket()) {

        } else {
            log.info("now is not trade Day");
            try {
                Thread.sleep(1000 * 60 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }




}
