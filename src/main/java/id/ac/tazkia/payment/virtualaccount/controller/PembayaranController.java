package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.PembayaranDao;
import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Transactional @RestController
@RequestMapping("/api/pembayaran")
public class PembayaranController {
    @Autowired private PembayaranDao pembayaranDao;

    @GetMapping("/")
    public Page<Pembayaran> findAll(Pageable page){
        return pembayaranDao.findAll(page);
    }

    @PostMapping("/") @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Pembayaran p){
        pembayaranDao.save(p);
    }
}
