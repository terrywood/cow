package j8;

import app.bean.ApiDayResult;
import app.bean.Gudong;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class TestMain {
    private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    private static String site = "http://www.zhuoniugu.com/api.php";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String code ="002552";
    ObjectMapper objectMapper = new ObjectMapper();
    List<Gudong> guDong = new ArrayList<>();

    public void getGuDong(){
        Document doc = null;
        try {
            doc = Jsoup.connect(site).userAgent(userAgent)
                    .data("c", "hudong")
                    .data("a", "gudong")
                    .data("show", "html")
                    .data("stock_id", code)
                    .get();
            Element element = doc.getElementsByAttributeValue("style", "text-align:center").get(2);
            String raw[] = StringUtils.split(element.html(), "<br>");
            for (int i=0;i<raw.length;i++) {
                String day = raw[i];
                String[] row = StringUtils.split(day.trim(), "|");
                Date startDate = sdf.parse(row[0].trim());
              /*  if(i==raw.length-1){
                    httpSession.setAttribute("startDate",startDate);
                }*/
                guDong.add(new Gudong(startDate, Integer.valueOf(row[1].trim())));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public Integer getCount(Date date){
        for(Gudong entity : guDong){
            if(entity.getDate().equals(date) || date.after(entity.getDate())){
                return  entity.getCount();
            }
        }
        return 0;
    }


    public void getPrice(){
        List<Object[]> list = new ArrayList<>();
        String market = code.startsWith("6") ? "sh" : "sz";
        String  time ="";
        String  price ="";
        String  count ="";

        List prices = new ArrayList<>();
        List counts = new ArrayList<>();
        List dates = new ArrayList<>();
        try {
            Date startDate =guDong.get(guDong.size()-1).getDate();
             URL url = new URL("http://api.finance.ifeng.com/akdaily/?code=" + market + code + "&type=last");
            ApiDayResult result = objectMapper.readValue(url, ApiDayResult.class);
            System.out.println(startDate);
            List<String[]> list2 = result.getRecord();
            for (String[] args : list2) {
                Date date = sdf.parse(args[0]);
                if (date.after(startDate)) {
                      //Double price =  Double.valueOf(args[3].trim());
                     time = time + "'"+sdf.format(date)+"',";
                     price = price + args[3].trim()+",";
                     count = count+getCount(date)+",";

                    prices.add(Double.valueOf(args[3].trim()));
                    counts.add(getCount(date));
                    dates.add(sdf.format(date));

                }
            }

            System.out.println(time);
            System.out.println(price);
            System.out.println(count);
            Map<String ,Object> map = new HashMap<>();

            map.put("dates",dates);
            map.put("counts",counts);
            map.put("prices",prices);

            String string = objectMapper.writeValueAsString(map);
            System.out.println(string);
        } catch (Exception e) {
            System.out.println("can not get code[" + code + "] message:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ParseException, IOException {

        TestMain  main = new TestMain();
        main.getGuDong();
        main.getPrice();
    }

}
