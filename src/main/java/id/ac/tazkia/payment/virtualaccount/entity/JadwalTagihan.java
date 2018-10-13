package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity @Data
public class JadwalTagihan {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_debitur")
    private Debitur debitur;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_konfigurasi_jadwal_tagihan")
    private KonfigurasiJadwalTagihan konfigurasiJadwalTagihan;

    @Min(0)
    private BigDecimal nilai;
}