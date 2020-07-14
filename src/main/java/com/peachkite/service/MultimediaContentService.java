package com.peachkite.service;

import com.peachkite.db.domain.MultimediaContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MultimediaContentService {

    Page<MultimediaContent> findByCompanyIdAndStatus(String companyId, MultimediaContent.Status status, Pageable pageable);
    Page<MultimediaContent> findByCompanyId(String companyId, Pageable pageable);
    MultimediaContent saveContent(MultimediaContent content);
    List<MultimediaContent> saveContent(List<MultimediaContent> contentList);
    MultimediaContent findOne(String id);
}
