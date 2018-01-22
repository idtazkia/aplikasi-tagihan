package id.ac.tazkia.payment.virtualaccount.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TagihanRequest {
    private String jenisTagihan;
    private String debitur;
    private BigDecimal nilaiTagihan;
    private Date tanggalJatuhTempo;
    private String keterangan;
}
