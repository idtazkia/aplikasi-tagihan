package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.DebiturDao;
import id.ac.tazkia.payment.virtualaccount.dao.JenisTagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.dto.UpdateTagihan;
import id.ac.tazkia.payment.virtualaccount.dto.UploadError;
import id.ac.tazkia.payment.virtualaccount.entity.Debitur;
import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.StatusTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.service.KafkaSenderService;
import id.ac.tazkia.payment.virtualaccount.service.TagihanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tagihan")
public class TagihanController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagihanController.class);

    @Autowired
    private TagihanDao tagihanDao;
    @Autowired
    private TagihanService tagihanService;
    @Autowired private KafkaSenderService kafkaSenderService;
    @Autowired
    private JenisTagihanDao jenisTagihanDao;
    @Autowired private DebiturDao debiturDao;

    @GetMapping("/list")
    public ModelMap listTagihan(@RequestParam(value = "jenis", required = false) JenisTagihan jenisTagihan,
                                @RequestParam(value = "debitur", required = false) Debitur debitur,
                                @PageableDefault(size = 10, sort = "nomor") Pageable pageable) {
        if (jenisTagihan != null) {
            return new ModelMap()
                    .addAttribute("jenisTagihan", jenisTagihan)
                    .addAttribute("listTagihan", tagihanDao
                            .findByJenisTagihanAndStatusTagihanOrderByTanggalTagihan(
                                    jenisTagihan, StatusTagihan.AKTIF, pageable));
        }

        if (debitur != null) {
            return new ModelMap()
                    .addAttribute("listTagihan", tagihanDao
                            .findByDebiturAndStatusTagihanOrderByTanggalTagihan(
                                    debitur, StatusTagihan.AKTIF, pageable));
        }

        return new ModelMap("listTagihan", tagihanDao.findAllByStatusTagihan(StatusTagihan.AKTIF, pageable));
    }

    @ModelAttribute("listJenisTagihan")
    public Iterable<JenisTagihan> daftarJenisTagihan() {
        return jenisTagihanDao.findByAktifOrderByKode(true);
    }

    @ModelAttribute("listDebitur")
    public Iterable<Debitur> daftarDebitur() {
        return debiturDao.findAll(new Sort(Sort.Direction.ASC, "nomorDebitur"));
    }

    @GetMapping("/form")
    public ModelMap displayForm(
            @RequestParam Debitur debitur,
            @RequestParam(name = "id", required = false) Tagihan tagihan) {
        if (tagihan == null) {
            tagihan = new Tagihan();
            tagihan.setNomor("--- otomatis ditentukan sistem ---");
            tagihan.setTanggalJatuhTempo(Date.from(LocalDate.now().plusMonths(6).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        tagihan.setDebitur(debitur);

        return new ModelMap().addAttribute("tagihan", tagihan);

    }

    @PostMapping("/form")
    public String processForm(@ModelAttribute @Valid Tagihan tagihan, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "tagihan/form";
        }
        tagihanService.saveTagihan(tagihan);
        status.setComplete();
        return "redirect:list";
    }

    @GetMapping("/update")
    public ModelMap displayUpdateForm(@RequestParam Tagihan tagihan) {
        UpdateTagihan ut = new UpdateTagihan();
        ut.setTanggalJatuhTempo(tagihan.getTanggalJatuhTempo());
        ut.setNilaiTagihan(tagihan.getNilaiTagihan());

        return new ModelMap()
                .addAttribute("updateTagihan", ut)
                .addAttribute("tagihan", tagihan);
    }

    @PostMapping("/update")
    public String processUpdateForm(@RequestParam Tagihan tagihan, Model data,
                                    @ModelAttribute @Valid UpdateTagihan updateTagihan,
                                    BindingResult errors, SessionStatus status) {
        if (tagihan == null) {
            LOGGER.warn("Update tagihan null");
            return "redirect:list";
        }

        if (errors.hasErrors()) {
            data.addAttribute("updateTagihan", updateTagihan).addAttribute("tagihan", tagihan);
            LOGGER.debug("Update tagihan datanya tidak valid {}", errors.getAllErrors());
            return "/tagihan/update";
        }

        tagihan.setNilaiTagihan(updateTagihan.getNilaiTagihan());
        tagihan.setTanggalJatuhTempo(updateTagihan.getTanggalJatuhTempo());

        tagihanService.saveTagihan(tagihan);
        status.setComplete();
        return "redirect:list";
    }

    @GetMapping("/hapus")
    public ModelMap displayHapusForm(@RequestParam Tagihan tagihan) {

        return new ModelMap()
                .addAttribute("tagihan", tagihan);
    }

    @PostMapping("/hapus")
    public String processHapusForm(@RequestParam Tagihan tagihan) {
        if (tagihan == null) {
            LOGGER.warn("Update tagihan null");
            return "redirect:list";
        }
        tagihan.setStatusTagihan(StatusTagihan.NONAKTIF);
        tagihanService.saveTagihan(tagihan);
        return "redirect:list";
    }

    @GetMapping("/notifikasi")
    public ModelMap displayNotifikasiForm(@RequestParam Tagihan tagihan) {
        return new ModelMap()
                .addAttribute("tagihan", tagihan);
    }

    @PostMapping("/notifikasi")
    public String processNotifikasiForm(@RequestParam Tagihan tagihan) {
        if (tagihan == null) {
            LOGGER.warn("Notifikasi tagihan null");
            return "redirect:list";
        }
        kafkaSenderService.sendNotifikasiTagihan(tagihan);
        return "redirect:list";
    }

    @GetMapping("/upload/form")
    public void displayFormUpload(){}

    @PostMapping("/upload/form")
    public String processFormUpload(@RequestParam JenisTagihan jenisTagihan,
                              @RequestParam(required = false) Boolean pakaiHeader,
                              MultipartFile fileTagihan,
                              RedirectAttributes redirectAttrs) {
        LOGGER.debug("Jenis Tagihan : {}",jenisTagihan.getNama());
        LOGGER.debug("Pakai Header : {}",pakaiHeader);
        LOGGER.debug("Nama File : {}",fileTagihan.getName());
        LOGGER.debug("Ukuran File : {}",fileTagihan.getSize());

        List<UploadError> errors = new ArrayList<>();
        Integer baris = 0;

        if(jenisTagihan == null){
            errors.add(new UploadError(baris, "Jenis tagihan harus diisi", ""));
            redirectAttrs
                    .addFlashAttribute("jumlahBaris", 0)
                    .addFlashAttribute("jumlahSukses", 0)
                    .addFlashAttribute("jumlahError", errors.size())
                    .addFlashAttribute("errors", errors);
            return "redirect:hasil";
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileTagihan.getInputStream()));
            String content;

            if((pakaiHeader != null && pakaiHeader)){
                content = reader.readLine();
            }

            while ((content = reader.readLine()) != null) {
                baris++;
                String[] data = content.split(",");
                if (data.length != 4) {
                    errors.add(new UploadError(baris, "Format data salah", content));
                    continue;
                }

                Tagihan t = new Tagihan();
                t.setJenisTagihan(jenisTagihan);
                t.setTanggalTagihan(new Date());
                t.setNomor(data[0]);

                Debitur d = debiturDao.findByNomorDebitur(data[0]);
                if (d == null) {
                    errors.add(new UploadError(baris, "Debitur "+data[1]+" tidak terdaftar", content));
                    continue;
                }

                t.setDebitur(d);
                t.setKeterangan(data[1]);

                try {
                    t.setNilaiTagihan(new BigDecimal(data[2]));
                } catch (NumberFormatException ex) {
                    errors.add(new UploadError(baris, "Format nilai tagihan salah", content));
                    continue;
                }

                try {
                    Date tanggalJatuhTempo = Date.from(LocalDate.parse(data[3], DateTimeFormatter.ISO_LOCAL_DATE)
                            .atStartOfDay(ZoneId.systemDefault()).toInstant());
                    t.setTanggalJatuhTempo(tanggalJatuhTempo);
                } catch (DateTimeParseException ex) {
                    errors.add(new UploadError(baris, "Format tanggal salah", content));
                    continue;
                }

                try {
                    tagihanService.saveTagihan(t);
                } catch (Exception ex) {
                    LOGGER.warn(ex.getMessage(), ex);
                    errors.add(new UploadError(baris, "Gagal simpan ke database", content));
                    continue;
                }

            }
        } catch (Exception err){
            LOGGER.warn(err.getMessage(), err);
            errors.add(new UploadError(0, "Format file salah", ""));
        }

        redirectAttrs
                .addFlashAttribute("jumlahBaris", baris)
                .addFlashAttribute("jumlahSukses", baris - errors.size())
                .addFlashAttribute("jumlahError", errors.size())
                .addFlashAttribute("errors", errors);

        return "redirect:hasil";
    }

    @GetMapping("/upload/hasil")
    public void hasilFormUpload(){}

}
