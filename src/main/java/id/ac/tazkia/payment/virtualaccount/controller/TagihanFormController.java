package id.ac.tazkia.payment.virtualaccount.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

@Transactional
@Controller
public class TagihanFormController {
    @Autowired private TagihanDao tagihanDao;
    
    @GetMapping("/tagihan/list")
    public ModelMap findAllHtml() {
        return new ModelMap()
                .addAttribute("tagihan", tagihanDao.findAll());
    }
    
    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "Data Tagihan";
    }

    
}
