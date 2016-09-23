package app.service;

import app.bean.WeiCard;
import app.bean.WeiSheep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class SheepService implements InitializingBean{
    private static final Logger log = LoggerFactory.getLogger(SheepService.class);
    @Autowired
    ObjectMapper objectMapper;

    String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";
    BasicCookieStore cookieStore  = new BasicCookieStore();

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public List<WeiSheep> get() throws IOException {
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .setUserAgent(userAgent)
                .build();
        HttpGet httpget3 = new HttpGet("http://m.weibo.cn/container/getIndex?containerid=100808c89f05c71c753fc25d508164539f9303");
        httpget3.setHeader("Referer","http://m.weibo.cn/p/100808c89f05c71c753fc25d508164539f9303");
        CloseableHttpResponse response3 = httpclient.execute(httpget3);
        WeiCard obj = objectMapper.readValue(response3.getEntity().getContent(), WeiCard.class);
        List<WeiSheep> ret = obj.getList();
        Collections.sort(ret, new Comparator<WeiSheep>() {
            @Override
            public int compare(WeiSheep o1, WeiSheep o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return  ret;
    }
}
