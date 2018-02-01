package id.ac.tazkia.payment.virtualaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
public class RekapPembayaran {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tanggal;
    private BigDecimal nilai;
    private Long jumlah;
}
