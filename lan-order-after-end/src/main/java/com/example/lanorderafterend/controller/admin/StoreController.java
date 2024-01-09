package com.example.lanorderafterend.controller.admin;

import com.example.lanorderafterend.entity.Result;
import com.example.lanorderafterend.service.Admin;
import com.example.lanorderafterend.service.impl.AdminServer;
import com.example.lanorderafterend.util.mybatis.TabStore;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1")
@RestController
public class StoreController {
    @Resource
    Admin admin;

    private static final Logger logger = LoggerFactory.getLogger(AdminServer.class);

    @PostMapping("/admin/store/insert")
    public Result insert(@RequestParam("name") String name
            , @RequestParam("sort") String sort
            , @RequestParam("price") Double price
            , @RequestParam("img") MultipartFile img) {
        TabStore tabStore = new TabStore();
        tabStore.setSort(sort);
        tabStore.setPrice(price);
        tabStore.setName(name);
        logger.debug("input store:{}", tabStore);

        switch (admin.addStore(tabStore, img.getOriginalFilename(), img)) {
            case 1 -> {
                logger.info("成功添加商品{}", tabStore);
                return Result.success(1, "Inset is succeed");
            }
            case -1 -> {
                return Result.error(-1, "Insert is error");
            }
            default -> throw new IllegalStateException("Unexpected value: "
                    + admin.addStore(tabStore, img.getOriginalFilename(), img));
        }
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

    @GetMapping("/admin/store/show")
        public Result getStoreInTab(){
            return Result.success(1,"succeed",admin.getStoreList());
    }

    @PostMapping("/admin/store/sellout")
    public Result sellOutStore(@RequestBody List<TabStore> storeList){
        logger.debug("storeList : {}",storeList.get(1));
        return Result.success(admin.sellout(storeList),"sellout msg");
    }
}
