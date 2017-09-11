package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.JenisTagihanDao;
import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional @RestController
@RequestMapping("/api/jenis_tagihan")
public class JenisTagihanController {
    @Autowired private JenisTagihanDao jenisTagihanDao;

    @GetMapping("/")
    public Page<JenisTagihan> findAll(Pageable page){
        return jenisTagihanDao.findAll(page);
    }
}
