package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.dto.LaporanTagihan;
import id.ac.tazkia.payment.virtualaccount.dto.RekapTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.StatusPembayaran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class HomeController {
    private static final String TANGGAL_LIVE = "20180101";

    @Value("classpath:sample/tagihan.csv")
    private Resource contohFileTagihan;

    @Value("classpath:sample/debitur.csv")
    private Resource contohFileDebitur;

    @Autowired private TagihanDao tagihanDao;

    @GetMapping("/home")
    public ModelMap home(){
        List<RekapTagihan> rekap = tagihanDao
                .rekapTagihan(Date.from(LocalDate.parse(TANGGAL_LIVE, DateTimeFormatter.BASIC_ISO_DATE).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Map<String, LaporanTagihan> daftarLaporanTagihan = new LinkedHashMap<>();

        for (RekapTagihan r : rekap) {
            LaporanTagihan laporanTagihan = daftarLaporanTagihan.get(r.getJenisTagihan().getId());
            if (laporanTagihan == null) {
                laporanTagihan = new LaporanTagihan();
                laporanTagihan.setJenisTagihan(r.getJenisTagihan());
                daftarLaporanTagihan.put(r.getJenisTagihan().getId(), laporanTagihan);
            }

            if (!StatusPembayaran.LUNAS.equals(r.getStatusPembayaran())) {
                laporanTagihan.setJumlahTagihanBelumLunas(
                        laporanTagihan.getJumlahTagihanBelumLunas()
                                + r.getJumlahTagihan());
            }

            if (StatusPembayaran.LUNAS.equals(r.getStatusPembayaran())) {
                laporanTagihan.setJumlahTagihanLunas(
                        laporanTagihan.getJumlahTagihanLunas()
                                + r.getJumlahTagihan());
            }

            laporanTagihan.setNilaiTagihan(
                    laporanTagihan.getNilaiTagihan()
                            .add(r.getNilaiTagihan()));

            laporanTagihan.setNilaiPembayaran(
                    laporanTagihan.getNilaiPembayaran()
                            .add(r.getNilaiPembayaran()));
        }
        return new ModelMap()
                .addAttribute("daftarLaporanTagihan", new ArrayList<>(daftarLaporanTagihan.values()))
                .addAttribute("pageTitle", "Dashboard");
    }

    @GetMapping("/contoh/tagihan")
    public void downloadContohFileTagihan(HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=contoh-tagihan.csv");
        FileCopyUtils.copy(contohFileTagihan.getInputStream(), response.getOutputStream());
        response.getOutputStream().flush();
    }

    @GetMapping("/contoh/debitur")
    public void downloadContohFileDebitur(HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=contoh-debitur.csv");
        FileCopyUtils.copy(contohFileDebitur.getInputStream(), response.getOutputStream());
        response.getOutputStream().flush();
    }
}
