package com.lanorder.web.controller.admin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.lanorder.common.entity.pojo.Tabs;
import com.lanorder.common.entity.request.receive.DeskMsg;
import com.lanorder.common.log.MethodLog;
import com.lanorder.common.response.ErrorEnum;
import com.lanorder.common.response.Result;
import com.lanorder.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

//@CrossOrigin(origins = "http://127.0.0.1")
@RestController
@RequestMapping("/admin")
public class ManageQRController {
    @Resource
    private Admin admin;

    @MethodLog
    @GetMapping("/manage_qr/show_tab")
    public Result<List<Tabs>> showList(){
        return Result.result(ErrorEnum.SUCCESS,admin.getTabList());
    }

    @MethodLog
    @DeleteMapping("/manage_qr/delete")
    public Result<Object> delete(@RequestParam Integer id) throws JsonProcessingException {
        return Result.result(admin.deleteTabByPath(id),null);
    }

    @MethodLog
    @PostMapping("/manage_qr/add")
    public Result<Object> add(@RequestBody DeskMsg deskMsg) throws IOException {
        return Result.result(admin.addTab(deskMsg),null);
    }
}
