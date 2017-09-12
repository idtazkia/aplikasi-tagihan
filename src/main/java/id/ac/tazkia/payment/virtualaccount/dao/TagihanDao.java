package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Siswa;
import id.ac.tazkia.payment.virtualaccount.entity.StatusPembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface TagihanDao extends PagingAndSortingRepository<Tagihan, String> {
    public Page<Tagihan> findBySiswaAndStatusPembayaranInOrderByUpdatedAtDesc(Siswa s, Collection<StatusPembayaran> status, Pageable pageable);
}
