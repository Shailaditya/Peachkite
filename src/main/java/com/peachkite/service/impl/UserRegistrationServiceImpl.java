package com.peachkite.service.impl;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peachkite.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Service;
import com.peachkite.exception.CryptoException;
import com.peachkite.db.domain.User;
import com.peachkite.exception.UserServiceException;
import com.peachkite.service.UserRegistrationService;
import com.peachkite.service.UserService;
import com.peachkite.util.CryptoUtils;

import java.util.Arrays;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService{
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	TokenBasedRememberMeServices rememberMeService;

	@Autowired
	EmailService emailService;

	@Override
	public void register(HttpServletRequest request,HttpServletResponse response,User user) throws UserServiceException {
		try {
			user.setPassword(CryptoUtils.encrypt(user.getPassword()));
			user.setRoles(Arrays.asList(User.ROLE_USER));
			user=userService.create(user);
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(),CryptoUtils.decrypt(user.getPassword()));
			Authentication authentication=authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			rememberMeService.loginSuccess(request, response, authentication);
			emailService.sendRegistrationMail(user.getEmail());
		}catch(Exception e) {
			throw new UserServiceException("Error in user registration : ",e);
		}
	}

}
