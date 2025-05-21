package com.honeystone.follow.model.service;

import com.honeystone.common.security.MyUserPrincipal;

public interface FollowService {

    void addFollow(MyUserPrincipal user, Long followingUserId);

    void removeFollow(MyUserPrincipal user, Long followingUserId);
}
