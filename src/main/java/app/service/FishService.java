package app.service;

import app.entity.Fish;
import app.repository.FishRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by terry.wu on 2016/4/12 0012.
 */
@Service
public class FishService implements InitializingBean{
    private static final Logger log = LoggerFactory.getLogger(FishService.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private Map<String,String> map = new HashMap() ;

    @Autowired
    private FishRepository fishRepository;
    private String userAgent ="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    @Override
    public void afterPropertiesSet() throws Exception {
            map. put("上证", "000001");
            map. put("创业板", "399006");
            map. put("国证有色", "399395");
            map. put("中证军工", "399967");
            map. put("证券公司", "399975");

           process();
    }


    String site = "http://chuansong.me/account/gushequ";
    public void process() throws IOException {
        Document doc = Jsoup.connect(site)
                .userAgent(userAgent)
                .get();
        Elements elements = doc.getElementsByClass("pagedlist_item");

        for(Element element : elements){
            Element link =   element.getElementsByClass("question_link").get(0);
            Element date =  link.nextElementSibling();
            String href =link.attr("href");
            System.out.println(href);
            try {
                post(href,date.text());
                System.out.println("---------------------------------");
            }catch (IOException e){
                System.out.println(e.getMessage());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public Fish matchBean(String html){
        for(String key : map.keySet()){
            if(html.startsWith(key)){
                Fish fish = new Fish(map.get(key), key);
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

    public void post(String sub,String date) throws IOException, ParseException {
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
                    fish.setDate(sdf.parse(date));
                    fish.setId(date+"-"+fish.getIndex());
                    this.fishRepository.save(fish);
                    System.out.println(fish);
                }
            }
        }


    }



}
