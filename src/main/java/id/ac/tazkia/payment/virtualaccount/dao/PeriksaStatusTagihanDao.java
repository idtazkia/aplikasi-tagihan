package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.PeriksaStatusTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.StatusPemeriksaanTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PeriksaStatusTagihanDao extends PagingAndSortingRepository<PeriksaStatusTagihan, String> {
    Page<PeriksaStatusTagihan> findByVirtualAccountTagihanOrderByWaktuPeriksaDesc(Tagihan tagihan, Pageable page);
    List<PeriksaStatusTagihan> findByVirtualAccountAndStatusPemeriksaanTagihan(VirtualAccount va, StatusPemeriksaanTagihan statusPemeriksaanTagihan);
}
