package com.peachkite.service.impl;

import com.peachkite.db.domain.CompanyClaimRequest;
import com.peachkite.service.EmailService;
import com.peachkite.vo.QueryVO;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Service
public class ZohoEmailService implements EmailService,InitializingBean {

    /*private static final String AUTH_USERNAME="support@peachkite.com";
    private static final String AUTH_PASSWORD="Peachkite@7583";*/

    private static final Logger logger = LoggerFactory.getLogger(ZohoEmailService.class);

    private static Session session;

    @Value("${mail.auth.username}")
    private String mailAuthUsername;

    private String mailAuthPassword="Peachkite@7583";

    @Value("${mail.default.sender.email}")
    private String defaultSenderEmail;

    @Value("${mail.debug}")
    private Boolean mailDebugEnabled;

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

    @Value("${mail.company-claim.template.body}")
    private String companyClaimMailBodyTemplate;

    @Value("${mail.company-claim.template.subject}")
    private String companyClaimMailSubjectTemplate;

    @Value("${mail.company-claim.receiver.email}")
    private String companyClaimMailReceiver;

    @Value("${company-claim.verify.endpoint}")
    private String companyClaimVerifyEndpoint;

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

    private static void createSession(String enableDebug,String mailAuthUsername,String mailAuthPassword) throws GeneralSecurityException {
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        properties.put("mail.pop3.ssl.trust", "*");
        properties.put("mail.pop3.ssl.socketFactory", sf);

        properties.setProperty("mail.smtp.socketFactory.fallback","false");
        properties.setProperty("mail.smtp.host", "smtp.zoho.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
        properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.startssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", enableDebug);
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.debug.auth", enableDebug);
        session = Session.getDefaultInstance(properties,new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(mailAuthUsername,mailAuthPassword);
            }
        });
        logger.info("Mail session created successfully with username : "+mailAuthUsername);
    }

    public void afterPropertiesSet(){
        try{
            createSession(mailDebugEnabled.toString(),mailAuthUsername,mailAuthPassword);
        }catch(GeneralSecurityException e){
            logger.error("Exception thrown while creating mail session for username : "+mailAuthUsername);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void sendRegistrationMail(String userEmail) throws MessagingException{
        sendMail(registrationSenderMailAddress,
                userEmail,
                String.format(registrationMailSubjectTemplate,userEmail),
                String.format(registrationMailBodyTemplate,userEmail));
        logger.info("Registration mail send to "+userEmail);
    }

    @Override
    @Async
    public void sendForgetPasswordMail(String link,String userEmail) throws MessagingException{
        sendMail(registrationSenderMailAddress,
                userEmail,
                String.format(passwordResetSubjectTemplate,userEmail),
                String.format(passwordResetBodyTemplate,link));
        logger.info("Forgot password mail send to "+userEmail);
    }

    @Override
    @Async
    public void sendQueryMail(QueryVO query) throws MessagingException{
        sendMail(querySenderMailAddress,
                queryReceiverMailAddress,
                String.format(queryMailSubjectTemplate,query.getEmail()),
                String.format(queryMailBodyTemplate,query.getName(),query.getEmail(),query.getPhone(),query.getQuery()));
        logger.info("Query mail send on behalf of user "+query.getEmail());
    }

    @Override
    @Async
    public void sendCompanyQueryMail(QueryVO query,String companyEmailId) throws MessagingException {
        sendMail(querySenderMailAddress,
                companyEmailId,
                String.format(queryMailSubjectTemplate, query.getEmail()),
                String.format(queryMailBodyTemplate, query.getName() == null ? "" : query.getName(), query.getEmail(), query.getPhone() == null ? "" : query.getPhone(), query.getQuery()));
        logger.info("CompanyQuery mail send on behalf of user " + query.getEmail());
    }
    @Override
    @Async
    public void sendFeedbackMail(String email) throws MessagingException{
        sendMail(querySenderMailAddress,
                email,
                feedbackMailSubjectTemplate,
                feedbacknMailBodyTemplate);
        logger.info("Feedback mail send to "+email);
    }

    @Override
    public void sendNotificationReplyMail(String to, String notificationDetail, String reply, String companyName) throws Exception {

    }

    @Override
    public void sendCompanyClaimMailReceivedToUser(String to, String companyName) throws Exception {

    }

    @Override
    public void sendCompanyClaimAcceptedMailToUser(String to, String companyName) throws Exception {

    }

    @Override
    @Async
    public void sendClaimCompanyApprovalMail(String hostname,CompanyClaimRequest companyClaimRequest) throws MessagingException {
        String url = String.format(this.companyClaimVerifyEndpoint, companyClaimRequest.getId());
        sendMail(this.defaultSenderEmail,
                this.companyClaimMailReceiver,
                String.format(this.companyClaimMailSubjectTemplate, companyClaimRequest.getCompanName()),
                String.format(this.companyClaimMailBodyTemplate, companyClaimRequest.getCompanName(), companyClaimRequest.getUserEmail(), url));
        logger.info("Company Claim request sent for " + companyClaimRequest.getCompanName());
    }
    @Override
    @Async
    public void sendBackUpEmail(String attachmentPath) throws MessagingException{
        sendMail(defaultSenderEmail,
                backReceiverMailAddress,
                "Backup",
                "Backup",attachmentPath);
        logger.info("Backup mail send to "+backReceiverMailAddress);
    }

    private void sendMail(String senderEmail,String receiverEmail,String subject,String body) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(senderEmail));
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO,InternetAddress.parse(receiverEmail));
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(body,"text/html");
        Transport.send(mimeMessage);
    }

    private void sendMail(String senderEmail,String receiverEmail,String subject,String body,String attachmentPath) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(senderEmail));
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO,InternetAddress.parse(receiverEmail));
        mimeMessage.setSubject(subject);
//        mimeMessage.setContent(body,"text/html");
        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart1.setText(body);

        //4) create new MimeBodyPart object and set DataHandler object to this object
        MimeBodyPart messageBodyPart2 = new MimeBodyPart();

        String filename = attachmentPath;//change accordingly
        DataSource source = new FileDataSource(filename);
        messageBodyPart2.setDataHandler(new DataHandler(source));
        messageBodyPart2.setFileName(filename);


        //5) create Multipart object and add MimeBodyPart objects to this object
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart1);
        multipart.addBodyPart(messageBodyPart2);

        //6) set the multiplart object to the message object
        mimeMessage.setContent(multipart );

        //7) send message
        Transport.send(mimeMessage);
    }
}
