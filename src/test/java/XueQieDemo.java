import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

public class XueQieDemo {

    ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws ParseException, IOException {
        XueQieDemo demo = new XueQieDemo();
        demo.login();

    }



    public void login() throws ParseException, IOException {
        URL url = new URL("https://xueqiu.com/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        System.out.println(connection.getHeaderFields());
        InputStream in = connection.getInputStream();







    }


}