package id.ac.tazkia.payment.virtualaccount.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TagihanRequest {
    private String jenisTagihan;
    private String kodeBiaya;
    private String debitur;
    private BigDecimal nilaiTagihan;
    private LocalDate tanggalJatuhTempo;
    private String keterangan;
}
