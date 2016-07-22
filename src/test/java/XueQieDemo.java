import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

public class XueQieDemo {

    ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws ParseException, IOException {
        XueQieDemo demo = new XueQieDemo();
        //demo.gf();
        demo.stocklistitem();

    }



    public void stocklistitem() throws ParseException, IOException {

        HttpURLConnection connection = null;
        URL url = new URL("https://xueqiu.com/P/ZH914042");
        //URL url = new URL("https://xueqiu.com/P/ZH902949");
        connection = (HttpURLConnection) url.openConnection();
        InputStream in = connection.getInputStream();
        BufferedReader bd = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String text;
        String cubeInfo = null;
        while ((text = bd.readLine()) != null){
            if(text.startsWith("SNB.cubeInfo")){
                cubeInfo = text.substring(15);
                break;
            }
           // System.out.println(text);
          // System.out.println("---------------");
            //builder.append(text);
        }
    /*    String result = builder.toString();
        System.out.println("---------------");
      */

        System.out.println(cubeInfo);
    }


}