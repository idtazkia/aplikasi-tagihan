package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.RunningNumber;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;

public interface RunningNumberDao extends CrudRepository<RunningNumber, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    RunningNumber findByPemakaianAndPrefix(String pemakaian, String prefix);
}
