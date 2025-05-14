package com.honeystone.user.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.honeystone.common.dto.user.UserSignupRequest;
import com.honeystone.common.util.FileRemove;
import com.honeystone.common.util.FileUpload;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.user.model.dao.UserDao;

@Service
public class UserServiceImpl implements UserService{
	
	private final UserDao userDao;
	private final FileUpload fileUpload;
	private final FileRemove fileRemove;
	private final PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserDao userDao, FileUpload fileUpload, FileRemove fileRemove, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.fileUpload = fileUpload;
		this.fileRemove = fileRemove;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void signupUser(UserSignupRequest user) throws ServerException {
		String encoded = passwordEncoder.encode(user.getPassword());
		user.setPassword(encoded); //비밀번호 암호화
		
		if (!confirmNickname(user.getNickname())) {
			throw new BusinessException("이미 존재하는 닉네임입니다");
		}
		userDao.createUser(user);
	}

	@Override
	public Boolean confirmNickname(String nickname) {
		return userDao.countByNickname(nickname) == 0;
	}

}
