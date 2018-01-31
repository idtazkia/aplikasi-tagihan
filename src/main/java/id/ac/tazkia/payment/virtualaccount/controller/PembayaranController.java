package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.JenisTagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.PembayaranDao;
import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/pembayaran")
public class PembayaranController {

    @Autowired private PembayaranDao pembayaranDao;
    @Autowired private JenisTagihanDao jenisTagihanDao;

    @ModelAttribute("listJenisTagihan")
    public Iterable<JenisTagihan> daftarJenisTagihan() {
        return jenisTagihanDao.findAll(new Sort(Sort.Direction.ASC, "kode"));
    }

    @PreAuthorize("hasAuthority('EDIT_PEMBAYARAN')")
    @GetMapping("/form")
    public ModelMap displayForm(@RequestParam(value = "id", required = false) String id) {
        Pembayaran p;

        if (id != null) {
            p = pembayaranDao.findOne(id);
        } else {
            p = new Pembayaran();
        }

        return new ModelMap()
                .addAttribute("pembayaran", p);
    }

    @PreAuthorize("hasAuthority('VIEW_PEMBAYARAN')")
    @GetMapping("/list")
    public ModelMap findAllHtml(@RequestParam(required = false) JenisTagihan jenis,
                                @RequestParam(required = false) Tagihan tagihan,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date mulai,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sampai,
                                @PageableDefault(sort = "waktuTransaksi", direction = Sort.Direction.DESC) Pageable pageable) {

        if(tagihan != null) {
            return new ModelMap()
                    .addAttribute("data", pembayaranDao.findByTagihanOrderByWaktuTransaksi(tagihan, pageable));
        } else {
            if (mulai != null && sampai != null) {
                if (jenis != null) {
                    Page<Pembayaran> hasil = pembayaranDao
                            .findByJenisTagihanAndWaktuTransaksi(jenis, mulai, sampai, pageable);
                    return new ModelMap()
                            .addAttribute("jenisTagihan", jenis)
                            .addAttribute("data", hasil);
                } else {
                    return new ModelMap()
                            .addAttribute("data",
                                    pembayaranDao
                                            .findByWaktuTransaksi(mulai, sampai, pageable));
                }
            } else {
                if (jenis != null) {
                    return new ModelMap()
                            .addAttribute("jenisTagihan", jenis)
                            .addAttribute("data", pembayaranDao.findByTagihanJenisTagihan(jenis, pageable));
                }
            }
        }

        return new ModelMap().addAttribute("data", pembayaranDao.findAll(pageable));
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "Data Pembayaran";
    }
}
