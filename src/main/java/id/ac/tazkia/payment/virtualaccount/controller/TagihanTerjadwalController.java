package id.ac.tazkia.payment.virtualaccount.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jadwal/tagihan")
public class TagihanTerjadwalController {

    @GetMapping("/konfigurasi/form")
    public void displayFormKonfigurasiJadwalTagihan(){

    }

    @PostMapping("/konfigurasi/form")
    public void prosesFormKonfigurasiJadwalTagihan(){

    }

    @GetMapping("/upload")
    public void displayFormUploadJadwalTagihan(){

    }

    @PostMapping("/upload")
    public void prosesFormUploadJadwalTagihan(){

    }

    @GetMapping("/list")
    public void daftarJadwalTagihan(){

    }
}