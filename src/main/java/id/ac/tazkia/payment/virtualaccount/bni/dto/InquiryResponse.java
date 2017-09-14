package id.ac.tazkia.payment.virtualaccount.bni.dto;

public class InquiryResponse {
    private String clientId;
    private String trxId;
    private String trxAmount;
    private String billingType;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String virtualAccount;
    private String datetimeCreatedIso8601;
    private String datetimeExpiredIso8601;
    private String datetimeLastUpdatedIso8601;
    private String description;
    private String vaStatus;
    private String paymentAmount;
    private String paymentNtb;
    private String datetimePaymentIso8601;

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

    public String getDatetimeCreatedIso8601() {
        return datetimeCreatedIso8601;
    }

    public void setDatetimeCreatedIso8601(String datetimeCreatedIso8601) {
        this.datetimeCreatedIso8601 = datetimeCreatedIso8601;
    }

    public String getDatetimeExpiredIso8601() {
        return datetimeExpiredIso8601;
    }

    public void setDatetimeExpiredIso8601(String datetimeExpiredIso8601) {
        this.datetimeExpiredIso8601 = datetimeExpiredIso8601;
    }

    public String getDatetimeLastUpdatedIso8601() {
        return datetimeLastUpdatedIso8601;
    }

    public void setDatetimeLastUpdatedIso8601(String datetimeLastUpdatedIso8601) {
        this.datetimeLastUpdatedIso8601 = datetimeLastUpdatedIso8601;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVaStatus() {
        return vaStatus;
    }

    public void setVaStatus(String vaStatus) {
        this.vaStatus = vaStatus;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentNtb() {
        return paymentNtb;
    }

    public void setPaymentNtb(String paymentNtb) {
        this.paymentNtb = paymentNtb;
    }

    public String getDatetimePaymentIso8601() {
        return datetimePaymentIso8601;
    }

    public void setDatetimePaymentIso8601(String datetimePaymentIso8601) {
        this.datetimePaymentIso8601 = datetimePaymentIso8601;
    }
}
