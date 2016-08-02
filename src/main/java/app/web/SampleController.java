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


import app.entity.AccountDaily;
import app.entity.AccountData;
import app.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SampleController {
    @Autowired
    AccountService accountService;
    @RequestMapping("/accounts")
    public String accounts(/*
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date date,*/
            Model model) throws ParseException {
        List<AccountData> list = accountService.findAll();
        Map<Long,List<AccountDaily>> map = new HashMap<>();
        for(AccountData data :list){
            List<AccountDaily> dailies = accountService.findByAccountID(data.getAccountID());
            map.put(data.getAccountID(),dailies);
        }
        model.addAttribute("daily",map);
        model.addAttribute("pageList", list);
        return "accounts";
    }
    @RequestMapping("/accounts/{id}")
    public String detail( Model model,
                          @PathVariable("id") String id) throws ParseException {
        model.addAttribute("id",id);
        return "detail";
    }

	/*@Autowired
	private StockListService stockListService;
	@Autowired
	private TraderSessionService traderSessionService;
	@RequestMapping("/hi")
	@ResponseBody
	@Transactional(readOnly = true)
	public List<StockListData> helloWorld() {
		List<StockListData> list = stockListService.findByAccountID("607955");
		return list;
	}
	@RequestMapping("/id")
	@ResponseBody
	public StockListData id(Long id) {
		StockListData entity = stockListService.findOne(id);
		return entity;
	}
	@RequestMapping("/sav")
	@ResponseBody
	public StockListData dd(Long id) {
		StockListData entity = stockListService.findOne(id);
		if(entity!=null){
			entity.setLastTradingTime(new Date());
			stockListService.save(entity);
		}
		return entity;
	}
	@RequestMapping("/del")
	@ResponseBody
	public String del(Long id) {
		StockListData entity = stockListService.findOne(id);
		if(entity!=null){
			stockListService.delete(id);
		}
		return "OK";
	}*/


/*

	//@RequestMapping("/map")
	@RequestMapping(value="/map", method= RequestMethod.GET, produces="application/json")
	@ResponseBody
	@Transactional(readOnly = true)
	public HistoryData map() {
		HistoryData data = new HistoryData();
		data.setDelegateID(10l);
		data.setAddTime(new Date());
		return data;
	}
*/


}
