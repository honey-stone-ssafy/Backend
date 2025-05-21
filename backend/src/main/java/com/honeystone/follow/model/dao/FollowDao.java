package com.honeystone.follow.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface FollowDao {

    public void insertFollow(@Param("userId") Long userId, @Param("followingUserId") Long followingUserId);

    public void deleteFollow(@Param("userId") Long userId, @Param("followingUserId") Long followingUserId);

    public int existsFollow(@Param("userId") Long userId, @Param("followingUserId") Long followingUserId);
}
