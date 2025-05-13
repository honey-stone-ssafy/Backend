package com.honeystone.user.model.service;

import com.honeystone.user.model.dto.UserSignupRequest;

public interface UserService {
	
	void signupUser(UserSignupRequest user);
}
