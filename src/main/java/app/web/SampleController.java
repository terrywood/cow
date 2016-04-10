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


import app.entity.AccountData;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SampleController {

/*
	@Autowired
	private CityService cityService;
*/

	@RequestMapping("/hi")
	@ResponseBody
	@Transactional(readOnly = true)
	public String helloWorld() {
		return "hello";
	}


	//@RequestMapping("/map")
	@RequestMapping(value="/map", method= RequestMethod.GET, produces="application/json")
	@ResponseBody
	@Transactional(readOnly = true)
	public AccountData map() {
		AccountData data = new AccountData();
		data.setBackgroundUrl("back url");
		data.setTitle("title000");
		data.setToday(new Date());
		return data;
	}

}
