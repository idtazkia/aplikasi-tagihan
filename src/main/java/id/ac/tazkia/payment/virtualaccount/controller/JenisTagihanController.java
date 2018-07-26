package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.BankDao;
import id.ac.tazkia.payment.virtualaccount.dao.JenisTagihanDao;
import id.ac.tazkia.payment.virtualaccount.entity.Bank;
import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Controller
public class JenisTagihanController {
    private static final String REDIRECT_JENIS_TAGIHAN_BANK = "redirect:/jenistagihan/bank?id=";
    
    @Autowired
    private JenisTagihanDao jenisTagihanDao;

    @Autowired
    private BankDao bankDao;

    @GetMapping("/api/client/jenistagihan/")
    public Page<JenisTagihan> findAll(Pageable page) {
        return jenisTagihanDao.findAll(page);
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "Jenis Tagihan";
    }

    @GetMapping("/jenistagihan/list")
    public ModelMap findAllHtml() {
        return new ModelMap()
                .addAttribute("daftarJenisTagihan", jenisTagihanDao.findAll(new Sort(Sort.Direction.ASC, "kode")));
    }

    @GetMapping("/jenistagihan/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) JenisTagihan jenisTagihan) {
        if (jenisTagihan == null) {
            jenisTagihan = new JenisTagihan();
        }

        return new ModelMap().addAttribute("jenisTagihan", jenisTagihan);
    }

    @PostMapping("/jenistagihan/form")
    public String prosesForm(@ModelAttribute @Valid JenisTagihan jenisTagihan, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "/jenistagihan/form";
        }

        jenisTagihanDao.save(jenisTagihan);
        status.setComplete();
        return "redirect:list";
    }

    @GetMapping("/jenistagihan/bank")
    public ModelMap jenisTagihanBank(@RequestParam(value = "id") JenisTagihan jenisTagihan) {
        Iterable<Bank> pilihanBankJenisTagihan = bankDao.findAll();
        if (!jenisTagihan.getDaftarBank().isEmpty()) {
            Set<String> idBanks = new HashSet<>();
            jenisTagihan.getDaftarBank().forEach(bank -> idBanks.add(bank.getId()));
            pilihanBankJenisTagihan = bankDao.findByIdNotIn(idBanks);
        }

        return new ModelMap()
                .addAttribute("jenisTagihan", jenisTagihan)
                .addAttribute("pilihanBankJenisTagihan", pilihanBankJenisTagihan);
    }

    @PostMapping("/jenistagihan/bank")
    public String tambahJenisTagihanBank(@RequestParam(value = "id") JenisTagihan jenisTagihan, @RequestParam(name = "bank") Bank bank) {
        if (jenisTagihan == null) {
            return "redirect:/jenistagihan/list";
        }

        if (bank == null) {
            return REDIRECT_JENIS_TAGIHAN_BANK + jenisTagihan.getId();
        }

        jenisTagihan.getDaftarBank().add(bank);
        jenisTagihanDao.save(jenisTagihan);
        return REDIRECT_JENIS_TAGIHAN_BANK + jenisTagihan.getId();
    }
    
    @PostMapping("/jenistagihan/bank/hapus")
    public String hapusJenisTagihanBank(@RequestParam(value = "id") JenisTagihan jenisTagihan, @RequestParam(name = "bank") Bank bank) {
        if (jenisTagihan == null) {
            return "redirect:/jenistagihan/list";
        }

        if (bank == null) {
            return REDIRECT_JENIS_TAGIHAN_BANK + jenisTagihan.getId();
        }

        jenisTagihan.getDaftarBank().remove(bank);
        jenisTagihanDao.save(jenisTagihan);

        return REDIRECT_JENIS_TAGIHAN_BANK + jenisTagihan.getId();
    }
}
