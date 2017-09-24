package id.ac.tazkia.payment.virtualaccount.dao;

import id.ac.tazkia.payment.virtualaccount.entity.Siswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SiswaDao extends PagingAndSortingRepository<Siswa, String> {
    public Siswa findByNomorSiswa(String nomor);
    public Page<Siswa> findByNomorSiswaContainsOrNamaContains(String nomorSiswa, String nama, Pageable page);
}
