package com.example.lanorderafterend.controller.admin;

import com.example.lanorderafterend.entity.Result;
import com.example.lanorderafterend.service.Admin;
import com.example.lanorderafterend.util.mybatis.TabRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1")
@RestController
public class RecordController {
    @Resource
    private Admin admin;
    @GetMapping("/admin/record/show")
    public Result showList() throws JsonProcessingException {
        return Result.success(1,"record list",admin.getRecordList());
    }

    @DeleteMapping("/admin/record/delete")
    public Result delete(@RequestParam int id){
        return admin.deleteRecordById(id) > 0
                ? Result.success(1,"succeed")
                : Result.error(-1,"error");
    }

    @PatchMapping("/admin/record/update")
    public Result update(@RequestBody TabRecord tabRecord) throws JsonProcessingException {
        return admin.updateRecordById(tabRecord) > 0
                ? Result.success(1,"succeed")
                : Result.error(-1,"error");
    }

    @PostMapping("/admin/record/add")
    public Result add(@RequestBody TabRecord tabRecord) throws JsonProcessingException {
        return admin.addRecord(tabRecord) > 0
                ? Result.success(1,"succeed")
                : Result.error(0,"error");
    }
}
