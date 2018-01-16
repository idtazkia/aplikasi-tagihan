package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Bank;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

public interface BankDao extends PagingAndSortingRepository<Bank, String> {
    Iterable<Bank> findByIdNotIn(Set<String> ids);
}
