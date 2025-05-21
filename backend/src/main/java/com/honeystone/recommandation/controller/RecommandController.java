package com.honeystone.recommandation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.board.model.type.Level;
import com.honeystone.common.dto.error.ApiError;
import com.honeystone.recommandation.model.service.RecommandService;
import com.honeystone.recommandation.model.type.LocClass;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/recommandations")
@Tag(name = "Recommand API", description = "추천 관련 API 입니다.")
public class RecommandController {

	private final RecommandService recommandService;

	public RecommandController(RecommandService recommandService) {
		this.recommandService = recommandService;
	}

	@Operation(summary = "추천 게시글 목록 조회", description = """
				쿼리 파라미터로 전달된 level 값(WHITE, YELLOW, ORANGE 등)에 따라 추천 게시글 목록을 조회합니다.
			""", responses = {
			@ApiResponse(responseCode = "200", description = "추천 게시글 목록 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LocClass.class)))),
			@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@GetMapping("")
	public ResponseEntity<List<LocClass>> getRecommandationList(@RequestParam Level level) {
		List<LocClass> list = recommandService.getRecommandationList(level);

		return new ResponseEntity<List<LocClass>>(list, HttpStatus.OK);
	}
}
