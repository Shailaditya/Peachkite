package com.peachkite.db.repository;

import com.peachkite.db.constants.MongoFields;
import com.peachkite.db.domain.MultimediaContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;

public interface MultimediaContentRepository extends MongoRepository<MultimediaContent,String> {

    @Query("{"+ MongoFields.COMPANY_ID+":?0 , "+MongoFields.STATUS+":?1, "+MongoFields.START_DATE+":{$lt:?2}, "+MongoFields.END_DATE+":{$gt:?2}}")
    Page<MultimediaContent> findByCompanyIdAndStatus(String companyId, MultimediaContent.Status status, Date date, Pageable pageable);

    Page<MultimediaContent> findByCompanyId(String companyId, Pageable pageable);
}
