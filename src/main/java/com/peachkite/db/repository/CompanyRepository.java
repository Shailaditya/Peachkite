package com.peachkite.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.peachkite.db.domain.Company;

public interface CompanyRepository extends MongoRepository<Company,String>{

}
