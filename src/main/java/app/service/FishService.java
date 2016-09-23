package app.service;

import app.entity.Fish;
import app.repository.FishRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FishService implements InitializingBean{
    private static final Logger log = LoggerFactory.getLogger(FishService.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private  Map<String,Fish> map = new HashMap<String,Fish>() ;
//http://api.money.126.net/data/feed/0000001,1399006,1399005,1399395,1399967,1399975,money.api
    @Autowired
    private FishRepository fishRepository;
    private static String userAgent ="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    private static String site = "http://chuansong.me/account/gushequ";

    @Override
    public void afterPropertiesSet() throws Exception {
            map. put("上证", new Fish("000001","上证","510300"));
            map. put("中小板",new Fish( "399005","中小板","159902"));
            map. put("创业板",new Fish("399006","创业板","159915"));
            map. put("国证有色", new Fish("399395","国证有色","150197"));
            map. put("中证军工", new Fish("399967","中证军工","512660"));
            map. put("证券公司", new Fish("399975","证券公司","512880"));
    }

    public List<Fish> getByDay(String day) throws ParseException, IOException {
        Date  day2 = sdf.parse(day);
        List list = fishRepository.findByDate(day2);
        if(list.size()>0){
            return  list;
        }
        String wei = sdf.format(DateUtils.addDays(day2,-1));
        String href  =  process(wei);
        System.out.println(href);
        if(href!=null){
            list = post(href,day2);
            fishRepository.save(list);
            return  list ;
        }else{
            return  null;
        }

    }

    public void save2db(String start)  {
        Document doc = null;
        try {
            doc = Jsoup.connect(site).userAgent(userAgent).data("start",start) .get();
            Elements elements = doc.getElementsByClass("pagedlist_item");
            for(Element element : elements){
                Element link =   element.getElementsByClass("question_link").get(0);
                Element date =  link.nextElementSibling();
                String href =link.attr("href");
                Date  day2 = sdf.parse(date.text());
                Date day = DateUtils.addDays(day2,1);

                List<Fish> l = fishRepository.findByDate(day);
                if(l.size()>0){
                    continue;
                }

                List<Fish>  list = post(href,day);
                fishRepository.save(list);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public String process(String day) throws IOException {
        Document doc = Jsoup.connect(site)
                .userAgent(userAgent)
                .get();
        Elements elements = doc.getElementsByClass("pagedlist_item");
        for(Element element : elements){
            Element link =   element.getElementsByClass("question_link").get(0);
            Element date =  link.nextElementSibling();
            String href =link.attr("href");
            if(day.equals(date.text())){
                return href;
            }
        }
        return null;
    }

    public Fish matchBean(String html){
        for(String key : map.keySet()){
            if(html.startsWith(key)){
                Fish fish =map.get(key);
                if (StringUtils.contains(html, "yes")) {
                    fish.setWork("y");
                } else if (StringUtils.contains(html, "no")) {
                    fish.setWork("n");
                }
                String edge = StringUtils.substringAfterLast(html, "临界");
                if(NumberUtils.isDigits(edge) && StringUtils.isNotBlank(fish.getWork())){
                    fish.setEdge(edge);
                    return  fish;
                }
            }
        }
        return  null;
    }

    public List<Fish> post(String sub, Date day2) throws IOException, ParseException {

        List<Fish> list = new ArrayList<>();
        Document doc = Jsoup.connect("http://chuansong.me"+sub)
                .userAgent(userAgent)
                .timeout(100000)
                .get();
        Element element = doc.getElementById("js_content");
        Elements _p =element.getElementsByTag("p");
        for(Element p : _p){
            if(p.hasAttr("style")){
                Fish fish = matchBean(StringUtils.trim(p.text()));
                if(fish!=null){
                    fish.setDate(day2);
                    fish.setId(fish.getSymbol()+day2.getTime());
                    list.add(fish);
                }
            }
        }

       return   list;
    }


    public List<Fish> findBySymbol(String symbol){
        return  fishRepository.findBySymbol(symbol);
    }

}
