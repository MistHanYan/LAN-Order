package com.example.lanorderafterend.util.tools.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketCfg implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new PushOrder(), "login://localhost:8080/login")
                .setAllowedOrigins("*");
    }

    @Bean
    public PushOrder myWebSocketHandler() {
        return new PushOrder();
    }

}
