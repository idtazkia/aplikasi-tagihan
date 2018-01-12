package id.ac.tazkia.payment.virtualaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@SpringBootApplication
public class PaymentVirtualAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentVirtualAccountApplication.class, args);
	}
        
        @Bean
        public SpringSecurityDialect springSecurityDialect(){
            return new SpringSecurityDialect();
        }
        
        @Bean
        public SpringDataDialect springDataDialect(){
            return new SpringDataDialect();
        }
}
