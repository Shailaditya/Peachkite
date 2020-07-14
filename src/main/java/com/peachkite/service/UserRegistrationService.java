package com.peachkite.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peachkite.db.domain.User;
import com.peachkite.exception.UserServiceException;

public interface UserRegistrationService {
	
	public void register(HttpServletRequest request,HttpServletResponse response,User user) throws UserServiceException ;
	
}
