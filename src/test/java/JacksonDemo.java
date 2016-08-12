import app.bean.StockList;
import app.bean.YJBResult;
import app.entity.HistoryData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class JacksonDemo {

    ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws ParseException, IOException {
        JacksonDemo demo = new JacksonDemo();
        //demo.gf();
        demo.cow();

    }



    public void stocklistitem() throws ParseException, IOException {
        URL url = new URL("https://swww.niuguwang.com/tr/201411/stocklistitem.ashx?id=20635635&s=xiaomi&version=3.4.4&packtype=1");
        ObjectMapper jacksonObjectMapper = new ObjectMapper();
        StockList bean = jacksonObjectMapper.readValue(url, StockList.class);
        List<HistoryData> list =(bean.getHistoryData());
        for(HistoryData data:list){
            // only handle 10 min around order
            if(System.currentTimeMillis()-data.getAddTime().getTime()<= (1000 *60 *10)){
                System.out.println(data);
            }

        }
    }


    public void cow() throws ParseException, IOException {
        URL url = new URL("https://swww.niuguwang.com/foll/api/friendmylist.ashx?usertoken=hleVEQYrnxdWRXiQjj1IN1nVq1Va7aqF37J5L8I56leXtXOhwkCl1Q**&type=2&index=1&size=60&s=xiaomi&version=3.5.4&packtype=1");
        Map map = objectMapper.readValue(url,java.util.Map.class);
        List<Map> data = (List<Map>) map.get("data");
        String out = "";
        for(Map user : data){
            Integer userID = (Integer)user.get("userID");
            //String userName = (String)user.get("userName");
            URL url2 = new URL("https://str.niuguwang.com/tr/2016/other001.ashx?userid="+userID+"&usertoken=hleVEQYrnxdWRXiQjj1IN1nVq1Va7aqF37J5L8I56leXtXOhwkCl1Q**&s=xiaomi&version=3.5.4&packtype=1");
            Map acc = objectMapper.readValue(url2,java.util.Map.class);
            List<Map> datas = (List<Map>) acc.get("datas");
            String accountID = (String) datas.get(0).get("accountID");
            out+= accountID +",";
        }

        System.out.println(out);
    }

    public  void gf() throws ParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,true);
       // String str ="{\"login_type\":null,\"returnJson\":\"{function_id: '403', msg_info: '', msg_no: '0', error_grids: '', grid_count: '1', Func403: [{position_str: '定位串', stock_code: '证券代码', stock_name: '证券名称', current_amount: '当前数量', enable_amount: '可卖数量', last_price: '最新价', cost_price: '摊薄成本价', keep_cost_price: '保本价', income_balance: '摊薄浮动盈亏', market_value: '证券市值'},{position_str: '0003300000000004013217200010000000000A491467753600480', stock_code: '600480', stock_name: '凌云股份', current_amount:'100', enable_amount:'0', last_price:'13.110', cost_price:'13.043', keep_cost_price:'13.043', income_balance:'6.630', market_value:'1311.000'}, {position_str: '00033000000000040132172000200000000000126862343000731', stock_code: '000731', stock_name: '四川美丰', current_amount:'0', enable_amount:'0', last_price:'7.570', cost_price:'7.560', keep_cost_price:'7.560', income_balance:'-13.250', market_value:'0.000'}], end: '0' }\"}";

    /*    String str ="{\"login_type\":null,\"returnJson\":\"{function_id: '405', msg_info: '', msg_no: '0', error_grids: '', grid_count: '1', Func405: [{money_type: '币种', current_balance: '当前余额', enable_balance: '可用金额', market_value: '证券市值', asset_balance: '资产总值', pre_interest: '预计利息'},{money_type:'人民币', current_balance:'1.500', enable_balance:'2785.800', market_value:'1309.000', asset_balance:'4828.300', pre_interest:'0.000'}], end: '0' }\"}";
        str = (str.substring(260,str.length()-15));
 */
