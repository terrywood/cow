import app.bean.WeiCard;
import app.bean.WeiSheep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WeiboDemo {

    String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";
    BasicCookieStore cookieStore  = new BasicCookieStore();

    public static void main(String[] args) throws ParseException, IOException {
        WeiboDemo demo = new WeiboDemo();
        demo.login();

/*
        String d ="Tue Aug 30 14:50:57 +0800 2016";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.US);
        System.out.println(sdf.format(new Date()));
        System.out.println(sdf.parse(d));
*/

    }



    public void login() throws ParseException, IOException {
        System.out.println("--------------------------------------------");
        try {
            long start = System.currentTimeMillis();
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .setUserAgent(userAgent)
                    .build();
            try {
                HttpGet httpget3 = new HttpGet("http://m.weibo.cn/container/getIndex?containerid=100808c89f05c71c753fc25d508164539f9303");
                httpget3.setHeader("Referer","http://m.weibo.cn/p/100808c89f05c71c753fc25d508164539f9303");
                CloseableHttpResponse response3 = httpclient.execute(httpget3);
                HttpEntity entity3 = response3.getEntity();
                String content2 =EntityUtils.toString(entity3);
                System.out.println(content2);
                EntityUtils.consume(entity3);
                ObjectMapper objectMapper = new ObjectMapper();
                WeiCard obj = objectMapper.readValue(entity3.getContent(), WeiCard.class);
                List<WeiSheep> ret = obj.getList();
                Collections.sort(ret, new Comparator<WeiSheep>() {
                    @Override
                    public int compare(WeiSheep o1, WeiSheep o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                });

                System.out.println(obj);

                /*
                MyCheckCodeTool tool = new MyCheckCodeTool("guojin");
                String code = tool.getCheckCode_from_image(image);
                HttpUriRequest login = RequestBuilder.post()
                        .setUri(new URI("https://jy.yongjinbao.com.cn/winner_gj/gjzq/exchange.action"))

                        .build();

                CloseableHttpResponse response2 = httpclient.execute(login);*/
               /* try {
                    HttpEntity entity = response2.getEntity();
                    System.out.println("Login form get: " + response2.getStatusLine());
                    String result = IOUtils.toString(entity.getContent(), "UTF-8");
                    EntityUtils.consume(entity);
                    System.out.println("result:" + result);
                    System.out.println("Post logon cookies:");
                    List<Cookie> cookies = cookieStore.getCookies();
                    if (cookies.isEmpty()) {
                        System.out.println("None");
                    } else {
                        for (int i = 0; i < cookies.size(); i++) {
                            //cookieStore.addCookie(cookies.get(i));
                            System.out.println("- " + cookies.get(i).toString());
                        }
                    }
                } finally {
                    response2.close();
                }*/
            } finally {
                httpclient.close();
            }

        } catch (Exception e) {
             e.printStackTrace();
        }



    }


}