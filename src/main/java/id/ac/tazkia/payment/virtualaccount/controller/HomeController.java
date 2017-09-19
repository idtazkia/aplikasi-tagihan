package id.ac.tazkia.payment.virtualaccount.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public ModelMap home(){
        return new ModelMap().addAttribute("pageTitle", "Dashboard");
    }
}
