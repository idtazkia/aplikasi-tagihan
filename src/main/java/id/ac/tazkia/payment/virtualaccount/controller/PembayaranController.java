package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.PembayaranDao;
import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller @RequestMapping("/pembayaran")
public class PembayaranController {
    @Autowired private PembayaranDao pembayaranDao;

    @GetMapping("/form")
    public ModelMap displayForm(@RequestParam Tagihan tagihan) {
        Pembayaran p = new Pembayaran();
        p.setTagihan(tagihan);
        return new ModelMap()
                .addAttribute("pembayaran", p);
    }
}
