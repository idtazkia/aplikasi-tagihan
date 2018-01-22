package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dao.DebiturDao;
import id.ac.tazkia.payment.virtualaccount.dto.UploadError;
import id.ac.tazkia.payment.virtualaccount.entity.Debitur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Controller
public class DebiturController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebiturController.class);

    @Autowired
    private DebiturDao debiturDao;

    @Autowired
    private Validator validator;

    @PreAuthorize("hasAuthority('VIEW_DEBITUR')")
    @GetMapping("/api/debitur/")
    @ResponseBody
    public Page<Debitur> findAll(String search, Pageable page) {
        return debiturDao.findByNomorDebiturOrNamaContainingIgnoreCase(search, search, page);
    }

    @PreAuthorize("hasAuthority('VIEW_DEBITUR')")
    @GetMapping("/debitur/list")
    public String daftarDebitur(ModelMap mm, @RequestParam(value = "key", required = false) String key, 
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Debitur> result;
        
        if (key != null) {
            result = debiturDao.findByNomorDebiturContainingIgnoreCase(key, pageable);
            mm.addAttribute("key", key);
        } else {
            result = debiturDao.findAll(pageable);
        }
        
        mm.addAttribute("data", result);

        return "debitur/list";
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "Data Debitur";
    }

    @PreAuthorize("hasAuthority('EDIT_DEBITUR')")
    @GetMapping("/debitur/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) String id) {
        Debitur debitur;

        if (id == null) {
            debitur = new Debitur();
        } else {
            debitur = debiturDao.findOne(id);
        }

        return new ModelMap("debitur", debitur);
    }

    @PreAuthorize("hasAuthority('EDIT_DEBITUR')")
    @PostMapping("/debitur/form")
    public String prosesForm(@ModelAttribute @Valid Debitur debitur, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "/debitur/form";
        }

        debiturDao.save(debitur);
        status.setComplete();
        return "redirect:list";
    }

    @PreAuthorize("hasAuthority('EDIT_DEBITUR')")
    @GetMapping("/debitur/delete{id}")
    public String hapusData(@RequestParam(value = "id", required = false) String id) {
        debiturDao.delete(id);

        return "redirect:/debitur/list";

    }

    @GetMapping("/debitur/upload/form")
    public void displayFormUpload() {
    }

    @PostMapping("/debitur/upload/form")
    public String processFormUpload(@RequestParam(required = false) Boolean pakaiHeader,
            MultipartFile fileDebitur,
            RedirectAttributes redirectAttrs) {
        LOGGER.debug("Pakai Header : {}", pakaiHeader);
        LOGGER.debug("Nama File : {}", fileDebitur.getName());
        LOGGER.debug("Ukuran File : {}", fileDebitur.getSize());

        List<UploadError> errors = new ArrayList<>();
        Integer baris = 0;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileDebitur.getInputStream()));
            String content;

            if ((pakaiHeader != null && pakaiHeader)) {
                content = reader.readLine();
            }

            while ((content = reader.readLine()) != null) {
                baris++;
                String[] data = content.split(",", -1);
                if (data.length != 4) {
                    errors.add(new UploadError(baris, "Format data salah", content));
                    continue;
                }

                if (!StringUtils.hasText(data[0])) {
                    errors.add(new UploadError(baris, "Nomor debitur harus diisi", content));
                    continue;
                }

                if (!StringUtils.hasText(data[1])) {
                    errors.add(new UploadError(baris, "Nama debitur harus diisi", content));
                    continue;
                }

                Debitur d = new Debitur();
                d.setNomorDebitur(data[0]);
                d.setNama(data[1]);

                if (StringUtils.hasText(data[2])) {
                    d.setEmail(data[2]);
                    BeanPropertyBindingResult binder = new BeanPropertyBindingResult(d, "debitur");
                    validator.validate(d, binder);

                    if (binder.hasFieldErrors("email")) {
                        errors.add(new UploadError(baris, "Format email salah", content));
                        continue;
                    }
                }

                if (StringUtils.hasText(data[3])) {
                    d.setNoHp(data[3]);
                }

                if (debiturDao.findByNomorDebitur(d.getNomorDebitur()) != null) {
                    errors.add(new UploadError(baris, "Nomor debitur " + data[0] + " sudah digunakan", content));
                    continue;
                }

                try {
                    debiturDao.save(d);
                } catch (Exception ex) {
                    errors.add(new UploadError(baris, "Gagal simpan ke database", content));
                    LOGGER.warn(ex.getMessage(), ex);
                    continue;
                }
            }
        } catch (Exception err) {
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

    @GetMapping("/debitur/upload/hasil")
    public void hasilFormUpload() {
    }

}
