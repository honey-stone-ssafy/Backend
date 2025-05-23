package com.honeystone.user.model.service;

import java.util.List;

import com.honeystone.common.dto.user.GetUser;
import com.honeystone.common.dto.user.UserSignupRequest;
import com.honeystone.common.security.MyUserPrincipal;

public interface UserService {
	
	void signupUser(UserSignupRequest user);

	Boolean confirmNickname(String nickname);
	
	Boolean confirmEmail(String email);
	
	List<GetUser> searchUsersByNickname(MyUserPrincipal requestUser, String nickname);
}
