package com.honeystone.common.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
//        logger.info("Processing request: {} {}", request.getMethod(), requestURI);

        String token = jwtTokenProvider.resolveToken(request);
//        logger.info("Token from request: {}", token != null ? "present" : "null");

        if (token != null) {
            try {
                // 토큰 유효성 검사 (서명, 만료, 블랙리스트)
                if (!jwtTokenProvider.validateToken(token)) {
                    logger.error("Token validation failed for request: {} {} - Token: {}", 
                        request.getMethod(), requestURI, token);
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    response.getWriter().write("{\"error\":\"유효하지 않은 토큰입니다.\"}");
                    return;
                }

                String email = jwtTokenProvider.getEmailFromToken(token);
//                logger.info("Token validated successfully for email: {}", email);
                
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//                logger.info("User details loaded for email: {}", email);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
//                logger.info("Authentication set in SecurityContext for user: {}", email);
            } catch (Exception e) {
                logger.error("Error processing JWT token for request: {} {} - Error: {}", 
                    request.getMethod(), requestURI, e.getMessage());
                SecurityContextHolder.clearContext();
            }
        } else {
            logger.info("No token found in request: {} {}", request.getMethod(), requestURI);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 로그인, 회원가입 등 인증이 필요없는 경로는 필터링하지 않음
        boolean shouldNotFilter = path.startsWith("/api/auth/") || 
                                path.startsWith("/api/users/") ||
                                path.startsWith("/v3/api-docs/") ||
                                path.startsWith("/swagger-ui/") ||
                                path.startsWith("/swagger-resources/") ||
                                path.startsWith("/webjars/");
        
//        logger.info("Request path: {} - Should not filter: {}", path, shouldNotFilter);
        return shouldNotFilter;
    }
}
