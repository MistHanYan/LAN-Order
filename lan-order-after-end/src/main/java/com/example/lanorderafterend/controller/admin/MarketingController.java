package com.example.lanorderafterend.controller.admin;

import com.example.lanorderafterend.entity.Marketing;
import com.example.lanorderafterend.entity.Result;
import com.example.lanorderafterend.service.Admin;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://127.0.0.1")
@RestController
public class MarketingController {
    @Resource
    private Admin admin;

    private static final Logger logger = LoggerFactory.getLogger(MarketingController.class);
    @PostMapping("/admin/marketing/set/add")
    public Result add(@RequestBody Marketing marketing){
        return admin.addMarketingAll(marketing) != 0
                ? Result.success(1,"succeed")
                : Result.error(-1,"error");
    }

    @DeleteMapping("/admin/marketing/set/delete")
    public Result delete(@RequestParam int id){
        logger.debug("input id : {}",id);
        return admin.deleteMarketing(id) != 0
                ? Result.success(1,"succeed")
                : Result.error(-1,"error");
    }

    @PostMapping("/admin/marketing/generate_qr")
    public Result generateQR(@RequestBody Marketing marketing) throws IOException {
        return Result.success(1,"succeed",admin.getMarketingQR(marketing));
    }
}
