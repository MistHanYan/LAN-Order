package com.example.lanorderafterend.controller.user;

import com.example.lanorderafterend.entity.Order;
import com.example.lanorderafterend.entity.Result;
import com.example.lanorderafterend.service.User;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoOrder {

    @Resource
    private User user;

    @PostMapping("/user/order/add")
    public Result add(@RequestBody Order order) throws Exception {
        return Result.success(user.addOrder(order),"add oder");
    }
}
