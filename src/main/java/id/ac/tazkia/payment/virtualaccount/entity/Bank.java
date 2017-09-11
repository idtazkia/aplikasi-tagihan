package id.ac.tazkia.payment.virtualaccount.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Bank {
    @Id
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @NotNull @NotEmpty @Column(unique = true)
    private String kode;
    @NotNull @NotEmpty
    private String nama;
    @NotNull @NotEmpty
    private String nomorRekening;
    @NotNull @NotEmpty
    private String namaRekening;
}
