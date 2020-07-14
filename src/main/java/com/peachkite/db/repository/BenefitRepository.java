package com.peachkite.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.peachkite.db.domain.Benefit;

public interface BenefitRepository extends MongoRepository<Benefit,String>{

}
