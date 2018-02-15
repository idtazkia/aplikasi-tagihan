package id.ac.tazkia.payment.virtualaccount;


import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.VirtualAccountDao;
import id.ac.tazkia.payment.virtualaccount.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateVaBulkTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateVaBulkTests.class);
    @Autowired private TagihanDao tagihanDao;
    @Autowired private VirtualAccountDao virtualAccountDao;

    @Test
    public void generateVaBsm() {
        Bank bsm = new Bank();
        bsm.setId("bsm001");

        JenisTagihan spp = new JenisTagihan();
        spp.setId("5b4cf035-4538-4722-aeb2-ab6971c8f967");

        Iterable<Tagihan> daftarTagihanSpp = tagihanDao.findByJenisTagihanAndStatusTagihanOrderByTanggalTagihan(spp, StatusTagihan.AKTIF);
        for (Tagihan t : daftarTagihanSpp) {
            LOGGER.info("Create VA untuk tagihan {} atas nama {}",
                    t.getNomor()+"/"+t.getNilaiTagihanEfektif(), t.getDebitur().getNomorDebitur()+" - "+t.getDebitur().getNama());
            VirtualAccount va = new VirtualAccount();
            va.setBank(bsm);
            va.setTagihan(t);
            va.setVaStatus(VaStatus.NONAKTIF);
            virtualAccountDao.save(va);
        }
    }
}
