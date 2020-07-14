package com.peachkite.service;

import com.peachkite.db.domain.User;
import com.peachkite.exception.UserServiceException;
import com.peachkite.vo.UserTransfer;

public interface UserService {
	
	User create(User user);
	User update(User user) throws UserServiceException;
	User delete(String userId) throws UserServiceException;
	User get(String userId) throws UserServiceException;
	UserTransfer getLoggedInUser();
	User getUserByEmail(String emailId);
	
}