//{"login_type":null,"returnJson":"{function_id: '302', msg_info: '', msg_no: '-10000', error_grids: 'Func302', grid_count: '2', CSRF_Token:'Njg3ODg1ODE5NTQzMTIyNzcxMTc4ODY3MjMyNDMzMDQ0MjUzODU=', Func302: [{error_pathinfo: '', error_no: '错误号', error_info: '错误原因'},{error_pathinfo: 'F333002() -> F2250003() -> F3103000()', error_no: '-57', error_info: '可用资金不足'}], end: '-10000' }"}
        String str ="{\"login_type\":null,\"returnJson\":\"{function_id: '302', msg_info: '', msg_no: '0', error_grids: '', grid_count: '2', CSRF_Token:'NzY0NjIwOTUyMjMxNzA5NDU1My05MDIwNjIzMDkwODAyODY0NTk3', Func302: [{entrust_no: '委托编号', init_date: '发生日期', batch_no: '委托批号', report_no: '申报号', seat_no: '席位编号', entrust_time: '委托时间', entrust_price: '委托价格', entrust_amount: '委托数量', stock_code: '证券代码', entrust_bs: '买卖方向', entrust_type: '委托类别', entrust_status: '委托状态', fund_account: '资金帐号', error_no: '错误号', error_info: '错误原因', order_id: '', orig_order_id: '', report_no_old: ''},{entrust_no: '62267', init_date: '20160706', batch_no: '62267', report_no: '18150', seat_no: '28356', entrust_time: '95201', entrust_price: '21.480', entrust_amount: '200.00', stock_code: '603022', entrust_bs: '1', entrust_type: '0', entrust_status: '1', fund_account: '40132172', error_no: '0', error_info: '', order_id: '18150', orig_order_id: '18150', report_no_old: '18150'}], end: '0' }\"}";

      // str="{\"login_type\":null,\"returnJson\":\"{function_id: '302', msg_info: '', msg_no: '-10000', error_grids: 'Func302', grid_count: '2', CSRF_Token:'Njg3ODg1ODE5NTQzMTIyNzcxMTc4ODY3MjMyNDMzMDQ0MjUzODU=', Func302: [{error_pathinfo: '', error_no: '错误号', error_info: '错误原因'},{error_pathinfo: 'F333002() -> F2250003() -> F3103000()', error_no: '-57', error_info: '可用资金不足'}], end: '-10000' }\"}";

       // String str ="{\"login_type\":null,\"returnJson\":\"{function_id: '401', msg_info: '', msg_no: '0', error_grids: '', grid_count: '1', Func401: [{ entrust_no: '委托编号', stock_code: '证券代码', entrust_bs: '买卖方向', entrust_price: '委托价格', entrust_amount: '委托数量', business_amount: '成交数量', business_price: '成交价格', report_time: '申报时间', entrust_status: '委托状态', stock_name: '证券名称'},{ entrust_no: '126006', stock_code: '000829', entrust_bs:'卖出', entrust_price:'11.070', entrust_amount:'200', business_amount:'200', business_price:'11.070', report_time: '111237', entrust_status:'已成', stock_name: '天音控股'}, { entrust_no: '122793', stock_code: '002545', entrust_bs:'买入', entrust_price:'7.110', entrust_amount:'300', business_amount:'300', business_price:'7.110', report_time: '110612', entrust_status:'已成', stock_name: '东方铁塔'}, { entrust_no: '38486', stock_code: '000829', entrust_bs:'卖出', entrust_price:'11.550', entrust_amount:'200', business_amount:'0', business_price:'0.000', report_time: '94413', entrust_status:'已撤', stock_name: '天音控股'}], end: '0' }\"}";
      //  str = ""+(str.substring(536,str.length()-15));

      //

        str = org.apache.commons.lang3.StringUtils.replace(str,"\"{","{");
        str = org.apache.commons.lang3.StringUtils.replace(str,"}\"","}");

        //str = org.apache.commons.lang3.StringUtils.replace(str,"'","\"");
       // str = org.apache.commons.lang3.StringUtils.replace(str,"null","0");
        System.out.println(str);

        YJBResult result =   mapper.readValue(str, YJBResult.class);
        System.out.println(result);

    /*    if(str.length()>50){
            List<YJBEntrust> beanList = mapper.readValue(str, new TypeReference<List<YJBEntrust>>() {});
            System.out.println(beanList);
        }*/
 /*
        Double balance= 2569d;
        String price ="5.2";

        Double a = ((balance/Double.valueOf(price)) /100d) ;
        int amount = a.intValue()*100;

        System.out.println(amount);*/

    }



}