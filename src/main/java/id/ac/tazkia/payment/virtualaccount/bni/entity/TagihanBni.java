package id.ac.tazkia.payment.virtualaccount.bni.entity;

import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity @Table(name = "bni_tagihan")
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
    @NotNull @NotEmpty
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String virtualAccount;
    private String datetimeExpired;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tagihan getTagihan() {
        return tagihan;
    }

    public void setTagihan(Tagihan tagihan) {
        this.tagihan = tagihan;
    }

    public VirtualAccount getVa() {
        return va;
    }

    public void setVa(VirtualAccount va) {
        this.va = va;
    }

    public StatusTagihan getStatusTagihan() {
        return statusTagihan;
    }

    public void setStatusTagihan(StatusTagihan statusTagihan) {
        this.statusTagihan = statusTagihan;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getTrxAmount() {
        return trxAmount;
    }

    public void setTrxAmount(String trxAmount) {
        this.trxAmount = trxAmount;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getVirtualAccount() {
        return virtualAccount;
    }

    public void setVirtualAccount(String virtualAccount) {
        this.virtualAccount = virtualAccount;
    }

    public String getDatetimeExpired() {
        return datetimeExpired;
    }

    public void setDatetimeExpired(String datetimeExpired) {
        this.datetimeExpired = datetimeExpired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
