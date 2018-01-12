
package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.PembayaranDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PembayaranFormController {
    @Autowired private PembayaranDao pembayaranDao;
    
    @GetMapping("/pembayaran/listt")
    public ModelMap findAllHtml() {
        return new ModelMap()
                .addAttribute("data", pembayaranDao.findAll());
    }
    
    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "Data Pembayaran";
    }
}
