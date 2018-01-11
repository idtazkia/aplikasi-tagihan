package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.JenisTagihanDao;
import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Transactional
@Controller
public class JenisTagihanController {
    @Autowired
    private JenisTagihanDao jenisTagihanDao;

    @GetMapping("/api/client/jenistagihan/")
    public Page<JenisTagihan> findAll(Pageable page) {
        return jenisTagihanDao.findAll(page);
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "Jenis Tagihan";
    }

    @GetMapping("/jenistagihan/list")
    public ModelMap findAllHtml() {
        return new ModelMap()
                .addAttribute("daftarJenisTagihan", jenisTagihanDao.findAll(new Sort(Sort.Direction.ASC, "kode")));
    }

    @GetMapping("/jenistagihan/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) JenisTagihan jenisTagihan) {
        if (jenisTagihan == null) {
            jenisTagihan = new JenisTagihan();
        }

        return new ModelMap().addAttribute("jenisTagihan", jenisTagihan);
    }

    @PostMapping("/jenistagihan/form")
    public String prosesForm(@ModelAttribute @Valid JenisTagihan jenisTagihan, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "/jenistagihan/form";
        }

        jenisTagihanDao.save(jenisTagihan);
        status.setComplete();
        return "redirect:list";
    }
}
