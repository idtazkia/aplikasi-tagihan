package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Bank {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String kode;
    @NotNull
    @NotEmpty
    private String nama;
    @NotNull
    @NotEmpty
    private String nomorRekening;
    @NotNull
    @NotEmpty
    private String namaRekening;
    @NotNull
    private Boolean aktif = Boolean.TRUE;
    @NotNull
    @Min(0)
    private Integer jumlahDigitVirtualAccount = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomorRekening() {
        return nomorRekening;
    }

    public void setNomorRekening(String nomorRekening) {
        this.nomorRekening = nomorRekening;
    }

    public String getNamaRekening() {
        return namaRekening;
    }

    public void setNamaRekening(String namaRekening) {
        this.namaRekening = namaRekening;
    }

    public Boolean getAktif() {
        return aktif;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }

    public Integer getJumlahDigitVirtualAccount() {
        return jumlahDigitVirtualAccount;
    }

    public void setJumlahDigitVirtualAccount(Integer jumlahDigitVirtualAccount) {
        this.jumlahDigitVirtualAccount = jumlahDigitVirtualAccount;
    }

    
}
