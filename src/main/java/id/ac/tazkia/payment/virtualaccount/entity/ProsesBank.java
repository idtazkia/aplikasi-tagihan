package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity @Data
public class ProsesBank {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date waktuPembuatan;

    @Temporal(TemporalType.TIMESTAMP)
    private Date waktuEksekusi;

    @NotNull
    @ManyToOne @JoinColumn(name = "id_bank")
    private Bank bank;

    @NotNull
    @ManyToOne @JoinColumn(name = "id_tagihan")
    private Tagihan tagihan;

    @NotNull
    @Enumerated(EnumType.STRING)
    private JenisProsesBank jenisProsesBank;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusProsesBank statusProsesBank;
}
