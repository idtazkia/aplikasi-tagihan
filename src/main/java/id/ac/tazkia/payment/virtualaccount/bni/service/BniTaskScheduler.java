package id.ac.tazkia.payment.virtualaccount.bni.service;

import id.ac.tazkia.payment.virtualaccount.bni.config.BniEcollectionConfiguration;
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

    @Autowired private BankDao bankDao;
    @Autowired private ProsesBankDao prosesBankDao;
    @Autowired private BniEcollectionConfiguration config;
    @Autowired private BniVaService bniVaService;


    @Scheduled(fixedDelay = 10000L)
    public void prosesAntrianCreateVa(){
        Bank b = bankDao.findOne(config.getBankId());
        if(b == null){
            throw new IllegalStateException("Bank ID " + config.getBankId() + " tidak ditemukan");
        }
        for (ProsesBank pb : prosesBankDao.findByBankAndAndJenisProsesBankAndStatusProsesBankOrderByWaktuPembuatan(b, JenisProsesBank.CREATE_VA, StatusProsesBank.BARU)) {
            LOGGER.info("Membuat VA untuk tagihan {} atas nama {} sejumlah {}",
                    pb.getTagihan().getJenisTagihan().getNama(),
                    pb.getTagihan().getSiswa().getNomorSiswa() + " - "+ pb.getTagihan().getSiswa().getNama(),
                    pb.getTagihan().getJumlahTagihan());

            bniVaService.createVa(pb.getTagihan());

            pb.setWaktuEksekusi(new Date());
            pb.setStatusProsesBank(StatusProsesBank.SUKSES);
        }
    }

    @Scheduled(fixedDelay = 10000L)
    public void prosesAntrianUpdateVa(){
        Bank b = bankDao.findOne(config.getBankId());
        if(b == null){
            throw new IllegalStateException("Bank ID " + config.getBankId() + " tidak ditemukan");
        }
        for (ProsesBank pb : prosesBankDao.findByBankAndAndJenisProsesBankAndStatusProsesBankOrderByWaktuPembuatan(b, JenisProsesBank.UPDATE_VA, StatusProsesBank.BARU)) {
            LOGGER.info("Membuat VA untuk tagihan {} atas nama {} sejumlah {}",
                    pb.getTagihan().getJenisTagihan().getNama(),
                    pb.getTagihan().getSiswa().getNomorSiswa() + " - "+ pb.getTagihan().getSiswa().getNama(),
                    pb.getTagihan().getJumlahTagihan());

            bniVaService.updateVa(pb.getTagihan());

            pb.setWaktuEksekusi(new Date());
            pb.setStatusProsesBank(StatusProsesBank.SUKSES);
        }
    }
}
