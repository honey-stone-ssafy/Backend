package com.honeystone.user.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.honeystone.common.dto.user.UserSignupRequest;

@Mapper
public interface UserDao {

	void createUser(UserSignupRequest user);
	
}
