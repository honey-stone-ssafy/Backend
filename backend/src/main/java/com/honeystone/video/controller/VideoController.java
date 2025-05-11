package com.honeystone.video.controller;

import java.io.IOException;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.honeystone.common.dto.video.Video;
import com.honeystone.video.model.service.VideoService;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Content;
import com.honeystone.common.dto.ApiError;
import com.honeystone.exception.ServerException;
import org.springframework.dao.DataAccessException;
import jakarta.validation.Valid;
import org.springframework.validation.BindException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/videos")
@Tag(name= "Video API", description = "비디오 게시판 관련 API 입니다.")
public class VideoController {
	
	private final VideoService videoService;
	private final ObjectMapper objectMapper;

	public VideoController(VideoService videoService, ObjectMapper objectMapper) {
		this.videoService = videoService;
		this.objectMapper = objectMapper;
	}

	@GetMapping("")
	public ResponseEntity<?> getVideoList() {
		List<Video> list = videoService.getVideoList();

		if(list == null || list.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Video>>(list,HttpStatus.OK);
	}

	@Operation(
		summary = "게시글 업로드",
		description = "Video DTO(JSON)와 첨부 파일(Binary)을 multipart/form-data 로 전송합니다. skill 필드는 배열 형태로 전송해야 합니다. (예: [\"PINCH\", \"CRIMP\"])",
		responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "201",
				description = "비디오 업로드 성공",
				content = @Content
			),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "400",
				description = "잘못된 요청 (예: 잘못된 형식의 JSON, 필수 필드 누락 등)",
				content = @Content(schema = @Schema(implementation = ApiError.class))
			),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "500",
				description = "서버 내부 오류 (예: 파일 업로드 실패, DB 오류 등)",
				content = @Content(schema = @Schema(implementation = ApiError.class))
			)
		}
	)
	@PostMapping(value = "", consumes = MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> createVideo(
		@Parameter(description = "비디오 정보 (skill 필드는 배열 형태로 전송)", schema = @Schema(implementation = Video.class)) 
		@Valid @RequestPart(value = "video", required = true) Video video,
		@Parameter(description = "첨부 파일", schema = @Schema(type = "string", format = "binary")) 
		@RequestPart(value = "file", required = true) MultipartFile file
	) throws IOException {
		// todo: 인증인가 구현되면 사용자 검증해야 함. (userId 받기)
		videoService.createVideo(video, file);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

//	@PatchMapping("/{id}")
//	public ResponseEntity<Void> updateVideo(@PathVariable("id") int id, @ModelAttribute Video video){
//		video.setId(id);
//		videoService.updateVideo(video);
//		return new ResponseEntity<>(HttpStatus.OK);
//	}

}
