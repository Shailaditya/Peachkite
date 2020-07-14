package com.peachkite.service;

import com.peachkite.db.domain.CompanyClaimRequest;
import com.peachkite.vo.QueryVO;

public interface EmailService {

    void sendRegistrationMail(String userEmail) throws Exception;
    void sendCompanyQueryMail(QueryVO query,String companyEmailId) throws Exception;
    void sendClaimCompanyApprovalMail(String hostname,CompanyClaimRequest companyClaimRequest) throws Exception;
    void sendForgetPasswordMail(String link,String userEmail) throws Exception;
    void sendQueryMail(QueryVO query) throws Exception;
    void sendBackUpEmail(String attachmentPath) throws Exception;
    void sendFeedbackMail(String email) throws Exception;
    void sendNotificationReplyMail(String to,String notificationDetail,String reply,String companyName) throws Exception;
    void sendCompanyClaimMailReceivedToUser(String to,String companyName) throws Exception;
    void sendCompanyClaimAcceptedMailToUser(String to,String companyName) throws Exception;
}
