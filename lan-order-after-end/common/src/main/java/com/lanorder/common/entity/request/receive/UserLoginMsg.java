package com.lanorder.common.entity.request.receive;

import lombok.Data;

@Data
public class UserLoginMsg {
    private String type;
    private String user_num;
    private String passwd;
    private String jwt;
}
