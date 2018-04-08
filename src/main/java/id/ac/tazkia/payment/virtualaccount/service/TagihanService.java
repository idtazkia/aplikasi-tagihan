package id.ac.tazkia.payment.virtualaccount.service;

import id.ac.tazkia.payment.virtualaccount.dao.PeriksaStatusTagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.VirtualAccountDao;
import id.ac.tazkia.payment.virtualaccount.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service @Transactional
public class TagihanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagihanService.class);

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String TIMEZONE = "GMT+07:00";

    @Autowired private RunningNumberService runningNumberService;
    @Autowired private TagihanDao tagihanDao;
    @Autowired private VirtualAccountDao virtualAccountDao;
    @Autowired private PeriksaStatusTagihanDao periksaStatusTagihanDao;

    public void saveTagihan(Tagihan t) {
        t.setNilaiTagihan(t.getNilaiTagihan().setScale(0, RoundingMode.DOWN));

        // tagihan baru
        if (t.getId() == null) {
            String datePrefix = DATE_FORMAT.format(LocalDateTime.now(ZoneId.of(TIMEZONE)));
            Long runningNumber = runningNumberService.getNumber(datePrefix);
            String nomorTagihan = datePrefix + t.getJenisTagihan().getKode() + String.format("%06d", runningNumber);
            t.setNomor(nomorTagihan);
            tagihanDao.save(t);
            for (Bank b : t.getJenisTagihan().getDaftarBank()) {
                VirtualAccount va = new VirtualAccount();
                va.setBank(b);
                va.setTagihan(t);
                virtualAccountDao.save(va);
            }
        } else {
            for (VirtualAccount va : virtualAccountDao.findByTagihan(t)) {
                va.setVaStatus(StatusTagihan.AKTIF.equals(t.getStatusTagihan()) ? VaStatus.UPDATE : VaStatus.DELETE);
                virtualAccountDao.save(va);
            }
            tagihanDao.save(t);
        }
    }

    public void periksaStatus(Tagihan tagihan) {
        for (VirtualAccount va : virtualAccountDao.findByTagihan(tagihan)) {
            PeriksaStatusTagihan p = new PeriksaStatusTagihan();
            p.setVirtualAccount(va);
            p.setWaktuPeriksa(LocalDateTime.now());
            p.setStatusPemeriksaanTagihan(StatusPemeriksaanTagihan.BARU);
            periksaStatusTagihanDao.save(p);

            va.setVaStatus(VaStatus.INQUIRY);
            virtualAccountDao.save(va);
        }
    }
}
