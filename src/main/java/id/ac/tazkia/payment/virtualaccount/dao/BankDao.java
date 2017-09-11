package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Bank;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BankDao extends PagingAndSortingRepository<Bank, String> {
}
