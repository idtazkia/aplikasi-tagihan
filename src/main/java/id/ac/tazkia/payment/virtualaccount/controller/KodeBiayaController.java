package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.KodeBiayaDao;
import id.ac.tazkia.payment.virtualaccount.entity.KodeBiaya;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/kode_biaya")
public class KodeBiayaController {
    @Autowired private KodeBiayaDao kodeBiayaDao;

    @GetMapping("/list")
    public ModelMap daftarKodeBiaya(Pageable pageable) {
        return new ModelMap().addAttribute("daftarKodeBiaya",kodeBiayaDao.findAll(pageable));
    }

    @GetMapping("/form")
    public ModelMap displayForm(@RequestParam(name = "id", required = false) KodeBiaya kodeBiaya) {
        if (kodeBiaya == null) {
            kodeBiaya = new KodeBiaya();
        }

        return new ModelMap("kodeBiaya", kodeBiaya);
    }

    @PostMapping("/form")
    public String processForm(@ModelAttribute KodeBiaya kodeBiaya, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "form";
        }

        kodeBiayaDao.save(kodeBiaya);
        status.setComplete();
        return "redirect:list";
    }

}
