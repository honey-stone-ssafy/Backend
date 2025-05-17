package com.honeystone.user.model.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.honeystone.common.dto.user.User;
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

	public void signupUser(UserSignupRequest user) throws ServerException {
		//이메일 중복 확인
		if (userDao.countByEmail(user.getEmail()) > 0) {
			throw new BusinessException("이미 가입한 이메일입니다.");
		}
		
		//닉네임 중복 확인
		if (!confirmNickname(user.getNickname())) {
			throw new BusinessException("이미 존재하는 닉네임입니다.");
		}
		
		//비밀번호 암호화
		String encoded = passwordEncoder.encode(user.getPassword());
		
		//유저 생성
		UserSignupRequest newUser = UserSignupRequest.builder()
				.email(user.getEmail())
				.nickname(user.getNickname())
				.password(encoded)
				.build();
		
		try {			
			userDao.createUser(newUser);
		} catch (DataAccessException e) {
			throw new ServerException("회원가입 중 DB 오류가 발생했습니다.", e);
		}
	}

	@Override
	public Boolean confirmNickname(String nickname) throws ServerException {
		if (userDao.countByNickname(nickname) != 0) {
			throw new BusinessException("이미 존재하는 닉네임입니다.");
		}
		return true;
	}

	@Override
	public List<User> searchUsersByNickname(String nickname) throws ServerException {
		return userDao.searchByNickname(nickname);
	}
	
}
