package app.service;

import app.entity.Stock;
import app.repository.StockRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {
    private static final Logger log = LoggerFactory.getLogger(StockService.class);


    @Autowired
    private StockRepository stockRepository;
    private static String userAgent ="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    private static String site = "http://chuansong.me/account/gushequ";



    public Stock findByPK(String id){
        return stockRepository.findOne(id);
    }
    //@PostConstruct
    public void initDate() throws IOException {
        String _headers ="code,name,industry,area,pe,outstanding,totals,totalAssets,liquidAssets,fixedAssets,reserved,reservedPerShare,esp,bvps,pb,timeToMarket";
        String headers[] = _headers.split(",");
        URL url = new URL("http://218.244.146.57/static/all.csv");
        Reader reader = new InputStreamReader(url.openStream(), "GBK");
        CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
        try {
            List<Stock> list = new ArrayList<>();
            for ( CSVRecord record : parser) {
                Stock stock = new Stock();
                Class cls = stock.getClass();
                for(String title :headers){
                    String value = record.get(title);
                    Field field = cls.getDeclaredField(title);
                    String type = field.getType().toString();
                    field.setAccessible(true);
                    if(type.endsWith("Double")){
                        field.set(stock, Double.valueOf(value));
                    }else{
                        field.set(stock, value);
                    }
                }
                list.add(stock);
            }
            stockRepository.save(list);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            parser.close();
            reader.close();
        }
    }


}
