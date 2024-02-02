package com.lanorder.lanorderserver.web.admin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.lanorder.lanorderserver.common.entity.pojo.TabOrder;
import com.lanorder.lanorderserver.common.log.MethodLog;
import com.lanorder.lanorderserver.common.response.ErrorEnum;
import com.lanorder.lanorderserver.common.response.Result;
import com.lanorder.lanorderserver.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1")
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
    @GetMapping("/order/list")
    public Result<Map<String,Object>> showOrderList() throws JsonProcessingException {
        return Result.result(ErrorEnum.SUCCESS,admin.getOrderList());
    }
}
