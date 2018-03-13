package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.dto.RekapPembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Debitur;
import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PembayaranDao extends PagingAndSortingRepository<Pembayaran, String> {
    Page<Pembayaran> findByTagihanOrderByWaktuTransaksi(Tagihan tagihan, Pageable pageable);
    Page<Pembayaran> findByTagihanDebiturOrderByWaktuTransaksi(Debitur debitur, Pageable pageable);

    @Query("select p from Pembayaran  p where p.tagihan.jenisTagihan = :jenis " +
            "and p.waktuTransaksi >= :mulai and p.waktuTransaksi <= :sampai " +
            "order by p.waktuTransaksi desc")
    Page<Pembayaran> findByJenisTagihanAndWaktuTransaksi(
            @Param("jenis")JenisTagihan jenis,
            @Param("mulai")LocalDate mulai,
            @Param("sampai")LocalDate sampai,
            Pageable page
    );

    @Query("select p from Pembayaran  p where " +
            "p.waktuTransaksi >= :mulai and p.waktuTransaksi <= :sampai " +
            "order by p.waktuTransaksi desc")
    Page<Pembayaran> findByWaktuTransaksi(@Param("mulai")LocalDate mulai,
                                          @Param("sampai")LocalDate sampai,
                                          Pageable pageable);

    Page<Pembayaran> findByTagihanJenisTagihan(JenisTagihan jenisTagihan, Pageable pageable);

    @Query("select new id.ac.tazkia.payment.virtualaccount.dto.RekapPembayaran(cast(p.waktuTransaksi as date), sum(p.jumlah), count(p)) " +
            "from Pembayaran p where p.waktuTransaksi >= :mulai and p.waktuTransaksi <= :sampai " +
            "group by cast(p.waktuTransaksi as date) "+
            "order by cast(p.waktuTransaksi as date) ")
    List<RekapPembayaran> rekapPembayaran(@Param("mulai") LocalDateTime mulai,
                                          @Param("sampai") LocalDateTime sampai);

    Iterable<Pembayaran> findByTagihanJenisTagihanAndWaktuTransaksiBetweenOrderByWaktuTransaksi(JenisTagihan jenis, LocalDate mulai, LocalDate sampai);
}
