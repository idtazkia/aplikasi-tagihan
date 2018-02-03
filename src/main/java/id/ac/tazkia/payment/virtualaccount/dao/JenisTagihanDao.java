package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface JenisTagihanDao extends PagingAndSortingRepository<JenisTagihan, String> {
    List<JenisTagihan> findByAktifOrderByKode(Boolean aktif);
}
