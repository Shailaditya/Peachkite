package com.peachkite.db.repository;

import com.peachkite.db.domain.ResetPassword;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResetPasswordRepository  extends MongoRepository<ResetPassword,String> {
}
