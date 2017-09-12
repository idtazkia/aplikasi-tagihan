package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.*;
import id.ac.tazkia.payment.virtualaccount.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Transactional @RestController
@RequestMapping("/api/tagihan")
public class TagihanController {

    @Autowired private TagihanDao tagihanDao;
    @Autowired private PembayaranDao pembayaranDao;
    @Autowired private ProsesBankDao prosesBankDao;
    @Autowired private BankDao bankDao;
    @Autowired private SiswaDao siswaDao;

    @PostMapping("/") @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Tagihan t){
        Siswa s = siswaDao.findByNomorSiswa(t.getSiswa().getNomorSiswa());

        if(s == null){
            s = t.getSiswa();
            siswaDao.save(s);
        }
        t.setSiswa(s);
        tagihanDao.save(t);
        for (Bank b : bankDao.findAll()) {
            if(!b.getAktif()) {
                continue;
            }
            ProsesBank pb = new ProsesBank();
            pb.setWaktuPembuatan(new Date());
            pb.setTagihan(t);
            pb.setBank(b);
            pb.setJenisProsesBank(JenisProsesBank.CREATE_VA);
            pb.setStatusProsesBank(StatusProsesBank.BARU);
            prosesBankDao.save(pb);
        }
    }

    @PutMapping("/{id}") @ResponseStatus(HttpStatus.CREATED)
    public void update(@PathVariable("id") String id, @RequestBody @Valid Tagihan t){
        Tagihan tx = tagihanDao.findOne(id);
        if(tx != null) {
            t.setId(id);
            tagihanDao.save(t);
        }
    }

    @GetMapping("/")
    public Page<Tagihan> findAll(Pageable page){
        return tagihanDao.findAll(page);
    }

    @GetMapping("/{id}")
    public Tagihan findById(@PathVariable("id") Tagihan t){
        return t;
    }

    @GetMapping("/{id}/pembayaran")
    public Iterable<Pembayaran> findPembayaranByTagihan(@PathVariable("id") Tagihan t){
        return pembayaranDao.findByVirtualAccountTagihanOrderByWaktuTransaksi(t);
    }
}
