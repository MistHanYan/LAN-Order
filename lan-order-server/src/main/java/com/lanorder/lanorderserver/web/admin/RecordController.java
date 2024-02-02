package com.lanorder.lanorderserver.web.admin;


import com.lanorder.lanorderserver.common.entity.pojo.TabRecord;
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
public class RecordController {
    @Resource
    private Admin admin;
    @GetMapping("/record/show")
    public Result<List<TabRecord>> showList() {
        return Result.result(ErrorEnum.SUCCESS,admin.getRecordList());
    }

    @MethodLog
    @DeleteMapping("/record/delete")
    public Result<Object> delete(@RequestParam int id){
        return Result.result(admin.deleteRecordById(id),null);
    }

    @MethodLog
    @PatchMapping("/record/update")
    public Result<Object> update(@RequestBody TabRecord tabRecord) {
        return Result.result(admin.updateRecordById(tabRecord),null);
    }

    @MethodLog
    @PostMapping("/record/add")
    public Result<Object> add(@RequestBody TabRecord tabRecord) {
        return Result.result(admin.addRecord(tabRecord),null);
    }
}
