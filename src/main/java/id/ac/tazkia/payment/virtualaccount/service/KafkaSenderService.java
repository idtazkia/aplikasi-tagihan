package id.ac.tazkia.payment.virtualaccount.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.VirtualAccountDao;
import id.ac.tazkia.payment.virtualaccount.dto.*;
import id.ac.tazkia.payment.virtualaccount.entity.*;
import id.ac.tazkia.payment.virtualaccount.helper.VirtualAccountNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Service @Transactional
public class KafkaSenderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSenderService.class);
    private static final SimpleDateFormat FORMATTER_ISO_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat FORMATTER_ISO_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Integer NOTIFICATION_BATCH_SIZE = 50;

    @Value("${kafka.topic.notification.request}") private String kafkaTopicNotificationRequest;
    @Value("${kafka.topic.va.request}") private String kafkaTopicVaRequest;
    @Value("${kafka.topic.debitur.response}") private String kafkaTopicDebiturResponse;
    @Value("${kafka.topic.tagihan.response}") private String kafkaTopicTagihanResponse;
    @Value("${kafka.topic.tagihan.payment}") private String kafkaTopicPembayaranTagihan;

    @Value("${notifikasi.delay.menit}") private Integer delayNotifikasi;

    @Value("${notifikasi.konfigurasi.tagihan}") private String konfigurasiTagihan;
    @Value("${notifikasi.konfigurasi.pembayaran}") private String konfigurasiPembayaran;
    @Value("${notifikasi.contactinfo}") private String contactinfo;
    @Value("${notifikasi.contactinfoFull}") private String contactinfoFull;

    @Autowired private ObjectMapper objectMapper;
    @Autowired private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired private VirtualAccountDao virtualAccountDao;
    @Autowired private TagihanDao tagihanDao;

    @Scheduled(fixedDelay = 1000)
    public void prosesVaBaru() {
        processVa(VaStatus.CREATE);
    }

    @Scheduled(fixedDelay = 1000)
    public void prosesVaUpdate() {
        processVa(VaStatus.UPDATE);
    }

    @Scheduled(fixedDelay = 1000)
    public void prosesVaDelete() {
        processVa(VaStatus.DELETE);
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void prosesNotifikasiTagihan() {
        for(Tagihan tagihan : tagihanDao.findByStatusNotifikasi(StatusNotifikasi.BELUM_TERKIRIM,
                new PageRequest(0, NOTIFICATION_BATCH_SIZE)).getContent()) {
            // tunggu aktivasi VA dulu selama 60 menit
            if (LocalDateTime.now().isBefore(
                    tagihan.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDateTime().plusMinutes(delayNotifikasi))) {
                continue;
            }

            // tidak ada VA, tidak usah kirim notifikasi
            Iterator<VirtualAccount> daftarVa = virtualAccountDao.findByTagihan(tagihan).iterator();
            Boolean adaVaAktif = false;
            while (daftarVa.hasNext()) {
                VirtualAccount va = daftarVa.next();
                if (VaStatus.AKTIF.equals(va.getVaStatus())) {
                    adaVaAktif = true;
                    break;
                }
            }

            if (!adaVaAktif) {
                continue;
            }

            sendNotifikasiTagihan(tagihan);
        }
    }

    public void sendNotifikasiTagihan(Tagihan tagihan) {
        try {
            String email = tagihan.getDebitur().getEmail();
            String hp = tagihan.getDebitur().getNoHp();

            Map<String, Object> notifikasi = new LinkedHashMap<>();
            notifikasi.put("email", email);
            notifikasi.put("mobile", hp);
            notifikasi.put("konfigurasi", konfigurasiTagihan);


            StringBuilder rekening = new StringBuilder("");
            StringBuilder rekeningFull = new StringBuilder("<ul>");

            for (VirtualAccount va : virtualAccountDao.findByTagihan(tagihan)) {
                if (!VaStatus.AKTIF.equals(va.getVaStatus())) {
                    continue;
                }
                if (rekening.length() > 0) {
                    rekening.append("/");
                }
                rekening.append(va.getBank().getNama() + " " + va.getNomor());
                rekeningFull.append("<li>" + va.getBank().getNama() + " " + va.getNomor() + "</li>");
            }
            rekeningFull.append("</ul>");


            NotifikasiTagihanRequest requestData = NotifikasiTagihanRequest.builder()
                    .jumlah(tagihan.getNilaiTagihan())
                    .keterangan(tagihan.getJenisTagihan().getNama())
                    .nama(tagihan.getDebitur().getNama())
                    .email(tagihan.getDebitur().getEmail())
                    .noHp(tagihan.getDebitur().getNoHp())
                    .nomorTagihan(tagihan.getNomor())
                    .rekening(rekening.toString())
                    .rekeningFull(rekeningFull.toString())
                    .tanggalTagihan(FORMATTER_ISO_DATE.format(tagihan.getTanggalTagihan()))
                    .contactinfo(contactinfo)
                    .contactinfoFull(contactinfoFull)
                    .build();

            notifikasi.put("data", requestData);
            kafkaTemplate.send(kafkaTopicNotificationRequest, objectMapper.writeValueAsString(notifikasi));
            tagihan.setStatusNotifikasi(StatusNotifikasi.SUDAH_TERKIRIM);
            tagihanDao.save(tagihan);
        } catch (Exception err) {
            LOGGER.warn(err.getMessage(), err);
        }
    }

    public void sendNotifikasiPembayaran(Pembayaran pembayaran) {
        sendPembayaranTagihan(pembayaran);
        try {
            NotifikasiPembayaranRequest request = NotifikasiPembayaranRequest.builder()
                    .contactinfo(contactinfo)
                    .contactinfoFull(contactinfoFull)
                    .keterangan(pembayaran.getTagihan().getJenisTagihan().getNama())
                    .nomorTagihan(pembayaran.getTagihan().getNomor())
                    .nama(pembayaran.getTagihan().getDebitur().getNama())
                    .noHp(pembayaran.getTagihan().getDebitur().getNoHp())
                    .tanggalTagihan(FORMATTER_ISO_DATE.format(pembayaran.getTagihan().getTanggalTagihan()))
                    .nilaiPembayaran(pembayaran.getJumlah())
                    .nilaiTagihan(pembayaran.getTagihan().getNilaiTagihan())
                    .rekening(pembayaran.getBank().getNama())
                    .waktu(FORMATTER_ISO_DATE_TIME.format(pembayaran.getWaktuTransaksi()))
                    .referensi(pembayaran.getReferensi())
                    .build();

            Map<String, Object> notifikasi = new LinkedHashMap<>();
            notifikasi.put("email", pembayaran.getTagihan().getDebitur().getEmail());
            notifikasi.put("mobile", pembayaran.getTagihan().getDebitur().getNoHp());
            notifikasi.put("konfigurasi", konfigurasiPembayaran);

            notifikasi.put("data", request);
            kafkaTemplate.send(kafkaTopicNotificationRequest, objectMapper.writeValueAsString(notifikasi));
        } catch (Exception err) {
            LOGGER.warn(err.getMessage(), err);
        }
    }

    public void sendTagihanResponse(TagihanResponse tagihanResponse) {
        try {
            kafkaTemplate.send(kafkaTopicTagihanResponse, objectMapper.writeValueAsString(tagihanResponse));
        } catch (Exception err) {
            LOGGER.warn(err.getMessage(), err);
        }
    }

    public void sendDebiturResponse(Map<String, Object> data) {
        try {
            kafkaTemplate.send(kafkaTopicDebiturResponse, objectMapper.writeValueAsString(data));
        } catch (Exception err) {
            LOGGER.warn(err.getMessage(), err);
        }
    }

    public void sendPembayaranTagihan(Pembayaran p) {
        PembayaranTagihan pt = PembayaranTagihan.builder()
                .bank(p.getBank().getId())
                .nomorTagihan(p.getTagihan().getNomor())
                .nomorDebitur(p.getTagihan().getDebitur().getNomorDebitur())
                .namaDebitur(p.getTagihan().getDebitur().getNama())
                .keteranganTagihan(p.getTagihan().getKeterangan())
                .statusTagihan(p.getTagihan().getStatusTagihan().toString())
                .nilaiTagihan(p.getTagihan().getNilaiTagihan())
                .nilaiPembayaran(p.getJumlah())
                .nilaiAkumulasiPembayaran(p.getTagihan().getJumlahPembayaran())
                .referensiPembayaran(p.getReferensi())
                .waktuPembayaran(FORMATTER_ISO_DATE_TIME.format(p.getWaktuTransaksi()))
                .tanggalTagihan(FORMATTER_ISO_DATE.format(p.getTagihan().getTanggalTagihan()))
                .tanggalJatuhTempo(FORMATTER_ISO_DATE.format(p.getTagihan().getTanggalJatuhTempo()))
                .build();

        try {
            kafkaTemplate.send(kafkaTopicPembayaranTagihan, objectMapper.writeValueAsString(pt));
        } catch (Exception err) {
            LOGGER.warn(err.getMessage(), err);
        }
    }

    private void processVa(VaStatus status) {
        Iterator<VirtualAccount> daftarVa = virtualAccountDao.findByVaStatus(status).iterator();
        if (daftarVa.hasNext()) {
            VirtualAccount va = daftarVa.next();
            try {
                VaRequest vaRequest = createRequest(va, status);
                String json = objectMapper.writeValueAsString(vaRequest);
                LOGGER.debug("VA Request : {}", json);
                kafkaTemplate.send(kafkaTopicVaRequest, json);
                va.setVaStatus(VaStatus.SEDANG_PROSES);
                virtualAccountDao.save(va);
            } catch (Exception err) {
                LOGGER.warn(err.getMessage(), err);
            }
        }
    }

    private VaRequest createRequest(VirtualAccount va, VaStatus requestType) {
        VaRequest vaRequest
                = VaRequest.builder()
                .accountType(va.getTagihan().getJenisTagihan().getTipePembayaran())
                .requestType(requestType)
                .accountNumber(VirtualAccountNumberGenerator
                        .generateVirtualAccountNumber(
                                va.getTagihan().getDebitur().getNomorDebitur()
                                        + va.getTagihan().getJenisTagihan().getKode(),
                                va.getBank().getJumlahDigitVirtualAccount()))
                .amount(va.getTagihan().getNilaiTagihan().subtract(va.getTagihan().getJumlahPembayaran()))
                .description(va.getTagihan().getKeterangan())
                .email(va.getTagihan().getDebitur().getEmail())
                .phone(va.getTagihan().getDebitur().getNoHp())
                .expireDate(FORMATTER_ISO_DATE.format(va.getTagihan().getTanggalJatuhTempo()))
                .invoiceNumber(va.getTagihan().getNomor())
                .name(va.getTagihan().getDebitur().getNama())
                .bankId(va.getBank().getId())
                .build();
        return vaRequest;
    }
}
