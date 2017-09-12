package id.ac.tazkia.payment.virtualaccount.bni.dao;

import id.ac.tazkia.payment.virtualaccount.bni.entity.RunningNumber;
import org.springframework.data.repository.CrudRepository;

public interface RunningNumberDao extends CrudRepository<RunningNumber, String> {
    public RunningNumber findByPrefix(String prefix);
}
