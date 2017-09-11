package id.ac.tazkia.payment.virtualaccount.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class VirtualAccount {

    @Id
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @ManyToOne @NotNull
    @JoinColumn(name = "id_tagihan")
    private Tagihan tagihan;
    @ManyToOne @NotNull
    @JoinColumn(name = "id_bank")
    private Bank bank;

    @NotNull @NotEmpty
    private String idVirtualAccount;
    @NotNull @NotEmpty
    private String nomorVirtualAccount;
}
