package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PembayaranDao extends PagingAndSortingRepository<Pembayaran, String> {
}
