/*
 * Copyright 2012-2015 the original author or authors.
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

import org.springframework.stereotype.Controller;

@Controller
public class WelcomeController {

/*
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
    }*/

   /* @RequestMapping("/accounts")
    public String accounts(
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
    }*/
/*
    @Value("${application.message:Hello World}")
    private String message = "Hello World";

    @RequestMapping("/welcome")
    public String welcome(Map<String, Object> model) {
        model.put("time", new Date());
        model.put("message", this.message);
        return "welcome";
    }

    @RequestMapping("/fail")
    public String fail() {
        throw new MyException("Oh dear!");
    }

    @RequestMapping("/fail2")
    public String fail2() {
        throw new IllegalStateException();
    }

    @ExceptionHandler(MyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public
    @ResponseBody
    MyRestResponse handleMyRuntimeException(MyException exception) {
        return new MyRestResponse("Some data I want to send back to the client.");
    }*/

}
