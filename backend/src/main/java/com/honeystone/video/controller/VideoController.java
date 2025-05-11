package com.honeystone.video.controller;

import java.io.IOException;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.honeystone.common.dto.video.Video;
import com.honeystone.video.model.service.VideoService;
import org.springframework.web.multipart.MultipartFile;

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

	// todo: 스웨거 작성
	@PostMapping(value = "", consumes = MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> createVideo(@RequestPart Video video, @RequestPart MultipartFile file) throws IOException {
		// todo: 인증인가 구현되면 사용자 검증해야 함. (userId 받기)
		System.out.println(video);
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
