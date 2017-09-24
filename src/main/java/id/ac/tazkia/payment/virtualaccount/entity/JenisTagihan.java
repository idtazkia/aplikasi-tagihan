package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity @Data
public class JenisTagihan {
    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull @NotEmpty @Column(unique = true)
    private String kode;

    @NotNull @NotEmpty @Column(unique = true)
    private String nama;

    @NotNull @Enumerated(EnumType.STRING)
    private TipePembayaran tipePembayaran;
}
