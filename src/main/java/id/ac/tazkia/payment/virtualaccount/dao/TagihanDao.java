package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Debitur;
import id.ac.tazkia.payment.virtualaccount.entity.StatusPembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.StatusTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface TagihanDao extends PagingAndSortingRepository<Tagihan, String> {
    Page<Tagihan> findByDebiturAndStatusPembayaranInOrderByUpdatedAtDesc(Debitur d, Collection<StatusPembayaran> status, Pageable pageable);
    Tagihan findByNomor(String nomor);
    Page<Tagihan> findAllByStatusTagihan(StatusTagihan status, Pageable pageable);
}
