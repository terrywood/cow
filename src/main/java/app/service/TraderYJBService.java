package app.service;

import app.bean.YJBAccount;
import app.bean.YJBBalance;
import app.entity.Trader;
import app.entity.TraderSession;
import app.repository.TraderRepository;
import cn.skypark.code.MyCheckCodeTool;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Service("TraderService")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Lazy(value = false)
public class TraderYJBService implements TraderService, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(TraderYJBService.class);
    @Autowired
    TraderRepository traderRepository;
    @Autowired
    ObjectMapper jacksonObjectMapper;
    String userAgent = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E)";
    private Map<String, YJBAccount> yjbAccountMap = new HashMap<>();
    private Double yjbBalance;
    private Double lotsBalance = 2500d;
    BasicCookieStore cookieStore;
    TraderSession entity;
    @Override
    public void afterPropertiesSet() throws Exception {
        jacksonObjectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        jacksonObjectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        this.yjbBalance = 0d;
        this.cookieStore = new BasicCookieStore();
        this.entity = new TraderSession();
        entity.setBrand("yjb");
        entity.setShAccount("A491467753");
        entity.setSzAccount("0126862343");
        //guo jin
        entity.setSid("40132172");
        entity.setPassword("A+9BQUFnQUJBQUFnQVRuZnNuOE1ZTEJjOEJRWlE4VU9QZzRDc1l0Skx2VjlKUGFQZ1dabjZBdDJmNQ==");
       // System.out.println("yjbAccount afterPropertiesSet begin-----------------------------");
/*
        entity.setSid("40128457");
        entity.setPassword("A+9BQUFnQUJBQUJRQ0VuWlY4UlNrMjh0RlVVOEN5dFpzOFVPUGc0Q3NZdEJVRHRaSlJMeUFQM2taSw==");
*/
    }

    @Scheduled(cron = "0/30 * 9-16 * * MON-FRI")
    public void cornJob(){
        yjbAccount();
        balance();
       //log.info("lotsBalance : "+this.yjbBalance+" account:"+ yjbAccountMap);
    }

    @Override
    @Cacheable(value = "traderCache",key = "#id" ,unless="#result == null")
    public Trader findOne(Long id) {
        System.out.println("get by db [" + id + "]");
        return traderRepository.findOne(id);
    }
    @Override
    @CacheEvict(value = "traderCache",key = "#id")
    public void trading(String market, Long id, String code, Integer _amount, String price, String type, Boolean fast) {
        String account = null;
        String requestId = null;
        Integer amount = 0;
        String remark = null;
        if (market.equals("2")) {
            account = entity.getSzAccount();
        } else {
            account = entity.getShAccount();
        }
        if (type.equals("1")) {
            requestId = "buystock_302";
            Double balance ;
            if (yjbBalance < lotsBalance && yjbBalance > 0d) {
                balance = yjbBalance;
            }else{
                balance = lotsBalance;
            }
            Double a = ((balance / Double.valueOf(price)) / 100d);
            amount = a.intValue() * 100;

        } else {
            log.info("-------------sell-----------sell--------------");
            System.out.println(yjbAccountMap);
            requestId = "sellstock_302";
            YJBAccount yjbAccount = yjbAccountMap.get(code);
            if (yjbAccount != null) {
                amount = yjbAccount.getEnableAmount();
                yjbAccountMap.remove(code);
                System.out.println("stock amount in yjb is "+amount);
            }else{
                System.out.println("cant not find stock in yjb");
            }

        }

        if (amount > 0) {
            try {
                CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                        .setUserAgent(userAgent)
                        .build();
                HttpUriRequest trading = RequestBuilder.get()
                        .setUri(new URI("https://jy.yongjinbao.com.cn/winner_gj/gjzq/stock/exchange.action"))
                        .addParameter("CSRF_Token", "undefined")
                        .addParameter("request_id", requestId)
                        .addParameter("stock_account", account)
                        .addParameter("exchange_type", market)
                        .addParameter("entrust_prop", "0")
                        .addParameter("entrust_bs", type)
                        .addParameter("stock_code", code)
                        .addParameter("entrust_price", price)
                        .addParameter("entrust_amount", String.valueOf(amount))
                        .addParameter("elig_riskmatch_flag", "1")
                        .addParameter("service_type", "stock")
                        .build();


                CloseableHttpResponse response3 = httpclient.execute(trading);
                HttpEntity entity = response3.getEntity();
                remark = IOUtils.toString(entity.getContent(), "UTF-8");
                System.out.println(remark);
                EntityUtils.consume(entity);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Trader trader = new Trader();
        trader.setType(type);
        trader.setDelegateID(id);
        trader.setTransactionAmount(amount);
        trader.setTransactionUnitPrice(Float.valueOf(price));
        trader.setCode(code);
        trader.setFast(fast);
        trader.setRemark(remark);
        this.traderRepository.save(trader);
    }

    public void yjbAccount() {
        try {
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setUserAgent(userAgent)
                    .build();
            HttpGet httpget = new HttpGet("https://jy.yongjinbao.com.cn/winner_gj/gjzq/stock/exchange.action?CSRF_Token=undefined&service_type=stock&sort_direction=0&request_id=mystock_403");
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String result = IOUtils.toString(entity.getContent(), "UTF-8");
            //log.info(result);
            if (result.indexOf("msg_no: '0'") == -1) {
                login();
            } else {
                String str = "[" + (result.substring(346, result.length() - 14));
                //log.info("yo hua:"+str);
                if (str.length() > 50) {
                    List<YJBAccount> list = jacksonObjectMapper.readValue(str, new TypeReference<List<YJBAccount>>() {});
                    //if (list.size() > 1) {
                        for (YJBAccount bean : list) {
                            //System.out.println(bean);
                            yjbAccountMap.put(bean.getStockCode(), bean);
                        }
                    //}
                }
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void balance() {
        try {
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setUserAgent(userAgent)
                    .build();
            HttpGet httpget3 = new HttpGet("https://jy.yongjinbao.com.cn/winner_gj/gjzq/stock/exchange.action?request_id=mystock_405");
            CloseableHttpResponse response3 = httpclient.execute(httpget3);
            HttpEntity entity = response3.getEntity();
            String str = IOUtils.toString(entity.getContent(), "UTF-8");
            //log.info(result);
           /*if(result.indexOf("msg_no: '0'")==-1){
                login();
            }*/
            str = (str.substring(260, str.length() - 15));
            //log.info("yjb lotsBalance result :" + str);
            YJBBalance yjbBalance = this.jacksonObjectMapper.readValue(str, YJBBalance.class);
            this.yjbBalance = yjbBalance.getEnableBalance();
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void login() {
        try {
            long start = System.currentTimeMillis();
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .setUserAgent(userAgent)
                    .build();
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
                        .addParameter("account_content", entity.getSid())
                        .addParameter("password", entity.getPassword())
                        .addParameter("loginPasswordType", "B64")
                        .addParameter("validateCode", code)
                        .addParameter("mac_addr", "54-59-57-07-B9-0F")
                        .addParameter("cpuid", "-306C3-7FFAFBBF")
                        .addParameter("disk_serial_id", "WD-WMC3F0J4P22T")
                        .addParameter("machinecode", "-306C3-7FFAFBBF")
                        .setHeader("Referer", "https://jy.yongjinbao.com.cn/winner_gj/gjzq/")
                        .build();

                CloseableHttpResponse response2 = httpclient.execute(login);
                try {
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
                }
            } finally {
                httpclient.close();
            }
            long end = System.currentTimeMillis() - start;
            log.info("use times :" + end);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    public void cancelEntrust(String code,String account,String market){

            try {
                CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                        .setUserAgent(userAgent)
                        .build();
                HttpUriRequest trading = RequestBuilder.get()
                        .setUri(new URI("https://jy.yongjinbao.com.cn/winner_gj/gjzq/stock/exchange.action"))
                        .addParameter("CSRF_Token", "undefined")
                        .addParameter("request_id", "chedan_304")
                        .addParameter("stock_account", account)
                        .addParameter("exchange_type", market)
                        .addParameter("entrust_no", entrustNo)
                        .addParameter("stock_code",code )
                        .build();
                CloseableHttpResponse response3 = httpclient.execute(trading);
                HttpEntity entity = response3.getEntity();
                System.out.println(EntityUtils.toString(entity));
                EntityUtils.consume(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }*/



/*
 public static void main(String[] args) throws ParseException, IOException {
        TraderYJBService service = new TraderYJBService();
        try {
            service.afterPropertiesSet();
            service.jacksonObjectMapper = new ObjectMapper();
            service.login();
            service.trust401();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


}
