package com.lanorder.web.controller.admin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.lanorder.common.entity.pojo.TabOrder;
import com.lanorder.common.log.MethodLog;
import com.lanorder.common.response.ErrorEnum;
import com.lanorder.common.response.Result;
import com.lanorder.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@CrossOrigin(origins = "http://127.0.0.1")
@RestController
@RequestMapping("/admin")
public class OrderController {

    @Resource
    private Admin admin;

    @MethodLog
    @PatchMapping("/order/update")
    public Result<TabOrder> update(@RequestBody TabOrder order){
        return Result.result(ErrorEnum.SUCCESS,admin.updateOrderByTabNum(order));
    }



    @MethodLog
    @GetMapping("/order/show")
    public Result<Map<String,Object>> showOrderList() throws JsonProcessingException {
        return Result.result(ErrorEnum.SUCCESS,admin.getOrderList());
    }
}
