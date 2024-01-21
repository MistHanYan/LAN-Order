package com.lanorder.web.controller.admin;


import com.lanorder.common.entity.request.receive.Marketing;
import com.lanorder.common.log.MethodLog;
import com.lanorder.common.response.Result;
import com.lanorder.service.Admin;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

//@CrossOrigin(origins = "http://127.0.0.1")
@RestController
@RequestMapping("/admin")
public class MarketingController {
    @Resource
    private Admin admin;

    private static final Logger logger = LoggerFactory.getLogger(MarketingController.class);

    @MethodLog
    @PostMapping("/marketing/set/add")
    public Result<Object> add(@RequestBody Marketing marketing){
        return Result.result(admin.addMarketingAll(marketing),null);
    }

    @MethodLog
    @DeleteMapping("/marketing/set/delete")
    public Result<Object> delete(@RequestParam Integer id){
        logger.debug("input id : {}",id);
        return Result.result(admin.deleteMarketing(id),null);
    }

    @MethodLog
    @PostMapping("/marketing/generate_qr")
    public Result<Object> generateQR(@RequestBody Marketing marketing) throws IOException {
        return Result.result(admin.getMarketingQR(marketing),null);
    }
}
