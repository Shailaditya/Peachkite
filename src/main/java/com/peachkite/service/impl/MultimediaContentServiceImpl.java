package com.peachkite.service.impl;

import com.peachkite.db.constants.MongoFields;
import com.peachkite.db.domain.MultimediaContent;
import com.peachkite.db.repository.MultimediaContentRepository;
import com.peachkite.service.MultimediaContentService;
import com.peachkite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.List;

@Service
public class MultimediaContentServiceImpl implements MultimediaContentService {

    @Autowired
    MultimediaContentRepository multimediaContentRepository;

    @Autowired
    UserService userService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Page<MultimediaContent> findByCompanyIdAndStatus(String companyId,
                                                            MultimediaContent.Status status,
                                                            Pageable pageable) {
        return multimediaContentRepository.findByCompanyIdAndStatus(companyId,status,new Date(),pageable);
    }

    @Override
    public Page<MultimediaContent> findByCompanyId(String companyId,
                                                   Pageable pageable) {
        return multimediaContentRepository.findByCompanyId(companyId,pageable);
    }

    @Override
    public MultimediaContent saveContent(MultimediaContent content) {
        return multimediaContentRepository.save(content);
    }

    @Override
    public List<MultimediaContent> saveContent(List<MultimediaContent> contentList) {
        return multimediaContentRepository.save(contentList);
    }

    @Override
    public MultimediaContent findOne(String id){
        return multimediaContentRepository.findOne(id);
    }
}
