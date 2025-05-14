package com.honeystone.user.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.honeystone.common.dto.user.User;
import com.honeystone.common.dto.user.UserSignupRequest;

@Mapper
public interface UserDao {

	void createUser(UserSignupRequest user);

	int countByNickname(String nickname);

	User findByEmail(@Param("email") String email);
	
}
