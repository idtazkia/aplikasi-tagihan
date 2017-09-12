package id.ac.tazkia.payment.virtualaccount.bni.service;

import id.ac.tazkia.payment.virtualaccount.bni.dao.RunningNumberDao;
import id.ac.tazkia.payment.virtualaccount.bni.entity.RunningNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(propagation = Propagation.REQUIRES_NEW)
public class BniRunningNumberService {
    @Autowired private RunningNumberDao runningNumberDao;

    public Long getNumber(String prefix){
        RunningNumber rn = runningNumberDao.findByPrefix(prefix);
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
