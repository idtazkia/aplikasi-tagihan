package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity @Data
public class Bank {
    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull @NotEmpty @Column(unique = true)
    private String kode;
    @NotNull @NotEmpty
    private String nama;
    @NotNull @NotEmpty
    private String nomorRekening;
    @NotNull @NotEmpty
    private String namaRekening;
    @NotNull
    private Boolean aktif = Boolean.TRUE;
    @NotNull @Min(0)
    private Integer jumlahDigitVirtualAccount = 0;
}
