package com.honeystone.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.common.dto.user.GetUser;
import com.honeystone.common.dto.user.User;
import com.honeystone.common.dto.user.UserSignupRequest;
import com.honeystone.common.security.MyUserPrincipal;
import com.honeystone.user.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
	@Operation(summary = "회원가입", description = """
        회원 정보를 JSON 형식으로 전송하여 새로운 계정을 생성합니다.  
        이메일, 닉네임, 비밀번호는 유효성 검사를 통과해야 하며, 중복된 닉네임은 허용되지 않습니다.
    """,
			responses = {
					@ApiResponse(responseCode = "201", description = "회원가입 성공"),
					@ApiResponse(responseCode = "400", description = "입력값 오류"),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류")
			})
	public ResponseEntity<Void> signupUser(@Valid @RequestBody UserSignupRequest user) {
		userService.signupUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	//이메일 중복 확인
		@GetMapping("/confirm/email")
		@Operation(summary = "이메일 중복 확인", description = """
	        이미 가입한 이메일인지 확인합니다.  
	        가입하지 않은 경우 true, 가입한 경우 false를 반환합니다.
	    """,
				responses = {
						@ApiResponse(responseCode = "200", description = "중복 확인 성공"),
						@ApiResponse(responseCode = "400", description = "잘못된 요청")
				})

		public ResponseEntity<Boolean> confirmEmail(@RequestParam String email) {
			boolean isAvailable = userService.confirmEmail(email);
			return ResponseEntity.ok(isAvailable);
		}
	
	
	//닉네임 중복 확인
	@GetMapping("/confirm/nickname")
	@Operation(summary = "닉네임 중복 확인", description = """
        닉네임이 이미 사용 중인지 확인합니다.  
        중복되지 않은 경우 true, 중복된 경우 false를 반환합니다.
    """,
			responses = {
					@ApiResponse(responseCode = "200", description = "중복 확인 성공"),
					@ApiResponse(responseCode = "400", description = "잘못된 요청")
			})

	public ResponseEntity<Boolean> confirmNickname(@RequestParam String nickname) {
		boolean isAvailable = userService.confirmNickname(nickname);
		return ResponseEntity.ok(isAvailable);
	}
	
	//이메일 인증코드 전송
	@PostMapping("/email-verification")
	@Operation(summary = "이메일 인증코드 전송", description = """
        입력한 이메일 주소로 인증 코드를 전송합니다.  
        해당 이메일은 실제 존재하고 사용 가능한 주소여야 합니다.
    """,
			responses = {
					@ApiResponse(responseCode = "200", description = "인증 코드 전송 성공"),
					@ApiResponse(responseCode = "400", description = "잘못된 요청")
			})

	public ResponseEntity<?> sendVerificationCode() {
		return null;
	}
	
	//이메일 인증코드 확인
	@PostMapping("/email-verification/comfirm")
	@Operation(summary = "이메일 인증코드 확인", description = """
        입력한 인증코드가 해당 이메일로 발송된 코드와 일치하는지 확인합니다.
    """,
			responses = {
					@ApiResponse(responseCode = "200", description = "인증 성공"),
					@ApiResponse(responseCode = "400", description = "인증 실패 또는 만료")
			})

	public ResponseEntity<?> confirmEmailCode() {
		return null;
	}
	
	//닉네임으로 유저 검색
	@GetMapping("")
	@Operation(summary = "닉네임으로 유저 검색", description = """
        입력한 닉네임이 포함된 유저들을 조회합니다.  
        닉네임이 빈 문자열일 경우 전체 유저 목록을 반환합니다.
    """,
    		security = @SecurityRequirement(name = "bearerAuth"),
			responses = {
					@ApiResponse(responseCode = "200", description = "유저 목록 조회 성공"),
					@ApiResponse(responseCode = "400", description = "잘못된 요청")
			})

	public ResponseEntity<List<GetUser>> searchUsers(@AuthenticationPrincipal MyUserPrincipal requestUser, @RequestParam(required = false, defaultValue = "") String nickname) {
	    List<GetUser> users = userService.searchUsersByNickname(requestUser, nickname);
		return new ResponseEntity<List<GetUser>>(users, HttpStatus.OK);
	}
}
