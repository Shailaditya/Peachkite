package com.peachkite.db.repository;

import com.peachkite.db.domain.Faq;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FaqRepository extends MongoRepository<Faq,String> {

    public List<Faq> findByCompanyIdAndStatusOrderBySerialNumber(String companyId,Faq.Status status);
    public List<Faq> findByCompanyIdOrderBySerialNumber(String companyId);
}
