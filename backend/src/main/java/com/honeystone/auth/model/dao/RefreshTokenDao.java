package com.honeystone.auth.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.honeystone.common.dto.auth.RefreshToken;

@Mapper
public interface RefreshTokenDao {

    void save(RefreshToken refreshToken);

    RefreshToken findByToken(@Param("token") String token);

    void deleteByToken(@Param("token") String token);

    void deleteByUserId(@Param("userId") Long userId); // 로그아웃 시 전체 삭제
}
