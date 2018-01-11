package id.ac.tazkia.payment.virtualaccount.service;

import id.ac.tazkia.payment.virtualaccount.dao.BankDao;
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

@Service @Transactional
public class TagihanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagihanService.class);

    @Autowired private TagihanDao tagihanDao;
    @Autowired private VirtualAccountDao virtualAccountDao;
    @Autowired private BankDao bankDao;

    public void createTagihan(Tagihan t) {
        LOGGER.debug("ID Tagihan : "+t.getId());
        tagihanDao.save(t);
        for (Bank b : bankDao.findAll()) {
            if (b.getAktif()) {
                VirtualAccount va = new VirtualAccount();
                va.setBank(b);
                va.setTagihan(t);
                virtualAccountDao.save(va);
            }
        }

    }
}
