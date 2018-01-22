package id.ac.tazkia.payment.virtualaccount.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TagihanResponse {
    private String status;
    private String debitur;
    private String jenisTagihan;
    private BigDecimal nilai;
    private Date tanggalTagihan;
    private Date tanggalJatuhTempo;
    private String nomorTagihan;
    private String keterangan;
}
