package id.ac.tazkia.payment.virtualaccount;

import com.bni.encrypt.BNIHash;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.tazkia.payment.virtualaccount.bni.config.BniEcollectionConfiguration;
import id.ac.tazkia.payment.virtualaccount.bni.dto.PaymentNotificationRequest;
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


    @Test
    public void testEncryptPaymentNotification() throws Exception{
        String data = "{ \"trx_id\" : \"1230000001\", \"virtual_account\" : \"8001000000000001\", \"customer_name\" : \"Mr. X\", \"trx_amount\" : \"100000\", \"payment_amount\" : \"100000\", \"cumulative_payment_amount\" : \"100000\", \"payment_ntb\" : \"233171\", \"datetime_payment\" : \"2016-03-01 14:00:00\", \"datetime_payment_iso8601\" : \"2016-03-01T14:00:00+07:00\" }";
        ObjectMapper mapper = new ObjectMapper();
        PaymentNotificationRequest payReq = mapper.readValue(data, PaymentNotificationRequest.class);
        String encrypted = BNIHash.hashData(data, config.getClientId(), config.getClientKey());
        System.out.println("Encrypted : "+encrypted);
    }

}
