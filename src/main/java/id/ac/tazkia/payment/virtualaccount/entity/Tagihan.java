package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Data
public class Tagihan {

    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull @NotEmpty
    private String nomor;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_debitur")
    private Debitur debitur;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_jenis_tagihan")
    private JenisTagihan jenisTagihan;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_kode_biaya")
    private KodeBiaya kodeBiaya;

    @NotNull @Min(0)
    private BigDecimal nilaiTagihan;

    @NotNull @Min(0)
    private BigDecimal jumlahPembayaran = BigDecimal.ZERO;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate tanggalJatuhTempo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate tanggalTagihan = LocalDate.now();

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String keterangan;

    @NotNull @Enumerated(EnumType.STRING)
    private StatusPembayaran statusPembayaran = StatusPembayaran.BELUM_DIBAYAR;

    @NotNull @Enumerated(EnumType.STRING)
    private StatusTagihan statusTagihan = StatusTagihan.AKTIF;

    @NotNull @Enumerated(EnumType.STRING)
    private StatusNotifikasi statusNotifikasi = StatusNotifikasi.BELUM_TERKIRIM;

    public BigDecimal getNilaiTagihanEfektif() {
        return nilaiTagihan.subtract(jumlahPembayaran);
    }
}
