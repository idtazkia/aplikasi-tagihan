package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.dto.RekapTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TagihanDao extends PagingAndSortingRepository<Tagihan, String> {
    Tagihan findByNomor(String nomor);
    Page<Tagihan> findByJenisTagihanAndStatusTagihanOrderByTanggalTagihan(JenisTagihan jenisTagihan, StatusTagihan status, Pageable pageable);
    Page<Tagihan> findByDebiturAndStatusTagihanOrderByTanggalTagihan(Debitur debitur, StatusTagihan status, Pageable pageable);
    Page<Tagihan> findAllByStatusTagihan(StatusTagihan status, Pageable pageable);
    Page<Tagihan> findByStatusNotifikasi(StatusNotifikasi statusNotifikasi, Pageable page);
    @Query(
            "select sum(t.nilaiTagihan) from Tagihan t where t.jenisTagihan = :jenisTagihan and t.statusTagihan = :statusTagihan"
    )
    BigDecimal totalTagihanByJenisTagihanAndStatusTagihan(@Param("jenisTagihan") JenisTagihan jenisTagihan,
                                                          @Param("statusTagihan") StatusTagihan statusTagihan);

    @Query(
            "select count(t) from Tagihan t where t.jenisTagihan = :jenisTagihan and t.statusTagihan = :statusTagihan"
    )
    Long jumlahTagihanByJenisTagihanAndStatusTagihan(@Param("jenisTagihan") JenisTagihan jenisTagihan,
                                                    @Param("statusTagihan") StatusTagihan statusTagihan);

    @Query("select new id.ac.tazkia.payment.virtualaccount.dto.RekapTagihan(t.jenisTagihan, t.statusPembayaran, " +
            "count(t), sum(t.nilaiTagihan), sum(t.jumlahPembayaran)) " +
            "from Tagihan t " +
            "where t.tanggalTagihan >= :mulai and t.tanggalTagihan <= :sampai " +
            "group by t.jenisTagihan, t.statusPembayaran")
    List<RekapTagihan> rekapTagihan(@Param("mulai") Date mulai,
                                    @Param("sampai") Date sampai);
}
