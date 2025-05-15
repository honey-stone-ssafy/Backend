package com.honeystone.auth.model.service;

import org.springframework.http.ResponseEntity;

import com.honeystone.common.dto.user.UserLoginRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
	
	public ResponseEntity<?> login(UserLoginRequest request);
	
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request);
	
	public ResponseEntity<?> logout(HttpServletRequest request);
}
