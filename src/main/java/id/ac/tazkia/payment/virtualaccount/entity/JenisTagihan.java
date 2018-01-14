package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "jenis_tagihan_bank",
            joinColumns = @JoinColumn(name = "id_jenis_tagihan"),
            inverseJoinColumns = @JoinColumn(name = "id_bank")
    )
    private Set<Bank> daftarBank = new HashSet<>();
}
