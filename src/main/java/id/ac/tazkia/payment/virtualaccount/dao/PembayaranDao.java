package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PembayaranDao extends PagingAndSortingRepository<Pembayaran, String> {
    public Iterable<Pembayaran> findByVirtualAccountOrderByWaktuTransaksi(VirtualAccount va);
    public Iterable<Pembayaran> findByVirtualAccountTagihanOrderByWaktuTransaksi(Tagihan t);
}
