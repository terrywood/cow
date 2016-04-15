import cn.skypark.code.MyCheckCodeTool;
import org.apache.commons.codec.binary.StringUtils;
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
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;
/**
 * Created by Administrator on 16-4-14.
 */
public class ClientFormLogin {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        BasicCookieStore cookieStore = new BasicCookieStore();
 /*       Cookie cookie = new BasicClientCookie("JSESSIONID","B9A36160C47F08B46E037FAF638757A4");
        Cookie cookie2 = new BasicClientCookie("pgv_pvi","595542016");
        Cookie cookie3 = new BasicClientCookie("pgv_si","s7088572416");
        cookieStore.addCookie(cookie);
        cookieStore.addCookie(cookie2);
        cookieStore.addCookie(cookie3);*/
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        try {
            HttpGet httpget = new HttpGet("https://jy.yongjinbao.com.cn/winner_gj/gjzq/");
            CloseableHttpResponse response1 = httpclient.execute(httpget);
            try {
                HttpEntity entity = response1.getEntity();

                System.out.println("Login form get: " + response1.getStatusLine());
                EntityUtils.consume(entity);

                System.out.println("Initial set of cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response1.close();
            }

            HttpGet httpget3 = new HttpGet("https://jy.yongjinbao.com.cn/winner_gj/gjzq/user/extraCode.jsp");
            CloseableHttpResponse response3 = httpclient.execute(httpget3);


            HttpEntity entity3 = response3.getEntity();
            BufferedImage image = ImageIO.read(entity3.getContent());


            EntityUtils.consume(entity3);


          /*  URL url = new URL("https://jy.yongjinbao.com.cn/winner_gj/gjzq/user/extraCode.jsp");
            BufferedImage image = ImageIO.read(url);
*/
            MyCheckCodeTool tool = new MyCheckCodeTool("guojin");
            String code = tool.getCheckCode_from_image(image);

            //String code = "2342";

            System.out.println("very code["+code+"]");
            HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI("https://jy.yongjinbao.com.cn/winner_gj/gjzq/exchange.action"))
                    .addParameter("validateCode", code)
                    .addParameter("account_content", "40128457")
                    .addParameter("password", "A%2B9BQUFnQUJBQUFRQk5mcSttVjdjRXJjN2FqRHhRNCtEZ0t4aTBZL3J1MFp4eW9idWx1bXJpSFdNOQ%3D%3D")
                    .addParameter("function_id", "200")
                    .addParameter("login_type", "stock")
                    .addParameter("version", "200")
                    .addParameter("identity_type", "")
                    .addParameter("remember_me", "")
                    .addParameter("input_content", "1")
                    .addParameter("content_type", "0")
                    .addParameter("loginPasswordType", "B64")
                    .addParameter("disk_serial_id", "")
                    .addParameter("mac_addr", "1C-39-47-20-CA-3A")
                    .addParameter("cpuid", "-306D4-7FFAFBBF")
                    .addParameter("machinecode", "-306D4-7FFAFBBF")
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
        System.out.println("use times :"+end);
    }
}
