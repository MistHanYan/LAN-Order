package com.lanorder.web.controller.user;


import com.lanorder.common.entity.pojo.TabOrder;
import com.lanorder.common.log.MethodLog;
import com.lanorder.common.response.ErrorEnum;
import com.lanorder.common.response.Result;
import com.lanorder.service.User;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "http://127.0.0.1")
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
