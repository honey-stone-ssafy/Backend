package com.honeystone.user.model.dao;

import java.util.List;

import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.user.UserFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.honeystone.common.dto.user.GetUser;
import com.honeystone.common.dto.user.User;
import com.honeystone.common.dto.user.UserSignupRequest;

@Mapper
public interface UserDao {

	void createUser(UserSignupRequest user);

	int countByNickname(String nickname);
	
	int countByEmail(@Param("email") String email);

	User findByEmail(@Param("email") String email);
	
	List<GetUser> searchByNickname(@Param("requestUserId") Long requestUserId, @Param("nickname") String nickname, int offset, int size);

	int existsById(Long userId);

	void updateProfileImage(@Param("userId") Long userId, @Param("img") String img);

	void createUserFile(UserFile userFile);

	UserFile findUserFileByUserId(Long userId);

	void deleteUserFileByUserId(Long userId);

	String findNicknameByUserId(Long userId);

	void updateNicknameAndDescription(@Param("userId") Long userId, @Param("nickname") String nickname, @Param("description") String description);

	void deleteUser(Long userId);

	void updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

	long countBoards(Long userId);

	List<GetBoard> getBoardList(Long userId, int offset, int size);

	long countSearchByNickname(String nickname);

	GetUser getUserByNickname(@Param("userId") Long userId, @Param("nickname") String nickname);
}
