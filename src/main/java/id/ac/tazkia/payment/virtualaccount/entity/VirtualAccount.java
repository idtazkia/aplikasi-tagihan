package id.ac.tazkia.payment.virtualaccount.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class VirtualAccount {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_tagihan")
    private Tagihan tagihan;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_bank")
    private Bank bank;

    private String nomor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VaStatus vaStatus = VaStatus.BARU;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tagihan getTagihan() {
        return tagihan;
    }

    public void setTagihan(Tagihan tagihan) {
        this.tagihan = tagihan;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public VaStatus getVaStatus() {
        return vaStatus;
    }

    public void setVaStatus(VaStatus vaStatus) {
        this.vaStatus = vaStatus;
    }

}
