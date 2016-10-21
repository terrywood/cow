import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class XueQieDemo {
    private static String userAgent ="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    private static String site = "http://www.zhuoniugu.com/api.php";
    private ObjectMapper objectMapper = new ObjectMapper();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static void main(String[] args) throws ParseException, IOException {
        XueQieDemo demo = new XueQieDemo();
        demo.login2("sh000001","sh000001");
    }

    public void login2(String sinaCode,String ifengCode) throws ParseException, IOException {


/*

        String code ="000850";
        List<Object[]> list = new ArrayList<>();
        String market = code.startsWith("6") ? "sh" : "sz";
        try {
            Date csvStartDate =DateUtils.parseDate("2016-01-01","yyyy-MM-dd");
            URL url = new URL("http://api.finance.ifeng.com/akdaily/?code=" + market + code + "&type=last");
            ApiDayResult result = objectMapper.readValue(url, ApiDayResult.class);
            List<String[]> list2 = result.getRecord();
            for (String[] args : list2) {
                Date date = sdf.parse(args[0]);
                if (date.after(csvStartDate)) {
                    String price  = args[3];
                    Object[] data = new Object[]{date.getTime(), Double.valueOf(price.trim())};
                    list.add(data);
                }
            }
        } catch (Exception e) {
            System.out.println("can not get code[" + code + "] message:" + e.getMessage());
            e.printStackTrace();
        }
        System.out.println(list);*/
    }
    public void login() throws ParseException, IOException {
        Document doc = null;
        try {
            doc = Jsoup.connect(site).userAgent(userAgent)
                    .data("c","hudong")
                    .data("a","gudong")
                    .data("show","html")
                    .data("stock_id","002679")
                    .get();
            Element element = doc.getElementsByAttributeValue("style","text-align:center").get(2);
            String raw[] = StringUtils.split(element.html(),"<br>");
            List<Object[]> list = new ArrayList<>();
            for(String day :raw){
                String[] row = StringUtils.split(day.trim(),"|");
                long time = sdf.parse(row[0].trim()).getTime();
                Object[] data = new Object[]{time,Integer.valueOf(row[1].trim())};
                list.add(0,data);
            }

            String json2 = objectMapper.writeValueAsString(list);
            System.out.println("The JSON from Class is: " + json2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}