package id.ac.tazkia.payment.virtualaccount.bni.entity;

import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

//@Entity @Table(name = "bni_pembayaran")
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
    private BigDecimal trxAmount;
    private BigDecimal paymentAmount;
    private BigDecimal cumulativePaymentAmount;
    private BigDecimal paymentNtb;
    private Date datetimePayment;
    private Date datetimePaymentIso8601;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VirtualAccount getVa() {
        return va;
    }

    public void setVa(VirtualAccount va) {
        this.va = va;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getVirtualAccount() {
        return virtualAccount;
    }

    public void setVirtualAccount(String virtualAccount) {
        this.virtualAccount = virtualAccount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getTrxAmount() {
        return trxAmount;
    }

    public void setTrxAmount(BigDecimal trxAmount) {
        this.trxAmount = trxAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getCumulativePaymentAmount() {
        return cumulativePaymentAmount;
    }

    public void setCumulativePaymentAmount(BigDecimal cumulativePaymentAmount) {
        this.cumulativePaymentAmount = cumulativePaymentAmount;
    }

    public BigDecimal getPaymentNtb() {
        return paymentNtb;
    }

    public void setPaymentNtb(BigDecimal paymentNtb) {
        this.paymentNtb = paymentNtb;
    }

    public Date getDatetimePayment() {
        return datetimePayment;
    }

    public void setDatetimePayment(Date datetimePayment) {
        this.datetimePayment = datetimePayment;
    }

    public Date getDatetimePaymentIso8601() {
        return datetimePaymentIso8601;
    }

    public void setDatetimePaymentIso8601(Date datetimePaymentIso8601) {
        this.datetimePaymentIso8601 = datetimePaymentIso8601;
    }
}
