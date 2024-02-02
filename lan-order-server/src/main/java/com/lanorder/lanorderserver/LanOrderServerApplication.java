package com.lanorder.lanorderserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
@EnableTransactionManagement
public class LanOrderServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LanOrderServerApplication.class, args);
    }

}
