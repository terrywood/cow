import app.bean.WeiCard;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

public class WeiboDemo {


    public static void main(String[] args) throws ParseException, IOException {
        WeiboDemo demo = new WeiboDemo();
        demo.login();

/*
        String d ="Tue Aug 30 14:50:57 +0800 2016";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.US);
        System.out.println(sdf.format(new Date()));
        System.out.println(sdf.parse(d));
*/

    }



    public void login() throws ParseException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        URL url = new URL("http://api.weibo.cn/2/page?networktype=wifi&extparam=%E7%BE%8A%E5%9C%88%E6%A8%A1%E5%9E%8B&uicode=10000011&page_id=100808c89f05c71c753fc25d508164539f9303&moduleID=708&featurecode=10000326&lcardid=1076031544062044_-_WEIBO_SECOND_PROFILE_WEIBO_-_4014185464196586&c=android&i=316edc2&s=f49b6925&ua=Xiaomi-MI%204LTE__weibo__6.9.0__android__android6.0.1&wm=20005_0002&aid=01AoDdYa_V9Uir8wj8z6HrKHAAPDu0R7zoiPQRCQtvtSZSoYg.&fid=100808c89f05c71c753fc25d508164539f9303&mid=4014185464196586&v_f=2&v_p=34&from=1069095010&gsid=_2A256wVofDeTxGeRG7VQW9i3IwzyIHXVX1-rXrDV6PUJbrdANLXWnkWodlDKwJJPmuobskExR5tFafu_ShA..&imsi=460000031690924&lang=zh_CN&lfid=1076031544062044_-_WEIBO_SECOND_PROFILE_WEIBO&page=1&skin=default&count=20&oldwm=20005_0002&sflag=1&containerid=100808c89f05c71c753fc25d508164539f9303&luicode=10000198");
        WeiCard obj = objectMapper.readValue(url, WeiCard.class);
        System.out.println(obj);

    }


}