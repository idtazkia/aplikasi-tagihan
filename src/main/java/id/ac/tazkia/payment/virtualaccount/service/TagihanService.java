package id.ac.tazkia.payment.virtualaccount.service;

import id.ac.tazkia.payment.virtualaccount.dao.*;
import id.ac.tazkia.payment.virtualaccount.entity.Debitur;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class TagihanService {

    @Autowired private TagihanDao tagihanDao;
    @Autowired private PembayaranDao pembayaranDao;
    @Autowired private VirtualAccountDao virtualAccountDao;
    @Autowired private BankDao bankDao;
    @Autowired private DebiturDao debiturDao;

    public void createTagihan(Tagihan t){
        Debitur s = debiturDao.findByNomorDebitur(t.getDebitur().getNomorDebitur());

        if(s == null){
            s = t.getDebitur();
            debiturDao.save(s);
        }
        t.setDebitur(s);
        tagihanDao.save(t);
    }

    public void updateTagihan(Tagihan tx){
        tagihanDao.save(tx);
    }
}
