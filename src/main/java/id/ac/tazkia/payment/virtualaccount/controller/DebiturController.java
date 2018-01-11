package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.DebiturDao;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.entity.Debitur;
import id.ac.tazkia.payment.virtualaccount.entity.StatusPembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;

@Transactional
@Controller
public class DebiturController {

    @Autowired
    private DebiturDao debiturDao;
    
    @Autowired
    private TagihanDao tagihanDao;

    @PreAuthorize("hasAuthority('VIEW_DEBITUR')")
    @GetMapping("/api/client/debitur/")
    @ResponseBody
    public Page<Debitur> findAll(Pageable page) {
        return debiturDao.findAll(page);
    }

    @PreAuthorize("hasAuthority('EDIT_DEBITUR')")
    @PostMapping("/api/client/debitur/")
    public String create(@ModelAttribute @Valid Debitur s) {
        debiturDao.save(s);

        return "redirect:/debitur/list";
    }

    @PreAuthorize("hasAuthority('VIEW_TAGIHAN')")
    @GetMapping("/api/client/debitur/{id}/tagihan")
    @ResponseBody
    public Page<Tagihan> findTagihanOutstandingByDebitur(Debitur s, Pageable page) {
        return tagihanDao.findByDebiturAndStatusPembayaranInOrderByUpdatedAtDesc(s,
                Arrays.asList(StatusPembayaran.BELUM_DIBAYAR, StatusPembayaran.DIBAYAR_SEBAGIAN), page);
    }

    @PreAuthorize("hasAuthority('VIEW_DEBITUR')")
    @GetMapping("/api/debitur/")
    @ResponseBody
    public Page<Debitur> findAll(String search, Pageable page) {
        return debiturDao.findByNomorDebiturOrNamaContainingIgnoreCase(search, search, page);
    }

    @PreAuthorize("hasAuthority('VIEW_DEBITUR')")
    @GetMapping("/debitur/list")
    public String daftarDebitur(ModelMap mm) {
        mm.addAttribute("data", debiturDao.findAll());
        return "debitur/list";
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "Data Debitur";
    }

    @GetMapping("/debitur/forminput")
    public String tampil(ModelMap mm) {
        mm.addAttribute("debitur", new Debitur());
        return "debitur/form";
    }

    @PreAuthorize("hasAuthority('EDIT_DEBITUR')")
    @GetMapping("/debitur/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) String id) {
        Debitur debitur;

        if (id == null) {
            debitur = new Debitur();
        } else {
            debitur = debiturDao.findOne(id);
        }

        return new ModelMap("debitur", debitur);
    }

    @PreAuthorize("hasAuthority('EDIT_DEBITUR')")
    @PostMapping("/debitur/form")
    public String prosesForm(@ModelAttribute @Valid Debitur debitur, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "/debitur/form";
        }

        debiturDao.save(debitur);
        status.setComplete();
        return "redirect:list";
    }
    
    @PreAuthorize("hasAuthority('EDIT_DEBITUR')")
    @GetMapping("/debitur/delete{id}")
    public String hapusData(@RequestParam(value = "id", required = false) String id){
        debiturDao.delete(id);
        
        return "redirect:/debitur/list";
        
    }
    

}
