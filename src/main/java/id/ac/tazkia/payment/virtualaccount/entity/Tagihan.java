package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity @Data
public class Tagihan {

    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull @NotEmpty
    private String nomor;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_siswa")
    private Siswa siswa;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_jenis_tagihan")
    private JenisTagihan jenisTagihan;

    @NotNull @Min(0)
    private BigDecimal jumlahTagihan;

    @NotNull @Min(0)
    private BigDecimal jumlahPembayaran = BigDecimal.ZERO;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date tanggalKadaluarsa;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private String keterangan;

    @NotNull @Enumerated(EnumType.STRING)
    private StatusPembayaran statusPembayaran = StatusPembayaran.BELUM_DIBAYAR;
}
