package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Debitur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DebiturDao extends PagingAndSortingRepository<Debitur, String> {
    public Debitur findByNomorDebitur(String nomor);
    public Page<Debitur> findByNomorDebiturOrNamaContainingIgnoreCase(String nomor, String nama, Pageable page);
    public Page<Debitur> findByNomorDebiturContainingIgnoreCase(String nomor, Pageable page);
}
