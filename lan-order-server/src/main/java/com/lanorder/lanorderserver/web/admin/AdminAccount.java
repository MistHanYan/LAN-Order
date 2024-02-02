package com.lanorder.lanorderserver.web.admin;

import com.lanorder.lanorderserver.common.entity.request.receive.UserLoginMsg;
import com.lanorder.lanorderserver.common.log.MethodLog;
import com.lanorder.lanorderserver.common.response.ErrorEnum;
import com.lanorder.lanorderserver.common.response.Result;
import com.lanorder.lanorderserver.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1")
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
