package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity @Data
public class KonfigurasiJadwalTagihan {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_jenis_tagihan")
    private JenisTagihan jenisTagihan;

    @ManyToOne
    @JoinColumn(name = "id_kode_biaya")
    private KodeBiaya kodeBiaya;

    @NotNull
    @Min(1) @Max(28)
    private Integer tanggalPenagihan;

    @NotNull
    @Min(1)
    private Integer jumlahPenagihan;

    @NotNull
    private LocalDate tanggalMulai;

    @NotNull @Min(1)
    private Integer jatuhTempoBulan = 12;

    @NotNull
    private Boolean otomatisAkumulasi = true;
}
