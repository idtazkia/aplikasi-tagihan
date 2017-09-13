package id.ac.tazkia.payment.virtualaccount.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class UpdateTagihanRequest {

    @NotNull @Min(1)
    private BigDecimal nilaiTagihan;

    @NotNull
    private Date tanggalKadaluarsa;

    @NotNull @NotEmpty
    private String keterangan;

    public BigDecimal getNilaiTagihan() {
        return nilaiTagihan;
    }

    public void setNilaiTagihan(BigDecimal nilaiTagihan) {
        this.nilaiTagihan = nilaiTagihan;
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
}
