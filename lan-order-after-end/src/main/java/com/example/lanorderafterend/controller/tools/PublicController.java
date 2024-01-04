package com.example.lanorderafterend.controller.tools;

import com.example.lanorderafterend.entity.Result;
import com.example.lanorderafterend.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {
    @Resource
    private Admin admin;

    @GetMapping("/show/store")
    public Result showStore(){
        return Result.success(1,"Msg of store list",admin.getOnSalesStore());
    }
}
