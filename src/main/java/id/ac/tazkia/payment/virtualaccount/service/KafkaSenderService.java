package id.ac.tazkia.payment.virtualaccount.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.tazkia.payment.virtualaccount.dao.VirtualAccountDao;
import id.ac.tazkia.payment.virtualaccount.dto.AccountType;
import id.ac.tazkia.payment.virtualaccount.dto.VaRequest;
import id.ac.tazkia.payment.virtualaccount.dto.VaRequestType;
import id.ac.tazkia.payment.virtualaccount.entity.VaStatus;
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

@Service @Transactional
public class KafkaSenderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSenderService.class);
    private static final SimpleDateFormat FORMATTER_ISO_DATE = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${kafka.topic.bni.va.request}") private String kafkaTopicBniVaRequest;

    @Autowired private ObjectMapper objectMapper;
    @Autowired private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired private VirtualAccountDao virtualAccountDao;

    @Scheduled(fixedDelay = 10)
    public void prosesVaBaru() {
        virtualAccountDao.findByVaStatus(VaStatus.BARU)
                .forEach((va -> {
                    try {
                        VaRequest vaRequest
                                = VaRequest.builder()
                                .accountType(AccountType.CLOSED)
                                .requestType(VaRequestType.CREATE)
                                .accountNumber(VirtualAccountNumberGenerator.generateVirtualAccountNumber(va.getTagihan().getDebitur().getNomorDebitur(), va.getBank().getJumlahDigitVirtualAccount()))
                                .amount(va.getTagihan().getNilaiTagihan())
                                .description(va.getTagihan().getKeterangan())
                                .email(va.getTagihan().getDebitur().getEmail())
                                .phone(va.getTagihan().getDebitur().getNoHp())
                                .expireDate(FORMATTER_ISO_DATE.format(va.getTagihan().getTanggalJatuhTempo()))
                                .invoiceNumber(va.getTagihan().getNomor())
                                .name(va.getTagihan().getDebitur().getNama())
                                .build();

                        String json = objectMapper.writeValueAsString(vaRequest);
                        LOGGER.debug("VA Request BNI : {}", json);
                        kafkaTemplate.send(kafkaTopicBniVaRequest, json);
                        va.setVaStatus(VaStatus.SEDANG_PROSES);
                        virtualAccountDao.save(va);
                    } catch (Exception err) {
                        LOGGER.warn(err.getMessage(), err);
                    }
                }));
    }
}
