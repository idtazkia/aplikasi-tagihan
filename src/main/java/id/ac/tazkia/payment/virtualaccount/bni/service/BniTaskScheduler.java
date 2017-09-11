package id.ac.tazkia.payment.virtualaccount.bni.service;

import id.ac.tazkia.payment.virtualaccount.dao.BankDao;
import id.ac.tazkia.payment.virtualaccount.dao.ProsesBankDao;
import id.ac.tazkia.payment.virtualaccount.entity.Bank;
import id.ac.tazkia.payment.virtualaccount.entity.JenisProsesBank;
import id.ac.tazkia.payment.virtualaccount.entity.ProsesBank;
import id.ac.tazkia.payment.virtualaccount.entity.StatusProsesBank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional @Service
public class BniTaskScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BniTaskScheduler.class);
    private static final String BANK_ID = "bnisy001";

    @Autowired private BankDao bankDao;
    @Autowired private ProsesBankDao prosesBankDao;

    @Scheduled(fixedDelay = 10000L)
    public void prosesAntrianCreateVa(){
        Bank b = bankDao.findOne(BANK_ID);
        if(b == null){
            throw new IllegalStateException("Bank ID " + BANK_ID + " tidak ditemukan");
        }
        for (ProsesBank pb : prosesBankDao.findByBankAndAndJenisProsesBankAndStatusProsesBankOrderByWaktuPembuatan(b, JenisProsesBank.CREATE_VA, StatusProsesBank.BARU)) {
            LOGGER.info("Membuat VA untuk tagihan {} atas nama {} sejumlah {}",
                    pb.getTagihan().getJenisTagihan().getNama(),
                    pb.getTagihan().getSiswa().getNomorSiswa() + " - "+ pb.getTagihan().getSiswa().getNama(),
                    pb.getTagihan().getJumlahTagihan());


            pb.setWaktuEksekusi(new Date());
            pb.setStatusProsesBank(StatusProsesBank.SUKSES);
        }
    }
}
