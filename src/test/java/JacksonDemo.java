import app.entity.TraderSession;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

public class JacksonDemo {
    public static void main(String[] args) throws ParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,true);
        String str ="{\"login_type\":null,\"returnJson\":\"{function_id: '401', msg_info: '', msg_no: '0', error_grids: '', grid_count: '1', Func401: [{ entrust_no: '委托编号', stock_code: '证券代码', entrust_bs: '买卖方向', entrust_price: '委托价格', entrust_amount: '委托数量', business_amount: '成交数量', business_price: '成交价格', report_time: '申报时间', entrust_status: '委托状态', stock_name: '证券名称'},{ entrust_no: '', stock_code: '', entrust_bs: '', entrust_price:'', entrust_amount:'', business_amount:'', business_price:'', report_time: '', entrust_status: '', stock_name: ''}], end: '0' }\"}";



        str = (str.substring(124,str.length()-14));
        System.out.println(str);

        List yjbEntrustResult =mapper.readValue(str, List.class);
        for(int i =0 ;i<yjbEntrustResult.size();i++){
            System.out.println(yjbEntrustResult.get(i));
        }

     /*   for(YJBEntrust obj : yjbEntrustResult.getList()){
            System.out.println(obj.getEntrust_no());
        }
*/
        JacksonDemo demo = new JacksonDemo();
       // demo.connectGf();
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