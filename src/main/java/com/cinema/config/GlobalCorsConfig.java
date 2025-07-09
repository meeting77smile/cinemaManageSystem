package com.cinema.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")          // 所有路径
                .allowedOrigins("*")        // 允许所有域名（生产环境建议替换为具体域名）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的HTTP方法
                .allowedHeaders("*")        // 允许所有请求头
                .allowCredentials(true)     // 允许携带凭证（如Cookie）
                .maxAge(3600);              // 预检请求缓存时间（秒）
    }
}