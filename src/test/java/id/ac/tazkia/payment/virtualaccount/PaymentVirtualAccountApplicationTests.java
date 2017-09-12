package id.ac.tazkia.payment.virtualaccount;

import id.ac.tazkia.payment.virtualaccount.bni.config.BniEcollectionConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class PaymentVirtualAccountApplicationTests {

    @Autowired private BniEcollectionConfiguration config;

    @Test
	public void checkConfig() {
        Assert.assertNotNull(config.getBankId());
        Assert.assertNotNull(config.getClientId());
        Assert.assertNotNull(config.getClientKey());
        Assert.assertNotNull(config.getServerUrl());

        System.out.println("Bank ID : "+config.getBankId());
        System.out.println("Client ID : "+config.getClientId());
        System.out.println("Client Key : "+config.getClientKey());
        System.out.println("Server URL : "+config.getServerUrl());
    }

}
