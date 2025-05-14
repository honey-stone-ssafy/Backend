package com.honeystone.board.controller;

import java.io.IOException;
import java.util.List;

import com.honeystone.common.dto.board.GetBoard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.honeystone.common.dto.board.Board;
import com.honeystone.board.model.service.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.honeystone.common.dto.ApiError;
import jakarta.validation.Valid;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/boards")
@Tag(name= "Board API", description = "비디오 게시판 관련 API 입니다.")
public class BoardController {
	
	private final BoardService boardService;
	public BoardController(BoardService boardService) {

		this.boardService = boardService;
	}
	// todo: 스웨거 작성
	@GetMapping("")
	public ResponseEntity<?> getBoardList() {
		// todo: 필터링 작업 필요
		List<Board> list = boardService.getBoardList();

		if(list == null || list.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Board>>(list,HttpStatus.OK);
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

	@Operation(summary = "게시글 업로드", description = """
			Board DTO와 첨부 파일을 multipart/form-data로 전송합니다. skill 필드는 여러 개 선택 시 Shift 혹은 ctrl 이용하면 됩니다.\s
			게시물 인덱스, 생성 및 수정 날짜는 empty value로 보내주세요.
		""",
		responses = {
			@ApiResponse(responseCode = "201", description = "비디오 업로드 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ApiError.class)))
		}
	)
	@PostMapping(value = "", consumes = MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> createBoard(
		@Parameter(description = "비디오 정보와 첨부 파일", schema = @Schema(implementation = Board.class))
		@Valid @ModelAttribute Board board
	) throws IOException {
		// todo: 인증인가 구현되면 사용자 검증해야 함. (userId 받기)
		boardService.createBoard(board, board.getFile());
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@Operation(summary = "게시글 수정", description = """
      		PathVariable로 지정된 게시글 ID의 내용을 수정합니다.
      		수정 가능한 필드: title, description, level, skill
      		※ 요청 바디에 포함된 값만 변경되고, 나머지는 그대로 유지됩니다. \s
      		게시물 인덱스, 생성 및 수정 날짜는 empty value로 보내주세요.
   	""",
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
	public ResponseEntity<Void> updateBoard(@PathVariable("id") Long id, @RequestBody Board board){
		boardService.updateBoard(id, board);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "게시글 삭제", description = """
      		PathVariable로 지정된 게시글 ID의 내용을 삭제합니다.
    """,
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
	public ResponseEntity<Void> deleteBoard(@PathVariable("id") Long id){
		boardService.deleteBoard(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}



}
