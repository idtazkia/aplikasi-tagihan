package id.ac.tazkia.payment.virtualaccount.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data @Builder
public class NotifikasiTagihanRequest {
    private String nomorTagihan;
    private String tanggalTagihan;
    private String nama;
    private String email;
    private String noHp;
    private BigDecimal jumlah;
    private String rekening;
    private String rekeningFull;
    private String keterangan;
    private String contactinfo;
    private String contactinfoFull;
}
