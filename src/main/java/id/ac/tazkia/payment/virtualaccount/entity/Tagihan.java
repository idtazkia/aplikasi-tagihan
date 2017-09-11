package id.ac.tazkia.payment.virtualaccount.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Tagihan {

    @Id @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_siswa")
    private Siswa siswa;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_jenis_tagihan")
    private JenisTagihan jenisTagihan;

    @NotNull @Min(0)
    private BigDecimal jumlahTagihan;

    @NotNull @Min(0)
    private BigDecimal jumlahPembayaran;

    @NotNull @Future
    @Temporal(TemporalType.DATE)
    private Date tanggalKadaluarsa;

    private String keterangan;

    @NotNull @Enumerated(EnumType.STRING)
    private StatusPembayaran statusPembayaran;
}
