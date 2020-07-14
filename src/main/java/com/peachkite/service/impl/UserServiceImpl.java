package com.peachkite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.peachkite.db.domain.User;
import com.peachkite.db.repository.UserRepository;
import com.peachkite.exception.UserNotFoundException;
import com.peachkite.exception.UserServiceException;
import com.peachkite.security.PeachkiteUserDetail;
import com.peachkite.service.UserService;
import com.peachkite.vo.UserTransfer;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public User create(User user) {
		return userRepository.save(user);
	}

	@Override
	public User update(User user) throws UserServiceException {
		if(user.getId() == null)
			throw new UserServiceException("User id cannot be null for existing user");
		if(userRepository.findOne(user.getId()) == null)
			throw new UserNotFoundException("User Not found with id "+user.getId());
		return userRepository.save(user);
	}

	@Override
	public User delete(String userId) throws UserServiceException {
		User user=userRepository.findOne(userId);
		if(user == null)
			throw new UserNotFoundException("User Not found with id "+userId);
		userRepository.delete(userId);
		return user;
	}

	@Override
	public User get(String userId) throws UserServiceException {
		User user=userRepository.findOne(userId);
		if(user == null)
			throw new UserNotFoundException("User Not found with id "+userId);
		return user;
	}
	
	@Override
	public UserTransfer getLoggedInUser(){
		if(SecurityContextHolder.getContext().getAuthentication() != null){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth.getPrincipal() instanceof PeachkiteUserDetail) {
				PeachkiteUserDetail userDetail=(PeachkiteUserDetail)auth.getPrincipal();
				return new UserTransfer(userDetail);
			}
		}
		return null;
	}

	@Override
	public User getUserByEmail(String emailId){
		return userRepository.findByEmail(emailId);
	}

}
