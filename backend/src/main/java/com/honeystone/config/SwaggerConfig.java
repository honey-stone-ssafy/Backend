package com.honeystone.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springBoardOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("HoneyStone API")
                .description("꿀멩이 API 페이지 입니다.")
                .version("v0.0.1")
                .license(new License().name("HoneyStone")));
    }
}

// 스웨거 페이지 url
// http://localhost:8080/swagger-ui/index.html
