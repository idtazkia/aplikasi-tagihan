package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.BankDao;
import id.ac.tazkia.payment.virtualaccount.entity.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Transactional @RestController
@RequestMapping("/api/bank")
public class BankController {
    @Autowired private BankDao bankDao;

    @GetMapping("/")
    public Page<Bank> findAll(Pageable page){
        return bankDao.findAll(page);
    }
}
