package com.honeystone.follow.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.honeystone.common.dto.user.GetUser;
import com.honeystone.common.security.MyUserPrincipal;
import com.honeystone.exception.BusinessException;
import com.honeystone.follow.model.dao.FollowDao;
import com.honeystone.user.model.dao.UserDao;

@Transactional
@Service
public class FollowServiceImpl implements FollowService {

    private final FollowDao followDao;
    private final UserDao userDao;
    public FollowServiceImpl(FollowDao followDao, UserDao userDao) {
        this.followDao = followDao;
        this.userDao = userDao;
    }

    @Override
    public void addFollow(MyUserPrincipal user, Long followingUserId) {
        if(userDao.findByEmail(user.getEmail()) == null || userDao.existsById(followingUserId) == 0)
        	throw new BusinessException("존재하지 않는 사용자입니다.");
        
        if(user.getId() == followingUserId)
        	throw new BusinessException("나 자신을 팔로잉할 수 없습니다.");

        if (followDao.existsFollow(user.getId(), followingUserId) > 0)
            throw new BusinessException("이미 팔로잉 중인 사용자입니다.");

        followDao.insertFollow(user.getId(), followingUserId);
    }

    @Override
    public void removeFollow(MyUserPrincipal user, Long followingUserId) {
        if(userDao.findByEmail(user.getEmail()) == null || userDao.existsById(followingUserId) == 0)
        	throw new BusinessException("존재하지 않는 사용자입니다.");

        if (followDao.existsFollow(user.getId(), followingUserId) == 0)
            throw new BusinessException("팔로잉 중인 사용자가 아닙니다.");

        followDao.deleteFollow(user.getId(), followingUserId);
    }

	@Override
	public Page<GetUser> getFollowingList(MyUserPrincipal requestUser, Long userId, Pageable pageable) {
		long total = followDao.countFollowing(userId);
		int offset = pageable.getPageNumber() * pageable.getPageSize();
	    int size = pageable.getPageSize();
	    
		List<GetUser> followingUsers = followDao.getFollowingList(requestUser == null ? -1 : requestUser.getId(), userId, offset, size);
		return new PageImpl<>(followingUsers, pageable, total);
	}
	
	@Override
	public Page<GetUser> getFollowerList(MyUserPrincipal requestUser, Long userId, Pageable pageable) {
		long total = followDao.countFollower(userId);
		int offset = pageable.getPageNumber() * pageable.getPageSize();
	    int size = pageable.getPageSize();
	    
		List<GetUser> followerUsers = followDao.getFollowerList(requestUser == null ? -1 : requestUser.getId(), userId, offset, size);
		return new PageImpl<>(followerUsers, pageable, total);
	}
}
