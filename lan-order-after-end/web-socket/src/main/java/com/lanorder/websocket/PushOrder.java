package com.lanorder.websocket;

import com.lanorder.common.util.jwt.JwtCfg;
import com.lanorder.websocket.config.WebSocketCfg;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@Component
@ServerEndpoint(value = "/admin/connection/{user_num}",configurator = WebSocketCfg.class)
public class PushOrder{

    // 用来存在线连接用户信息
    private static final ConcurrentHashMap<String,Session> sessionPool = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    /**
     * 用户ID
     */
    private String user_num;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    // 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
    //  注：底下WebSocket是当前类名
    private static final CopyOnWriteArraySet<PushOrder> webSockets =new CopyOnWriteArraySet<>();


    private static final Logger logger = LoggerFactory.getLogger(PushOrder.class);

    /**
     * 建立连接时方法
     * 将保存用户账号和session
     * */
    @OnOpen
    public void onOpen(Session session ,  @PathParam(value = "user_num") String user_num) throws IOException {
        try {
            this.session = session;
            this.user_num = user_num;
            // 获取请求头信息
            if(!JwtCfg.checkJwtTimed(getHeader(session,"Authorization"))){
                logger.info("令牌无效断开链接");
                throw new RuntimeException();
            }
            webSockets.add(this);
            sessionPool.put(user_num, session);
            logger.info("【websocket消息】有新的连接请求，连接用户为:{}，目前连接总数为:{}", user_num, webSockets.size());
        } catch (Exception ignored) {
            session.close();
        }
    }

    /**
     * 连接建立后收到信息处理方法
     * */
    @OnMessage
    public void onMessage(@PathParam(value="user_num")String user_num , String message) {
        logger.debug("收到连接信息:{}", message);
        logger.info("用户{}，在{}时发送信息:{}", user_num, LocalDate.now(),message);
        sendOneMessage(user_num, "connection success");
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.user_num);
            logger.info("【websocket消息】连接断开，总数为:"+webSockets.size());
        } catch (Exception ignored) {
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

        logger.error("用户错误,原因:"+error.getMessage());
        error.printStackTrace();
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        logger.info("【websocket消息】广播消息:"+message);
        for(PushOrder webSocket : webSockets) {
            try {
                if(webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String user_num, String message) {
        Session session = sessionPool.get(user_num);
        if (session != null&&session.isOpen()) {
            try {
                logger.info("【websocket消息】 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getHeader(Session session, String headerName) {
        final String header = (String) session.getUserProperties().get(headerName);
        if (header == null || header.trim().isEmpty()) {
            logger.error("获取header失败，不安全的链接，即将关闭");
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return header;
    }
}
