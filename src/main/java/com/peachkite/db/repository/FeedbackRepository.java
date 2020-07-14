package com.peachkite.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.peachkite.db.domain.Feedback;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FeedbackRepository extends MongoRepository<Feedback,String>{

    @Query(value = "{cmpid:?0,$and:[{cmt:{$ne:null}},{cmt:{$ne:''}}]}")
    Page<Feedback> findByCompanyIdCommentNotNull(String companyId, Pageable pageable);
    Page<Feedback> findByCompanyIdOrderByCreatedAtDesc(String companyId, Pageable pageable);
    List<Feedback> findByCompanyIdAndUserId(String companyId, String userId);

}
