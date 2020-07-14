package com.peachkite.db.repository;

import com.peachkite.db.domain.CompanyClaimRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyClaimRequestRepository extends MongoRepository<CompanyClaimRequest,String> {

}
