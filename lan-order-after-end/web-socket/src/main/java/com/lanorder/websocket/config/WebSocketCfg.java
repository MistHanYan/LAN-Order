package com.lanorder.websocket.config;

import com.lanorder.websocket.PushOrder;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;
import java.util.Map;

@Configuration
public class WebSocketCfg extends ServerEndpointConfig.Configurator{
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        ServerEndpointExporter exporter = new ServerEndpointExporter();

        // 手动注册 WebSocket 端点
        exporter.setAnnotatedEndpointClasses(PushOrder.class);

        return exporter;
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 这个userProperties 可以通过 session.getUserProperties()获取
        final Map<String, Object> userProperties = sec.getUserProperties();
        Map<String, List<String>> headers = request.getHeaders();
        List<String> remoteIp = headers.get("Authorization");
        userProperties.put("Authorization", remoteIp.get(0));
    }
}
