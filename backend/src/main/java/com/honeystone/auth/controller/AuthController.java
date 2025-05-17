package com.honeystone.auth.controller;

import com.honeystone.common.dto.user.UserLoginResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
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
    @Operation(summary = "로그인", description = """
        이메일과 비밀번호를 입력받아 인증을 수행하고,  
        Access Token과 Refresh Token을 발급합니다.
    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 및 토큰 발급 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })

    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "액세스 토큰 재발급", description = """
        HTTP Header에 포함된 Refresh Token을 검증하여  
        새로운 Access Token을 발급합니다.
    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "액세스 토큰 재발급 성공"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않거나 만료된 리프레시 토큰"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })

    public ResponseEntity<UserLoginResponse> refreshAccessToken(HttpServletRequest request) {
        UserLoginResponse response = authService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = """
        HTTP Header에 포함된 Refresh Token을 삭제하여 로그아웃 처리합니다.  
        토큰이 삭제되면 더 이상 재발급 요청이 불가능합니다.
    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
                    @ApiResponse(responseCode = "400", description = "이미 삭제되었거나 존재하지 않는 토큰"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })

    public ResponseEntity<String> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

}
