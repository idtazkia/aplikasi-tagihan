package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity @Data
public class VirtualAccount {

    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
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
