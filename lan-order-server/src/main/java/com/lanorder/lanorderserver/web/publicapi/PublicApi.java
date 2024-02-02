package com.lanorder.lanorderserver.web.publicapi;

import com.lanorder.lanorderserver.common.entity.pojo.TabOrder;
import com.lanorder.lanorderserver.common.log.MethodLog;
import com.lanorder.lanorderserver.common.response.ErrorEnum;
import com.lanorder.lanorderserver.common.response.Result;
import com.lanorder.lanorderserver.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://127.0.0.1")
@RequestMapping("/public")
public class PublicApi {
    @Resource
    private Admin admin;

    @MethodLog
    @GetMapping("/onsale/store")
    public Result<Map<String,Object>> showStore(){
        return Result.result(ErrorEnum.SUCCESS,admin.getOnSalesStore());
    }

    @MethodLog
    @GetMapping ("/order/end")
    public Result<TabOrder> endOrder(@RequestParam int tabNum) {
        return Result.result(ErrorEnum.SUCCESS,admin.isEnd(tabNum));
    }
}
