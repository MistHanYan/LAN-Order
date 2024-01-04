package com.example.lanorderafterend.controller.admin;

import com.example.lanorderafterend.entity.DeskMsg;
import com.example.lanorderafterend.entity.Result;
import com.example.lanorderafterend.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://127.0.0.1")
@RestController
public class ManageQRController {
    @Resource
    private Admin admin;

    @GetMapping("/admin/manage_qr/show_tab")
    public Result showList(){
        return Result.success(1,"succeed",admin.getTabList());
    }

    @DeleteMapping("/admin/manage_qr/delete")
    public Result delete(@RequestParam String path){
        return admin.deleteTabByPath(path) != 0
                ? Result.success(1,"succeed")
                : Result.error(-1,"error");
    }

    @PostMapping("/admin/manage_qr/add")
    public Result add(@RequestBody DeskMsg deskMsg) throws IOException {
        return deskMsg != null
                ? Result.success(1,"succeed",admin.addTab(deskMsg))
                : Result.error(-1,"error");
    }
}
