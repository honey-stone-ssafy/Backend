package com.honeystone.user.model.service;

import java.io.IOException;
import java.util.List;

import com.honeystone.auth.model.dao.RefreshTokenDao;
import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.user.UserFile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
	private final RefreshTokenDao refreshTokenDao;
	
	public UserServiceImpl(UserDao userDao, FileUpload fileUpload, FileRemove fileRemove, PasswordEncoder passwordEncoder, RefreshTokenDao refreshTokenDao) {
		this.userDao = userDao;
		this.fileUpload = fileUpload;
		this.fileRemove = fileRemove;
		this.passwordEncoder = passwordEncoder;
		this.refreshTokenDao = refreshTokenDao;
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
	public GetUser updateUserProfile(MyUserPrincipal user, Long userId, String nickname, String description, MultipartFile file) throws ServerException {
		if (user == null || user.getId() != userId) {
			throw new BusinessException("접근할 권한이 없는 유저입니다.");
		}

		if (userDao.existsById(userId) == 0) {
			throw new BusinessException("존재하지 않는 유저입니다.");
		}

		// 닉네임 중복 검사 (현재 닉네임과 다를 때만 검사)
		String currentNickname = userDao.findNicknameByUserId(userId);
		if (!nickname.equals(currentNickname)) {
			if (userDao.countByNickname(nickname) > 0) {
				throw new BusinessException("이미 존재하는 닉네임입니다.");
			}
		}

		// 닉네임/소개글 업데이트
		try {
			userDao.updateNicknameAndDescription(userId, nickname, description);
		} catch (DataAccessException e) {
			throw new ServerException("닉네임/소개글 수정 중 DB 오류가 발생했습니다.", e);
		}

		// 파일 유효성 검사
//		if (file == null || file.isEmpty()) {
//			throw new BusinessException("파일이 비어 있습니다.");
//		}

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

		try {
			userDao.createUserFile(newFile);
			userDao.updateProfileImage(userId, fileUrl);
		} catch (DataAccessException e) {
			throw new ServerException("프로필 이미지 저장 중 DB 오류가 발생했습니다.", e);
		}
		return userDao.searchByNickname(userId, nickname).get(0);
	}

	@Override
	public void deleteUser(MyUserPrincipal user, Long userId) {
		if (user == null || user.getId() != userId) {
			throw new BusinessException("접근할 권한이 없는 유저입니다.");
		}

		// 1. 유저 존재 확인
		if (userDao.existsById(userId) == 0) {
			throw new BusinessException("존재하지 않는 유저입니다.");
		}

		// 2. 기존 프로필 이미지 삭제
		UserFile userFile = userDao.findUserFileByUserId(userId);
		if (userFile != null) {
			try {
				userDao.deleteUserFileByUserId(userId);
				fileRemove.removeUserProfileFile(userFile.getUrl());
			} catch (DataAccessException e) {
				throw new ServerException("프로필 이미지 삭제 중 오류가 발생했습니다.", e);
			}
		}

		// 3. users 테이블 논리 삭제
		try {
			userDao.deleteUser(userId);
		} catch (DataAccessException e) {
			throw new ServerException("회원 탈퇴 처리 중 DB 오류가 발생했습니다.", e);
		}

		// 4. 해당 유저의 리프레시 토큰 삭제
		try {
			refreshTokenDao.deleteByUserId(userId);
		} catch (DataAccessException e) {
			throw new ServerException("리프레시 토큰 삭제 중 오류가 발생했습니다.", e);
		}
	}

	@Override
	public void verifyPassword(MyUserPrincipal user, Long userId, String password) {
		if (user == null || user.getId() != userId) {
			throw new BusinessException("접근할 권한이 없는 유저입니다.");
		}

		if (userDao.existsById(userId) == 0) {
			throw new BusinessException("존재하지 않는 유저입니다.");
		}

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new BusinessException("비밀번호가 일치하지 않습니다.");
		}
	}

	@Override
	public void changePassword(MyUserPrincipal user, Long userId, String newPassword) {
		if (user == null || user.getId() != userId) {
			throw new BusinessException("접근할 권한이 없는 유저입니다.");
		}

		if (userDao.existsById(userId) == 0) {
			throw new BusinessException("존재하지 않는 유저입니다.");
		}

		String encodedNewPassword = passwordEncoder.encode(newPassword);
		userDao.updatePassword(userId, encodedNewPassword);
	}

	@Override
	public Page<GetBoard> getUserBoardList(Long userId, Pageable pageable) {
		long total = userDao.countBoards(userId);
		int offset = pageable.getPageNumber() * pageable.getPageSize();
		int size = pageable.getPageSize();

		List<GetBoard> boards = userDao.getBoardList(userId, offset, size);
		return new PageImpl<>(boards, pageable, total);
	}

}
