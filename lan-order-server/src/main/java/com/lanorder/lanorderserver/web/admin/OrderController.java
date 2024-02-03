package com.lanorder.lanorderserver.web.admin;


import com.lanorder.lanorderserver.common.entity.pojo.TabOrder;
import com.lanorder.lanorderserver.common.entity.pojo.TabStore;
import com.lanorder.lanorderserver.common.log.MethodLog;
import com.lanorder.lanorderserver.common.response.ErrorEnum;
import com.lanorder.lanorderserver.common.response.Result;
import com.lanorder.lanorderserver.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result<List<TabOrder>> getOrderListAll() {
        return Result.result(ErrorEnum.SUCCESS,admin.getOrderListAll());
    }

    @MethodLog
    @GetMapping("/order/store/list")
    public Result<List<TabStore>> getStoreListInOrder(@RequestParam String tabNum) {
        return Result.result(ErrorEnum.SUCCESS,admin.getStoreList(tabNum));
    }

    @MethodLog
    @GetMapping("/order")
    public Result<TabOrder> getOrderByTabNum(@RequestParam String tabNum) {
        return Result.result(ErrorEnum.SUCCESS,admin.getOrderByTabNum(tabNum));
    }
}
