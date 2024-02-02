package com.lanorder.lanorderserver.entrance.interceptor.config;

import com.lanorder.lanorderserver.entrance.interceptor.AdminCheckTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //配置类
public class WebConfig implements WebMvcConfigurer {

    private final AdminCheckTime adminCheck;
    @Autowired
    public WebConfig(AdminCheckTime adminCheck) {
        this.adminCheck = adminCheck;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminCheck)
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://47.109.47.32:8080")
                .allowedMethods("GET", "POST", "OPTIONS","DELETE","PATCH")
                .allowedHeaders("Content-Type", "Authorization")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
