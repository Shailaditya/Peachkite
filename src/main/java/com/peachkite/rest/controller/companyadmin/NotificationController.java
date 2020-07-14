package com.peachkite.rest.controller.companyadmin;

import com.peachkite.db.constants.MongoFields;
import com.peachkite.db.domain.Company;
import com.peachkite.db.domain.Notification;
import com.peachkite.db.repository.NotificationRepository;
import com.peachkite.service.CompanyService;
import com.peachkite.service.EmailService;
import com.peachkite.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/notification")
public class NotificationController {

    @Autowired
    protected NotificationRepository notificationRepository;

    @Autowired
    protected CompanyService companyService;

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected FeedbackService feedbackService;

    @RequestMapping("")
    ResponseEntity<?> getNotifications(@RequestParam String companyId,
                                 @RequestParam Integer pageNumber,
                                 @RequestParam Integer pageSize){
        Pageable pageable=new PageRequest(pageNumber,pageSize, Sort.Direction.DESC, MongoFields.CREATED_AT);
        Page<Notification> notificationPage= notificationRepository.findByCompanyId(companyId,pageable);
        //Hide user information
        for(Notification notification: notificationPage.getContent()){
            notification.setFrom(null);
        }
        return new ResponseEntity<>(notificationPage, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    ResponseEntity<?> getNotification(@PathVariable String id){
        Notification notification=notificationRepository.findOne(id);
        return notification!=null?new ResponseEntity<>(notification, HttpStatus.OK):new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/{id}/reply",method = RequestMethod.POST)
    ResponseEntity<?> replyNotification(@PathVariable String id,@RequestBody Map<String,String> body) throws Exception {
        replyNotification(id,body.get("response"));
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @RequestMapping(value="/feedback/{id}",method = RequestMethod.GET)
    ResponseEntity<?> replyNotification(@PathVariable String id){
        return new ResponseEntity<Object>(feedbackService.getFeedbackNotificationVO(id),HttpStatus.OK);
    }

    private void replyNotification(String notificationId,String responseText) throws Exception {
        Notification notification=notificationRepository.findOne(notificationId);
        Company company = companyService.findCompanyById(notification.getCompanyId());
        emailService.sendNotificationReplyMail(notification.getFrom(),notification.getDetail(),responseText,company.getName());
        notification.setRepliedDate(new Date());
        notification.setResponse(responseText);
        notification.setStatus(Notification.Status.Replied);
        notificationRepository.save(notification);
    }

}
