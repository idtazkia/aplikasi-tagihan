package id.ac.tazkia.payment.virtualaccount.dto;

import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import id.ac.tazkia.payment.virtualaccount.entity.StatusTagihan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @AllArgsConstructor @NoArgsConstructor
public class RekapTagihan {
    private JenisTagihan jenisTagihan;
    private StatusTagihan statusTagihan;
    private Long jumlahTagihan;
    private BigDecimal nilaiTagihan;
    private BigDecimal nilaiPembayaran;
}
