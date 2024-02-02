package com.lanorder.lanorderserver.web.user;


import com.lanorder.lanorderserver.common.entity.pojo.TabOrder;
import com.lanorder.lanorderserver.common.log.MethodLog;
import com.lanorder.lanorderserver.common.response.ErrorEnum;
import com.lanorder.lanorderserver.common.response.Result;
import com.lanorder.lanorderserver.service.User;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1")
@RequestMapping("/user")
public class GoOrder {

    @Resource
    private User user;

    @MethodLog
    @PostMapping("/order/add")
    public Result<TabOrder> add(@RequestBody TabOrder order) {
        TabOrder tabOrder = user.addOrder(order);
        return tabOrder != null
                ? Result.result(ErrorEnum.SUCCESS,tabOrder)
                : Result.result(ErrorEnum.PARAMETER_ERROR,null);
    }
}
