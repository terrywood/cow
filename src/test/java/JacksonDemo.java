import app.bean.YJBAccount;
import app.bean.YJBBalance;
import app.entity.TraderSession;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
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
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,true);
       // String str ="{\"login_type\":null,\"returnJson\":\"{function_id: '403', msg_info: '', msg_no: '0', error_grids: '', grid_count: '1', Func403: [{position_str: '定位串', stock_code: '证券代码', stock_name: '证券名称', current_amount: '当前数量', enable_amount: '可卖数量', last_price: '最新价', cost_price: '摊薄成本价', keep_cost_price: '保本价', income_balance: '摊薄浮动盈亏', market_value: '证券市值'},{position_str: '0003300000000004013217200010000000000A491467753600480', stock_code: '600480', stock_name: '凌云股份', current_amount:'100', enable_amount:'0', last_price:'13.110', cost_price:'13.043', keep_cost_price:'13.043', income_balance:'6.630', market_value:'1311.000'}, {position_str: '00033000000000040132172000200000000000126862343000731', stock_code: '000731', stock_name: '四川美丰', current_amount:'0', enable_amount:'0', last_price:'7.570', cost_price:'7.560', keep_cost_price:'7.560', income_balance:'-13.250', market_value:'0.000'}], end: '0' }\"}";
        String str ="{\"login_type\":null,\"returnJson\":\"{function_id: '405', msg_info: '', msg_no: '0', error_grids: '', grid_count: '1', Func405: [{money_type: '币种', current_balance: '当前余额', enable_balance: '可用金额', market_value: '证券市值', asset_balance: '资产总值', pre_interest: '预计利息'},{money_type:'人民币', current_balance:'1.500', enable_balance:'2785.800', market_value:'1309.000', asset_balance:'4828.300', pre_interest:'0.000'}], end: '0' }\"}";
        str = (str.substring(260,str.length()-15));
        System.out.println(str);
        if(str.length()>50){
            YJBBalance yjbBalance = mapper.readValue(str, YJBBalance.class);
           // System.out.println(yjbBalance);

          /*  List<YJBAccount> list =mapper.readValue(str, new TypeReference<List<YJBAccount>>() {});
            if(list.size()>1){
                for(YJBAccount bean : list){
                    System.out.println(bean);
                }
            }*/
        }

        Double balance= 2569d;
        String price ="5.2";

        Double a = ((balance/Double.valueOf(price)) /100d) ;
        int amount = a.intValue()*100;

        System.out.println(amount);

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