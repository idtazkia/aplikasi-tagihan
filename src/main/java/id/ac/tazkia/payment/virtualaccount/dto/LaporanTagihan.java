package id.ac.tazkia.payment.virtualaccount.dto;

import id.ac.tazkia.payment.virtualaccount.entity.JenisTagihan;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LaporanTagihan {
    private JenisTagihan jenisTagihan;
    private Date mulai;
    private Date sampai;
    private Long jumlahTagihanLunas = 0L;
    private Long jumlahTagihanBelumLunas = 0L;
    private BigDecimal nilaiTagihan = BigDecimal.ZERO;
    private BigDecimal nilaiPembayaran = BigDecimal.ZERO;
}
