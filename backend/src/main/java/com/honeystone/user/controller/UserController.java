package com.honeystone.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.common.dto.user.UserSignupRequest;
import com.honeystone.user.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name= "User API", description = "유저 관련 API 입니다.")
public class UserController {
	
	private final UserService userService;
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	//회원가입
	@PostMapping("")
	@Operation(summary = "회원가입")
	public ResponseEntity<?> signupUser(@RequestBody UserSignupRequest user) {
		userService.signupUser(user);
		return ResponseEntity.ok(HttpStatus.CREATED);
	}
	
	//이메일 인증코드 전송
	@PostMapping("/email-verification")
	@Operation(summary = "이메일 인증코드 전송")
	public ResponseEntity<?> sendVerificationCode() {
		return null;
	}
	
	//이메일 인증코드 확인
	@PostMapping("/email-verification/comfirm")
	@Operation(summary = "이메일 인증코드 확인")
	public ResponseEntity<?> confirmEmailCode() {
		return null;
	}
}
