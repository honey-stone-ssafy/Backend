package com.honeystone.video.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.honeystone.video.model.dto.Video;

@Mapper
public interface VideoDao {

	public List<Video> selectAll();
	

}
