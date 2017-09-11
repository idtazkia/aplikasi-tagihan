package id.ac.tazkia.payment.virtualaccount.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class ProsesBank {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date waktu;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getWaktu() {
        return waktu;
    }

    public void setWaktu(Date waktu) {
        this.waktu = waktu;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Tagihan getTagihan() {
        return tagihan;
    }

    public void setTagihan(Tagihan tagihan) {
        this.tagihan = tagihan;
    }

    public JenisProsesBank getJenisProsesBank() {
        return jenisProsesBank;
    }

    public void setJenisProsesBank(JenisProsesBank jenisProsesBank) {
        this.jenisProsesBank = jenisProsesBank;
    }

    public StatusProsesBank getStatusProsesBank() {
        return statusProsesBank;
    }

    public void setStatusProsesBank(StatusProsesBank statusProsesBank) {
        this.statusProsesBank = statusProsesBank;
    }
}
