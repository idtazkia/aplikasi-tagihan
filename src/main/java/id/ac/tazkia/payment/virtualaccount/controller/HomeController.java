package id.ac.tazkia.payment.virtualaccount.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @Value("classpath:sample/tagihan.csv")
    private Resource contohFileTagihan;

    @Value("classpath:sample/debitur.csv")
    private Resource contohFileDebitur;

    @GetMapping("/home")
    public ModelMap home(){
        return new ModelMap().addAttribute("pageTitle", "Dashboard");
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
