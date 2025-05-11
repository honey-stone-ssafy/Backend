// src/main/java/com/honeystone/video/handler/SkillSetTypeHandler.java
package com.honeystone.video.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import com.honeystone.video.model.type.Skill;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SkillSetTypeHandler extends BaseTypeHandler<Set<Skill>> {

    // 파라미터 바인딩: Set<Skill> → "PINCH,CRIMP"
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<Skill> parameter, JdbcType jdbcType)
        throws SQLException {
        String joined = parameter.stream()
            .map(Skill::name)
            .collect(Collectors.joining(","));
        ps.setString(i, joined);
    }

    // 조회 결과 파싱: "PINCH,CRIMP" → Set.of(Skill.PINCH, Skill.CRIMP)
    @Override
    public Set<Skill> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toSet(rs.getString(columnName));
    }
    @Override
    public Set<Skill> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toSet(rs.getString(columnIndex));
    }
    @Override
    public Set<Skill> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toSet(cs.getString(columnIndex));
    }

    private Set<Skill> toSet(String dbValue) {
        if (dbValue == null || dbValue.isBlank()) return Collections.emptySet();
        return Arrays.stream(dbValue.split(","))
            .map(String::trim)
            .map(Skill::valueOf)
            .collect(Collectors.toSet());
    }
}
