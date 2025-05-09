package com.honeystone.video.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.video.model.dto.Video;
import com.honeystone.video.model.service.VideoService;

@RestController
@RequestMapping("/api/videos")
public class VideoController {
	
	private final VideoService videoService;
	public VideoController(VideoService videoService) {
		this.videoService = videoService;
	}
	
	@GetMapping("")
	public String list() {
		List<Video> list = videoService.getVideoList();
		return "success";
	}

}
