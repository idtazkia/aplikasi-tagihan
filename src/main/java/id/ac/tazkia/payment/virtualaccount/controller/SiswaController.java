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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@Transactional @RestController
@RequestMapping("/api/siswa")
public class SiswaController {
    @Autowired private SiswaDao siswaDao;
    @Autowired private TagihanDao tagihanDao;

    @GetMapping("/")
    public Page<Siswa> findAll(Pageable page){
        return siswaDao.findAll(page);
    }

    @PostMapping("/") @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Siswa s){
        siswaDao.save(s);
    }

    @GetMapping("/{id}/tagihan")
    public Page<Tagihan> findTagihanOutstandingBySiswa(Siswa s, Pageable page){
        return tagihanDao.findBySiswaAndStatusPembayaranInOrderByUpdatedAtDesc(s,
                Arrays.asList(StatusPembayaran.BELUM_DIBAYAR, StatusPembayaran.DIBAYAR_SEBAGIAN), page);
    }
}
