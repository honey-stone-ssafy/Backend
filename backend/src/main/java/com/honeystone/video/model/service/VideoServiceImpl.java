package com.honeystone.video.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.honeystone.video.model.dao.VideoDao;
import com.honeystone.video.model.dto.Video;


@Service
public class VideoServiceImpl implements VideoService {

	private final VideoDao videoDao;
	
	public VideoServiceImpl(VideoDao videoDao) {
		this.videoDao = videoDao;
	}
	
	@Override
	public List<Video> getVideoList() {
		System.out.println("게시글 전체 목록");
		return videoDao.selectAll();
	}
}
