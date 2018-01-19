package id.ac.tazkia.payment.virtualaccount.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UpdateTagihan {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull @Future
    private Date tanggalJatuhTempo;

    @NotNull @Min(1)
    private BigDecimal nilaiTagihan;
}
