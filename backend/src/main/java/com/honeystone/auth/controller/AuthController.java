package com.honeystone.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.auth.model.service.AuthService;
import com.honeystone.common.dto.auth.RefreshToken;
import com.honeystone.common.dto.auth.TokenRefreshRequest;
import com.honeystone.common.dto.user.UserLoginRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "인증 관련 API입니다.")
public class AuthController {

	private final AuthService authService;
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
    
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 JWT 토큰을 발급받습니다.")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request) {
        return authService.login(request);
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 통해 액세스 토큰을 새로 발급받습니다.")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
    	return authService.refreshAccessToken(request);
    }
    
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "리프레시 토큰을 삭제하여 세션을 무효화합니다.")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return authService.logout(request);
    }

}
