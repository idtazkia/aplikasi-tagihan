package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.VirtualAccountDao;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/va")
public class VirtualAccountController {

    @Autowired private VirtualAccountDao virtualAccountDao;

    @GetMapping("/list")
    public ModelMap daftarVa(@RequestParam Tagihan tagihan, Pageable page) {
        return new ModelMap()
                .addAttribute("listVa",
                        virtualAccountDao.findByTagihan(tagihan, page));
    }
}
