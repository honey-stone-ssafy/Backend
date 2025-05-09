package com.honeystone.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({
    "com.honeystone.video.model.dao",
    "com.honeystone.user.model.dao"
})
public class DBConfig {}
