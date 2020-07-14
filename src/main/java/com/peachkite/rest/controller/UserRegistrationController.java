package com.peachkite.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peachkite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peachkite.db.domain.User;
import com.peachkite.exception.UserServiceException;
import com.peachkite.service.UserRegistrationService;

@RestController
@RequestMapping("/api/public/register")
public class UserRegistrationController {
	
	@Autowired
	UserRegistrationService registrationService;

	@Autowired
	UserService userService;

	@RequestMapping("")
	public ResponseEntity<?> register(HttpServletRequest request,HttpServletResponse response,@RequestBody User user){
		try {
			if(userService.getUserByEmail(user.getEmail()) != null)
				return new ResponseEntity<Object>(HttpStatus.CONFLICT);
			registrationService.register(request, response, user);
		} catch (UserServiceException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(userService.getLoggedInUser(),HttpStatus.CREATED);
	}
	
}
