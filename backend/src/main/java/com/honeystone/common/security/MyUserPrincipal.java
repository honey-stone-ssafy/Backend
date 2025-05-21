package com.honeystone.common.security;

import com.honeystone.common.dto.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class MyUserPrincipal implements UserDetails {

    private final User user;

    public MyUserPrincipal(User user) {
        this.user = user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    // 아래부터는 UserDetails 인터페이스 구현부

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 예: user.getRoles() 등을 이용해 권한 리스트 리턴
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 혹은 user.getUsername()
    }

    @Override public boolean isAccountNonExpired()    { return true; }
    @Override public boolean isAccountNonLocked()     { return true; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
    @Override public boolean isEnabled()              { return true; }

    // 필요하다면 user 전체를 꺼내는 getter 추가
    public User getUser() {
        return user;
    }
}
