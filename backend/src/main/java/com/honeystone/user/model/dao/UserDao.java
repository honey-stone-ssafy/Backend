package com.honeystone.user.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.honeystone.user.model.dto.UserSignupRequest;

@Mapper
public interface UserDao {

	void createUser(UserSignupRequest user);
	
}
