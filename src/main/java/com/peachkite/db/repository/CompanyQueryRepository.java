package com.peachkite.db.repository;

import com.peachkite.db.domain.CompanyQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyQueryRepository extends MongoRepository<CompanyQuery,String> {
}
