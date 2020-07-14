package com.peachkite.rest.controller.secure;

import com.peachkite.service.EmailService;
import com.peachkite.service.UserService;
import com.peachkite.vo.QueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/query")
public class QueryController {

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @RequestMapping(value="",method= RequestMethod.POST)
    ResponseEntity<?> submitQuery(@RequestBody QueryVO query){
        if(query.getEmail() == null && userService.getLoggedInUser() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if(query.getEmail() == null)
            query.setEmail(userService.getLoggedInUser().getEmail());
        try{
            emailService.sendQueryMail(query);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

