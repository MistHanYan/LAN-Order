package com.lanorder.web.controller.publicapi;

import com.lanorder.common.entity.pojo.TabOrder;
import com.lanorder.common.log.MethodLog;
import com.lanorder.common.response.ErrorEnum;
import com.lanorder.common.response.Result;
import com.lanorder.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicApi {
    @Resource
    private Admin admin;

    @MethodLog
    @GetMapping("/show/store")
    public Result<Map<String,Object>> showStore(){
        return Result.result(ErrorEnum.SUCCESS,admin.getOnSalesStore());
    }

    @MethodLog
    @GetMapping ("/order/end")
    public Result<TabOrder> endOrder(@RequestParam int tabNum) {
        return Result.result(ErrorEnum.SUCCESS,admin.isEnd(tabNum));
    }
}
