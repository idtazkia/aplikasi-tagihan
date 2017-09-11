package id.ac.tazkia.payment.virtualaccount.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Pembayaran {

    @Id
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date waktuTransaksi;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_virtual_account")
    private VirtualAccount virtualAccount;

    @NotNull @Min(1)
    private BigDecimal jumlah;

    @NotNull @NotEmpty
    private String referensiBank;
    private String keterangan;
}
