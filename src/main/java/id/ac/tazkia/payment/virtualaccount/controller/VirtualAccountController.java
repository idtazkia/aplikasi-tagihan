package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.VirtualAccountDao;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.entity.VaStatus;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/retry")
    public String retryCreateVa(@RequestParam(name = "id") VirtualAccount virtualAccount) {
        if (virtualAccount == null) {
            return "redirect:/home";
        }
        virtualAccount.setVaStatus(VaStatus.CREATE);
        virtualAccountDao.save(virtualAccount);
        return "redirect:/va/list?tagihan=" + virtualAccount.getTagihan().getId();
    }
}
