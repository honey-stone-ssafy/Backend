package com.honeystone.user.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.error.ApiError;
import com.honeystone.common.dto.user.GetUser;
import com.honeystone.common.dto.user.User;
import com.honeystone.common.security.MyUserPrincipal;
import com.honeystone.favorite.model.service.BoardFavoriteService;
import com.honeystone.follow.model.service.FollowService;
import com.honeystone.user.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users/{userId}")
@Tag(name= "GetUser API", description = "유저 조회 관련 API 입니다.")
public class GetUserController {
	
	private final UserService userService;
	private final BoardFavoriteService favoriteService;
	private final FollowService followService;
	public GetUserController(UserService userService, BoardFavoriteService favoriteService, FollowService followService) {
		this.userService = userService;
		this.favoriteService = favoriteService;
		this.followService = followService;
	}
	
	@Operation(summary = "찜 목록 조회", description = """
		    찜한 전체 목록을 최신순으로 조회합니다.

		    페이징 관련 파라미터:
		    - `page`: 페이지 번호 (0부터 시작, 기본값: 0)
		    - `size`: 한 페이지당 게시글 수 (기본값: 12)

		    결과가 없을 경우 204(No Content)를 반환합니다.
		""",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = { @ApiResponse(responseCode = "200", description = "찜 목록 조회 성공"),
	@ApiResponse(responseCode = "204", description = "찜한 게시물이 없음"),
	@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
	@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@GetMapping("/favorites")
	public ResponseEntity<?> getFavoriteBoardList(@PathVariable("userId") Long userId,
			  @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
		
		Pageable pageable = PageRequest.of(page, size);
		Page<GetBoard> list = favoriteService.getFavoriteBoardList(userId, pageable);
		
		if (list == null || list.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Page<GetBoard>>(list, HttpStatus.OK);
	}
	
	@Operation(summary = "팔로잉 목록 조회", description = """
		    내가 팔로잉하고 있는 사용자의 전체 목록을 닉네임 오름차순으로 조회합니다.

		    페이징 관련 파라미터:
		    - `page`: 페이지 번호 (0부터 시작, 기본값: 0)
		    - `size`: 한 페이지당 게시글 수 (기본값: 12)

		    결과가 없을 경우 204(No Content)를 반환합니다.
		""",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = { @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공"),
	@ApiResponse(responseCode = "204", description = "팔로잉 중인 사용자가 없음"),
	@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
	@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@GetMapping("/following")
	public ResponseEntity<?> getFollowingList(@AuthenticationPrincipal MyUserPrincipal requestUser, @PathVariable("userId") Long userId,
			  @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
		
		Pageable pageable = PageRequest.of(page, size);
		Page<GetUser> list = followService.getFollowingList(requestUser, userId, pageable);
		
		if (list == null || list.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Page<GetUser>>(list, HttpStatus.OK);
	}
	
	@Operation(summary = "팔로워 목록 조회", description = """
		    나를 팔로우하고 있는 사용자의 전체 목록을 닉네임 오름차순으로 조회합니다.

		    페이징 관련 파라미터:
		    - `page`: 페이지 번호 (0부터 시작, 기본값: 0)
		    - `size`: 한 페이지당 게시글 수 (기본값: 12)

		    결과가 없을 경우 204(No Content)를 반환합니다.
		""",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = { @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공"),
	@ApiResponse(responseCode = "204", description = "팔로잉 중인 사용자가 없음"),
	@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
	@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@GetMapping("/followers")
	public ResponseEntity<?> getFollowerList(@AuthenticationPrincipal MyUserPrincipal requestUser, @PathVariable("userId") Long userId,
			  @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
		
		Pageable pageable = PageRequest.of(page, size);
		Page<GetUser> list = followService.getFollowerList(requestUser, userId, pageable);
		
		if (list == null || list.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Page<GetUser>>(list, HttpStatus.OK);
	}
}
