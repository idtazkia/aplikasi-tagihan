package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.DebiturDao;
import id.ac.tazkia.payment.virtualaccount.dao.JenisTagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.entity.Debitur;
import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Controller
@RequestMapping("/tagihan")
public class TagihanController {
    @Autowired
    private TagihanDao tagihanDao;
    @Autowired
    private JenisTagihanDao jenisTagihanDao;
    @Autowired private DebiturDao debiturDao;

    @GetMapping("/list")
    public ModelMap listTagihan(Pageable pageable) {
        return new ModelMap("listTagihan", tagihanDao.findAll(pageable));
    }

    @ModelAttribute("listJenisTagihan")
    public Iterable<JenisTagihan> daftarJenisTagihan() {
        return jenisTagihanDao.findAll();
    }

    @ModelAttribute("listDebitur")
    public Iterable<Debitur> daftarDebitur() {
        return debiturDao.findAll();
    }

    @GetMapping("/form")
    public ModelMap displayForm(@RequestParam(name = "id", required = false) Tagihan tagihan) {
        if (tagihan == null) {
            tagihan = new Tagihan();
        }

        return new ModelMap().addAttribute("tagihan", tagihan);

    }

    @PostMapping("/form")
    public String processForm(@ModelAttribute @Valid Tagihan tagihan, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "tagihan/form";
        }
        tagihanDao.save(tagihan);
        status.setComplete();
        return "redirect:list";
    }
}
