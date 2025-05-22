package com.honeystone.follow.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.honeystone.common.dto.user.GetUser;

@Mapper
public interface FollowDao {

    public void insertFollow(@Param("userId") Long userId, @Param("followingUserId") Long followingUserId);

    public void deleteFollow(@Param("userId") Long userId, @Param("followingUserId") Long followingUserId);

    public int existsFollow(@Param("userId") Long userId, @Param("followingUserId") Long followingUserId);

	public long countFollowing(@Param("userId") Long userId);
	
	public long countFollower(@Param("userId") Long userId);

	public List<GetUser> getFollowingList(@Param("requestUserId") Long requestUserId, @Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);
	
	public List<GetUser> getFollowerList(@Param("requestUserId") Long requestUserId, @Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);
}
