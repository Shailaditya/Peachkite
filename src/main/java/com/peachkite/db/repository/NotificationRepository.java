package com.peachkite.db.repository;

import com.peachkite.db.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification,String> {

    Page<Notification> findByCompanyId(String companyId, Pageable pageable);
}
