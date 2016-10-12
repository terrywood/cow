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
import app.bean.IFengDayResult;
import app.entity.AccountDaily;
import app.entity.AccountData;
import app.entity.Fish;
import app.service.AccountService;
import app.service.FishService;
import app.service.SheepService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/accounts")
    public String accounts(/*
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date date,*/
                           Model model) throws ParseException {
        List<AccountData> list = accountService.findAll();
        Map<Long, List<AccountDaily>> map = new HashMap<>();
        for (AccountData data : list) {
            List<AccountDaily> dailies = accountService.findByAccountID(data.getAccountID());
            map.put(data.getAccountID(), dailies);
        }
        model.addAttribute("daily", map);
        model.addAttribute("pageList", list);
        return "accounts";
    }

    @RequestMapping("/accounts/{id}")
    public String detail(Model model,
                         @PathVariable("id") String id) throws ParseException {
        model.addAttribute("id", id);
        return "detail";
    }

    @RequestMapping("/28")
    public String tow8(Model model) throws ParseException, IOException {
        URL url = new URL("http://api.finance.ifeng.com/akdaily/?code=sz159915&type=last");
        URL url2 = new URL("http://api.finance.ifeng.com/akdaily/?code=sh518880&type=last");
        IFengDayResult cy = objectMapper.readValue(url, IFengDayResult.class);
        IFengDayResult gold = objectMapper.readValue(url2, IFengDayResult.class);
        List<String[]> list = cy.getRecord();
        List<String[]> list2 = gold.getRecord();
        Double cyb = (Double.valueOf(list.get(list.size() - 22)[3]) + Double.valueOf(list.get(list.size() - 20)[3]) + Double.valueOf(list.get(list.size() - 21)[3])) / 3;
        Double hj = (Double.valueOf(list2.get(list2.size() - 22)[3]) + Double.valueOf(list2.get(list2.size() - 20)[3]) + Double.valueOf(list2.get(list2.size() - 21)[3])) / 3;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String link = "http://hq.sinajs.cn/list=sz159915,sh518880";
        HttpGet httpget = new HttpGet(link);
        CloseableHttpResponse response = httpClient.execute(httpget);
        String data = EntityUtils.toString(response.getEntity());
        String[] cybA = StringUtils.substringBetween(data, "hq_str_sz159915=\"", "\";").split(",");
        String[] hjA = StringUtils.substringBetween(data, "hq_str_sh518880=\"", "\";").split(",");
        Double cybPrice = Double.valueOf(cybA[3]);
        Double hjPrice = Double.valueOf(hjA[3]);
        double x2 = (cybPrice - cyb) / cyb;
        double x8 = (hjPrice - hj) / hj;
        String desc = null;

        if (x2 > 0d && x8 > 0d) {
            if (x2 > x8) {
                desc = ("持有创业板159915");
            } else {
                desc = ("持有黄金518880");
            }
        } else {
            desc = ("卖出逆回购204001!");
        }
        desc += " [创业板:" + String.format("%1$.2f", x2) + "  黄金:" + String.format("%1$.2f", x8) + "]";
        model.addAttribute("sh518880", hjPrice);
        model.addAttribute("sz159915", cybPrice);
        model.addAttribute("desc28", desc);
        return "index";
    }

    @RequestMapping("/")
    public String index(Map<String, Object> model,
                        @RequestParam(required = false, name = "day") String day) throws IOException, ParseException {
        if (StringUtils.isBlank(day)) {
            day = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        }
        List<Fish> fishList = fishService.getByDay(day);
        model.put("fishList", fishList);
        model.put("day", day);
        model.put("weiList", sheepService.get());
        return "index";
    }

    @RequestMapping("/fish")
    public String fish(Model model) throws IOException, ParseException {

        return "fishChart";
    }

    private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    private static String site = "http://www.zhuoniugu.com/api.php";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    @RequestMapping("/guDong")
    public String guDong(Model model,
                               @RequestParam(required = false, name = "code", defaultValue = "002679") String code
    ) throws IOException, ParseException {
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

/*
    @RequestMapping("/price")
    public ResponseEntity price(Model model,
                                 HttpSession httpSession,
                                 @RequestParam(required = false, name = "code", defaultValue = "002679") String code
    ) throws IOException, ParseException {
        List<Object[]> list = new ArrayList<>();
        String market = code.startsWith("6") ? "sh" : "sz";
        try {
            Date csvStartDate =(Date) httpSession.getAttribute("startDate");
            URL url = new URL("http://api.finance.ifeng.com/akdaily/?code=" + market + code + "&type=last");
            ApiDayResult result = objectMapper.readValue(url, ApiDayResult.class);
            System.out.println(csvStartDate);
            List<String[]> list2 = result.getRecord();
            System.out.println(list2.size());
            for (String[] args : list2) {
                Date date = sdf.parse(args[0]);
                if (date.after(csvStartDate)) {
                    String price  = args[3];
                    Object[] data = new Object[]{date.getTime(), Double.valueOf(price.trim())};
                    list.add(data);
                }
            }
        } catch (Exception e) {
            System.out.println("can not get code[" + code + "] message:" + e.getMessage());
            e.printStackTrace();
        }

        return new ResponseEntity(list, HttpStatus.OK);
    }*/

}
