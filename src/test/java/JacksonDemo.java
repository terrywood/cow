import app.entity.TraderSession;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

public class JacksonDemo {
    public static void main(String[] args) throws ParseException, IOException {
    /*    ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
*/
        JacksonDemo demo = new JacksonDemo();
        demo.connectGf();
    }


    public void connectGf() {
        System.out.println("run connectGf");
        TraderSession entity = new TraderSession();
        entity.setCookie("name=value; JSESSIONID=8787931B98548A1BAD41E2C043F09B93; dse_sessionId=56E8D78B04F4E6937BD85CA3BB8F5028; userId=J*1C*8F*106*C1*F1*28*C6r*96k1p*B3*BBG*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00");
        entity.setSid("56E8D78B04F4E6937BD85CA3BB8F5028");
        if (entity != null) {
            String httpUrl ="https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.StockUF2Control&method=queryFund&_dc="+System.currentTimeMillis()+"&dse_sessionId="+entity.getSid();
            try {

                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Cookie", entity.getCookie());
                connection.connect();
                String result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                System.out.println(result);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}