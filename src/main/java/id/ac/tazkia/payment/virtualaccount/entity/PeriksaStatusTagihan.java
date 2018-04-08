package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity @Data
public class PeriksaStatusTagihan {
    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_virtual_account")
    private VirtualAccount virtualAccount;

    @NotNull
    private LocalDateTime waktuPeriksa;

    @Enumerated(EnumType.STRING)
    private StatusPembayaran statusPembayaran;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusPemeriksaanTagihan statusPemeriksaanTagihan = StatusPemeriksaanTagihan.BARU;
}
