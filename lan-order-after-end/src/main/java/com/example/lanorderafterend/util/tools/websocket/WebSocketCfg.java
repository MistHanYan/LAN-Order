package com.example.lanorderafterend.util.tools.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketCfg{
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        ServerEndpointExporter exporter = new ServerEndpointExporter();

        // 手动注册 WebSocket 端点
        exporter.setAnnotatedEndpointClasses(PushOrder.class);

        return exporter;
    }

}
