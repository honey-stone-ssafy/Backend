package com.honeystone.board.controller;

import java.io.IOException;

import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.searchCondition.SearchBoardCondition;
import com.honeystone.common.dto.user.User;
import com.honeystone.common.security.MyUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.honeystone.common.dto.board.Board;
import com.honeystone.board.model.service.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.honeystone.common.dto.error.ApiError;
import jakarta.validation.Valid;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/boards")
@Tag(name= "Board API", description = "게시물 관련 API 입니다.")
public class BoardController {

	private final BoardService boardService;
	public BoardController(BoardService boardService) {

		this.boardService = boardService;
	}
	@Operation(summary = "게시글 목록 조회", description = """
			    게시글 목록을 조건에 따라 필터링 및 정렬하여 조회합니다.

			    사용자는 다음 조건들을 조합하여 요청할 수 있습니다:
			    - `keyword`: 제목 또는 내용에서 포함되는 문자열 검색
			    - `locations`: 지점 필터 (복수 선택 가능, 예: GANGNAM, HONGDAE 등)
			    - `levels`: 난이도 필터 (복수 선택 가능, 예: RED, BLUE 등)
			    - `skills`: 기술명 필터 (복수 선택 가능, 예: DYNO, PINCH 등)
			    - `orderBy`: 정렬 기준 (created_at: 최신순, favorites: 찜 많은 순)

			    ⚠️ 한 필터에 여러 개의 데이터를 넣고 싶은 경우 배열 형식이 아닌 **반복된 파라미터로 입력해야 합니다.**
			    예시: `?levels=RED&levels=BLUE`

			    페이징 관련 파라미터:
			    - `page`: 페이지 번호 (0부터 시작, 기본값: 0)
			    - `size`: 한 페이지당 게시글 수 (기본값: 12)

			    조건이 없을 경우 전체 게시글을 최신순으로 반환하며,
			    조건이 일부만 주어질 경우 해당 조건에 해당하는 게시글만 필터링됩니다.

			    결과가 없을 경우 204(No Content)를 반환합니다.
			""", responses = { @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공"),
		@ApiResponse(responseCode = "204", description = "조건에 해당하는 게시글이 없음"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@GetMapping("")
	public ResponseEntity<?> getBoardList(@ParameterObject @ModelAttribute SearchBoardCondition search,
										  @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
		// 페이지네이션
		Pageable pageable = PageRequest.of(page, size);

		Page<GetBoard> list = boardService.getBoardList(search, pageable);

		if (list == null || list.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<Page<GetBoard>>(list, HttpStatus.OK);
	}



	@Operation(summary = "게시글 상세 조회", description = """
			PathVariable로 지정된 게시글 ID의 내용을 조회합니다.
		""",
		responses = {
			@ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ApiError.class)))
		}
	)
	@GetMapping("/{id}")
	public ResponseEntity<GetBoard> getBoard(@PathVariable("id") Long id){
		GetBoard board = boardService.getBoard(id);
		return new ResponseEntity<GetBoard>(board, HttpStatus.OK);
	}

	@Operation(
		summary = "게시글 업로드",
		description = """
            Board DTO와 첨부 파일을 multipart/form-data로 전송합니다.  
            skill 필드는 여러 개 선택 시 Shift 혹은 Ctrl 키를 이용하세요.  
            게시물 인덱스, 생성 및 수정 날짜는 빈 값(empty)으로 보내주세요.

            🔐 **인증 필요**  
            요청 시 Authorization 헤더에 JWT 토큰을 `Bearer {token}` 형식으로 포함해야 합니다.
        """,
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(responseCode = "201", description = "게시글 업로드 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ApiError.class)))
		}
	)
	@PostMapping(value = "", consumes = MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> createBoard(
		@AuthenticationPrincipal MyUserPrincipal user,
		@Parameter(description = "비디오 정보와 첨부 파일", schema = @Schema(implementation = Board.class))
		@Valid @ModelAttribute Board board
	) throws IOException {
		boardService.createBoard(user.getEmail(), board, board.getFile());
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@Operation(summary = "게시글 수정", description = """
      		PathVariable로 지정된 게시글 ID의 내용을 수정합니다.
      		수정 가능한 필드: title, description, level, skill
      		※ 요청 바디에 포함된 값만 변경되고, 나머지는 그대로 유지됩니다.
      		게시물 인덱스, 생성 및 수정 날짜는 empty value로 보내주세요.

		    🔐 **인증 필요** \s
		    요청 시 Authorization 헤더에 JWT 토큰을 `Bearer {token}` 형식으로 포함해야 합니다.
		""",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
			@ApiResponse(
				responseCode = "400",
				description  = "잘못된 요청",
				content      = @Content(schema = @Schema(implementation = ApiError.class))
			),
			@ApiResponse(
				responseCode = "404",
				description  = "게시글을 찾을 수 없음",
				content      = @Content(schema = @Schema(implementation = ApiError.class))
			),
			@ApiResponse(
				responseCode = "500",
				description  = "서버 내부 오류",
				content      = @Content(schema = @Schema(implementation = ApiError.class))
			)
		}
	)
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateBoard(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("id") Long id, @RequestBody Board board){
		boardService.updateBoard(user.getEmail(), id, board);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "게시글 삭제", description = """
      		PathVariable로 지정된 게시글 ID의 내용을 삭제합니다.
      		
            🔐 **인증 필요**  
            요청 시 Authorization 헤더에 JWT 토큰을 `Bearer {token}` 형식으로 포함해야 합니다.
        """,
		security = @SecurityRequirement(name = "bearerAuth"),
		responses   = {
			@ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
			@ApiResponse(
				responseCode = "400",
				description  = "잘못된 요청",
				content      = @Content(schema = @Schema(implementation = ApiError.class))
			),
			@ApiResponse(
				responseCode = "404",
				description  = "게시글을 찾을 수 없음",
				content      = @Content(schema = @Schema(implementation = ApiError.class))
			),
			@ApiResponse(
				responseCode = "500",
				description  = "서버 내부 오류",
				content      = @Content(schema = @Schema(implementation = ApiError.class))
			)
		}
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBoard(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("id") Long id){
		boardService.deleteBoard(user.getEmail(), id);
		return new ResponseEntity<>(HttpStatus.OK);
	}



}
