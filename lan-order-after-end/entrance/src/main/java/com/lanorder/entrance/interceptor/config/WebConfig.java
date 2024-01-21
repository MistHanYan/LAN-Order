package com.lanorder.entrance.interceptor.config;

import com.lanorder.entrance.interceptor.AdminCheckTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
}
