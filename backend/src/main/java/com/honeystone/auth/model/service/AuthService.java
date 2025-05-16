package com.honeystone.auth.model.service;

import com.honeystone.common.dto.user.UserLoginResponse;
import org.springframework.http.ResponseEntity;

import com.honeystone.common.dto.user.UserLoginRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
	
	public UserLoginResponse login(UserLoginRequest request);
	
	public UserLoginResponse refreshAccessToken(HttpServletRequest request);
	
	public void logout(HttpServletRequest request);
}
