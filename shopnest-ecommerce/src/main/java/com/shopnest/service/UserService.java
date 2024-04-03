package com.shopnest.service;

import com.shopnest.exception.UserException;
import com.shopnest.modal.User;

public interface UserService {
	
	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
}
