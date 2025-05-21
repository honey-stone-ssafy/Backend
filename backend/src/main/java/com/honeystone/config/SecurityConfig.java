package com.honeystone.config;

import org.springframework.http.HttpMethod;
import com.honeystone.common.security.CustomAccessDeniedHandler;
import com.honeystone.common.security.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.honeystone.common.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.debug.DebugFilter;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableWebSecurity
@EnableScheduling
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    CustomAuthenticationEntryPoint authEntryPoint;
    @Autowired
    CustomAccessDeniedHandler accessHandler;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .anonymous().disable()
            .authorizeHttpRequests()
		         // 인증 예외 경로 설정
	            .requestMatchers(
	                "/api/auth/**",         // 로그인
	                "/api/users/**",           // 회원가입
	                "/v3/api-docs/**",      // Swagger
	                "/swagger-ui/**",
	                "/swagger-resources/**",
	                "/webjars/**",
	                "/api/recommandations/**"
	            ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/boards/**").permitAll()
	            .anyRequest().authenticated()  // 그 외는 인증 필요
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)  // 인증 실패(401)
                .accessDeniedHandler(accessHandler)        // 권한 부족(403)
            .and()
            .addFilterAfter(jwtAuthenticationFilter, SecurityContextPersistenceFilter.class);
        return http.build();
    }

    // 비밀번호 암호화용 Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager Bean 등록 (로그인 인증용)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}