package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.dto.RekapTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.StatusTagihan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @Test
    public void rekapTagihan() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date mulai = formatter.parse("2018-01-01");
        Date sampai = formatter.parse("2018-02-01");
        List<RekapTagihan> rekapTagihanList = tagihanDao.rekapTagihan(mulai, sampai);
        for (RekapTagihan rekap : rekapTagihanList) {
            System.out.println("Jenis Tagihan : "+rekap.getJenisTagihan().getNama());
            System.out.println("Status Pembayaran : "+rekap.getStatusPembayaran());
            System.out.println("Jumlah Tagihan : "+rekap.getJumlahTagihan());
            System.out.println("Nilai Tagihan : "+rekap.getNilaiTagihan());
            System.out.println("Nilai Pembayaran : "+rekap.getNilaiPembayaran());
        }
    }
}
