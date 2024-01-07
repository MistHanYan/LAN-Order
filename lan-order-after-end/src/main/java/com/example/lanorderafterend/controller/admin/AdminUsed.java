package com.example.lanorderafterend.controller.admin;

import com.example.lanorderafterend.entity.Result;
import com.example.lanorderafterend.entity.UserLoginMsg;
import com.example.lanorderafterend.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminUsed {

    @Resource
    private Admin admin;

    @PostMapping("/admin/login")
    public Result login(@RequestBody UserLoginMsg userLoginMsg){
        String jwt = admin.login(userLoginMsg);
        return jwt.equals("")
                ? Result.error(-1,"login is error")
                : Result.success(1,"login successful",jwt);
    }
}
