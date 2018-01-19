package id.ac.tazkia.payment.virtualaccount;

import id.ac.tazkia.payment.virtualaccount.service.RunningNumberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@SpringBootApplication
@EnableScheduling
@EnableKafka
public class PaymentVirtualAccountApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentVirtualAccountApplication.class);

	@Autowired
	private RunningNumberService runningNumberService;

	@Override
	public void run(String... args) throws Exception {
		LOGGER.debug("Inisialisasi Running Number");
		runningNumberService.getNumber();
	}

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
