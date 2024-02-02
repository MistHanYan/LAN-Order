package com.lanorder.lanorderserver.entrance.interceptor;

import com.lanorder.lanorderserver.common.response.ErrorEnum;
import com.lanorder.lanorderserver.common.util.jwt.JwtCfg;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminCheckTime implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AdminCheckTime.class);
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        // 在处理请求之前执行的操作
        if(request.getRequestURI().contains("user") ||
                request.getRequestURI().contains("login")
        || request.getRequestURI().contains("public")){
            logger.info("Action is user or login for admin -> green light");
            return true;
        }else {
            String token = request.getHeader("Authorization");
            if(token != null && JwtCfg.checkJwtTimed(token)){
                logger.info("the token is overtime or invalid");
                return true;
            }
            response.getWriter().write(ErrorEnum.NO_PRIVILEGE.toString());
            return false;
        }
    }
}
