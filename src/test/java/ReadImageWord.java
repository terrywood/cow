import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class ReadImageWord {
    public static void main(String[] args) throws ParseException, IOException {

        File dir = new File("D:\\360Downloads\\avatar");
        int i=0;
        for(File file : dir.listFiles()){
            i++;
            file.renameTo(new File(dir.getPath()+"\\"+i+".jpg"));
        }
    }




}