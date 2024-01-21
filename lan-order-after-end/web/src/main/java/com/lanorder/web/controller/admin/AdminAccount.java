package com.lanorder.web.controller.admin;

import com.lanorder.common.entity.request.receive.UserLoginMsg;
import com.lanorder.common.log.MethodLog;
import com.lanorder.common.response.ErrorEnum;
import com.lanorder.common.response.Result;
import com.lanorder.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "http://127.0.0.1")
@RequestMapping("/admin")
public class AdminAccount {

    @Resource
    private Admin admin;

    @MethodLog
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginMsg userLoginMsg){
        String jwt = admin.login(userLoginMsg);
        return jwt.equals("")
                ? Result.result(ErrorEnum.NO_PRIVILEGE,null)
                : Result.result(ErrorEnum.SUCCESS.getCode(),ErrorEnum.SUCCESS.getMessage(),jwt);
    }
}
