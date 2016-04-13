import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;

public class JacksonDemo {
    public static void main(String[] args) throws ParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


    }



    public void httpGf(){
        String url ="https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.StockUF2Control&method=queryFund&dse_sessionId=7AE509D2D93A9D0B8FC9A8DE945AF6E9&_dc=1460519310303";


    }
}