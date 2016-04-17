import cn.skypark.code.MyCheckCodeTool;
import org.apache.commons.io.IOUtils;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Riky on 2016/4/16.
 */
public class ReadImage {
    public static void main(String[] args) throws ParseException, IOException {
       // File file = new File("D:\\dev\\workspace\\cow\\lib\\haitong.jpg");
        File file = new File("D:\\dev\\workspace\\cow\\lib\\code.png");
        BufferedImage image = ImageIO.read(file);

        FileInputStream  fis = new FileInputStream(file);

        System.out.print(IOUtils.toString(fis));

        int height = image.getHeight();
        int width = image.getWidth();

        System.out.println(height +"," +width);

        MyCheckCodeTool tool = new MyCheckCodeTool("guojin");
        String code = tool.getCheckCode_from_image(image);
        System.out.println(code);
    }
}
