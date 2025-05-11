package com.honeystone.video.controller;

import java.io.IOException;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.honeystone.common.dto.video.Video;
import com.honeystone.video.model.service.VideoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.honeystone.common.dto.ApiError;
import jakarta.validation.Valid;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/videos")
@Tag(name= "Video API", description = "비디오 게시판 관련 API 입니다.")
public class VideoController {
	
	private final VideoService videoService;
	public VideoController(VideoService videoService) {

		this.videoService = videoService;
	}
	// todo: 스웨거 작성
	@GetMapping("")
	public ResponseEntity<?> getVideoList() {
		// todo: 필터링 작업 필요
		List<Video> list = videoService.getVideoList();

		if(list == null || list.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Video>>(list,HttpStatus.OK);
	}

	@Operation(summary = "게시글 업로드", description = """
			Video DTO와 첨부 파일을 multipart/form-data로 전송합니다. skill 필드는 여러 개 선택 시 Shift 혹은 ctrl 이용하면 됩니다. 게시물 인덱스는 empty value로 보내주세요.
			""",
		responses = {
			@ApiResponse(responseCode = "201", description = "비디오 업로드 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ApiError.class)))
		}
	)
	@PostMapping(value = "", consumes = MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> createVideo(
		@Parameter(description = "비디오 정보와 첨부 파일", schema = @Schema(implementation = Video.class))
		@Valid @ModelAttribute Video video
	) throws IOException {
		// todo: 인증인가 구현되면 사용자 검증해야 함. (userId 받기)
		videoService.createVideo(video, video.getFile());
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	//todo: 스웨거
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateVideo(@PathVariable("id") Long id, @RequestBody Video video){
		videoService.updateVideo(id, video);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
