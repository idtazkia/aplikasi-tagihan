package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.DebiturDao;
import id.ac.tazkia.payment.virtualaccount.dao.JenisTagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.dto.UploadError;
import id.ac.tazkia.payment.virtualaccount.entity.Debitur;
import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.service.TagihanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
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
    @Autowired
    private JenisTagihanDao jenisTagihanDao;
    @Autowired private DebiturDao debiturDao;

    @GetMapping("/list")
    public ModelMap listTagihan(Pageable pageable) {
        return new ModelMap("listTagihan", tagihanDao.findAll(pageable));
    }

    @ModelAttribute("listJenisTagihan")
    public Iterable<JenisTagihan> daftarJenisTagihan() {
        return jenisTagihanDao.findAll(new Sort(Sort.Direction.ASC, "kode"));
    }

    @ModelAttribute("listDebitur")
    public Iterable<Debitur> daftarDebitur() {
        return debiturDao.findAll(new Sort(Sort.Direction.ASC, "nomorDebitur"));
    }

    @GetMapping("/form")
    public ModelMap displayForm(@RequestParam(name = "id", required = false) Tagihan tagihan) {
        if (tagihan == null) {
            tagihan = new Tagihan();
        }

        return new ModelMap().addAttribute("tagihan", tagihan);

    }

    @PostMapping("/form")
    public String processForm(@ModelAttribute @Valid Tagihan tagihan, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "tagihan/form";
        }
        tagihanService.createTagihan(tagihan);
        status.setComplete();
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
                if (data.length != 5) {
                    errors.add(new UploadError(baris, "Format data salah", content));
                    continue;
                }

                Tagihan t = new Tagihan();
                t.setJenisTagihan(jenisTagihan);
                t.setTanggalTagihan(new Date());
                t.setNomor(data[0]);

                Debitur d = debiturDao.findByNomorDebitur(data[1]);
                if (d == null) {
                    errors.add(new UploadError(baris, "Debitur "+data[1]+" tidak terdaftar", content));
                    continue;
                }

                t.setDebitur(d);
                t.setKeterangan(data[2]);

                try {
                    t.setNilaiTagihan(new BigDecimal(data[3]));
                } catch (NumberFormatException ex) {
                    errors.add(new UploadError(baris, "Format nilai tagihan salah", content));
                    continue;
                }

                try {
                    Date tanggalJatuhTempo = Date.from(LocalDate.parse(data[4], DateTimeFormatter.ISO_LOCAL_DATE)
                            .atStartOfDay(ZoneId.systemDefault()).toInstant());
                    t.setTanggalJatuhTempo(tanggalJatuhTempo);
                } catch (DateTimeParseException ex) {
                    errors.add(new UploadError(baris, "Format tanggal salah", content));
                    continue;
                }

                if (tagihanDao.findByNomor(data[0]) != null) {
                    errors.add(new UploadError(baris, "Nomor tagihan "+data[0]+" sudah digunakan", content));
                    continue;
                }

                try {
                    tagihanService.createTagihan(t);
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
