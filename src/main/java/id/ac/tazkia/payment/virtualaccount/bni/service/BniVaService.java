package id.ac.tazkia.payment.virtualaccount.bni.service;

import com.bni.encrypt.BNIHash;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.tazkia.payment.virtualaccount.bni.config.BniEcollectionConfiguration;
import id.ac.tazkia.payment.virtualaccount.bni.dao.TagihanBniDao;
import id.ac.tazkia.payment.virtualaccount.bni.dto.CreateBillingRequest;
import id.ac.tazkia.payment.virtualaccount.bni.entity.StatusTagihan;
import id.ac.tazkia.payment.virtualaccount.bni.entity.TagihanBni;
import id.ac.tazkia.payment.virtualaccount.dao.BankDao;
import id.ac.tazkia.payment.virtualaccount.dao.VirtualAccountDao;
import id.ac.tazkia.payment.virtualaccount.entity.Bank;
import id.ac.tazkia.payment.virtualaccount.entity.Tagihan;
import id.ac.tazkia.payment.virtualaccount.entity.TipePembayaran;
import id.ac.tazkia.payment.virtualaccount.entity.VirtualAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Service @Transactional
public class BniVaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BniVaService.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final String PREFIX_VA = "BNI-";
    private static final String TIMEZONE = "GMT+07:00";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired private BniRunningNumberService runningNumberService;
    @Autowired private BankDao bankDao;
    @Autowired private VirtualAccountDao virtualAccountDao;
    @Autowired private TagihanBniDao tagihanBniDao;

    @Autowired
    private BniEcollectionConfiguration config;

    public void createVa(Tagihan tagihan){

        Bank bankBni = bankDao.findOne(config.getBankId());
        if(bankBni == null){
            throw new IllegalStateException("Bank BNI belum terdaftar di database");
        }

        CreateBillingRequest b = new CreateBillingRequest();
        if(TipePembayaran.CLOSED_PAYMENT.equals(tagihan.getJenisTagihan().getTipePembayaran())) {
            b.setBillingType("c");
        } else if (TipePembayaran.OPEN_PAYMENT.equals(tagihan.getJenisTagihan().getTipePembayaran())) {
            b.setBillingType("o");
        } else if (TipePembayaran.INSTALLMENT.equals(tagihan.getJenisTagihan().getTipePembayaran())) {
            b.setBillingType("i");
        }

        b.setClientId(config.getClientId());
        b.setCustomerName(tagihan.getSiswa().getNama());
        b.setCustomerEmail(tagihan.getSiswa().getEmail());
        b.setCustomerPhone(tagihan.getSiswa().getNoHp());
        b.setDatetimeExpired(toIso8601(new Date(tagihan.getTanggalKadaluarsa().getTime())));
        b.setDescription(tagihan.getKeterangan());
        b.setTrxAmount(tagihan.getJumlahTagihan().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

        String datePrefix = DATE_FORMAT.format(new Date());
        String prefix = PREFIX_VA + datePrefix;
        Long runningNumber = runningNumberService.getNumber(prefix);

        b.setTrxId(datePrefix + String.format("%06d", runningNumber));
        b.setVirtualAccount("8"+config.getClientId()
                + tagihan.getJenisTagihan().getKode()
                + String.format("%10s", tagihan.getSiswa().getNomorSiswa()).replace(' ', '0'));

        try {
            Map<String, String> hasil = executeRequest(b);
            if(hasil != null) {
                VirtualAccount va = new VirtualAccount();
                va.setBank(bankBni);
                va.setTagihan(tagihan);
                va.setIdVirtualAccount(b.getTrxId());
                va.setNomorVirtualAccount(b.getVirtualAccount());
                virtualAccountDao.save(va);

                TagihanBni tb = new TagihanBni();
                BeanUtils.copyProperties(b, tb);
                tb.setTagihan(tagihan);
                tb.setVa(va);
                tb.setStatusTagihan(StatusTagihan.VA_AKTIF);
                tagihanBniDao.save(tb);
            } else {
                LOGGER.error("BNI : Error membuat VA {}", b.getVirtualAccount());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    private String toIso8601(Date d) {
        Instant i = d.toInstant();
        ZonedDateTime zdt = i.atZone(ZoneId.of(TIMEZONE));
        return zdt.truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private Map<String, String> executeRequest(Object request) throws Exception {
        String responseData = sendRequest(request);

        if(responseData != null) {
            LOGGER.debug("Response Data : {}", responseData);

            String decryptedResponse = BNIHash.parseData(responseData, config.getClientId(), config.getClientKey());
            LOGGER.debug("Decrypted Response : {}", decryptedResponse);

            return objectMapper.readValue(decryptedResponse, Map.class);
        }
        return null;
    }

    private String sendRequest(Object requestData) throws Exception {
        String rawData = objectMapper.writeValueAsString(requestData);
        LOGGER.debug("BNI : Raw Data : {}",rawData);

        String encryptedData = BNIHash.hashData(rawData, config.getClientId(), config.getClientKey());
        LOGGER.debug("BNI : Encrypted Data : {}",encryptedData);

        Map<String, String> wireData = new TreeMap<>();
        wireData.put("client_id", config.getClientId());
        wireData.put("data", encryptedData);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(wireData, headers);
        ResponseEntity<Map> response = restTemplate.exchange(config.getServerUrl(), HttpMethod.POST, request, Map.class);
        Map<String, String> hasil = response.getBody();
        String responseStatus = hasil.get("status");
        LOGGER.debug("BNI : Response Status : {}",responseStatus);

        if(!"000".equals(responseStatus)) {
            LOGGER.error("BNI : Response status : {}", responseStatus);
        }

        return hasil.get("data");
    }
}
