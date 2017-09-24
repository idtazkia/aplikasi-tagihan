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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@Transactional @Controller
public class SiswaController {
    @Autowired private SiswaDao siswaDao;
    @Autowired private TagihanDao tagihanDao;

    @GetMapping("/api/siswa/") @ResponseBody
    public Page<Siswa> findAll(String search, Pageable page){
        return siswaDao.findByNomorSiswaContainsOrNamaContains(search, search, page);
    }

    @PostMapping("/api/siswa/") @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Siswa s){
        siswaDao.save(s);
    }

    @GetMapping("/api/siswa/{id}/tagihan") @ResponseBody
    public Page<Tagihan> findTagihanOutstandingBySiswa(Siswa s, Pageable page){
        return tagihanDao.findBySiswaAndStatusPembayaranInOrderByUpdatedAtDesc(s,
                Arrays.asList(StatusPembayaran.BELUM_DIBAYAR, StatusPembayaran.DIBAYAR_SEBAGIAN), page);
    }

    @GetMapping("/siswa/list")
    public ModelMap daftarSiswa(){
        return new ModelMap().addAttribute("pageTitle", "Data Siswa");
    }
}
