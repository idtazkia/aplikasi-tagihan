package id.ac.tazkia.payment.virtualaccount.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import id.ac.tazkia.payment.virtualaccount.entity.TipePembayaran;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data @Builder
public class VaRequest {
    private String bankId;
    private String accountNumber;
    private String invoiceNumber;
    private String name;
    private String description;
    private String email;
    private String phone;
    private BigDecimal amount;
    private String expireDate;
    @Builder.Default private TipePembayaran accountType = TipePembayaran.CLOSED;
    @Builder.Default private VaRequestType requestType = VaRequestType.CREATE;
}
