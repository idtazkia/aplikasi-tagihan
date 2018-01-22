package id.ac.tazkia.payment.virtualaccount.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagihanResponse {
    private Boolean sukses;
    private String error;
    private String debitur;
    private String jenisTagihan;
    private BigDecimal nilaiTagihan;
    private Date tanggalTagihan;
    private Date tanggalJatuhTempo;
    private String nomorTagihan;
    private String keterangan;
}
