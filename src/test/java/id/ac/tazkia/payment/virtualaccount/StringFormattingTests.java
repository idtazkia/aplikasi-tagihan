package id.ac.tazkia.payment.virtualaccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.tazkia.payment.virtualaccount.dto.VaRequest;
import id.ac.tazkia.payment.virtualaccount.entity.TipePembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.VaStatus;
import id.ac.tazkia.payment.virtualaccount.helper.VirtualAccountNumberGenerator;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class StringFormattingTests {
    @Test
    public void testFormatVa(){
        Long runningNumber = 10L;
        System.out.println(String.format("%06d", runningNumber));

        System.out.println(VirtualAccountNumberGenerator
                .generateVirtualAccountNumber("123", 8));
        System.out.println(VirtualAccountNumberGenerator
                .generateVirtualAccountNumber("1234567890", 8));
    }

    @Test
    public void testNoVa() {
        System.out.println("8311201801016801".substring(4));
        System.out.println("900900151939721".substring(7));
    }

    @Test
    public void testVaRequest() throws Exception {
        SimpleDateFormat FORMATTER_ISO_DATE = new SimpleDateFormat("yyyy-MM-dd");
        VaRequest vaRequest
                = VaRequest.builder()
                .accountType(TipePembayaran.CLOSED)
                .requestType(VaStatus.CREATE)
                .accountNumber("123")
                .amount(BigDecimal.TEN)
                .description("Test VA")
                .expireDate(FORMATTER_ISO_DATE.format(new Date()))
                .invoiceNumber("123")
                .name("Test Debitur")
                .bankId("b123")
                .build();

        String json = new ObjectMapper().writeValueAsString(vaRequest);
        System.out.println(json);
    }

    @Test
    public void testKonversiTanggalIso8601() {
        Date d = new Date();
        String hasil = toIso8601(d);
        System.out.println("ISO 8601 : "+hasil);
    }

    private String toIso8601(Date d) {
        Instant i = d.toInstant();
        System.out.println("Instant : "+i);
        LocalDate ld = i.atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println("Local Date : " +ld);
        ZonedDateTime zdt = i.atZone(ZoneId.systemDefault());
        System.out.println("Zoned Date Time : "+zdt);
        return zdt.truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }


    @Test
    public void bcryptPassword(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(13);
        String password = "test123";
        String hashed = encoder.encode(password);
        System.out.println("Hashed : "+hashed);
    }
}
