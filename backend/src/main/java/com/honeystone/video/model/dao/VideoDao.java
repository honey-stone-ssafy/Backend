package com.honeystone.video.model.dao;

import java.util.List;

import com.honeystone.common.dto.video.GetVideo;
import com.honeystone.common.dto.video.VideoFile;
import org.apache.ibatis.annotations.Mapper;

import com.honeystone.common.dto.video.Video;

@Mapper
public interface VideoDao {

	public List<Video> selectAll();

	public void createVideo(Video video);

	public void createFile(VideoFile file);

	public void updateVideo(Video video);

	public int existsById(Long id); // id로 게시물 조회

	public void deleteVideo(Long id);

	public void deleteFile(Long id);

	public GetVideo getVideo(Long id);
}
