package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface TagihanDao extends PagingAndSortingRepository<Tagihan, String> {
    Tagihan findByNomor(String nomor);
    Page<Tagihan> findAllByStatusTagihan(StatusTagihan status, Pageable pageable);
    Iterable<Tagihan> findByStatusNotifikasi(StatusNotifikasi statusNotifikasi);
    @Query(
            "select sum(t.nilaiTagihan) from Tagihan t where t.jenisTagihan = :jenisTagihan and t.statusTagihan = :statusTagihan"
    )
    BigDecimal totalTagihanByJenisTagihanAndStatusTagihan(@Param("jenisTagihan") JenisTagihan jenisTagihan,
                                                          @Param("statusTagihan") StatusTagihan statusTagihan);

    @Query(
            "select count(t.nilaiTagihan) from Tagihan t where t.jenisTagihan = :jenisTagihan and t.statusTagihan = :statusTagihan"
    )
    Long jumlahTagihanByJenisTagihanAndStatusTagihan(@Param("jenisTagihan") JenisTagihan jenisTagihan,
                                                    @Param("statusTagihan") StatusTagihan statusTagihan);
}
