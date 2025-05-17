package com.honeystone.common.security;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.refresh-token-expire-ms}")
    private long refreshTokenExpireMs;
 
    @Value("${jwt.token-valid-time}")
    private long tokenValidTime;

    private Key key;
    private final ConcurrentHashMap<String, Long> tokenVersions = new ConcurrentHashMap<>();

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    
    /** 리프레시 토큰 생성 **/
    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpireMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("type", "refresh")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** JWT 토큰 생성 **/
    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenValidTime);
        Long version = tokenVersions.getOrDefault(email, 0L);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("type", "access")
                .claim("version", version)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** 토큰 버전 증가 (로그아웃 시 호출) **/
    public void incrementTokenVersion(String email) {
        tokenVersions.compute(email, (key, oldVersion) -> (oldVersion == null) ? 1L : oldVersion + 1);
//        logger.info("Token version incremented for user: {} to version: {}", email, tokenVersions.get(email));
    }

    /** JWT 토큰에서 사용자 이메일 추출 **/
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /** JWT 토큰 유효성 검사 **/
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 만료 시간 검증
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                logger.error("Token is expired: {}", token);
                return false;
            }

            // 토큰 타입 검증
            String tokenType = claims.get("type", String.class);
            if (tokenType == null || !tokenType.equals("access")) {
                logger.error("Invalid token type: {}", tokenType);
                return false;
            }

            // 토큰 버전 검증
            String email = claims.getSubject();
            Long tokenVersion = claims.get("version", Long.class);
            Long currentVersion = tokenVersions.getOrDefault(email, 0L);
            
            if (tokenVersion == null || tokenVersion < currentVersion) {
                logger.error("Token version mismatch for user: {}. Token version: {}, Current version: {}", 
                    email, tokenVersion, currentVersion);
                return false;
            }

//            logger.info("Token validation successful for email: {}", email);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Token validation failed: {} - Error: {}", token, e.getMessage());
            return false;
        }
    }

    /** 리프레시 토큰 유효성 검사 **/
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 만료 시간 검증
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                logger.error("Refresh token is expired: {}", token);
                return false;
            }

            // 토큰 타입 검증
            String tokenType = claims.get("type", String.class);
            if (tokenType == null || !tokenType.equals("refresh")) {
                logger.error("Invalid refresh token type: {}", tokenType);
                return false;
            }

//            logger.info("Refresh token validation successful for email: {}", claims.getSubject());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Refresh token validation failed: {} - Error: {}", token, e.getMessage());
            return false;
        }
    }

    /** 헤더에서 토큰 추출 **/
    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
//            logger.debug("Resolved token from request: {}", token);
            return token;
        }
        logger.debug("No token found in request headers");
        return null;
    }

    /** 토큰의 만료 시간 추출 **/
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }
}

