package app;

import app.bean.Account;
import app.entity.AccountData;
import app.service.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.URL;

@SpringBootApplication
@EnableScheduling
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    ObjectMapper jacksonObjectMapper;

    public static void main(String[] args) throws Exception {
      //  SpringApplication.run(Application.class);
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    /*    System.out.println("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }*/
    }

   /* @Bean
    public CommandLineRunner demo(AccountRepository repository) {
        return (args) -> {
            // save a couple of customers

            URL url = new URL("https://swww.niuguwang.com/tr/201411/account.ashx?aid=773183&s=xiaomi&version=3.4.4&packtype=1");
            Account bean = jacksonObjectMapper.readValue(url, Account.class);
           // repository.save(bean.getAccountData());
            log.info("");

        };
    }*/
}
