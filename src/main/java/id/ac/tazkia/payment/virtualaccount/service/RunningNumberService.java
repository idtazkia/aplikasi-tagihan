package id.ac.tazkia.payment.virtualaccount.service;

import id.ac.tazkia.payment.virtualaccount.dao.RunningNumberDao;
import id.ac.tazkia.payment.virtualaccount.entity.RunningNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service @Transactional
public class RunningNumberService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String TIMEZONE = "GMT+07:00";

    @Autowired
    private RunningNumberDao runningNumberDao;

    public Long getNumber() {
        return getNumber(DATE_FORMAT.format(LocalDateTime.now(ZoneId.of(TIMEZONE))));
    }

    public Long getNumber(String prefix){
        return getNumber(RunningNumber.PEMAKAIAN_DEFAULT, prefix);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long getNumber(String pemakaian, String prefix) {
        RunningNumber rn = runningNumberDao.findByPemakaianAndPrefix(pemakaian, prefix);
        if(rn == null){
            rn = new RunningNumber();
            rn.setPrefix(prefix);
            rn.setLastNumber(0L);
        }

        Long last = rn.getLastNumber() + 1;
        rn.setLastNumber(last);
        runningNumberDao.save(rn);
        return last;
    }
}
