package com.peachkite.rest.controller;

import com.peachkite.db.domain.ResetPassword;
import com.peachkite.db.domain.User;
import com.peachkite.db.repository.ResetPasswordRepository;
import com.peachkite.exception.CryptoException;
import com.peachkite.exception.UserServiceException;
import com.peachkite.service.EmailService;
import com.peachkite.service.UserService;
import com.peachkite.util.CryptoUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/*@RestController
@RequestMapping("/api/public/rp")*/
public class PasswordResetController {

    /*private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    @Value("${reset.password.link}")
    private String resetPasswordLink;

    @Autowired
    protected ResetPasswordRepository resetPasswordRepository;

    @Autowired
    protected UserService userSevice;

    @Autowired
    protected EmailService emailService;

    @RequestMapping("/ct")
    public ResponseEntity<?> checkToken(@RequestParam String id) {
        ResetPassword resetPassword=resetPasswordRepository.findOne(id);
        return this.validateRequest(resetPassword);
    }

    @RequestMapping(value="/generate", method = RequestMethod.POST)
    public ResponseEntity<?> generate(@RequestParam(value="email") String email, HttpServletRequest request) {
        User user=userSevice.getUserByEmail(email);
        if(user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ResetPassword resetPassword=new ResetPassword();
        resetPassword.setEmail(email);
        resetPassword.setCreatedAt(new Date());
        resetPassword=this.resetPasswordRepository.save(resetPassword);
        String link=String.format(resetPasswordLink,request.getRequestURI(),resetPassword.getId());
        logger.info("Reset password link for user "+email+" : "+link);
        try {
            this.emailService.sendForgetPasswordMail(link,email);
        } catch (MessagingException e) {
            this.logger.error("Error while sending reset password mail",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordVO resetPasswordVO) {
        ResetPassword resetPassword=resetPasswordRepository.findOne(resetPasswordVO.getToken());
        ResponseEntity<?> entity=this.validateRequest(resetPassword);
        if(entity.getStatusCodeValue() != 200)
            return entity;
        else{
            String encPasswd;
            try {
                encPasswd=CryptoUtils.encrypt(resetPasswordVO.getPassword());
            } catch (CryptoException e) {
                logger.error("Error in encrypting password.");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            User user=userSevice.getUserByEmail(resetPassword.getEmail());
            user.setPassword(encPasswd);
            try {
                userSevice.update(user);
            } catch (UserServiceException e) {
                logger.error("Error in updating user.");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private ResponseEntity<?> validateRequest(ResetPassword resetPassword){
        if(resetPassword == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if(resetPassword.getCreatedAt().getTime() + 1800000 > new Date().getTime())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(HttpStatus.OK);
    }*/
}