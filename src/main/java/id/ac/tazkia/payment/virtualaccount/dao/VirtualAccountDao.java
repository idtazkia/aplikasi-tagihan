package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Bank;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VirtualAccountDao extends PagingAndSortingRepository<VirtualAccount, String> {
    public Iterable<VirtualAccount> findByTagihanOrderByBankNomorRekening(Tagihan tagihan);
    public VirtualAccount findByBankAndTagihan(Bank b, Tagihan tagihan);
}
