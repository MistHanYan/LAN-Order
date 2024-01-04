package com.example.lanorderafterend.controller.admin;

import com.example.lanorderafterend.entity.Result;
import com.example.lanorderafterend.entity.Store;
import com.example.lanorderafterend.service.Admin;
import com.example.lanorderafterend.service.impl.AdminServer;
import com.example.lanorderafterend.util.mybatis.TabStore;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1")
@RestController
public class StoreController {
    @Resource
    Admin admin;

    private static final Logger logger = LoggerFactory.getLogger(AdminServer.class);
    @PostMapping("/admin/store/insert")
    public Result insert(@RequestBody TabStore tabStore){
        logger.debug("input store:{}",tabStore);
        return admin.addStore(tabStore) > 0
                ? Result.success(1,"Inset is succeed")
                : Result.error(-1,"Insert is error");
    }

    @PatchMapping("/admin/store/update")
    public Result update(@RequestBody TabStore tabStore){
        return admin.updateStoreById(tabStore) > 0
                ? Result.success(1,"Update is succeed")
                : Result.error(-1,"Update is error");
    }

    @PostMapping("/admin/store/seek")
    public Result seek(@RequestParam String seek_string){
        return Result.success(1,"outcome",admin.seekStoreLike(seek_string));
    }

    @DeleteMapping("/admin/store/delete")
    public Result delete(@RequestParam int id){
        return admin.deleteStoreById(id) > 0
                ? Result.success(1,"Delete is succeed")
                : Result.error(-1,"Delete is succeed");
    }

    @GetMapping("/admin/store/tab")
        public Result getStoreInTab(){
            return Result.success(1,"succeed",admin.getStoreList());
    }

    @PostMapping("/admin/store/sellout")
    public Result sellOutStore(@RequestBody List<Store> storeList){
        logger.debug("storeList : {}",storeList.get(1));
        return Result.success(admin.sellout(storeList),"sellout msg");
    }
}
