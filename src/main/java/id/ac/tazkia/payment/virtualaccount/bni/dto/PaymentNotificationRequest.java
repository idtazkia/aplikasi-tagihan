package id.ac.tazkia.payment.virtualaccount.bni.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PaymentNotificationRequest {
    private String virtualAccount;
    private String customerName;
    private String trxId;
    private String trxAmount;
    private String paymentAmount;
    private String cumulativePaymentAmount;
    private String paymentNtb;
    private String datetimePayment;
    private String datetimePaymentIso8601;

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

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCumulativePaymentAmount() {
        return cumulativePaymentAmount;
    }

    public void setCumulativePaymentAmount(String cumulativePaymentAmount) {
        this.cumulativePaymentAmount = cumulativePaymentAmount;
    }

    public String getPaymentNtb() {
        return paymentNtb;
    }

    public void setPaymentNtb(String paymentNtb) {
        this.paymentNtb = paymentNtb;
    }

    public String getDatetimePayment() {
        return datetimePayment;
    }

    public void setDatetimePayment(String datetimePayment) {
        this.datetimePayment = datetimePayment;
    }

    public String getDatetimePaymentIso8601() {
        return datetimePaymentIso8601;
    }

    public void setDatetimePaymentIso8601(String datetimePaymentIso8601) {
        this.datetimePaymentIso8601 = datetimePaymentIso8601;
    }
}
