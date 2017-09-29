package id.ac.tazkia.payment.virtualaccount.bni.entity;

import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity @Data
@Table(name = "bni_tagihan")
public class TagihanBni {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @ManyToOne @JoinColumn(name = "id_tagihan")
    private Tagihan tagihan;

    @ManyToOne @JoinColumn(name = "id_virtual_account")
    private VirtualAccount va;

    @Enumerated(EnumType.STRING)
    private StatusTagihan statusTagihan = StatusTagihan.BARU;

    @NotNull @NotEmpty
    private String clientId;
    @NotNull @NotEmpty
    private String trxId;
    @NotNull @NotEmpty
    private String trxAmount;
    @NotNull @NotEmpty
    private String billingType;
    @NotNull @NotEmpty @Size(max = 30)
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String virtualAccount;
    private String datetimeExpired;
    private String description;
}
