package com.honeystone.user.model.service;

import java.util.List;

import com.honeystone.common.dto.user.User;
import com.honeystone.common.dto.user.UserSignupRequest;

public interface UserService {
	
	void signupUser(UserSignupRequest user);

	Boolean confirmNickname(String nickname);
	
	List<User> searchUsersByNickname(String nickname);
}
