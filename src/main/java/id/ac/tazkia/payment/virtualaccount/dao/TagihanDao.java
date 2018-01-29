package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagihanDao extends PagingAndSortingRepository<Tagihan, String> {
    Tagihan findByNomor(String nomor);
    Page<Tagihan> findAllByStatusTagihan(StatusTagihan status, Pageable pageable);
    Iterable<Tagihan> findByStatusNotifikasi(StatusNotifikasi statusNotifikasi);
}
