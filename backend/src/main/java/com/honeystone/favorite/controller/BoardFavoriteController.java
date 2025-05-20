package com.honeystone.favorite.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.error.ApiError;
import com.honeystone.common.security.MyUserPrincipal;
import com.honeystone.favorite.model.service.BoardFavoriteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users/{userId}/favorites")
@Tag(name= "Favorite API", description = "찜 기능 관련 API 입니다.")
public class BoardFavoriteController {
	
	private final BoardFavoriteService favoriteService;
	public BoardFavoriteController(BoardFavoriteService favoriteService) {
		this.favoriteService = favoriteService;
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
	@GetMapping("")
	public ResponseEntity<?> getFavoriteBoardList(@PathVariable("userId") Long userId,
			  @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
		
		Pageable pageable = PageRequest.of(page, size);
		Page<GetBoard> list = favoriteService.getFavoriteBoardList(userId, pageable);
		
		if (list == null || list.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Page<GetBoard>>(list, HttpStatus.OK);
	}
	
	@PostMapping("/{boardId}")
    @Operation(summary = "찜 추가", description = "게시글을 찜 목록에 추가합니다.",
    		security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "찜 추가 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ApiError.class)))
            })
    public ResponseEntity<?> addFavorite(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("userId") Long userId, @PathVariable("boardId") Long boardId) {
		favoriteService.addFavorite(user.getEmail(), userId, boardId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
	
	@DeleteMapping("/{boardId}")
    @Operation(summary = "찜 삭제", description = "게시글을 찜 목록에서 제거합니다.",
    		security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "찜 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ApiError.class)))
            })
    public ResponseEntity<?> removeFavorite(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("userId") Long userId, @PathVariable("boardId") Long boardId) {
        favoriteService.removeFavorite(user.getEmail(), userId, boardId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
	
}
