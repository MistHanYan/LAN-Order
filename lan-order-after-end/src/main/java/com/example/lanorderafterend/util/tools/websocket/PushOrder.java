package com.example.lanorderafterend.util.tools.websocket;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class PushOrder implements WebSocketHandler {

    public List<WebSocketSession> getSessions() {
        return sessions;
    }

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        this.sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        session.sendMessage(message);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        this.sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session,@NonNull Throwable exception) {
        // 处理传输错误
    }
}
