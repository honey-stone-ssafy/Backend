package com.honeystone.video.model.service;

import java.io.IOException;
import java.util.List;

import com.honeystone.common.dto.video.GetVideo;
import com.honeystone.common.dto.video.Video;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
	public List<Video> getVideoList();

	public void createVideo(Video video, MultipartFile file) throws IOException;

	public void updateVideo(Long id, Video video);

	public void deleteVideo(Long id);

	public GetVideo getVideo(Long id);
}
