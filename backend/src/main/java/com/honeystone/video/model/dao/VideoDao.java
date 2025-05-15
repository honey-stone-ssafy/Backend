package com.honeystone.video.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.honeystone.common.dto.searchCondition.SearchBoardCondition;
import com.honeystone.common.dto.video.GetVideo;
import com.honeystone.common.dto.video.Video;
import com.honeystone.common.dto.video.VideoFile;

@Mapper
public interface VideoDao {

	public List<Video> selectAll(SearchBoardCondition search);

	public void createVideo(Video video);

	public void createFile(VideoFile file);

	public void updateVideo(Video video);

	public int existsById(Long id); // id로 게시물 조회

	public void deleteVideo(Long id);

	public void completeDeleteVideo(Long id);

	public void deleteFile(Long id);

	public GetVideo getVideo(Long id);

	public List<Video> findVideosToDelete();
}
