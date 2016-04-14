package app;

import app.entity.StockListData;
import app.entity.TraderSession;
import app.service.StockListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@EnableCaching
@SpringBootApplication
@EnableScheduling
@Configuration
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    ObjectMapper jacksonObjectMapper;
  /*  @Autowired
    TraderSessionService traderSessionService;*/

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);
        /*ApplicationContext ctx = SpringApplication.run(Application.class, args);
       System.out.println("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }*/

    }

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cmfb.setShared(true);
        return cmfb;
    }


/*   @Bean
    public CommandLineRunner demo() {
        return (args) -> {

            TraderSession entity = new TraderSession();
            //entity.setCookie("name=value; JSESSIONID=7FBD68852BD89E79C5D1102E2B8B64F0; dse_sessionId=64D3943AF312E53AC58207CC87615941; userId=J*1C*8F*106*C1*F1*28*C6r*96k1p*B3*BBG*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00");
            entity.setCookie("name=value; JSESSIONID=81CAD6382C06FF18AEDEF9857B207A28; dse_sessionId=3814400D19783EDC6BED1A48297079BF; userId=J*1C*8F*106*C1*F1*28*C6r*96k1p*B3*BBG*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00");
            entity.setSid("3814400D19783EDC6BED1A48297079BF");

            for(long i=0; i<Long.MAX_VALUE;i++){

                String httpUrl ="https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.StockUF2Control&method=queryFund&_dc="+System.currentTimeMillis()+"&dse_sessionId="+entity.getSid();
                try {
                    URL url = new URL(httpUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Cookie", entity.getCookie());
                    connection.connect();
                    String result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    //Map map = jacksonObjectMapper.readValue(connection.getInputStream(), Map.class);
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.sleep(5000);
            }


        };
    }*/
}
