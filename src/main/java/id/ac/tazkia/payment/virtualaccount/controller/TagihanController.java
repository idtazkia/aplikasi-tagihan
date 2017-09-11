package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.PembayaranDao;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Transactional @RestController
@RequestMapping("/api/tagihan")
public class TagihanController {

    @Autowired private TagihanDao tagihanDao;
    @Autowired private PembayaranDao pembayaranDao;

    @PostMapping("/") @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Tagihan t){
        tagihanDao.save(t);
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
