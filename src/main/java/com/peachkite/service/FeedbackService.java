package com.peachkite.service;

import com.peachkite.db.domain.Feedback;
import com.peachkite.vo.FeedbackNotificationVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {

    Feedback create(Feedback feedback) throws Exception;
    Page<Feedback> getFeedbackByCompanyId(String companyId, boolean includeEmptyComment, Pageable pageable);
    FeedbackNotificationVO getFeedbackNotificationVO(String feedbackId);
    boolean checkFeedbackExists(String userId,String companyId);
}
