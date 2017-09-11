package id.ac.tazkia.payment.virtualaccount.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class JenisTagihan {
    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull @NotEmpty @Column(unique = true)
    private String nama;

    @NotNull @Enumerated(EnumType.STRING)
    private TipePembayaran tipePembayaran;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public TipePembayaran getTipePembayaran() {
        return tipePembayaran;
    }

    public void setTipePembayaran(TipePembayaran tipePembayaran) {
        this.tipePembayaran = tipePembayaran;
    }
}
