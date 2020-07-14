package com.peachkite.service.impl;

import com.peachkite.db.domain.Feedback;
import com.peachkite.db.domain.Notification;
import com.peachkite.db.repository.FeedbackRepository;
import com.peachkite.db.repository.NotificationRepository;
import com.peachkite.service.EmailService;
import com.peachkite.service.FeedbackService;
import com.peachkite.service.UserService;
import com.peachkite.util.DateTimeUtils;
import com.peachkite.vo.FeedbackNotificationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    protected UserService userService;

    @Autowired
    protected FeedbackRepository feedbackRepository;

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected NotificationRepository notificationRepository;

    @Override
    public Feedback create(Feedback feedback) throws Exception{
        if(userService.getLoggedInUser()!=null){
            feedback.setUserId(userService.getLoggedInUser().getId());
            if(feedback.getUserAssociationYear()!=null){
                feedback.setUserAssociationDate(DateTimeUtils.parseDateString(
                        feedback.getUserAssociationYear()+"-02-02",
                        "YYYY-MM-DD"));
            }
            feedback.setCreatedAt(new Date());
            feedback=this.feedbackRepository.save(feedback);
            emailService.sendFeedbackMail(userService.getLoggedInUser().getEmail());
            Notification notification=new Notification();
            notification.setCompanyId(feedback.getCompanyId());
            notification.setCreatedAt(new Date());
            notification.setDetail(feedback.getComment());
            notification.setFrom(userService.getLoggedInUser().getEmail());
            notification.setStatus(Notification.Status.NA);
            notification.setType(Notification.Type.Review);
            notification.setFeedbackId(feedback.getId());
            notificationRepository.save(notification);
            return feedback;
        }
        throw new Exception("User session is null");
    }

    @Override
    public Page<Feedback> getFeedbackByCompanyId(String companyId, boolean includeEmptyComment, Pageable pageable){
        return includeEmptyComment?
                feedbackRepository.findByCompanyIdOrderByCreatedAtDesc(companyId,pageable):
                feedbackRepository.findByCompanyIdCommentNotNull(companyId,pageable);
    }

    @Override
    public FeedbackNotificationVO getFeedbackNotificationVO(String feedbackId) {
        return new FeedbackNotificationVO(feedbackRepository.findOne(feedbackId));
    }

    @Override
    public boolean checkFeedbackExists(String userId, String companyId) {
        List<Feedback> feedbacks=feedbackRepository.findByCompanyIdAndUserId(companyId,userId);
        return feedbacks.size()==0?false:true;
    }
}
