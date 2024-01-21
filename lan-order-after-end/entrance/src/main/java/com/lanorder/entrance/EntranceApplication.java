package com.lanorder.entrance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@MapperScan(value="com.lanorder.dao.mybatis.mapper")
@EnableTransactionManagement
@ComponentScan(value="com.lanorder")
@EnableWebSocket
public class EntranceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntranceApplication.class, args);
    }

}
