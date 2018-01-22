package id.ac.tazkia.payment.virtualaccount.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.tazkia.payment.virtualaccount.dao.TagihanDao;
import id.ac.tazkia.payment.virtualaccount.dao.VirtualAccountDao;
import id.ac.tazkia.payment.virtualaccount.dto.NotifikasiTagihanRequest;
import id.ac.tazkia.payment.virtualaccount.dto.TagihanResponse;
import id.ac.tazkia.payment.virtualaccount.dto.VaRequest;
import id.ac.tazkia.payment.virtualaccount.entity.StatusNotifikasi;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.entity.VaStatus;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import id.ac.tazkia.payment.virtualaccount.helper.VirtualAccountNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

@Service @Transactional
public class KafkaSenderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSenderService.class);
    private static final SimpleDateFormat FORMATTER_ISO_DATE = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${kafka.topic.notification.request}") private String kafkaTopicNotificationRequest;
    @Value("${kafka.topic.va.request}") private String kafkaTopicVaRequest;
    @Value("${kafka.topic.debitur.response}") private String kafkaTopicDebiturResponse;
    @Value("${kafka.topic.tagihan.response}") private String kafkaTopicTagihanResponse;

    @Value("${notifikasi.konfigurasi.tagihan}") private String konfigurasiTagihan;
    @Value("${notifikasi.contactinfo}") private String contactinfo;
    @Value("${notifikasi.contactinfoFull}") private String contactinfoFull;

    @Autowired private ObjectMapper objectMapper;
    @Autowired private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired private VirtualAccountDao virtualAccountDao;
    @Autowired private TagihanDao tagihanDao;

    @Scheduled(fixedDelay = 3000)
    public void prosesVaBaru() {
        processVa(VaStatus.CREATE);
    }

    @Scheduled(fixedDelay = 3000)
    public void prosesVaUpdate() {
        processVa(VaStatus.UPDATE);
    }

    @Scheduled(fixedDelay = 3000)
    public void prosesVaDelete() {
        processVa(VaStatus.DELETE);
    }

    @Scheduled(fixedDelay = 3000)
    public void sendNotifikasiTagihan() {
        for(Tagihan tagihan : tagihanDao.findByStatusNotifikasi(StatusNotifikasi.BELUM_TERKIRIM)) {
            // tunggu aktivasi VA dulu selama 5 menit
            if (LocalDateTime.now().isBefore(
                    tagihan.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDateTime().plusMinutes(5))) {
                continue;
            }
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
                        .keterangan(tagihan.getKeterangan())
                        .nama(tagihan.getDebitur().getNama())
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

    private void processVa(VaStatus status) {
        virtualAccountDao.findByVaStatus(status)
                .forEach((va -> {
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
                }));
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
                .amount(va.getTagihan().getNilaiTagihan())
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
