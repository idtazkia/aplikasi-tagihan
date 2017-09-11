package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Bank;
import id.ac.tazkia.payment.virtualaccount.entity.JenisProsesBank;
import id.ac.tazkia.payment.virtualaccount.entity.ProsesBank;
import id.ac.tazkia.payment.virtualaccount.entity.StatusProsesBank;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProsesBankDao extends PagingAndSortingRepository<ProsesBank, String> {
    public Iterable<ProsesBank> findByBankAndAndJenisProsesBankAndStatusProsesBankOrderByWaktuPembuatan(Bank b, JenisProsesBank jenis, StatusProsesBank status);
}
