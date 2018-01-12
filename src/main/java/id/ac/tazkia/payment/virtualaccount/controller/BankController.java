package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.BankDao;
import id.ac.tazkia.payment.virtualaccount.entity.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Transactional @Controller
public class BankController {
    @Autowired private BankDao bankDao;

    @GetMapping("/api/client/bank/")
    @ResponseBody
    public Page<Bank> findAll(Pageable page){
        return bankDao.findAll(page);
    }

    @GetMapping("/bank/list")
    public ModelMap listBank(){
        return new ModelMap()
                .addAttribute("daftarBank", bankDao.findAll(new Sort(Sort.Direction.ASC, "kode")))
                .addAttribute("pageTitle", "Daftar Bank");
    }
}
