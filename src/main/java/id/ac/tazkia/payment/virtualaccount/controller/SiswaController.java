package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.SiswaDao;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.entity.Siswa;
import id.ac.tazkia.payment.virtualaccount.entity.StatusPembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.Arrays;

@Transactional @Controller
public class SiswaController {
    @Autowired private SiswaDao siswaDao;
    @Autowired private TagihanDao tagihanDao;

    @PreAuthorize("hasAuthority('VIEW_SISWA')")
    @GetMapping("/api/client/siswa/") @ResponseBody
    public Page<Siswa> findAll(Pageable page){
        return siswaDao.findAll(page);
    }

    @PreAuthorize("hasAuthority('EDIT_SISWA')")
    @PostMapping("/api/client/siswa/") @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Siswa s){
        siswaDao.save(s);
    }

    @PreAuthorize("hasAuthority('VIEW_TAGIHAN')")
    @GetMapping("/api/client/siswa/{id}/tagihan") @ResponseBody
    public Page<Tagihan> findTagihanOutstandingBySiswa(Siswa s, Pageable page){
        return tagihanDao.findBySiswaAndStatusPembayaranInOrderByUpdatedAtDesc(s,
                Arrays.asList(StatusPembayaran.BELUM_DIBAYAR, StatusPembayaran.DIBAYAR_SEBAGIAN), page);
    }

    @PreAuthorize("hasAuthority('VIEW_SISWA')")
    @GetMapping("/api/siswa/") @ResponseBody
    public Page<Siswa> findAll(String search, Pageable page){
        return siswaDao.findByNomorSiswaOrNamaContainingIgnoreCase(search, search, page);
    }

    @PreAuthorize("hasAuthority('VIEW_SISWA')")
    @GetMapping("/siswa/list")
    public void daftarSiswa(){ }

    @ModelAttribute("pageTitle")
    public String pageTitle(){
        return "Data Siswa";
    }

    @PreAuthorize("hasAuthority('EDIT_SISWA')")
    @GetMapping("/siswa/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) Siswa siswa){
        if(siswa == null){
            siswa = new Siswa();
        }
        return new ModelMap("siswa", siswa);
    }

    @PreAuthorize("hasAuthority('EDIT_SISWA')")
    @PostMapping("/siswa/form")
    public String prosesForm(@ModelAttribute @Valid Siswa siswa, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "/siswa/form";
        }

        siswaDao.save(siswa);
        status.setComplete();
        return "redirect:list";
    }
}
