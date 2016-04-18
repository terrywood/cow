import cn.skypark.code.MyCheckCodeTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

public class ReadImageWord {
    public static void main(String[] args) throws ParseException, IOException {


        MyCheckCodeTool tool = new MyCheckCodeTool("guojin");
        URL url = new URL("https://jy.yongjinbao.com.cn/winner_gj/gjzq/user/extraCode.jsp");
        BufferedImage image = ImageIO.read(url);
        System.out.println("code=" + tool.getCheckCode_from_image(image));
    }




}