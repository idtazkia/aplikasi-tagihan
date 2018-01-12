package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity @Table
public class Pembayaran {

    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date waktuTransaksi;

    @NotNull
    @Enumerated(EnumType.STRING)
    private JenisPembayaran jenisPembayaran;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_tagihan")
    private Tagihan tagihan;

    @ManyToOne
    @JoinColumn(name = "id_bank")
    private Bank bank;
    
    @ManyToOne
    @JoinColumn(name = "id_virtual_account")
    private VirtualAccount virtualAccount;

    @NotNull @Min(1)
    private BigDecimal jumlah;

    @NotNull @NotEmpty
    private String referensi;
    private String keterangan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getWaktuTransaksi() {
        return waktuTransaksi;
    }

    public void setWaktuTransaksi(Date waktuTransaksi) {
        this.waktuTransaksi = waktuTransaksi;
    }

    public JenisPembayaran getJenisPembayaran() {
        return jenisPembayaran;
    }

    public void setJenisPembayaran(JenisPembayaran jenisPembayaran) {
        this.jenisPembayaran = jenisPembayaran;
    }

    public Tagihan getTagihan() {
        return tagihan;
    }

    public void setTagihan(Tagihan tagihan) {
        this.tagihan = tagihan;
    }

    public VirtualAccount getVirtualAccount() {
        return virtualAccount;
    }

    public void setVirtualAccount(VirtualAccount virtualAccount) {
        this.virtualAccount = virtualAccount;
    }

    public BigDecimal getJumlah() {
        return jumlah;
    }

    public void setJumlah(BigDecimal jumlah) {
        this.jumlah = jumlah;
    }

    public String getReferensi() {
        return referensi;
    }

    public void setReferensi(String referensi) {
        this.referensi = referensi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
    
}
