package id.ac.tazkia.payment.virtualaccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.tazkia.payment.virtualaccount.dto.VaPayment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentVirtualAccountApplicationTests {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
	public void checkConfig() {
        System.out.println("Application runs ok");
    }

    @Test
    public void testSerializeLocalDateTime() throws Exception {
        VaPayment payment = new VaPayment();
        payment.setPaymentTime(LocalDateTime.now());
        System.out.println(objectMapper.writeValueAsString(payment));
    }

    @Test
    public void testGeneratePassword() {
        String hashed = passwordEncoder.encode("test123");
        System.out.println("Hashed : "+hashed);
    }
}
