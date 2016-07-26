package app.service;

import app.bean.CowResult;
import app.entity.Trader;
import app.repository.TraderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
@Service("TraderService")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Lazy(value = false)
public class TraderCowService implements TraderService {
    private static final Logger log = LoggerFactory.getLogger(TraderCowService.class);
    @Autowired
    TraderRepository traderRepository;
    @Autowired
    ObjectMapper jacksonObjectMapper;
    private Double lotsBalance = 100000d;
    //private Double yjbBalance  = 100000d;
    private String userToken = "hleVEQYrnxdWRXiQjj1IN1nVq1Va7aqF37J5L8I56leXtXOhwkCl1Q**";

    @Override
    @Cacheable(value = "traderCache",key = "#id" ,unless="#result == null")
    public Trader findOne(Long id) {
        log.info("get Trader by db ---------->> [" + id + "]");
        return traderRepository.findOne(id);
    }


    private String checkAmount(String code, String price, String type, Integer amount){
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL("https://swww.niuguwang.com/tr/201411/getstock.ashx?usertoken="+userToken+"&stockCode="+code+"&contest=1&s=xiaomi&version=3.5.4&packtype=1");
            CowResult bean = jacksonObjectMapper.readValue(url, CowResult.class);
            log.info("checkAmount:"+bean);

                if(type.equals("1")){
                    Integer maxBuy = bean.getMaxBuy();
                    sb.append("&mp=0");
                   /* Double  buyAccount = (lotsBalance / Double.valueOf(price))/100d;
                    amount = buyAccount.intValue() *100;*/
                    if(maxBuy<amount){
                        amount = maxBuy-100;
                    }
                }else{
                    sb.append("&mp=1");
                    Integer  maxSell = bean.getMaxSell();
                    if(maxSell==0){
                       return "";
                    }else if(maxSell<amount){
                        amount =maxSell;
                    }
                }


                sb.append("&type=");
                sb.append(type);
                sb.append("&price=");
                sb.append(price);
                sb.append("&innerCode=");
                sb.append(bean.getInnercode());
                sb.append("&amount=");
                sb.append(amount);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    @CacheEvict(value = "traderCache",key = "#id")
    public void trading(String market, Long id, String code, Integer amount, String price, String type, Boolean fast) {
        String remark = null;

        System.out.println("code["+code+"] amount["+amount+"] price["+price+"] type["+type+"] id["+id+"] ");

        //if( Double.valueOf(price)*amount > 100000){

            String params = this.checkAmount(code,price,type,amount );

            if(StringUtils.hasText(params)){
                try {
                    URL url = new URL("https://swww.niuguwang.com/tr/delegateadd.ashx?usertoken="+userToken+"&contest=1&share=0&buy=0&plan=0&s=xiaomi&version=3.5.4&packtype=1"+params);
                    CowResult bean = jacksonObjectMapper.readValue(url, CowResult.class);
                    log.info(bean.toString());
                     remark  = bean.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                remark ="checkAmount return null getDelegateID["+id+"]";
            }


       // }else{
       //      remark ="less 10W ignore getDelegateID["+id+"]";
      //  }

        Trader obj = new Trader();
        obj.setType(type);
        obj.setDelegateID(id);
        obj.setTransactionAmount(0);
        obj.setTransactionUnitPrice(Float.valueOf(price));
        obj.setCode(code);
        obj.setFast(fast);
        obj.setRemark(remark);
        this.traderRepository.save(obj);
    }
}
