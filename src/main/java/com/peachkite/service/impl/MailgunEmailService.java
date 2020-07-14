package com.peachkite.service.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.peachkite.db.domain.CompanyClaimRequest;
import com.peachkite.service.EmailService;
import com.peachkite.vo.QueryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;

@Service
@Primary
public class MailgunEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(MailgunEmailService.class);

    @Value("${mail.mailgun.api.key}")
    private String apiKey;

    @Value("${mail.mailgun.domain}")
    private String domain;

    @Value("${mail.default.sender.email}")
    private String defaultSenderEmail;

    @Value("${mail.registration.template.body}")
    private String registrationMailBodyTemplate;

    @Value("${mail.registration.template.subject}")
    private String registrationMailSubjectTemplate;

    @Value("${mail.registration.sender.email}")
    private String registrationSenderMailAddress;

    @Value("${mail.query.template.body}")
    private String queryMailBodyTemplate;

    @Value("${mail.query.template.subject}")
    private String queryMailSubjectTemplate;

    @Value("${mail.query.sender.email}")
    private String querySenderMailAddress;

    @Value("${mail.query.receiver.email}")
    private String queryReceiverMailAddress;

    @Value("${mail.backup.receiver.email}")
    private String backReceiverMailAddress;

    @Value("${mail.feedback.template.body}")
    private String feedbacknMailBodyTemplate;

    @Value("${mail.feedback.template.subject}")
    private String feedbackMailSubjectTemplate;

    @Value("${mail.password.reset.template.body}")
    private String passwordResetBodyTemplate;

    @Value("${mail.password.reset.template.subject}")
    private String passwordResetSubjectTemplate;

    @Value("${mail.company-claim.template.body}")
    private String companyClaimMailBodyTemplate;

    @Value("${mail.company-claim.template.subject}")
    private String companyClaimMailSubjectTemplate;

    @Value("${mail.company-claim.receiver.email}")
    private String companyClaimMailReceiver;

    @Value("${company-claim.verify.endpoint}")
    private String companyClaimVerifyEndpoint;

    @Value("${mail.company.query.template.subject}")
    private String companyQueryMailSubject;

    @Value("${mail.company.query.template.body}")
    private String companyQueryMailBody;

    @Value("${mail.company.claim.received.user.template.subject}")
    private String companyClaimReceivedMailSubject;

    @Value("${mail.company.claim.received.user.template.body}")
    private String companyClaimReceivedMailBody;

    @Value("${mail.company.claim.accepted.user.template.subject}")
    private String companyClaimAcceptedMailSubject;

    @Value("${mail.company.claim.accepted.user.template.body}")
    private String companyClaimAcceptedMailBody;

    @Override
    @Async
    public void sendRegistrationMail(String userEmail) throws Exception {
        sendMail(registrationSenderMailAddress,
                userEmail,
                String.format(registrationMailSubjectTemplate,userEmail),
                String.format(registrationMailBodyTemplate,userEmail));
        logger.info("Registration mail send to "+userEmail);
    }

    @Override
    @Async
    public void sendForgetPasswordMail(String link,String userEmail) throws Exception{
        sendMail(registrationSenderMailAddress,
                userEmail,
                String.format(passwordResetSubjectTemplate,userEmail),
                String.format(passwordResetBodyTemplate,link));
        logger.info("Forgot password mail send to "+userEmail);
    }

    @Override
    @Async
    public void sendQueryMail(QueryVO query) throws Exception{
        sendMail(querySenderMailAddress,
                queryReceiverMailAddress,
                String.format(queryMailSubjectTemplate,query.getEmail()),
                String.format(queryMailBodyTemplate,query.getName(),query.getEmail(),query.getPhone(),query.getQuery()));
        logger.info("Query mail send on behalf of user "+query.getEmail());
    }

    @Override
    @Async
    public void sendFeedbackMail(String email) throws Exception{
        sendMail(querySenderMailAddress,
                email,
                feedbackMailSubjectTemplate,
                feedbacknMailBodyTemplate);
        logger.info("Feedback mail send to "+email);
    }

    @Override
    @Async
    public void sendBackUpEmail(String attachmentPath) throws Exception{
        sendMail(defaultSenderEmail,
                backReceiverMailAddress,
                "Backup",
                "Backup",attachmentPath);
        logger.info("Backup mail send to "+backReceiverMailAddress);
    }

    @Override
    @Async
    public void sendClaimCompanyApprovalMail(String hostname,CompanyClaimRequest companyClaimRequest) throws Exception {
        String url = String.format(this.companyClaimVerifyEndpoint, hostname,companyClaimRequest.getId());
        sendMail(this.defaultSenderEmail,
                this.companyClaimMailReceiver,
                String.format(this.companyClaimMailSubjectTemplate, companyClaimRequest.getCompanName()),
                String.format(this.companyClaimMailBodyTemplate, companyClaimRequest.getCompanName(), companyClaimRequest.getUserEmail(), url));
        logger.info("Company Claim request sent for " + companyClaimRequest.getCompanName());
    }

    @Override
    @Async
    public void sendCompanyQueryMail(QueryVO query,String companyEmailId) throws Exception {
        sendMail(querySenderMailAddress,
                companyEmailId,
                String.format(queryMailSubjectTemplate, query.getEmail()),
                String.format(queryMailBodyTemplate, query.getName() == null ? "" : query.getName(), query.getEmail(), query.getPhone() == null ? "" : query.getPhone(), query.getQuery()));
        logger.info("CompanyQuery mail send to " +companyEmailId);
    }

    @Override
    @Async
    public void sendNotificationReplyMail(String to,String notificationDetail,String reply,String companyName) throws Exception{
        sendMail(defaultSenderEmail,
                to,
                companyQueryMailSubject,
                String.format(companyQueryMailBody, companyName,notificationDetail,reply));
        logger.info("Notification Reply mail to user " + to);
    }

    @Override
    public void sendCompanyClaimMailReceivedToUser(String to, String companyName) throws Exception {
        sendMail(defaultSenderEmail,
                to,
                this.companyClaimReceivedMailSubject,
                String.format(this.companyClaimReceivedMailBody, companyName));
        logger.info("Company Claim Notification Mail to user " + to);
    }

    @Override
    public void sendCompanyClaimAcceptedMailToUser(String to, String companyName) throws Exception {
        sendMail(defaultSenderEmail,
                to,
                this.companyClaimAcceptedMailSubject,
                String.format(this.companyClaimAcceptedMailBody, companyName));
        logger.info("Company Claim Accepted Mail to user " + to);
    }

    private void sendMail(String senderEmail,String receiverEmail,String subject,String body) throws Exception {
        try{
            HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + domain + "/messages")
                    .basicAuth("api", apiKey)
                    .queryString("from", senderEmail)
                    .queryString("to", receiverEmail)
                    .queryString("subject", subject)
                    .queryString("html", body)
                    .field("o:tracking", "False")
                    .asJson();
        }catch(UnirestException e){
            logger.error("Exception while sending mail ",e);
            throw new Exception();
        }
    }

    private void sendMail(String senderEmail,String receiverEmail,String subject,String body,String attachmentPath) throws Exception {
        try{
            HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + domain + "/messages")
                    .basicAuth("api", apiKey)
                    .queryString("from", senderEmail)
                    .queryString("to", receiverEmail)
                    .queryString("subject", subject)
                    .queryString("html", body)
                    .field("o:tracking", "False")
                    .field("attachment", new File(attachmentPath))
                    .asJson();
        }catch(UnirestException e){
            logger.error("Exception while sending mail ",e);
            throw new Exception(e);
        }

    }
}
