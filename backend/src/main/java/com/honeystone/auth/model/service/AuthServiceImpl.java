package com.honeystone.auth.model.service;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honeystone.auth.model.dao.RefreshTokenDao;
import com.honeystone.common.dto.auth.RefreshToken;
import com.honeystone.common.dto.user.User;
import com.honeystone.common.dto.user.UserLoginRequest;
import com.honeystone.common.dto.user.UserLoginResponse;
import com.honeystone.common.security.JwtTokenProvider;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.user.model.dao.UserDao;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	private final UserDao userDao;
    private final RefreshTokenDao refreshTokenDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserLoginResponse login(UserLoginRequest request) throws ServerException {
        User user = userDao.findByEmail(request.getEmail());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        	throw new BusinessException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        RefreshToken token = RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenDao.save(token);
        return new UserLoginResponse(accessToken, refreshToken);
    }

    @Override
    public UserLoginResponse refreshAccessToken(HttpServletRequest request) throws ServerException {
        String refreshToken = jwtTokenProvider.resolveToken(request);

        RefreshToken storedToken = refreshTokenDao.findByToken(refreshToken);
        if (storedToken == null) {
        	throw new BusinessException("유효하지 않은 리프레시 토큰입니다.");
        }

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            refreshTokenDao.deleteByToken(refreshToken);
            throw new BusinessException("리프레시 토큰이 만료되었습니다.");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateToken(email);

        return new UserLoginResponse(newAccessToken, refreshToken);
    }

    @Override
    public void logout(HttpServletRequest request) throws ServerException {
        // 액세스 토큰 처리
        String accessToken = jwtTokenProvider.resolveToken(request);
        if (accessToken != null) {
            String email = jwtTokenProvider.getEmailFromToken(accessToken);
            // 사용자의 토큰 버전을 증가시켜 현재 액세스 토큰을 무효화
            jwtTokenProvider.incrementTokenVersion(email);
//            logger.info("Access token invalidated for user: {}", email);
        }

        // 리프레시 토큰 처리
        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken != null) {
            RefreshToken storedToken = refreshTokenDao.findByToken(refreshToken);
            if (storedToken != null) {
                // 리프레시 토큰 삭제
                refreshTokenDao.deleteByToken(refreshToken);
//                logger.info("Refresh token deleted: {}", refreshToken);
            }
        }
    }
}
