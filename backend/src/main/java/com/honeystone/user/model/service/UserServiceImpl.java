package com.honeystone.user.model.service;

import org.springframework.stereotype.Service;

import com.honeystone.common.util.FileRemove;
import com.honeystone.common.util.FileUpload;
import com.honeystone.user.model.dao.UserDao;
import com.honeystone.user.model.dto.UserSignupRequest;
import com.honeystone.video.model.dao.VideoDao;

@Service
public class UserServiceImpl implements UserService{
	
	private final UserDao userDao;
	private final FileUpload fileUpload;
	private final FileRemove fileRemove;
	
	public UserServiceImpl(UserDao userDao, FileUpload fileUpload, FileRemove fileRemove) {
		this.userDao = userDao;
		this.fileUpload = fileUpload;
		this.fileRemove = fileRemove;
	}

	@Override
	public void signupUser(UserSignupRequest user) {
		//비밀번호 암호화
		userDao.createUser(user);
	}

}
