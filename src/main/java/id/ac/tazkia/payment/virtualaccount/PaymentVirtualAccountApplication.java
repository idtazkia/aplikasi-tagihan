package id.ac.tazkia.payment.virtualaccount;

import id.ac.tazkia.payment.virtualaccount.service.RunningNumberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

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
}
