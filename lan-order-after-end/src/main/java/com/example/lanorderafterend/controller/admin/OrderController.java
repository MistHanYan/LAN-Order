package com.example.lanorderafterend.controller.admin;

import com.example.lanorderafterend.entity.Order;
import com.example.lanorderafterend.entity.Result;
import com.example.lanorderafterend.service.Admin;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1")
@RestController
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Resource
    private Admin admin;

    @PatchMapping("/admin/order/update")
    public Result update(@RequestBody Order order){
        logger.info("{}号桌，申请更新订单",order.getTabNum());
        admin.updateOrderByTabNum(order);
        return Result.success(1,"succeed");
    }

    @PostMapping("/admin/order/end")
    public Result endOrder(@RequestParam int tabNum) throws JsonProcessingException {
        logger.info("{}号桌，申请结束订单",tabNum);
        return admin.isEnd(tabNum) != 0
                ? Result.success(1,"succeed")
                : Result.error(-1,"error");
    }

    @GetMapping("/admin/order/show")
    public Result showOrderList() throws JsonProcessingException {
        return Result.success(1,"store list",admin.getOrderList());
    }
}
