package id.ac.tazkia.payment.virtualaccount;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

        String nim = "123";
        System.out.println(String.format("%10s", nim).replace(' ', '0'));
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
