package com.example.lanorderafterend.controller.admin;

import com.example.lanorderafterend.entity.Order;
import com.example.lanorderafterend.entity.Result;
import com.example.lanorderafterend.service.Admin;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1")
@RestController
public class OrderController {
    @Resource
    private Admin admin;

    @PatchMapping("/admin/order/update")
    public Result update(@RequestBody Order order){
        admin.updateOrderByTabNum(order);
        return Result.success(1,"succeed");
    }

    @PostMapping("/admin/order/end")
    public Result endOrder(@RequestParam int tabNum) throws JsonProcessingException {
        return admin.isEnd(tabNum) != 0
                ? Result.success(1,"succeed")
                : Result.error(-1,"error");
    }

    @GetMapping("/admin/order/show")
    public Result showOrderList() throws JsonProcessingException {
        return Result.success(1,"store list",admin.getOrderList());
    }
}
