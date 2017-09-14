package id.ac.tazkia.payment.virtualaccount.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Tagihan {

    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_siswa")
    private Siswa siswa;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_jenis_tagihan")
    private JenisTagihan jenisTagihan;

    @NotNull @Min(0)
    private BigDecimal jumlahTagihan;

    @NotNull @Min(0)
    private BigDecimal jumlahPembayaran = BigDecimal.ZERO;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date tanggalKadaluarsa;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private String keterangan;

    @NotNull @Enumerated(EnumType.STRING)
    private StatusPembayaran statusPembayaran = StatusPembayaran.BELUM_DIBAYAR;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Siswa getSiswa() {
        return siswa;
    }

    public void setSiswa(Siswa siswa) {
        this.siswa = siswa;
    }

    public JenisTagihan getJenisTagihan() {
        return jenisTagihan;
    }

    public void setJenisTagihan(JenisTagihan jenisTagihan) {
        this.jenisTagihan = jenisTagihan;
    }

    public BigDecimal getJumlahTagihan() {
        return jumlahTagihan;
    }

    public void setJumlahTagihan(BigDecimal jumlahTagihan) {
        this.jumlahTagihan = jumlahTagihan;
    }

    public BigDecimal getJumlahPembayaran() {
        return jumlahPembayaran;
    }

    public void setJumlahPembayaran(BigDecimal jumlahPembayaran) {
        this.jumlahPembayaran = jumlahPembayaran;
    }

    public Date getTanggalKadaluarsa() {
        return tanggalKadaluarsa;
    }

    public void setTanggalKadaluarsa(Date tanggalKadaluarsa) {
        this.tanggalKadaluarsa = tanggalKadaluarsa;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public StatusPembayaran getStatusPembayaran() {
        return statusPembayaran;
    }

    public void setStatusPembayaran(StatusPembayaran statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
