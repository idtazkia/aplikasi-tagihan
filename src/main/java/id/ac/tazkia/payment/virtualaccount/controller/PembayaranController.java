package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.JenisTagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.PembayaranDao;
import id.ac.tazkia.payment.virtualaccount.dto.RekapPembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Debitur;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/pembayaran")
public class PembayaranController {

    @Autowired private PembayaranDao pembayaranDao;
    @Autowired private JenisTagihanDao jenisTagihanDao;

    private SimpleDateFormat formatterTanggal = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatterWaktu = new SimpleDateFormat("HH:mm:ss");

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
                                @RequestParam(required = false) Debitur debitur,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date mulai,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sampai,
                                @PageableDefault(sort = "waktuTransaksi", direction = Sort.Direction.DESC) Pageable pageable) {

        if(tagihan != null) {
            return new ModelMap()
                    .addAttribute("data", pembayaranDao.findByTagihanOrderByWaktuTransaksi(tagihan, pageable));
        }

        if (jenis != null) {
            if (mulai != null && sampai != null) {
                Page<Pembayaran> hasil = pembayaranDao
                        .findByJenisTagihanAndWaktuTransaksi(jenis, mulai, sampai, pageable);
                return new ModelMap()
                        .addAttribute("jenisTagihan", jenis)
                        .addAttribute("data", hasil);
            }
            return new ModelMap()
                    .addAttribute("jenisTagihan", jenis)
                    .addAttribute("data", pembayaranDao.findByTagihanJenisTagihan(jenis, pageable));
        }

        if (mulai != null && sampai != null) {
            return new ModelMap()
                    .addAttribute("data",
                            pembayaranDao
                                    .findByWaktuTransaksi(mulai, sampai, pageable));

        }

        if (debitur != null) {
            return new ModelMap()
                    .addAttribute("debitur", debitur)
                    .addAttribute("data", pembayaranDao.findByTagihanDebiturOrderByWaktuTransaksi(debitur, pageable));
        }


        return new ModelMap().addAttribute("data", pembayaranDao.findAll(pageable));
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "Data Pembayaran";
    }

    @ModelAttribute("awalBulan")
    public String awalBulan(){
        return LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @ModelAttribute("akhirBulan")
    public String akhirBulan(){
        return LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @GetMapping("/rekap")
    @ResponseBody
    public List<RekapPembayaran> rekapPembayaranBulanan() {
        LocalDate sekarang = LocalDate.now();
        Date mulai = Date.from(sekarang.minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date sampai = Date.from(sekarang.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Map<String, RekapPembayaran> hasil = new LinkedHashMap<>();

        for(LocalDate date = sekarang.minusMonths(1); date.isBefore(sekarang); date = date.plusDays(1)) {
            RekapPembayaran rekap = new RekapPembayaran(
                    new java.sql.Date(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()),
                    BigDecimal.ZERO, 0L);
            hasil.put(date.format(DateTimeFormatter.BASIC_ISO_DATE), rekap);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        for (RekapPembayaran r : pembayaranDao.rekapPembayaran(mulai, sampai)) {
            r.setJumlah(r.getJumlah());
            hasil.put(formatter.format(r.getTanggal()), r);
        }

        return new ArrayList<>(hasil.values());
    }

    @GetMapping("/csv")
    public void rekapPembayaranCsv(@RequestParam JenisTagihan jenis,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date mulai,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date sampai,
                                   HttpServletResponse response) throws Exception {
        String filename = "pembayaran-"
                +formatterTanggal.format(mulai).replace("-", "")
                +"-"
                +formatterTanggal.format(sampai).replace("-", "")
                +".csv";
        response.setHeader("Content-Disposition", "attachment;filename="+filename);
        response.setContentType("text/csv");
        response.getWriter().println("No,NIM,Nama,Bank,Nominal,Tanggal Transfer,Waktu Transfer");


        Iterable<Pembayaran> dataPembayaran = pembayaranDao
                .findByTagihanJenisTagihanAndWaktuTransaksiBetweenOrderByWaktuTransaksi(jenis,mulai, sampai);

        Integer baris = 0;
        BigDecimal total = BigDecimal.ZERO;
        for (Pembayaran p : dataPembayaran) {
            baris++;
            total = total.add(p.getJumlah());
            response.getWriter().print(baris);
            response.getWriter().print(",");
            response.getWriter().print(p.getTagihan().getDebitur().getNomorDebitur());
            response.getWriter().print(",");
            response.getWriter().print(p.getTagihan().getDebitur().getNama());
            response.getWriter().print(",");
            response.getWriter().print(p.getBank().getNama());
            response.getWriter().print(",");
            response.getWriter().print(p.getJumlah().setScale(0).toPlainString());
            response.getWriter().print(",");
            response.getWriter().print(formatterTanggal.format(p.getWaktuTransaksi()));
            response.getWriter().print(",");
            response.getWriter().print(formatterWaktu.format(p.getWaktuTransaksi()));
            response.getWriter().println();
        }

        response.getWriter().print("-");
        response.getWriter().print(",");
        response.getWriter().print("-");
        response.getWriter().print(",");
        response.getWriter().print("Jumlah");
        response.getWriter().print(",");
        response.getWriter().print("-");
        response.getWriter().print(",");
        response.getWriter().print(total.setScale(0).toPlainString());
        response.getWriter().print(",");
        response.getWriter().print("-");
        response.getWriter().print(",");
        response.getWriter().print("-");
        response.getWriter().println();

        response.getWriter().flush();
    }
}
