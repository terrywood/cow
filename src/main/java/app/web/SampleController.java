/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.web;


import app.bean.ApiDayResult;
import app.bean.Gudong;
import app.bean.IndexData;
import app.bean.SinaMinute60;
import app.entity.Fish;
import app.service.AccountService;
import app.service.FishService;
import app.service.SheepService;
import app.service.StockService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class SampleController {
    @Autowired
    FishService fishService;
    @Autowired
    AccountService accountService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SheepService sheepService;
    @Autowired
    StockService stockService;

    private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    private static String site = "http://www.zhuoniugu.com/api.php";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



    @RequestMapping("/fish")
    public String index(Map<String, Object> model,
                        @RequestParam(required = false, name = "day") String day) throws IOException, ParseException {
        if (StringUtils.isBlank(day)) {
            day = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        }
        List<Fish> fishList = fishService.getByDay(day);
        model.put("fishList", fishList);
        model.put("day", day);
        model.put("weiList", sheepService.get());
        return "fish";
    }

    @RequestMapping("/index")
    public String volume(Model model) throws IOException, ParseException {
        String code="sh000001";
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        CloseableHttpClient httpclient = HttpClients.custom().build();
        HttpResponse response= httpclient.execute( new HttpGet("http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/_=/CN_MarketData.getKLineData?symbol="+code+"&scale=60&ma=no&datalen=90"));
        String data = EntityUtils.toString(response.getEntity());
        data = data.substring(3,data.length()-1);
        List<SinaMinute60> beanList = objectMapper.readValue(data, new TypeReference<List<SinaMinute60>>() {});
        List<IndexData> newList = new ArrayList<>();
        URL url = new URL("http://api.finance.ifeng.com/akdaily/?code=" +code + "&type=last");
        ApiDayResult result = objectMapper.readValue(url, ApiDayResult.class);
        List<String[]> list2 = result.getRecord();
        for(SinaMinute60 bean :beanList){
            String day =bean.getDay();
            if(day.endsWith("10:30:00")){
                String shortDay =(day.substring(0,10));
                IndexData entity = new IndexData();
                entity.setDay(shortDay);
                entity.setVolume60(bean.getVolume());
                for (String[] args : list2) {
                    if(shortDay.equals(args[0])){
                        String price  = args[3];
                        entity.setPrice_change(args[6]);
                        entity.setP_change(Double.valueOf(args[7]));
                        entity.setPrice(price);
                        break;
                    }
                }
                newList.add(entity);
                //newList.add(bean);
            }
        }
        //System.out.println(newList);
        model.addAttribute("list",newList);
        return "index";
    }



    @RequestMapping("/guDong")
    public String guDong(Model model,
                               @RequestParam(required = false, name = "code", defaultValue = "002679") String code
    ) throws IOException, ParseException {
        model.addAttribute("stock",stockService.findByPK(code));
        return "guDong";
    }

    @RequestMapping("/guDongData")
    public ResponseEntity guDongData(Model model,
                               @RequestParam(required = false, name = "code", defaultValue = "002679") String code
    ) throws IOException, ParseException {
        Map<String ,Object> map = new HashMap<>();
        List<Gudong> guDong = new ArrayList<>();
        Document doc = null;
        String market = code.startsWith("6") ? "sh" : "sz";
        try {
            doc = Jsoup.connect(site).userAgent(userAgent)
                    .data("c", "hudong")
                    .data("a", "gudong")
                    .data("show", "html")
                    .data("stock_id", code)
                    .get();
            Element element = doc.getElementsByAttributeValue("style", "text-align:center").get(2);
            String raw[] = StringUtils.split(element.html(), "<br>");
            for (int i=0;i<raw.length;i++) {
                String day = raw[i];
                String[] row = StringUtils.split(day.trim(), "|");
                Date startDate = sdf.parse(row[0].trim());
                guDong.add(new Gudong(startDate, Integer.valueOf(row[1].trim())));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List prices = new ArrayList<>();
        List counts = new ArrayList<>();
        List dates = new ArrayList<>();
        try {
            Date startDate =guDong.get(guDong.size()-1).getDate();
            URL url = new URL("http://api.finance.ifeng.com/akdaily/?code=" + market + code + "&type=last");
            ApiDayResult result = objectMapper.readValue(url, ApiDayResult.class);
            System.out.println(startDate);
            List<String[]> list2 = result.getRecord();
            for (String[] args : list2) {
                Date date = sdf.parse(args[0]);
                if (date.after(startDate)) {
                    prices.add(Double.valueOf(args[3].trim()));
                    dates.add(sdf.format(date));
                    for(Gudong entity : guDong){
                        if(entity.getDate().equals(date) || date.after(entity.getDate())){
                            counts.add(entity.getCount());
                            break;
                        }
                    }

                }
            }
            map.put("dates",dates);
            map.put("counts",counts);
            map.put("prices",prices);

        } catch (Exception e) {
            System.out.println("can not get code[" + code + "] message:" + e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity(map, HttpStatus.OK);
    }
}
