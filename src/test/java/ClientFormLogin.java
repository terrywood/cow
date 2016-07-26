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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
/**
 * Created by Administrator on 16-4-14.
 */
public class ClientFormLogin {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .setUserAgent("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E)")
                .build();
        try {
           /* HttpGet httpget = new HttpGet("https://jy.yongjinbao.com.cn/winner_gj/gjzq/");
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
            }*/

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
                String result = IOUtils.toString(entity.getContent(), "UTF-j8");
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
