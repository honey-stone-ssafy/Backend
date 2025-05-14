package com.honeystone.user.model.service;

import com.honeystone.common.dto.user.UserSignupRequest;

public interface UserService {
	
	void signupUser(UserSignupRequest user);

	Boolean confirmNickname(String nickname);
}
