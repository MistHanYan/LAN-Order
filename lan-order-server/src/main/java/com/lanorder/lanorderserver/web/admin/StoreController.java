package com.lanorder.lanorderserver.web.admin;

import com.lanorder.lanorderserver.common.entity.pojo.TabStore;
import com.lanorder.lanorderserver.common.log.MethodLog;
import com.lanorder.lanorderserver.common.response.ErrorEnum;
import com.lanorder.lanorderserver.common.response.Result;
import com.lanorder.lanorderserver.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1")
@RestController
@RequestMapping("/admin")
public class StoreController {
    @Resource
    private Admin admin;

    @PostMapping("/store/insert")
    public Result<Object> insert(@RequestParam("name") String name
            , @RequestParam("sort") String sort
            , @RequestParam("price") Double price
            , @RequestParam("img") MultipartFile img) throws IOException {
        TabStore tabStore = new TabStore(sort,name,price);
        return Result.result(admin.addStore(tabStore, img), null);
    }

    @MethodLog
    @PatchMapping("/store/update")
    public Result<Object> update(@RequestBody TabStore tabStore){
        return Result.result(admin.updateStoreById(tabStore), null);
    }

    @MethodLog
    @PostMapping("/store/seek")
    public Result<List<TabStore>> seek(@RequestParam String seek_string){
        return Result.result(ErrorEnum.SUCCESS,admin.seekStoreLike(seek_string));
    }

    @MethodLog
    @DeleteMapping("/store/delete")
    public Result<Object> delete(@RequestParam Integer id){
        return Result.result(admin.deleteStoreById(id),null);
    }

    @MethodLog
    @GetMapping("/store/list")
        public Result<List<TabStore>> getStoreInTab(){
            return Result.result(ErrorEnum.SUCCESS,admin.getStoreList());
    }

    @MethodLog
    @PostMapping("/store/sellout")
    public Result<Object> sellOutStore(@RequestBody List<TabStore> storeList){
        return Result.result(admin.sellout(storeList),null);
    }
}
