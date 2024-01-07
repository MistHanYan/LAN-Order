package com.example.lanorderafterend.entity;

import lombok.Data;

@Data
public class UserLoginMsg {
    private String type;
    private String user_num;
    private String passwd;
    private String jwt;
}
