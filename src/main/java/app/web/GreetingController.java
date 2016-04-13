package app.web;

import app.entity.TraderSession;
import app.service.TraderSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @Autowired
    TraderSessionService traderSessionService;

    @RequestMapping("/index")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("list", traderSessionService.findAll());
        //model.addAttribute("name", name);
        return "greeting";
    }


    @RequestMapping("/save")
    public String session(
            @ModelAttribute TraderSession entity) {
        entity.setShAccount("A471547189");
        entity.setSzAccount("0159230750 ");
        traderSessionService.save(entity);
        return "redirect:index";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("id") String id) {
        traderSessionService.delete(id);
        return "redirect:index";
    }

}
