package com.honeystone.follow.model.service;

import com.honeystone.common.security.MyUserPrincipal;
import com.honeystone.exception.BusinessException;
import com.honeystone.follow.model.dao.FollowDao;
import com.honeystone.user.model.dao.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if(userDao.findByEmail(user.getEmail()) == null) throw new BusinessException("존재하지 않는 사용자입니다.");

        if (followDao.existsFollow(user.getId(), followingUserId) > 0)
            throw new BusinessException("이미 팔로잉 중인 사용자입니다.");

        followDao.insertFollow(user.getId(), followingUserId);
    }

    @Override
    public void removeFollow(MyUserPrincipal user, Long followingUserId) {
        if(userDao.findByEmail(user.getEmail()) == null) throw new BusinessException("존재하지 않는 사용자입니다.");

        if (followDao.existsFollow(user.getId(), followingUserId) == 0)
            throw new BusinessException("팔로잉 중인 사용자가 아닙니다.");

        followDao.deleteFollow(user.getId(), followingUserId);
    }
}
