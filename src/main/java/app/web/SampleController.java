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


import app.bean.IFengDayResult;
import app.bean.WeiCard;
import app.entity.AccountDaily;
import app.entity.AccountData;
import app.entity.Fish;
import app.service.AccountService;
import app.service.FishService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SampleController {
    @Autowired
    FishService fishService;
    @Autowired
    AccountService accountService;
    @Autowired
    ObjectMapper objectMapper;

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
                        @RequestParam(required = false,name = "day") String day) throws IOException, ParseException {
        if (StringUtils.isBlank(day)) {
            day = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        }
        List<Fish> fishList = fishService.getByDay(day);
        model.put("fishList", fishList);
        model.put("day", day);

        URL url = new URL("http://api.weibo.cn/2/page?networktype=wifi&extparam=%E7%BE%8A%E5%9C%88%E6%A8%A1%E5%9E%8B&uicode=10000011&page_id=100808c89f05c71c753fc25d508164539f9303&moduleID=708&featurecode=10000326&lcardid=1076031544062044_-_WEIBO_SECOND_PROFILE_WEIBO_-_4014185464196586&c=android&i=316edc2&s=f49b6925&ua=Xiaomi-MI%204LTE__weibo__6.9.0__android__android6.0.1&wm=20005_0002&aid=01AoDdYa_V9Uir8wj8z6HrKHAAPDu0R7zoiPQRCQtvtSZSoYg.&fid=100808c89f05c71c753fc25d508164539f9303&mid=4014185464196586&v_f=2&v_p=34&from=1069095010&gsid=_2A256wVofDeTxGeRG7VQW9i3IwzyIHXVX1-rXrDV6PUJbrdANLXWnkWodlDKwJJPmuobskExR5tFafu_ShA..&imsi=460000031690924&lang=zh_CN&lfid=1076031544062044_-_WEIBO_SECOND_PROFILE_WEIBO&page=1&skin=default&count=20&oldwm=20005_0002&sflag=1&containerid=100808c89f05c71c753fc25d508164539f9303&luicode=10000198");
        WeiCard obj = objectMapper.readValue(url, WeiCard.class);
        model.put("weiList",obj.getList());

        return "index";
    }
    @RequestMapping("/fish")
    public String fish(Model model) throws IOException, ParseException {

        return "fishChart";
    }

    @ResponseBody
    @RequestMapping("/jsonp")
    public String jsonp(Model model,
                       @RequestParam(required = false,name = "f") String f
    ) throws IOException, ParseException {
        this.fishService.findBySymbol("000001");
        return "fishChart";
    }




}
