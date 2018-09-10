package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.entity.VaStatus;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VirtualAccountDao extends PagingAndSortingRepository<VirtualAccount, String> {
    Iterable<VirtualAccount> findByVaStatus(VaStatus status);
    List<VirtualAccount> findByVaStatusAndTagihanNomor(VaStatus status, String nomor);
    Page<VirtualAccount> findByTagihan(Tagihan tagihan, Pageable page);
    Iterable<VirtualAccount> findByTagihan(Tagihan tagihan);
    VirtualAccount findByVaStatusAndTagihanNomorAndBankId(VaStatus status, String invoiceNumber, String bankId);
}
