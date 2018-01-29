package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.StatusTagihan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagihanDaoTests {

    @Autowired private TagihanDao tagihanDao;

    @Test
    public void testHitungTotalTagihan() {
        JenisTagihan jt = new JenisTagihan();
        jt.setId("pmb2017");
        BigDecimal total = tagihanDao.totalTagihanByJenisTagihanAndStatusTagihan(jt, StatusTagihan.AKTIF);
        System.out.println("Total Tagihan : "+total);
    }

    @Test
    public void testHitungJumlahTagihan() {
        JenisTagihan jt = new JenisTagihan();
        jt.setId("pmb2017");
        Long jumlah = tagihanDao.jumlahTagihanByJenisTagihanAndStatusTagihan(jt, StatusTagihan.AKTIF);
        System.out.println("Jumlah Tagihan : "+jumlah);
    }
}
