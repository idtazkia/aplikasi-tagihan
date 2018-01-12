package id.ac.tazkia.payment.virtualaccount.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UpdateTagihanRequest {

    @NotNull @Min(1)
    private BigDecimal nilaiTagihan;

    @NotNull
    private Date tanggalJatuhTempo;

    @NotNull @NotEmpty
    private String keterangan;

}
