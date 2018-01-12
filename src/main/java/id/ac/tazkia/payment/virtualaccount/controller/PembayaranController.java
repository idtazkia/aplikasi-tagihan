package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.PembayaranDao;
import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pembayaran")
public class PembayaranController {

    @Autowired
    private PembayaranDao pembayaranDao;

    @PreAuthorize("hasAuthority('EDIT_PEMBAYARAN')")
    @GetMapping("/form")
    public ModelMap displayForm(@RequestParam(value = "id", required = false) String id) {
        Pembayaran p;

        if (id != null) {
            p = pembayaranDao.findOne(id);
        } else {
            p  = new Pembayaran();
        }
        
        return new ModelMap()
                .addAttribute("pembayaran", p);
    }

    @PreAuthorize("hasAuthority('VIEW_PEMBAYARAN')")
    @GetMapping("/list")
    public ModelMap findAllHtml(Pageable pageable) {
        return new ModelMap()
                .addAttribute("data", pembayaranDao.findAll(pageable));
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "Data Pembayaran";
    }
}
