package com.honeystone.user.model.service;

import java.util.List;

import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.user.GetUser;
import com.honeystone.common.dto.user.UserSignupRequest;
import com.honeystone.common.security.MyUserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
	
	void signupUser(UserSignupRequest user);

	Boolean confirmNickname(String nickname);
	
	Boolean confirmEmail(String email);
	
	Page<GetUser> searchUsersByNickname(MyUserPrincipal requestUser, String nickname, Pageable pageable);

	GetUser updateUserProfile(MyUserPrincipal user, Long userId, String nickname, String description, MultipartFile file);

	void deleteUser(MyUserPrincipal user, Long userId);

	void verifyPassword(MyUserPrincipal user, Long userId, String password);

	void changePassword(MyUserPrincipal user, Long userId, String newPassword);

	Page<GetBoard> getUserBoardList(Long userId, Pageable pageable);

	GetUser getUserById(MyUserPrincipal requestUser, Long userId);
}
