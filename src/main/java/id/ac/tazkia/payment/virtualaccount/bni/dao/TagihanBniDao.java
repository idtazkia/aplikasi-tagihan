package id.ac.tazkia.payment.virtualaccount.bni.dao;

import id.ac.tazkia.payment.virtualaccount.bni.entity.TagihanBni;
import org.springframework.data.repository.CrudRepository;

public interface TagihanBniDao extends CrudRepository<TagihanBni, String> {
    public TagihanBni findByTrxId(String trxId);
}
