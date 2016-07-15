package app.service;

import app.bean.*;
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
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("TraderService")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Lazy(value = false)
public class TraderYJBService implements TraderService, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(TraderYJBService.class);
    @Autowired
    HolidayService holidayService;
    @Autowired
    TraderRepository traderRepository;
    @Autowired
    ObjectMapper jacksonObjectMapper;
    String userAgent = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E)";
    private Map<String, YJBAccount> yjbAccountMap = new HashMap<>();
    private Map<String, Func302> yjbAccountOrderMap = new HashMap<>();
    private Double yjbBalance;
    private Double lotsBalance = 10000d;
    private Boolean isLogin = false;
    BasicCookieStore cookieStore;
    TraderSession entity;

    @Override
    public void afterPropertiesSet() throws Exception {
        jacksonObjectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        jacksonObjectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        this.yjbBalance = 0d;
        this.cookieStore = new BasicCookieStore();
        this.entity = new TraderSession();
        //guo jin
        entity.setBrand("yjb");
        entity.setShAccount("A491467753");
        entity.setSzAccount("0126862343");
        entity.setSid("40132172");
        entity.setPassword("A+9BQUFnQUJBQUFnQVRuZnNuOE1ZTEJjOEJRWlE4VU9QZzRDc1l0Skx2VjlKUGFQZ1dabjZBdDJmNQ==");


        // System.out.println("yjbAccount afterPropertiesSet begin-----------------------------");
/*
        //Terry
        entity.setSid("40128457");
        entity.setShAccount("A131806813");
        entity.setSzAccount("0100368361");
        entity.setPassword("A+9BQUFnQUJBQUJRQ0VuWlY4UlNrMjh0RlVVOEN5dFpzOFVPUGc0Q3NZdEJVRHRaSlJMeUFQM2taSw==");
*/
      //  login();
    }

    @Scheduled(cron = "0/30 * 9-16 * * MON-FRI")
    public void cornJob() {
        if (holidayService.isTradeDayTimeByMarket()) {
            if(isLogin){
                yjbAccount();
                balance();
            }else{
                login();
            }

        }
        //log.info("lotsBalance : "+this.yjbBalance+" account:"+ yjbAccountMap);
    }

    @Override
    @Cacheable(value = "traderCache", key = "#id", unless = "#result == null")
    public Trader findOne(Long id) {
        log.info("get by db [" + id + "]");
        return traderRepository.findOne(id);
    }


    public List<YJBEntrust> entrustList(){
        try {
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setUserAgent(userAgent)
                    .build();
            HttpUriRequest trading = RequestBuilder.get()
                    .setUri(new URI("https://jy.yongjinbao.com.cn/winner_gj/gjzq/stock/exchange.action"))
                    .addParameter("CSRF_Token", "undefined")
                    .addParameter("request_id", "trust_401")
                    .addParameter("sort_direction", "1")
                    .addParameter("service_type", "stock")
                    .build();

            CloseableHttpResponse response3 = httpclient.execute(trading);
            HttpEntity entity = response3.getEntity();
            String str = IOUtils.toString(entity.getContent(), "UTF-8");
            EntityUtils.consume(entity);
            str = "["+(str.substring(348,str.length()-14));
            System.out.println(str);
            if(str.length()>50){
                List<YJBEntrust> beanList = jacksonObjectMapper.readValue(str, new TypeReference<List<YJBEntrust>>() {});
                System.out.println(beanList);
                return  beanList;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return  Collections.emptyList();
    }
    public void cancelEntrustDo(String code,String entrustNo){
        try {
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setUserAgent(userAgent)
                    .build();
            HttpUriRequest trading = RequestBuilder.get()
                    .setUri(new URI("https://jy.yongjinbao.com.cn/winner_gj/gjzq/stock/exchange.action"))
                    .addParameter("CSRF_Token", "undefined")
                    .addParameter("request_id", "chedan_304")
                    .addParameter("entrust_no", entrustNo)
                    .addParameter("stock_code", code)
                    .build();
            CloseableHttpResponse response3 = httpclient.execute(trading);
            HttpEntity entity = response3.getEntity();
            String remark = IOUtils.toString(entity.getContent(), "UTF-8");
            log.info(remark);
            EntityUtils.consume(entity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cancelEntrust(String code){
        List<YJBEntrust> list = entrustList();
        list.stream().filter(entrust -> code.equals(entrust.getStockCode()) && (entrust.getEntrustStatus().equals("正常") || entrust.getEntrustStatus().equals("已报")) ).forEach(entrust -> {
            cancelEntrustDo(code,entrust.getEntrustNo());
        });
    }

    @Override
    @CacheEvict(value = "traderCache", key = "#id")
    public  void trading(String market, Long id, String code, Integer _amount, String price, String type, Boolean fast) {
        if( Double.valueOf(price)*_amount > 100000){
            Func302 func302 =this.yjbAccountOrderMap.get(code);
            if(func302!=null){
                log.info("cancel entrust and order new item :" + func302);
                cancelEntrustDo(code,func302.getEntrust_no());
                yjbAccountOrderMap.remove(code);
            }
            int amount = tradingDo(market, id, code, price, type, fast);
            if(amount==0){
                cancelEntrust(code);
                try {
                    Thread.sleep(10000); //wait 10 sec;
                    yjbAccount();
                    log.info("- retry to call api-----id[" + id + "] code[" + code + "]  price[" + price + "] type[" + type + "]");
                    tradingDo(market, id, code, price, type, fast); // retry
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }else{

            log.info("less 10W ignore getDelegateID["+id+"]");
            Trader trader = new Trader();
            trader.setType(type);
            trader.setDelegateID(id);
            trader.setTransactionAmount(0);
            trader.setTransactionUnitPrice(Float.valueOf(price));
            trader.setCode(code);
            trader.setFast(fast);
            trader.setRemark("less 10W ignore getDelegateID["+id+"]");
            this.traderRepository.save(trader);
        }


    }

    public  int tradingDo(String market, Long id, String code, String price, String type, Boolean fast) {
        String account = null;
        String requestId = null;
        int amount = 0;
        String remark = null;
        if (market.equals("2")) {
            account = entity.getSzAccount();
        } else {
            account = entity.getShAccount();
        }
        if (type.equals("1")) {
            requestId = "buystock_302";
            Double balance;
            if (yjbBalance < lotsBalance && yjbBalance > 0d) {
                balance = yjbBalance;
            } else {
                balance = lotsBalance;
            }
            Double a = ((balance / Double.valueOf(price)) / 100d);
            amount = a.intValue() * 100;
        } else {
            requestId = "sellstock_302";
            YJBAccount yjbAccount = yjbAccountMap.get(code);
            if (yjbAccount != null) {
                amount = yjbAccount.getEnableAmount();
                yjbAccountMap.remove(code);
                log.info("stock amount in yjb is " + amount);
            } else {
                log.info("cant not find stock in yjb");
            }

        }

        if(amount>0 ){

            log.info("------id[" + id + "] code[" + code + "] amount[" + amount + "] price[" + price + "] type[" + type + "]");
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
                remark = org.apache.commons.lang3.StringUtils.replace(remark,"\"{","{");
                remark = org.apache.commons.lang3.StringUtils.replace(remark,"}\"","}");
                log.info(remark);
               // if (type.equals("1") ) {
                    YJBResult result =   jacksonObjectMapper.readValue(remark, YJBResult.class);
                    System.out.println(result);
                    YJBReturnJson returnJson = result.getReturnJson();
                    if(returnJson.getMsgNo().equals("0") && returnJson.getFunc302().size()==2){
                        Func302 func302 = returnJson.getFunc302().get(1);
                        log.info("success order: " + func302);
                        yjbAccountOrderMap.put(code,func302);
                    }
               // }
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


        return  amount;
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
                isLogin = false;
            } else {
                String str = "[" + (result.substring(346, result.length() - 14));
                //log.info("yo hua:"+str);
                if (str.length() > 50) {
                    List<YJBAccount> list = jacksonObjectMapper.readValue(str, new TypeReference<List<YJBAccount>>() {
                    });
                    //if (list.size() > 1) {
                    for (YJBAccount bean : list) {
                        //System.out.println(bean);
                        yjbAccountMap.put(bean.getStockCode(), bean);
                    }
                    //}
                }
            }
        } catch (IOException e) {
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


    public synchronized void login() {
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
                        isLogin= true;
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
