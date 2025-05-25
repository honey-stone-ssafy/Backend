package com.honeystone.user.model.service;

import java.io.IOException;
import java.util.List;

import com.honeystone.common.dto.user.UserFile;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.honeystone.common.dto.user.GetUser;
import com.honeystone.common.dto.user.UserSignupRequest;
import com.honeystone.common.security.MyUserPrincipal;
import com.honeystone.common.util.FileRemove;
import com.honeystone.common.util.FileUpload;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.user.model.dao.UserDao;
import org.springframework.web.multipart.MultipartFile;

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
	public Boolean confirmEmail(String email) throws ServerException {
		if (userDao.countByEmail(email) != 0) {
			throw new BusinessException("이미 가입한 이메일입니다.");
		}
		return true;
	}

	@Override
	public List<GetUser> searchUsersByNickname(MyUserPrincipal requestUser, String nickname) throws ServerException {
		return userDao.searchByNickname(requestUser == null ? -1 : requestUser.getId(), nickname);
	}

	@Override
	public void updateUserProfileImage(Long userId, MultipartFile file) throws ServerException {
		if (file == null || file.isEmpty()) {
			throw new BusinessException("파일이 비어 있습니다.");
		}

		// 기존 파일 조회
		UserFile oldFile = userDao.findUserFileByUserId(userId);

		// 기존 파일 삭제
		if (oldFile != null) {
			userDao.deleteUserFileByUserId(userId);
			fileRemove.removeUserProfileFile(oldFile.getUrl());
		}

		// 새 파일 업로드
		String filename = fileUpload.generateFileName("user", userId, file);
		String fileUrl;
		try {
			fileUrl = fileUpload.uploadFile(file, filename, "users");
		} catch (IOException e) {
			throw new ServerException("S3 업로드 실패", e);
		}

		UserFile newFile = UserFile.builder()
				.userId(userId)
				.filename(filename)
				.url(fileUrl)
				.build();

		userDao.createUserFile(newFile);
		userDao.updateProfileImage(userId, fileUrl);
	}

}
