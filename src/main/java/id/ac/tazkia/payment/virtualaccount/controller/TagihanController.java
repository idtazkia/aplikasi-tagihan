package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.*;
import id.ac.tazkia.payment.virtualaccount.dto.UpdateTagihanRequest;
import id.ac.tazkia.payment.virtualaccount.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Transactional @RestController
@RequestMapping("/api/tagihan")
public class TagihanController {

    @Autowired private TagihanDao tagihanDao;
    @Autowired private PembayaranDao pembayaranDao;
    @Autowired private VirtualAccountDao virtualAccountDao;
    @Autowired private ProsesBankDao prosesBankDao;
    @Autowired private BankDao bankDao;
    @Autowired private SiswaDao siswaDao;

    @PreAuthorize("hasAuthority('EDIT_TAGIHAN')")
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

    @PreAuthorize("hasAuthority('EDIT_TAGIHAN')")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Tagihan tx, @Valid @RequestBody UpdateTagihanRequest request) {

        if(tx == null) {
            return ResponseEntity.notFound().build();
        }

        if(request.getNilaiTagihan().compareTo(tx.getJumlahPembayaran()) < 0){
            Map<String, String> error = new HashMap<>();
            error.put("status", "400");
            error.put("message", "Nilai tagihan baru [" + request.getNilaiTagihan()
                    + "] lebih kecil daripada yang sudah dibayar [" +
                    tx.getJumlahPembayaran() + "]");
            return ResponseEntity.badRequest().body(error);
        }

        tx.setJumlahTagihan(request.getNilaiTagihan());
        tx.setTanggalKadaluarsa(request.getTanggalKadaluarsa());
        tx.setKeterangan(request.getKeterangan());
        tagihanDao.save(tx);
        for (Bank b : bankDao.findAll()) {
            if(!b.getAktif()) {
                continue;
            }
            ProsesBank pb = new ProsesBank();
            pb.setWaktuPembuatan(new Date());
            pb.setTagihan(tx);
            pb.setBank(b);
            pb.setJenisProsesBank(JenisProsesBank.UPDATE_VA);
            pb.setStatusProsesBank(StatusProsesBank.BARU);
            prosesBankDao.save(pb);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('VIEW_TAGIHAN')")
    @GetMapping("/")
    public Page<Tagihan> findAll(Pageable page){
        return tagihanDao.findAll(page);
    }

    @PreAuthorize("hasAuthority('VIEW_TAGIHAN')")
    @GetMapping("/{id}")
    public Tagihan findById(@PathVariable("id") Tagihan t){
        return t;
    }

    @PreAuthorize("hasAuthority('VIEW_TAGIHAN')")
    @GetMapping("/{id}/pembayaran")
    public Iterable<Pembayaran> findPembayaranByTagihan(@PathVariable("id") Tagihan t){
        return pembayaranDao.findByVirtualAccountTagihanOrderByWaktuTransaksi(t);
    }

    @PreAuthorize("hasAuthority('VIEW_TAGIHAN')")
    @GetMapping("/{id}/va")
    public Iterable<VirtualAccount> findVirtualAccountByTagihan(@PathVariable("id") Tagihan t){
        return virtualAccountDao.findByTagihanOrderByBankNomorRekening(t);
    }
}
