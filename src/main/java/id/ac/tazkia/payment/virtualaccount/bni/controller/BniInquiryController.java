package id.ac.tazkia.payment.virtualaccount.bni.controller;

import id.ac.tazkia.payment.virtualaccount.bni.service.BniVaService;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/callback/bni")
public class BniInquiryController {
    @Autowired private BniVaService bniVaService;

    @GetMapping("/inquiry/{va}")
    public Map<String, String> inquiry(@PathVariable("va") VirtualAccount va){
        return bniVaService.inquiryVa(va.getIdVirtualAccount());
    }
}
