package id.ac.tazkia.payment.virtualaccount.bni.entity;

import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity @Data @Table(name = "bni_pembayaran")
public class PembayaranBni {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne @JoinColumn(name = "id_virtual_account")
    private VirtualAccount va;

    private String trxId;
    private String virtualAccount;
    private String customerName;
    private String trxAmount;
    private String paymentAmount;
    private String cumulativePaymentAmount;
    private String paymentNtb;
    private String datetimePayment;
    private String datetimePaymentIso8601;
}
