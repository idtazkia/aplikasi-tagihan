package id.ac.tazkia.payment.virtualaccount.bni.controller;

import id.ac.tazkia.payment.virtualaccount.bni.dao.TagihanBniDao;
import id.ac.tazkia.payment.virtualaccount.bni.dto.PaymentNotificationRequest;
import id.ac.tazkia.payment.virtualaccount.bni.entity.TagihanBni;
import id.ac.tazkia.payment.virtualaccount.dao.PembayaranDao;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.entity.Pembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.StatusPembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Transactional
@RestController
@RequestMapping("/api/callback/bni")
public class BniCallbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BniCallbackController.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired private TagihanBniDao tagihanBniDao;
    @Autowired private PembayaranDao pembayaranDao;
    @Autowired private TagihanDao tagihanDao;

    @PostMapping("/payment")
    public Map<String, String> paymentNotification(@RequestBody @Valid PaymentNotificationRequest paymentNotificationRequest){
        Map<String, String> response = new HashMap<>();
        response.put("status", "000");

        TagihanBni t = tagihanBniDao.findByTrxId(paymentNotificationRequest.getTrxId());
        if (t == null) {
            LOGGER.error("BNI : Tagihan dengan trx_id {}, va {}, atas nama {}, dengan nominal {} tidak ada di database",
                    paymentNotificationRequest.getTrxId(),
                    paymentNotificationRequest.getVirtualAccount(),
                    paymentNotificationRequest.getCustomerName(),
                    paymentNotificationRequest.getTrxAmount());
            return response;
        }

        Pembayaran p = new Pembayaran();
        p.setVirtualAccount(t.getVa());
        p.setKeterangan("Pembayaran tagihan atas nama "+p.getVirtualAccount().getTagihan().getSiswa().getNama()
        + "melalui BNI dengan VA "+t.getVirtualAccount());

        p.setJumlah(new BigDecimal(paymentNotificationRequest.getPaymentAmount()));
        p.setReferensiBank(paymentNotificationRequest.getPaymentNtb());

        Tagihan tag = t.getTagihan();
        tag.setJumlahPembayaran(tag.getJumlahPembayaran().add(p.getJumlah()));
        if(tag.getJumlahPembayaran().compareTo(tag.getJumlahTagihan()) > -1 ){
            tag.setStatusPembayaran(StatusPembayaran.LUNAS);
        } else {
            tag.setStatusPembayaran(StatusPembayaran.DIBAYAR_SEBAGIAN);
        }

        tagihanDao.save(tag);

        try {
            p.setWaktuTransaksi(DATE_FORMAT.parse(paymentNotificationRequest.getDatetimePayment()));
        } catch (ParseException err){
            LOGGER.error("BNI : Format tanggal {} tidak sesuai yyyy-MM-dd HH:mm:ss", paymentNotificationRequest.getDatetimePayment());
            p.setWaktuTransaksi(new Date());
        }

        pembayaranDao.save(p);

        return response;
    }
}
