package id.ac.tazkia.payment.virtualaccount.service;

import id.ac.tazkia.payment.virtualaccount.dao.*;
import id.ac.tazkia.payment.virtualaccount.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service @Transactional
public class TagihanService {

    @Autowired private TagihanDao tagihanDao;
    @Autowired private PembayaranDao pembayaranDao;
    @Autowired private VirtualAccountDao virtualAccountDao;
    @Autowired private ProsesBankDao prosesBankDao;
    @Autowired private BankDao bankDao;
    @Autowired private SiswaDao siswaDao;

    public void createTagihan(Tagihan t){
        Siswa s = siswaDao.findByNomorSiswa(t.getSiswa().getNomorSiswa());

        if(s == null){
            s = t.getSiswa();
            siswaDao.save(s);
        }
        t.setSiswa(s);
        tagihanDao.save(t);
        for (Bank b : bankDao.findAll()) {
            if(!b.getAktif()) {
                continue;
            }
            ProsesBank pb = new ProsesBank();
            pb.setWaktuPembuatan(new Date());
            pb.setTagihan(t);
            pb.setBank(b);
            pb.setJenisProsesBank(JenisProsesBank.CREATE_VA);
            pb.setStatusProsesBank(StatusProsesBank.BARU);
            prosesBankDao.save(pb);
        }
    }

    public void updateTagihan(Tagihan tx){
        tagihanDao.save(tx);
        for (Bank b : bankDao.findAll()) {
            if(!b.getAktif()) {
                continue;
            }
            ProsesBank pb = new ProsesBank();
            pb.setWaktuPembuatan(new Date());
            pb.setTagihan(tx);
            pb.setBank(b);
            pb.setJenisProsesBank(JenisProsesBank.UPDATE_VA);
            pb.setStatusProsesBank(StatusProsesBank.BARU);
            prosesBankDao.save(pb);
        }
    }
}
