package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PembayaranDao extends PagingAndSortingRepository<Pembayaran, String> {
    Page<Pembayaran> findByTagihanOrderByWaktuTransaksi(Tagihan tagihan, Pageable pageable);
}
