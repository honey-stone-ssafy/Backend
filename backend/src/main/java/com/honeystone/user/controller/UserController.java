package com.honeystone.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.user.model.dto.UserSignupRequest;
import com.honeystone.user.model.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	//회원가입
	@PostMapping("")
	public ResponseEntity<?> signupUser(@RequestBody UserSignupRequest user) {
		userService.signupUser(user);
		return ResponseEntity.ok(HttpStatus.CREATED);
	}
	
	//이메일 인증코드 전송
	@PostMapping("/email-verification")
	public ResponseEntity<?> sendVerificationCode() {
		return null;
	}
	
	//이메일 인증코드 확인
	@PostMapping("/email-verification/comfirm")
	public ResponseEntity<?> confirmEmailCode() {
		return null;
	}
}
