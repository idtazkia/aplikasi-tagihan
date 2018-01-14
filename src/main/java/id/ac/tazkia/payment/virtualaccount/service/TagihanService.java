package id.ac.tazkia.payment.virtualaccount.service;

import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.VirtualAccountDao;
import id.ac.tazkia.payment.virtualaccount.entity.Bank;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;

@Service @Transactional
public class TagihanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagihanService.class);

    @Autowired private TagihanDao tagihanDao;
    @Autowired private VirtualAccountDao virtualAccountDao;

    public void createTagihan(Tagihan t) {
        t.setNilaiTagihan(t.getNilaiTagihan().setScale(0, RoundingMode.DOWN));
        tagihanDao.save(t);
        for (Bank b : t.getJenisTagihan().getDaftarBank()) {
            VirtualAccount va = new VirtualAccount();
            va.setBank(b);
            va.setTagihan(t);
            virtualAccountDao.save(va);
        }

    }
}
