package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.JenisTagihanDao;
import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Transactional @Controller
public class JenisTagihanController {
    @Autowired private JenisTagihanDao jenisTagihanDao;

    @GetMapping("/api/jenistagihan/")
    public Page<JenisTagihan> findAll(Pageable page){
        return jenisTagihanDao.findAll(page);
    }

    @GetMapping("/jenistagihan/list")
    public ModelMap findAllHtml(){
        return new ModelMap()
                .addAttribute("pageTitle", "Jenis Tagihan")
                .addAttribute("daftarJenisTagihan", jenisTagihanDao.findAll());
    }
}
