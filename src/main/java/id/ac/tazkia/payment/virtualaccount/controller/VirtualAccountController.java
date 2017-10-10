package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.PembayaranDao;
import id.ac.tazkia.payment.virtualaccount.dao.VirtualAccountDao;
import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Transactional
@RestController @RequestMapping("/api/client/va")
public class VirtualAccountController {
    @Autowired private VirtualAccountDao virtualAccountDao;
    @Autowired private PembayaranDao pembayaranDao;

    @GetMapping("/")
    public Page<VirtualAccount> findAll(Pageable page){
        return virtualAccountDao.findAll(page);
    }

    @PostMapping("/") @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid VirtualAccount va){
        virtualAccountDao.save(va);
    }

    @GetMapping("/{id}")
    public VirtualAccount findById(@PathVariable("id") VirtualAccount va){
        return va;
    }

    @GetMapping("/{id}/pembayaran")
    public Iterable<Pembayaran> findPembayaranByVA(@PathVariable("id") VirtualAccount va){
        return pembayaranDao.findByVirtualAccountOrderByWaktuTransaksi(va);
    }
}
