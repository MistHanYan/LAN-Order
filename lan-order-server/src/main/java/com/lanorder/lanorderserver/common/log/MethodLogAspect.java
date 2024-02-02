package com.lanorder.lanorderserver.common.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lanorder.lanorderserver.common.util.json.Json;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class MethodLogAspect {

    /**
     * 自定义 @MethodLog 切点
     */
    @Pointcut("@annotation(com.lanorder.lanorderserver.common.log.MethodLog)")
    public void MethodLog() {
    }

    @Before("MethodLog()")
    public void doBefore(JoinPoint joinPoint) throws JsonProcessingException {
        log.info("Request Method:{}, Request Param:{}", joinPoint.getSignature(), Json.toJson(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "o", pointcut = "MethodLog()")
    public void doAfterReturning(Object o) throws JsonProcessingException {
        log.info("Response Result:{}", Json.toJson(o));
    }
}

