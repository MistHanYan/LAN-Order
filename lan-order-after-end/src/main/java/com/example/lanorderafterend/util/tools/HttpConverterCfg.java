package com.example.lanorderafterend.util.tools;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
* 配置图床*/
@Configuration
public class HttpConverterCfg implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取桌面二维码图片
        registry
                .addResourceHandler("/img/tab/**")
                .addResourceLocations("file:/home/mist/pictures/tabs/");

        // 获取优惠码图片
        registry
                .addResourceHandler("/img/maQR/**")
                .addResourceLocations("file:/home/mist/pictures/maQRs/");

        registry
                .addResourceHandler("/img/store/**")
                .addResourceLocations("file:/home/mist/pictures/stores/");
    }
}
