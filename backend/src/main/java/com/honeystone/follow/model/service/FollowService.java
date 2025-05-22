package com.honeystone.follow.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.honeystone.common.dto.user.GetUser;
import com.honeystone.common.security.MyUserPrincipal;

public interface FollowService {

    void addFollow(MyUserPrincipal user, Long followingUserId);

    void removeFollow(MyUserPrincipal user, Long followingUserId);

	Page<GetUser> getFollowingList(MyUserPrincipal requestUser, Long userId, Pageable pageable);
	
	Page<GetUser> getFollowerList(MyUserPrincipal requestUser, Long userId, Pageable pageable);
}
