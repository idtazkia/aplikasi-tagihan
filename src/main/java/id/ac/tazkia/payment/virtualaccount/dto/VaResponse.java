package id.ac.tazkia.payment.virtualaccount.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VaResponse {
    private VaRequestType requestType;
    private VaRequestStatus requestStatus;
    private String accountNumber;
    private String invoiceNumber;
    private String name;
    private BigDecimal amount;
}
