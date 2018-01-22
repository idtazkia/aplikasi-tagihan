package id.ac.tazkia.payment.virtualaccount.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.tazkia.payment.virtualaccount.dao.*;
import id.ac.tazkia.payment.virtualaccount.dto.*;
import id.ac.tazkia.payment.virtualaccount.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Service @Transactional
public class KafkaListenerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaListenerService.class);

    @Autowired private ObjectMapper objectMapper;
    @Autowired private Validator validator;

    @Autowired private VirtualAccountDao virtualAccountDao;
    @Autowired private BankDao bankDao;
    @Autowired private TagihanDao tagihanDao;
    @Autowired private PembayaranDao pembayaranDao;
    @Autowired private DebiturDao debiturDao;
    @Autowired private JenisTagihanDao jenisTagihanDao;
    @Autowired private TagihanService tagihanService;
    @Autowired private KafkaSenderService kafkaSenderService;

    @KafkaListener(topics = "${kafka.topic.debitur.request}", group = "${spring.kafka.consumer.group-id}")
    public void handleDebiturRequest(String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            LOGGER.debug("Terima message : {}", message);
            Debitur d = objectMapper.readValue(message, Debitur.class);
            BeanPropertyBindingResult binder = new BeanPropertyBindingResult(d, "debitur");
            validator.validate(d, binder);

            if (binder.hasErrors()) {
                LOGGER.warn("Gagal mendaftarkan debitur {}", binder.getAllErrors());
                response.put("sukses", false);
                response.put("data", binder.getAllErrors());
                kafkaSenderService.sendDebiturResponse(response);
                return;
            }

            if (debiturDao.findByNomorDebitur(d.getNomorDebitur()) != null) {
                response.put("sukses", false);
                response.put("data", "Nomor debitur " + d.getNomorDebitur() + " sudah ada");
                kafkaSenderService.sendDebiturResponse(response);
                return;
            }

            debiturDao.save(d);
            response.put("sukses", true);
            kafkaSenderService.sendDebiturResponse(response);
        } catch (Exception err) {
            LOGGER.warn(err.getMessage(), err);
            response.put("sukses", false);
            response.put("data", err.getMessage());
            kafkaSenderService.sendDebiturResponse(response);
        }
    }

    @KafkaListener(topics = "${kafka.topic.tagihan.request}", group = "${spring.kafka.consumer.group-id}")
    public void handleTagihanRequest(String message) {
        TagihanResponse response = new TagihanResponse();
        try {
            LOGGER.debug("Terima message : {}", message);
            TagihanRequest request = objectMapper.readValue(message, TagihanRequest.class);

            response.setSukses(true);
            BeanUtils.copyProperties(request, response);

            Tagihan t = new Tagihan();

            Debitur d = debiturDao.findByNomorDebitur(request.getDebitur());
            if (d == null) {
                LOGGER.warn("Debitur dengan nomor {} tidak terdaftar", request.getDebitur());
                response.setSukses(false);
                response.setError("Debitur dengan nomor "+request.getDebitur()+" tidak terdaftar");
                kafkaSenderService.sendTagihanResponse(response);
                return;
            }
            t.setDebitur(d);

            JenisTagihan jt = jenisTagihanDao.findOne(request.getJenisTagihan());
            if (jt == null) {
                LOGGER.warn("Jenis Tagihan dengan id {} tidak terdaftar", request.getJenisTagihan());
                response.setSukses(false);
                response.setError("Jenis Tagihan dengan id "+request.getJenisTagihan()+" tidak terdaftar");
                kafkaSenderService.sendTagihanResponse(response);
                return;
            }
            t.setJenisTagihan(jt);

            t.setNilaiTagihan(request.getNilaiTagihan());
            t.setKeterangan(request.getKeterangan());
            t.setTanggalJatuhTempo(request.getTanggalJatuhTempo());

            tagihanService.saveTagihan(t);

            response.setNomorTagihan(t.getNomor());
            response.setTanggalTagihan(t.getTanggalTagihan());
            kafkaSenderService.sendTagihanResponse(response);
        } catch (Exception err) {
            LOGGER.warn(err.getMessage(), err);
            response.setSukses(false);
            response.setError(err.getMessage());
            kafkaSenderService.sendTagihanResponse(response);
        }
    }

    @KafkaListener(topics = "${kafka.topic.va.response}", group = "${spring.kafka.consumer.group-id}")
    public void handleVaResponse(String message) {
        try {
            LOGGER.debug("Terima message : {}", message);
            VaResponse vaResponse = objectMapper.readValue(message, VaResponse.class);

            VirtualAccount va = virtualAccountDao.findByVaStatusAndTagihanNomor(VaStatus.SEDANG_PROSES, vaResponse.getInvoiceNumber());
            if (va == null) {
                LOGGER.warn("Tagihan dengan nomor {} tidak ditemukan", vaResponse.getInvoiceNumber());
                return;
            }

            if (VaRequestStatus.ERROR.equals(vaResponse.getRequestStatus())) {
                va.setVaStatus(VaStatus.NONAKTIF);
                virtualAccountDao.save(va);
                return;
            }

            va.setNomor(vaResponse.getAccountNumber());
            va.setVaStatus(VaStatus.AKTIF);
            virtualAccountDao.save(va);
        } catch (Exception err) {
            LOGGER.warn(err.getMessage(), err);
        }
    }

    @KafkaListener(topics = "${kafka.topic.va.payment}", group = "${spring.kafka.consumer.group-id}")
    public void handleVaPayment(String message) {
        try {
            LOGGER.debug("Terima message : {}", message);
            VaPayment payment = objectMapper.readValue(message, VaPayment.class);
            Bank bank = bankDao.findOne(payment.getBankId());
            if (bank == null) {
                LOGGER.warn("Bank dengan ID {} tidak terdaftar", payment.getBankId());
                return;
            }

            Tagihan tagihan = tagihanDao.findByNomor(payment.getInvoiceNumber());
            if (tagihan == null) {
                LOGGER.warn("Tagihan dengan nomor {} tidak terdaftar", payment.getInvoiceNumber());
                return;
            }
            if (StatusPembayaran.LUNAS.equals(tagihan.getStatusPembayaran())) {
                LOGGER.warn("Tagihan dengan nomor {} sudah lunas", tagihan.getNomor());
                return;
            }

            BigDecimal akumulasiPembayaran = tagihan.getJumlahPembayaran().add(payment.getAmount());
            if(akumulasiPembayaran.compareTo(tagihan.getNilaiTagihan()) > 0){
                LOGGER.warn("Nilai pembayaran [{}] lebih besar daripada nilai tagihan [{}] nomor [{}]",
                        akumulasiPembayaran, tagihan.getNilaiTagihan(), tagihan.getNomor());
                return;
            } if(akumulasiPembayaran.compareTo(tagihan.getNilaiTagihan()) < 0){
                tagihan.setStatusPembayaran(StatusPembayaran.DIBAYAR_SEBAGIAN);
            } else {
                tagihan.setStatusPembayaran(StatusPembayaran.LUNAS);
                VirtualAccount va = virtualAccountDao.findByVaStatusAndTagihanNomor(VaStatus.AKTIF, tagihan.getNomor());
                va.setVaStatus(VaStatus.NONAKTIF);
                virtualAccountDao.save(va);
            }
            tagihan.setJumlahPembayaran(akumulasiPembayaran);

            Pembayaran p = new Pembayaran();
            p.setBank(bank);
            p.setTagihan(tagihan);
            p.setJenisPembayaran(JenisPembayaran.VIRTUAL_ACCOUNT);
            p.setJumlah(payment.getAmount());
            p.setReferensi(payment.getReference());
            p.setKeterangan("Pembayaran melalui VA Bank "+bank.getNama()+" Nomor "+payment.getAccountNumber());
            p.setWaktuTransaksi(java.sql.Timestamp.valueOf(payment.getPaymentTime()));
            pembayaranDao.save(p);

            tagihanDao.save(tagihan);

            LOGGER.info("Pembayaran melalui VA Bank {} Nomor {} telah diterima", bank.getNama(), payment.getAccountNumber());
        } catch (Exception err) {
            LOGGER.warn(err.getMessage(), err);
        }
    }
}
