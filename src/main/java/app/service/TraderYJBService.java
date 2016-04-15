package app.service;

import app.entity.Trader;
import app.entity.TraderSession;
import app.repository.TraderRepository;
import cn.skypark.code.MyCheckCodeTool;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;


@Service("TraderService")
@CacheConfig(cacheNames = "trader")
public class TraderYJBService implements  TraderService {
    private static final Logger log = LoggerFactory.getLogger(TraderYJBService.class);
    @Autowired
    TraderRepository traderRepository;
    BasicCookieStore cookieStore = new BasicCookieStore();
    CloseableHttpClient httpclient = HttpClients.custom()
            .setDefaultCookieStore(cookieStore)
            .setUserAgent("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E)")
            .build();

    @Override
    @Cacheable(value="trader",key="#id")
    public Boolean exists(Long id){
        return  traderRepository.exists(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void save(Trader entity){
        traderRepository.save(entity);
    }

    public  void balance() throws IOException {
        HttpGet httpget3 = new HttpGet("https://jy.yongjinbao.com.cn/winner_gj/gjzq/user/extraCode.jsp");
        CloseableHttpResponse response3 = httpclient.execute(httpget3);
        HttpEntity entity = response3.getEntity();
        String result = IOUtils.toString(entity.getContent(), "UTF-8");     ;
        EntityUtils.consume(entity);
        System.out.println("result:" +result);

    }
    public void login() throws IOException, URISyntaxException {
        long start = System.currentTimeMillis();

        try {
            HttpGet httpget3 = new HttpGet("https://jy.yongjinbao.com.cn/winner_gj/gjzq/user/extraCode.jsp");
            CloseableHttpResponse response3 = httpclient.execute(httpget3);
            HttpEntity entity3 = response3.getEntity();
            BufferedImage image = ImageIO.read(entity3.getContent());
            EntityUtils.consume(entity3);
            MyCheckCodeTool tool = new MyCheckCodeTool("guojin");
            String code = tool.getCheckCode_from_image(image);
            HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI("https://jy.yongjinbao.com.cn/winner_gj/gjzq/exchange.action"))
                    .addParameter("function_id", "200")
                    .addParameter("login_type", "stock")
                    .addParameter("version", "200")
                    .addParameter("identity_type", "")
                    .addParameter("remember_me", "")
                    .addParameter("input_content", "1")
                    .addParameter("content_type", "0")
                    .addParameter("account_content", "40128457")
                    .addParameter("password", "A+9BQUFnQUJBQUJRQWdxZXI3Qkh0SDRmZXg5alYvK1VVOFVPUGc0Q3NZdEljcU5aeERtTUtYL3R5bQ==")
                    .addParameter("loginPasswordType", "B64")
                    .addParameter("validateCode", code)
                    .addParameter("mac_addr", "54-59-57-07-B9-0F")
                    .addParameter("cpuid", "-306C3-7FFAFBBF")
                    .addParameter("disk_serial_id", "WD-WMC3F0J4P22T")
                    .addParameter("machinecode", "-306C3-7FFAFBBF")
                    .setHeader("Referer","https://jy.yongjinbao.com.cn/winner_gj/gjzq/")
                    .build();

            CloseableHttpResponse response2 = httpclient.execute(login);
            try {
                HttpEntity entity = response2.getEntity();
                System.out.println("Login form get: " + response2.getStatusLine());
                String result = IOUtils.toString(entity.getContent(), "UTF-8");     ;
                EntityUtils.consume(entity);
                System.out.println("result:" +result);
                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response2.close();
            }
        } finally {
            httpclient.close();
        }
        long end=  System.currentTimeMillis() - start;
        log.info("use times :"+end);
    }

    @Override
    public void trading(String market, Long id, String code, Integer amount, String price, String type, Boolean fast) {
        if (!exists(id)) {
            TraderSession entity = new TraderSession();
            String account = null;
            if (market.equals("2")) {
                account = entity.getSzAccount();
            } else {
                account = entity.getShAccount();
            }
            String httpUrl = "https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.StockUF2Control&method=entrust&dse_sessionId=" + entity.getSid();
            try {
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Cookie", entity.getCookie());
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
                String result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Trader trader = new Trader();
            trader.setType(type);
            trader.setDelegateID(id);
            trader.setTransactionAmount(amount);
            trader.setTransactionUnitPrice(Float.valueOf(price));
            trader.setCode(code);
            trader.setFast(fast);
            save(trader);
        }
    }

    public static void main(String[] args) throws ParseException, IOException {
           TraderYJBService service = new TraderYJBService();
        try {
            service.login();
            service.balance();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

 /*   @CacheEvict(value="trader",key="#id")
    public void delete(Long id){
        traderRepository.delete(id);
    }*/
}
